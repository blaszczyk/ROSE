package bn.blaszczyk.roseapp.controller;

//
//import org.hibernate.*;
//import org.hibernate.cfg.AnnotationConfiguration;


import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

import bn.blaszczyk.roseapp.model.Entity;
import bn.blaszczyk.roseapp.model.EntityModel;

public class HibernateController implements FullModelController {

	private static SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
	private BasicModelController controller;
	
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
					for( Object o : (List<?>) entityModel.getEntityMember(i) )
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
			Integer id = (Integer) session.save(e);
			e.setId(id);
		}
		transaction.commit();
		session.close();	
		changedEntitys.clear();
	}
	
	@Override
	public List<?> getAll(Class<?> type)
	{
		Session session = sessionFactory.openSession();
		List<?> list = session.createCriteria(type).list();
		session.close();
		return list;
	}

}
