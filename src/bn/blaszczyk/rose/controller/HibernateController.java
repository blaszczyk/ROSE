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

public class HibernateController implements ModelController {

	private static SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
	private ModelController controller;
	
	private Set<Object> changedObjects = new HashSet<>();
	
	public HibernateController(ModelController controller)
	{
		this.controller = controller;
	}
	
	@Override
	public void setMember(Object entity, String name, Object value) throws ParseException
	{
		changedObjects.add(entity);
		controller.setMember(entity, name, value);
	}
	
	@Override
	public void setEntityMember(Object entity, String name, Object value) throws ParseException
	{
		changedObjects.add(entity);
		controller.setEntityMember(entity, name, value);
	}
	
	@Override
	public void addEntityMember(Object entity, String name, Object value) throws ParseException
	{
		changedObjects.add(entity);
		controller.addEntityMember(entity, name, value);
	}
	
	@Override
	public void deleteEntityMember(Object entity, String name, Object value) throws ParseException
	{
		changedObjects.add(entity);
		controller.deleteEntityMember(entity, name, value);
	}
	
	@Override
	public void commit()
	{
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		for(Object o : changedObjects)
		{
			Integer kundenId = (Integer) session.save(o);
		}
		transaction.commit();
		session.close();
		controller.commit();
	}
	
}
