package bn.blaszczyk.rose.creators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import bn.blaszczyk.rose.MetaData;
import bn.blaszczyk.rose.model.DBType;
import bn.blaszczyk.rose.model.Entity;

public class PersistenceCreator {
	
	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
			+ "<persistence  version=\"2.0\" xmlns=\"http://java.sun.com/xml/ns/persistence\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd\">\r\n"
			+ "\t<persistence-unit name=\"rosePersistenceUnit\">\r\n"
			+ "\t\t<provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>\r\n";
	
	private static final String XML_FOOTER = "\t\t</properties>\r\n"
			+ "\t</persistence-unit>\r\n"
			+ "</persistence>\r\n";

	public static void create(List<Entity> entities, MetaData metadata) throws CreateException
	{
		DBType dbType = DBType.getType(metadata.getDbtype());
		String fullpath = metadata.getSrcpath() + "META-INF/persistence.xml";
		File file = new File(fullpath);
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		try(FileWriter writer = new FileWriter(file))
		{
			writer.write( XML_HEADER );
			for(Entity entity : entities)
				writeEntity(entity, metadata, writer);
			writer.write("\t\t<properties>\r\n");
			
			writeProperty("javax.persistence.jdbc.url", "jdbc:" + dbType.getName() + "://" + metadata.getDbserver() 
							+ ":" + metadata.getDbport() + "/" + metadata.getDbname(), writer);
			writeProperty("javax.persistence.jdbc.user", metadata.getDbuser(), writer);
			writeProperty("javax.persistence.jdbc.password", metadata.getDbpassword(), writer);
			writeProperty("javax.persistence.jdbc.driver", dbType.getJdbcDriver(), writer);
			
			writeProperty("hibernate.dialect", dbType.getDialect(), writer);
			writeProperty("hibernate.id.new_generator_mappings", "false", writer);

			writer.write( XML_FOOTER );
			System.out.println( "File created: " + fullpath);
		}
		catch (IOException e)
		{
			throw new CreateException("error creating persistence.xml", e);
		}
	}
	
	private static void writeProperty(String name, String value, Writer writer) throws IOException
	{
		writer.write("\t\t\t<property name=\"" + name + "\" value=\"" + value + "\" />\r\n");
	}

	private static void writeEntity(Entity entity, MetaData metadata, Writer writer) throws IOException
	{
		String optionalImpl = metadata.isUsingInterfaces() ? "Impl" : "";
		writer.write("\t\t<class>" + metadata.getModelpackage() + "." + entity.getSimpleClassName() + optionalImpl + "</class>\r\n");
	}
	
}


