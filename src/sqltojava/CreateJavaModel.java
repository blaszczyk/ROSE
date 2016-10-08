package sqltojava;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class CreateJavaModel {

	public static void createModel(Pojotable pojotable, String path, boolean usingJPA)
	{
		if(path == null)
			path = "";
		String classname = firstCaseUp(pojotable.getJavaname());
		String fullpath = path + pojotable.getJavapackage().replaceAll("\\.", "/") + "/" + classname + ".java";
		System.out.println(fullpath);
		File file = new File(fullpath);
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		try(FileWriter writer = new FileWriter(file))
		{
			// package declaration
			writer.write("package " + pojotable.getJavapackage() + ";\n\n");
			// imports
			if(usingJPA)
				writer.write("import javax.persistence.*;\n\n");
			for(String importpackage : pojotable.getImports())
				if(importpackage != null)
					writer.write("import " + importpackage + ";\n");
			// annotations
			if(usingJPA)
				writer.write("\n@Entity\n@Table(name=\"" + pojotable.getSqlname() + "\")");
			// class declaration
			writer.write("\npublic class " + classname + "\n{\n");
			// member variables
			for(Member member : pojotable)
				writer.write("\n\tprivate " + member.getType().getJavaname() + " " + member.getJavaname() + ";");
			// default constructor
			writer.write("\n\n\tpublic " + classname + "()\n\t{\n\t}\n");
			// constructor for non-primaries
			writer.write("\n\tpublic " + classname + "( ");
			boolean first = true;
			for(Member member : pojotable)
			{
				if(!first)
					writer.write(", ");
				else
					first = false;
				writer.write( member.getType().getJavaname() + " " + member.getJavaname());
			}
			writer.write(" )\n\t{\n");
			for(Member member : pojotable)
				writer.write("\t\tthis." + member.getJavaname() + " = " + member.getJavaname() + ";\n");
			writer.write("\t}\n\n");
			// for each member:
			for(Member member : pojotable)
			{
				// annotations
				if(usingJPA)
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
