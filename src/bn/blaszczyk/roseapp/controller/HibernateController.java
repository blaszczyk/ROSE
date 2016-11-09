package bn.blaszczyk.roseapp.controller;

import java.util.*;

import org.hibernate.*;
import org.hibernate.cfg.AnnotationConfiguration;

import bn.blaszczyk.roseapp.model.*;

public class HibernateController implements FullModelController {

	private static SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
	private BasicModelController controller;

	private Map<Class<?>,List<EntityModel>> entityModelLists = new HashMap<>();
	private Set<Entity> changedEntitys = new HashSet<>();
	
	public HibernateController(BasicModelController controller)
	{
		this.controller = controller;
	}
	
	@Override
	public void setMember(Entity entity, String name, Object value)
	{
		changedEntitys.add(entity);
		controller.setMember(entity, name, value);
	}

	@Override
	public void setEntityMember(Entity entity, String name, Entity value)
	{
		changedEntitys.add(entity);
		if(value !=  null && !(value instanceof Enum))
			changedEntitys.add(value);
		controller.setEntityMember(entity, name, value);
	}
	
	@Override
	public void addEntityMember(Entity entity, String name, Entity value)
	{
		changedEntitys.add(entity);
		changedEntitys.add(value);
		controller.addEntityMember(entity, name, value);
	}
	
	@Override
	public void delete(Entity entity)
	{
		Session sesson = sessionFactory.openSession();
		sesson.beginTransaction();
		sesson.delete(entity);
		sesson.getTransaction().commit();
		sesson.close();			
		deleteModel(entity);
		controller.delete(entity);
	}
	
	private void deleteModel(Entity entity)
	{
		for( Object o : entityModelLists.get(entity.getClass()).toArray() )
		{
			EntityModel entityModel = (EntityModel) o;
			if(entityModel.getEntity().equals(entity))
			{
				entityModelLists.get(entity.getClass()).remove(entityModel);
				for(int i = 0; i < entityModel.getEntityCount(); i++)
					if(entityModel.getRelationType(i).equals(RelationType.ONETOONE))
						deleteModel((Entity) entityModel.getEntityMember(i));
			}
		}
	}
	

	@Override
	public Entity createNew(String className)
	{
		Entity newEntity = controller.createNew(className);
		entityModelLists.get(newEntity.getClass()).add(controller.createModel(newEntity));
		changedEntitys.add(newEntity);
		return newEntity;
	}
	
	@Override
	public Entity createCopy(Entity entity)
	{
		Entity copy = createNew(entity.getClass().getSimpleName()), subCopy;
		EntityModel entityModel = createModel(entity);
		for(int i = 0; i < entityModel.getMemberCount(); i++)
			setMember(copy, entityModel.getMemberName(i), entityModel.getMemberValue(i));
		for(int i = 0; i < entityModel.getEntityCount(); i++)
			switch(entityModel.getRelationType(i))
			{
			case ONETOONE:
				subCopy = createCopy( (Entity) entityModel.getEntityMember(i) );
				setEntityMember(copy, entityModel.getEntityName(i), subCopy );
				break;
			case ONETOMANY:
				for( Object o :  ((Set<?>) entityModel.getEntityMember(i)).toArray())
					addEntityMember(copy, entityModel.getEntityName(i), createCopy((Entity) o));
				break;
			case MANYTOONE:
			case ENUM:
				setEntityMember(copy, entityModel.getEntityName(i), (Entity) entityModel.getEntityMember(i));
				break;
			case MANYTOMANY:
				break;
			}
		return copy;
	}

	@Override
	public EntityModel createModel( Entity entity )
	{
		return controller.createModel(entity);
	}
	
	@Override
	public void commit()
	{
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		for(Entity entity : changedEntitys)
		{
			if(entity.getId() < 0)
			{
				Integer id = (Integer) session.save(entity);
				entity.setId(id);
			}
			else
				session.update(entity);
		}
		transaction.commit();
		session.close();
		changedEntitys.clear();
	}

	@Override
	public void loadEntities(Class<?>[] types)
	{
		Session session = sessionFactory.openSession();
		for(Class<?> type : types)
		{
			List<EntityModel> entityModels = new ArrayList<>();
			entityModelLists.put(type, entityModels);
			List<?> list = session.createCriteria(type).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
			for(Object o : list)
				entityModels.add(createModel((Entity)o));
		}
		session.close();
		for(Class<?> type : types)
			for(EntityModel entityModel : entityModelLists.get(type))
			{
				for(int i = 0; i < entityModel.getEntityCount(); i++)
				{
					if(entityModel.getRelationType(i) == RelationType.ONETOONE || entityModel.getRelationType(i) == RelationType.MANYTOONE)
					{
						Entity oldEntity = (Entity) entityModel.getEntityMember(i);
						if(oldEntity == null)
							continue;
						for( EntityModel newEntity : entityModelLists.get(oldEntity.getClass()) )
							if(newEntity.getId() == oldEntity.getId())
								setEntityMember(entityModel.getEntity(), entityModel.getEntityName(i), newEntity.getEntity());
					}
				}
			}
		changedEntitys.clear();
	}
	
	@Override
	public List<EntityModel> getAllModels(Class<?> type)
	{
		return entityModelLists.get(type);
	}

	@Override
	public EntityModel createCopy(EntityModel entityModel)
	{
		return createModel( createCopy(entityModel.getEntity()));
	}

}
