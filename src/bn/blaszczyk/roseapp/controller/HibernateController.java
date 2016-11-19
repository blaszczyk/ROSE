package bn.blaszczyk.roseapp.controller;

import java.util.*;

import org.hibernate.*;
import org.hibernate.cfg.AnnotationConfiguration;

import bn.blaszczyk.roseapp.model.*;
import bn.blaszczyk.roseapp.model.Readable;

public class HibernateController implements FullModelController {

	private static SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();

	private Map<Class<?>,List<Readable>> entityLists = new HashMap<>();
	private Set<Writable> changedEntitys = new HashSet<>();
	
	@Override
	public void setField(Writable entity, int index, Object value)
	{
		changedEntitys.add(entity);
		entity.setField(index, value);
	}

	@Override
	public void setEntityField(Writable entity, int index, Writable value)
	{
		changedEntitys.add(entity);
		if(value !=  null && !(value instanceof Enum))
			changedEntitys.add(value);
		entity.setEntity( index, value);
	}
	
	@Override
	public void addEntityField(Writable entity, int index, Writable value)
	{
		changedEntitys.add(entity);
		changedEntitys.add(value);
		entity.addEntity( index, value);
	}
	
	@Override
	public void delete(Writable entity)
	{
		Session sesson = sessionFactory.openSession();
		sesson.beginTransaction();
		sesson.delete(entity);
		sesson.getTransaction().commit();
		sesson.close();		
		entityLists.get(entity.getClass()).remove(entity);
		for(int i = 0; i < entity.getEntityCount(); i++)
			if(entity.getRelationType(i).equals(RelationType.ONETOONE) && entity.getEntityValue(i) instanceof Writable)
				delete((Writable) entity.getEntityValue(i));
	}
	

	@Override
	public Writable createNew(Class<Writable> type)
	{
		Writable entity;
		try
		{
			entity = type.newInstance();
			changedEntitys.add(entity);
			return entity;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Writable createCopy(Writable entity)
	{
		Writable copy = createNew((Class<Writable>) entity.getClass()), subCopy;
		for(int i = 0; i < copy.getFieldCount(); i++)
			copy.setField( i, copy.getFieldValue(i));
		for(int i = 0; i < copy.getEntityCount(); i++)
			switch(copy.getRelationType(i))
			{
			case ONETOONE:
				subCopy = createCopy( (Writable) copy.getEntityValue(i) );
				copy.setEntity( i, subCopy );
				break;
			case ONETOMANY:
				for( Object o :  ((Set<?>) copy.getEntityValue(i)).toArray())
					copy.addEntity( i, createCopy((Writable) o));
				break;
			case MANYTOONE:
				copy.setEntity(i, (Writable) copy.getEntityValue(i));
				break;
			case MANYTOMANY:
				break;
			}
		return copy;
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
			List<Readable> entities = new ArrayList<>();
			entityLists.put(type, entities);
			List<?> list = session.createCriteria(type).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
			for(Object o : list)
				entities.add((Readable) o);
		}
		session.close();
		for(Class<?> type : types)
		{
			List<Readable> entities = entityLists.get(type);			
			if( !entities.isEmpty() && entities.get(0) instanceof Writable)
			for(Readable entity : entities)
			{
				for(int i = 0; i < entity.getEntityCount(); i++)
				{
					if(entity.getRelationType(i) == RelationType.ONETOONE || entity.getRelationType(i) == RelationType.MANYTOONE)
					{
						Entity oldEntity = (Entity) entity.getEntityValue(i);
						if(oldEntity == null)
							continue;
						int nIndex = entityLists.get(oldEntity.getClass()).indexOf(oldEntity);
						if(nIndex >= 0)
							((Writable)entity).setEntity(i, (Writable) entityLists.get(oldEntity.getClass()).get(nIndex) );
					}
				}
			}
		}
		changedEntitys.clear();
	}

	@Override
	public void removeEntityField(Writable entity, int index, Writable value)
	{
		changedEntitys.add(entity);
		changedEntitys.add(value);
		entity.removeEntity( index, value);
	}

	@Override
	public List<Readable> getAllEntites(Class<?> type)
	{
		return entityLists.get(type);
	}
		

}
