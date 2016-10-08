package sqltojava;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class CreateJavaModel {

	public static void createModel(Entity entity, MetaData metadata)
	{
		String classname = firstCaseUp(entity.getJavaname());
		String fullpath = metadata.getModelpath() + metadata.getModelpackage().replaceAll("\\.", "/") + "/" + classname + ".java";
		System.out.println(fullpath);
		File file = new File(fullpath);
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		try(FileWriter writer = new FileWriter(file))
		{
			// package declaration
			writer.write("package " + metadata.getModelpackage() + ";\n\n");
			
			// imports
			if(metadata.isUsingAnnotations())
				writer.write("import javax.persistence.*;\n\n");
			for(String importpackage : entity.getImports())
				if(importpackage != null)
					writer.write("import " + importpackage + ";\n");
			
			// annotations
			if(metadata.isUsingAnnotations())
				writer.write("\n@Entity\n@Table(name=\"" + entity.getSqlname() + "\")");
			
			// class declaration
			writer.write("\npublic class " + classname + "\n{\n");
			
			// member variables
			for(Member member : entity)
				writer.write("\n\tprivate " + member.getType().getJavaname() + " " + member.getJavaname() + ";");
			
			// default constructor
			writer.write("\n\n\tpublic " + classname + "()\n\t{\n\t}\n");
			
			// constructor for non-primaries
			writer.write("\n\tpublic " + classname + "( ");
			boolean first = true;
			for(Member member : entity)
			{
				if(member.isPrimary())
					continue;
				if(!first)
					writer.write(", ");
				else
					first = false;
				writer.write( member.getType().getJavaname() + " " + member.getJavaname());
			}
			writer.write(" )\n\t{\n");
			for(Member member : entity)
				if(!member.isPrimary())
					writer.write("\t\tthis." + member.getJavaname() + " = " + member.getJavaname() + ";\n");
			writer.write("\t}\n\n");
			
			// for each member:
			for(Member member : entity)
			{
				
				// annotations
				if(metadata.isUsingAnnotations())
				{
					if(member.isPrimary())
						writer.write("\t@Id\n\t@GeneratedValue\n");
					writer.write("\t@Column(name=\"" + member.getSqlname() + "\")\n");	
				}
				
				// getter
				writer.write("\tpublic " + member.getType().getJavaname() + " get" + firstCaseUp(member.getJavaname()) 
							+ "()\n\t{\n\t\treturn " + member.getJavaname() + ";\n\t}\n" );
				
				// setter
				writer.write("\n\tpublic void set" + firstCaseUp(member.getJavaname()) + "( " + member.getType().getJavaname() + " " 
							+ member.getJavaname() + " )\n\t{\n\t\tthis." + member.getJavaname() + " = " + member.getJavaname() + ";\n\t}\n\n" );
			}
			
			// TODO? toString, equals, hashValue	
			// fin
			writer.write("}\n");
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	private static String firstCaseUp(String input)
	{
		return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
	}
	
}
