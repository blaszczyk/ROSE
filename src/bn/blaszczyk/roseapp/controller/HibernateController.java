package bn.blaszczyk.roseapp.controller;

//
//import org.hibernate.*;
//import org.hibernate.cfg.AnnotationConfiguration;


import java.text.ParseException;
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
	public void setMember(Entity entity, String name, Object value) throws ParseException
	{
		changedEntitys.add(entity);
		controller.setMember(entity, name, value);
	}
	
	@Override
	public void setEntityMember(Entity entity, String name, Entity value) throws ParseException
	{
		changedEntitys.add(entity);
		controller.setEntityMember(entity, name, value);
	}
	
	@Override
	public void addEntityMember(Entity entity, String name, Entity value) throws ParseException
	{
		changedEntitys.add(entity);
		controller.addEntityMember(entity, name, value);
	}
	
	@Override
	public void deleteEntityMember(Entity entity, String name, Entity value) throws ParseException
	{
		changedEntitys.add(entity);
		controller.deleteEntityMember(entity, name, value);
	}
	

	@Override
	public Entity createNew(String className)
	{
		return controller.createNew(className);
	}
	
	@Override
	public Entity createCopy(Entity entity)
	{
		Entity copy = createNew(entity.getClass().getSimpleName());
		EntityModel entityModel = createModel(entity);
		try
		{
			for(int i = 0; i < entityModel.getMemberCount(); i++)
				setMember(copy, entityModel.getMemberName(i), entityModel.getMemberValue(i));
			for(int i = 0; i < entityModel.getEntityCount(); i++)
				if(entityModel.getRelationType(i).isSecondMany())
					for( Object o : (Set<?>) entityModel.getEntityMember(i) )
						addEntityMember(copy, entityModel.getEntityName(i), (Entity) o);
				else
					setEntityMember(copy, entityModel.getEntityName(i), (Entity) entityModel.getEntityMember(i));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
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
		for(Entity e : changedEntitys)
		{
			if(e.getId() < 0)
			{
				Integer id = (Integer) session.save(e);
				e.setId(id);
			}
			else
				session.update(e);
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
					if(entityModel.getRelationType(i).isSecondMany())
					{
						Set<?> set = (Set<?>) entityModel.getEntityMember(i);
						Object[] os = set.toArray();
						for(int j = 0; j < set.size(); j++)
						{
							Entity oldEntity = (Entity) os[j];
							for( EntityModel newEntity : entityModelLists.get(oldEntity.getClass()) )
								if(newEntity.getId() == oldEntity.getId())
									try
									{
										deleteEntityMember(entityModel.getEntity(), entityModel.getEntityName(i), oldEntity);
										addEntityMember(entityModel.getEntity(), entityModel.getEntityName(i),newEntity.getEntity());
									}
									catch (ParseException e)
									{
										e.printStackTrace();
									}
						}
						
					}
					else
					{
						Entity oldEntity = (Entity) entityModel.getEntityMember(i);
						if(oldEntity == null)
							continue;
						for( EntityModel newEntity : entityModelLists.get(oldEntity.getClass()) )
							if(newEntity.getId() == oldEntity.getId())
								try
								{
									setEntityMember(entityModel.getEntity(), entityModel.getEntityName(i), newEntity.getEntity());
								}
								catch (ParseException e)
								{
									e.printStackTrace();
								}
					}
				}
			}
		for(Class<?> type : entityModelLists.keySet())
			System.out.println(type.getSimpleName() + " - " + entityModelLists.get(type).size());
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
