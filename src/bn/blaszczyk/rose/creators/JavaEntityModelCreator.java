package bn.blaszczyk.rose.creators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import bn.blaszczyk.rose.*;
import bn.blaszczyk.rose.model.*;


public class JavaEntityModelCreator {

	public static String getEntityModelName(Entity entity, MetaData metadata)
	{
		return String.format( metadata.getEntitymodelformat() , entity.getSimpleClassName() );
	}
	
	public static String getEntityFactoryName(MetaData metadata)
	{
		return metadata.getEntitymodelfactoryclass();
	}
	
	public static void createModel(Entity entity, MetaData metadata)
	{
		String classname = getEntityModelName(entity, metadata);
		String fullpath = metadata.getSrcpath() + metadata.getEntitymodelpackage().replaceAll("\\.", "/") + "/" + classname + ".java";
		File file = new File(fullpath);
		int count = 0;
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		try(FileWriter writer = new FileWriter(file))
		{
			// initial comment
			writer.write("/*\n * " + classname + ".java\n * generated by rose\n */\n\n");
			
			// package declaration
			writer.write("package " + metadata.getEntitymodelpackage() + ";\n\n");
			
			// class declaration
			writer.write("\npublic class " + classname + " implements bn.blaszczyk.roseapp.model.EntityModel\n{\n");
			
			// represented object
			writer.write("\tprivate final " + entity.getClassName() + " " + entity.getObjectName() + ";\n");

			// default constructor
			writer.write("\n\n\tpublic " + classname + "( " + entity.getClassName() + " " + entity.getObjectName() + " )\n\t{\n");
			writer.write("\t\tthis." + entity.getObjectName() + " = " + entity.getObjectName() + ";\n");
			writer.write("\t}\n\n");
			
			// implement EntityModel methods

			// public String getName();
			writer.write("\t@Override\n\tpublic String getName()\n\t{\n\t\treturn \"" + entity.getSimpleClassName() + "\";\n\t}\n\n");
			
			// public int getId();
			writer.write("\t@Override\n\tpublic int getId()\n\t{\n\t\treturn " + entity.getObjectName() + ".getId();\n\t}\n\n");

			//public Entity getEntity();
			writer.write("\t@Override\n\tpublic bn.blaszczyk.roseapp.model.Entity getEntity()\n\t{\n\t\treturn " + entity.getObjectName() + ";\n\t}\n\n");
			
			// public int getMemberCount();
			writer.write("\t@Override\n\tpublic int getMemberCount()\n\t{\n\t\treturn " + entity.getMembers().size() + ";\n\t}\n\n");
			
			// public String getMemberName( int index );
			writer.write("\t@Override\n\tpublic String getMemberName(int index)\n\t{\n\t\tswitch(index)\n\t\t{\n");
			count = 0;
			for(Member member : entity.getMembers())
				writer.write("\t\tcase " + count++ + ":\n\t\t\treturn \"" + member.getCapitalName() +  "\";\n" );
			writer.write("\t\t}\n\t\treturn \"\";\n\t}\n\n");
			
			// public Object getMemberValue( int index );
			writer.write("\t@Override\n\tpublic Object getMemberValue(int index)\n\t{\n\t\tswitch(index)\n\t\t{\n");
			count = 0;
			for(Member member : entity.getMembers())
				writer.write("\t\tcase " + count++ + ":\n\t\t\treturn " + entity.getObjectName() + "." + JavaModelCreator.getGetterName(member) +  "();\n" );
			writer.write("\t\t}\n\t\treturn null;\n\t}\n\n");
			
			// public int getEntityCount();
			writer.write("\t@Override\n\tpublic int getEntityCount()\n\t{\n\t\treturn " + entity.getEntityMembers().size() + ";\n\t}\n\n");
			
			// public Object getEntityMember( int index );
			writer.write("\t@Override\n\tpublic Object getEntityMember(int index)\n\t{\n\t\tswitch(index)\n\t\t{\n");
			count = 0;
			for(EntityMember entityMember : entity.getEntityMembers())
				writer.write("\t\tcase " + count++ + ":\n\t\t\treturn " + entity.getObjectName() + "." + JavaModelCreator.getGetterName(entityMember)+ "();\n" );
			writer.write("\t\t}\n\t\treturn null;\n\t}\n\n");
			
			// public String getEntityName( int index );
			writer.write("\t@Override\n\tpublic String getEntityName(int index)\n\t{\n\t\tswitch(index)\n\t\t{\n");
			count = 0;
			for(EntityMember entityMember : entity.getEntityMembers())
				writer.write("\t\tcase " + count++ + ":\n\t\t\treturn \"" + entityMember.getCapitalName() + ( entityMember.getType().isSecondMany() ? "s" : "" ) +  "\";\n" );
			writer.write("\t\t}\n\t\treturn \"\";\n\t}\n\n");
			
			// public RelationType getRelationType( int index );			
			writer.write("\t@Override\n\tpublic bn.blaszczyk.roseapp.model.RelationType getRelationType(int index)\n\t{\n\t\tswitch(index)\n\t\t{\n");
			count = 0;
			for(EntityMember entityMember : entity.getEntityMembers())
				writer.write("\t\tcase " + count++ + ":\n\t\t\treturn bn.blaszczyk.roseapp.model.RelationType." + entityMember.getType().getName().toUpperCase() + ";\n" );
			writer.write("\t\t}\n\t\treturn null;\n\t}\n\n");

			// public EntityModel createModel( Entity entity );
			writer.write("\t@Override\n\tpublic bn.blaszczyk.roseapp.model.EntityModel createModel( bn.blaszczyk.roseapp.model.Entity entity )\n\t{\n\t\treturn " 
						+ metadata.getEntitymodelfactoryclass() + ".createModel( entity );\n\t}\n\n");
			
			// fin
			writer.write("}\n");
			System.out.println( "File created: " + fullpath);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void createFactory( List<Entity> entities, MetaData metadata )
	{
	
		String fullpath = metadata.getSrcpath() + metadata.getEntitymodelpackage().replaceAll("\\.", "/") + "/" + metadata.getEntitymodelfactoryclass() + ".java";
		File file = new File(fullpath);
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		try(FileWriter writer = new FileWriter(file))
		{
			// initial comment
			writer.write("/*\n * " + metadata.getEntitymodelfactoryclass() + ".java\n * generated by rose\n */\n\n");
		
			// package declaration
			writer.write("package " + metadata.getEntitymodelpackage() + ";\n\n");
		
			// class declaration
			writer.write("\npublic class " + metadata.getEntitymodelfactoryclass() + "\n{\n");

			// createModel
			writer.write("\n\tpublic static bn.blaszczyk.roseapp.model.EntityModel createModel( bn.blaszczyk.roseapp.model.Entity entity )\n\t{\n\t\t");
			for(Entity entity : entities)
				writer.write("if( entity instanceof " + entity.getClassName() +" )\n\t\t\treturn new " + metadata.getEntitymodelpackage() + "."
							+ getEntityModelName(entity, metadata) +  "( ( " + entity.getClassName() + " ) entity );\n\t\telse " );
			writer.write("\n\t\t\treturn null;\n\t}\n" );
		
			writer.write("}\n");
			System.out.println( "File created: " + fullpath);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
}


}
