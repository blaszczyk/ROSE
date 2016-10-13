package bn.blaszczyk.rose.controller;

//
//import org.hibernate.*;
//import org.hibernate.cfg.AnnotationConfiguration;


import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

import bn.blaszczyk.rose.interfaces.Entity;

public class HibernateController implements ModelController {

	private static SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
	private ModelController controller;
	
	private Set<Entity> changedEntitys = new HashSet<>();
	
	public HibernateController(ModelController controller)
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
		controller.commit();
	}
	
}
