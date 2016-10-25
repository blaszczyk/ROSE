package bn.blaszczyk.rose.creators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import bn.blaszczyk.rose.*;
import bn.blaszczyk.rose.model.*;


public class JavaModelCreator {

	public static String getGetterName(Member member)
	{
		if(member.getType().equals(MemberType.BOOLEAN))
			return "is" + member.getCapitalName();
		return "get" + member.getCapitalName();
	}
	
	public static String getSetterName(Member member)
	{
		return "set" + member.getCapitalName();
	}

	public static String getGetterName(EntityMember entityMember)
	{
		if(entityMember.getType().isSecondMany())
			return "get" + entityMember.getCapitalName() + "s";
		return "get" + entityMember.getCapitalName();
	}
	
	public static String getSetterName(EnumMember enumMember)
	{
		return "set" + enumMember.getCapitalName();
	}

	public static String getGetterName(EnumMember enumMember)
	{
		return "get" + enumMember.getCapitalName();
	}
	
	public static String getSetterName(EntityMember entityMember)
	{
		if(entityMember.getType().isSecondMany())
			return "set" + entityMember.getCapitalName() + "s";
		return "set" + entityMember.getCapitalName();
	}
	
	/*
	 * create Entity
	 */
	public static void create(Entity entity, MetaData metadata)
	{
		String fullpath = metadata.getSrcpath() + metadata.getModelpackage().replaceAll("\\.", "/") + "/" + entity.getSimpleClassName() + ".java";
		File file = new File(fullpath);
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		try(FileWriter writer = new FileWriter(file))
		{
			// initial comment
			writer.write("/*\n * " + entity.getSimpleClassName() + ".java\n * generated by rose\n */\n\n");
			
			// package declaration
			writer.write("package " + metadata.getModelpackage() + ";\n\n");
			
			// imports
			if(metadata.isUsingAnnotations())
				writer.write("import javax.persistence.*;\n\n");
			
			// annotations
			if(metadata.isUsingAnnotations())
				writer.write("\n@Entity\n@Table(name=\"" + entity.getSimpleClassName() + "\")");
			
			// class declaration
			writer.write("\npublic class " + entity.getSimpleClassName() + " implements bn.blaszczyk.roseapp.model.Entity, Comparable<" + entity.getSimpleClassName() + ">\n{");
			
			// id
			writer.write("\n\tprivate int id = -1;\n");

			// member variables
			for(Member member : entity.getMembers())
			{
				// declaration
				writer.write("\tprivate " + member.getType().getJavaname() + " "  );
				if(member.getDefValue() != null && member.getDefValue() != "" )
					writer.write(  member.getName() + " = " + String.format( member.getType().getDefFormat(), member.getDefValue() ) );
				else
					writer.write(  member.getName() + " = " + member.getType().getDefValue() );
				writer.write( ";\n" );
			}
			
			// enum variables
			for(EnumMember enumMember : entity.getEnumMembers())
				writer.write("\tprivate " + enumMember.getEnumType().getClassName() + " " + enumMember.getName() + " = " 
				+ enumMember.getEnumType().getClassName() + "." + enumMember.getDefValue() + ";\n" );
			
			// entitymember variables
			for(EntityMember entitymember : entity.getEntityMembers())
			{
				if(entitymember.getType().isSecondMany())
					writer.write("\n\tprivate java.util.Set<" + entitymember.getEntity().getSimpleClassName() + "> " + entitymember.getName() + "s = new java.util.TreeSet<>();");
				else
					writer.write("\n\tprivate " + entitymember.getEntity().getSimpleClassName() + " " + entitymember.getName() + ";");
			}
	
			// default constructor
			writer.write("\n\n\tpublic " + entity.getSimpleClassName() + "()\n\t{\n\t}\n\n");
			
			// full constructor
			if( entity.getMembers().size() > 1 )
			{
				writer.write("\tpublic " + entity.getSimpleClassName() + "( ");
				boolean first = true;
				for(Member member : entity.getMembers())
				{
					if(!first)
						writer.write(", ");
					else
						first = false;
					writer.write( member.getType().getJavaname() + " " + member.getName());
				}
				writer.write(" )\n\t{\n");
				for(Member member : entity.getMembers())
					writer.write("\t\tthis." + member.getName() + " = " + member.getName() + ";\n");
				writer.write("\t}\n\n");
			}

			// for Id:			
			if(metadata.isUsingAnnotations())
				writer.write("\n\t@Id\n\t@GeneratedValue\n\t@Column(name=\"" + entity.getObjectName() + "_id\")");
			writer.write("\n\t@Override\n\tpublic Integer getId()\n\t{\n\t\treturn id;\n\t}\n" );
			writer.write("\n\t@Override\n\tpublic void setId( Integer id )\n\t{\n\t\tthis.id = id;\n\t}\n\n");

			// for each member:
			for(Member member : entity.getMembers())
			{
				// annotations
				if(metadata.isUsingAnnotations())
					writer.write("\n\t@Column(name=\"" + member.getName() + "\")");
				// getter
				writer.write("\n\tpublic " + member.getType().getJavaname() + " " + getGetterName(member) 
							+ "()\n\t{\n\t\treturn " + member.getName() + ";\n\t}\n" );
				
				// setter
				writer.write("\n\tpublic void " + getSetterName(member) + "( " + member.getType().getJavaname() + " " 
							+ member.getName() + " )\n\t{\n\t\tthis." + member.getName() + " = " + member.getName() + ";\n\t}\n\n" );
			}
			

			// for each enum:
			for(EnumMember enumMember : entity.getEnumMembers())
			{
				// annotations
				if(metadata.isUsingAnnotations())
					writer.write("\n\t@Column(name=\"" + enumMember.getName() + "\")\n\t@Enumerated(EnumType.ORDINAL)");
				// getter
				writer.write("\n\tpublic " + enumMember.getEnumType().getClassName() + " " + getGetterName(enumMember) 
							+ "()\n\t{\n\t\treturn " + enumMember.getName() + ";\n\t}\n" );
				
				// setter
				writer.write("\n\tpublic void " + getSetterName(enumMember) + "( " + enumMember.getEnumType().getClassName() + " " 
							+ enumMember.getName() + " )\n\t{\n\t\tthis." + enumMember.getName() + " = " + enumMember.getName() + ";\n\t}\n\n" );
			}

			// for each entitymember:
			for(EntityMember entityMember : entity.getEntityMembers())
			{
				//Annotations
				if(metadata.isUsingAnnotations())
				{
					switch (entityMember.getType())
					{
					case ONETOONE:
						writer.write("\t@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL) \n\t@JoinColumn(name=\"" + entityMember.getName() + "_id\")\n" );
						break;
					case ONETOMANY:
						writer.write("\t@OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy=\"" + entityMember.getCounterName() + "\")\n");
						break;
					case MANYTOONE:
						writer.write("\t@ManyToOne(fetch=FetchType.EAGER,  cascade = CascadeType.ALL) \n\t@JoinColumn(name=\"" + entityMember.getName() + "_id\")\n" );						
						break;
					case MANYTOMANY:
						break;
					case ENUM:
						break;
					}
				}
				// for Lists
				if(entityMember.getType().isSecondMany())
				{
					// getter
					writer.write("\tpublic java.util.Set<" + entityMember.getEntity().getSimpleClassName() + "> " + getGetterName(entityMember)	+ "()\n\t{\n\t\treturn " + entityMember.getName() + "s;\n\t}\n" );
				
					// setter
					writer.write("\n\tpublic void " + getSetterName(entityMember) 
					+ "( java.util.Set<" + entityMember.getEntity().getSimpleClassName() + "> " + entityMember.getName() 
					+ "s )\n\t{\n\t\tthis." + entityMember.getName() + "s = " + entityMember.getName() + "s;\n\t}\n\n" );
				}
				// Singles
				else
				{
					// getter
					writer.write("\tpublic " + entityMember.getEntity().getSimpleClassName() + " " + getGetterName(entityMember) 
								+ "()\n\t{\n\t\treturn " + entityMember.getName() + ";\n\t}\n" );
					
					// setter
					writer.write("\n\tpublic void " + getSetterName(entityMember) + "( " + entityMember.getEntity().getSimpleClassName() 
							+ " " 	+ entityMember.getName() + " )\n\t{\n\t\tthis." + entityMember.getName() + " = " 
							+ entityMember.getName() + ";\n");
//					if(entityMember.getType().isFirstMany())
//						writer.write("\t\t" + entityMember.getName() + "." + getGetterName(entityMember.getCouterpart()) + "().add(this);\n");
					writer.write("\t}\n\n" );
				}
			}
			
			// Comparable.compareto
			writer.write("\t@Override\n\tpublic int compareTo(" + entity.getSimpleClassName() + " that)\n\t{\n"
					+ "\t\treturn Integer.compare( this.id, that.id );\n"
					+ "\t}\n\n");
			
			// toString
			String toString = "\"" + entity.getToString() + "\"";
			for(Member member : entity.getMembers() )
				toString = toString.replaceAll("\\%" + member.getName(), "\" + " + member.getName() + " + \"");			
			toString = toString.replaceAll("\\\"\\\" \\+ ", "").replaceAll(" \\+ \\\"\\\"", "");
			writer.write("\t@Override\n\tpublic String toString()\n\t{\n\t\treturn " + toString + ";\n\t}\n\n");
			
			// equals
			writer.write("\t@Override\n\tpublic boolean equals( Object that)\n\t{\n\t\tif(!(that instanceof " + entity.getSimpleClassName() + "))\n"
					+ "\t\t\treturn false;\n\t\treturn this.id == ((" + entity.getSimpleClassName() + ")that).id;\n"
					+ "\t}\n\n");
			
			// fin
			writer.write("}\n");
			System.out.println( "File created: " + fullpath);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	/*
	 * create Enum
	 */
	public static void create(EnumType enumType, MetaData metadata)
	{
		String fullpath = metadata.getSrcpath() + metadata.getModelpackage().replaceAll("\\.", "/") + "/" + enumType.getSimpleClassName() + ".java";
		File file = new File(fullpath);
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		try(FileWriter writer = new FileWriter(file))
		{
			// initial comment
			writer.write("/*\n * " + enumType.getSimpleClassName() + ".java\n * generated by rose\n */\n\n");
			
			// package declaration
			writer.write("package " + metadata.getModelpackage() + ";\n\n");
			
			// class declaration
			writer.write("\npublic enum " + enumType.getSimpleClassName() + " implements bn.blaszczyk.roseapp.model.Entity\n{");
			
			// options variables
			boolean first = true;
			int count = 1;
			for(String option : enumType)
			{
				if(first)
					first = false;
				else
					writer.write( "," );
				writer.write("\n\t" + option + "(" + count++ + ")" );
			}
			
			writer.write(";\n\n\tprivate int id;\n\n\tprivate " + enumType.getSimpleClassName() + "(int id)\n\t{\n\t\tthis.id = id;\n\t}\n\n"
					+ "\t@Override\n\tpublic Integer getId()\n\t{\n\t\treturn id;\n\t}\n\n"
					+ "\t@Override\n\tpublic void setId(Integer id)\n\t{\n\t}");
			// fin
			writer.write("\n}\n");
			System.out.println( "File created: " + fullpath);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	
}
