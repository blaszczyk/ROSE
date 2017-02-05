package bn.blaszczyk.rose.creators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import bn.blaszczyk.rose.MetaData;
import bn.blaszczyk.rose.model.Entity;

public class RoseAppLauncherCreator {

	private static final String CONTROLLER_PACKAGE = "bn.blaszczyk.roseapp.controller";
	private static final String TOOLS_PACKAGE = "bn.blaszczyk.roseapp.tools";
	private static final String RESOURCE_PACKAGE = "bn.blaszczyk.roseapp.resources";

	private static final String DELIMITER = ",|;";
	private static final String MESSAGE_EXTENSION = ".messages";
	
	public static void createMain(List<Entity> entities, MetaData metadata)
	{
		String classname = metadata.getMainname();
		String fullpath = metadata.getSrcpath() + metadata.getMainpackage().replaceAll("\\.", "/") + "/" + classname + ".java";
		File file = new File(fullpath);
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		try(FileWriter writer = new FileWriter(file))
		{	
			// initial comment
			writer.write("/*\r\n * " + metadata.getMainname() + ".java\r\n * generated by rose\r\n */\r\n");
			
			// package declaration
			writer.write("package " + metadata.getMainpackage() + ";\r\n\r\n");
			
			// imports
			writer.write("import " + CONTROLLER_PACKAGE + ".*;\r\n"
					+ "import " + TOOLS_PACKAGE + ".*;\r\n");
			
			// class declaration
			writer.write("\r\npublic class " + classname + "\r\n{\r\n\r\n");
			
			// main
			writer.write("\tpublic static void main(String[] args)\r\n"
					+ "\t{\r\n"
					+ "\t\tTypeManager.parseRoseFile( " + classname + ".class.getClassLoader().getResourceAsStream(\"" 
					+ getRoseCopyFileName(metadata) + "\" ) );\r\n");
			
			loadMessages(writer, classname, RESOURCE_PACKAGE , metadata.getRoseappmessages());
			loadMessages(writer, classname, metadata.getResourcepackage(), metadata.getCustommessages());
			
			writer.write("\t\tfinal ModelController modelController = new " + "HibernateController();\r\n"
					+ "\t\tfinal GUIController guiController = new GUIController(modelController);\r\n");

			String[] split = (metadata.getInitialcommands() + " ").split(";");
			for(int i = 0; i < split.length - 1; i++)
				writer.write("\t\t" + split[i] + ";\r\n");
			
			writer.write("\t\tguiController.createMainFrame( \"" + metadata.getMainname() + "\" );\r\n"
					+ "\t}\r\n");
			
			//fin
			writer.write("}\r\n");
			System.out.println( "File created: " + fullpath);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static void loadMessages(Writer writer, String classname, String resourcePackage, String messages) throws IOException
	{
		if(messages == null || messages.equals(""))
			return;
		String[] split = messages.split(DELIMITER);
		String resourcePath = resourcePackage.replaceAll("\\.", "/") + "/";
		for(String message : split)
			writer.write("\t\tMessages.load( " + classname + ".class.getClassLoader().getResourceAsStream(\""
					+ resourcePath + message + MESSAGE_EXTENSION + "\" ) );\r\n");
	}

	public static void copyRose(File file, MetaData metadata)
	{
		String copyName = metadata.getSrcpath() + getRoseCopyFileName(metadata);
		File copy = new File(copyName);
		try
		{
			copy.getParentFile().mkdirs();
			Files.copy(file.toPath(), copy.toPath(), StandardCopyOption.REPLACE_EXISTING);
			System.out.println("File created: " + copyName);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static String getRoseCopyFileName(MetaData metadata)
	{
		return metadata.getResourcepackage().replaceAll("\\.", "/") + "/" + metadata.getMainname() + ".rose";
	}
	
}
