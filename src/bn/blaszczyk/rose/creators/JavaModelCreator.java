package bn.blaszczyk.rose.creators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import bn.blaszczyk.rose.*;
import bn.blaszczyk.rose.model.*;


public class JavaModelCreator {
	
	private static final String FETCH_TYPE = "EAGER";

	public static String getGetterName(Field field)
	{
		if(field instanceof PrimitiveField && ((PrimitiveField)field).getType().equals(PrimitiveType.BOOLEAN))
			return "is" + field.getCapitalName();
		if(field instanceof EntityField && ((EntityField)field).getType().isSecondMany())
			return "get" + field.getCapitalName() + "s";
		return "get" + field.getCapitalName();
	}
	
	public static String getSetterName(Field field)
	{
		if(field instanceof EntityField && ((EntityField)field).getType().isSecondMany())
			return "set" + field.getCapitalName() + "s";
		return "set" + field.getCapitalName();
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
		try(Writer writer = new FileWriter(file))
		{
			// initial comment
			writer.write("/*\n * " + entity.getSimpleClassName() + ".java\n * generated by rose\n */\n");
			
			// package declaration
			writer.write("package " + metadata.getModelpackage() + ";\n\n");
			
			if(metadata.isUsingAnnotations())
				writeAnnotationHeader(entity, writer);
			
			writeClassDeclaration(entity, writer);			
			
			writer.write("{\n");
			
			writeFieldDeclarations(entity, writer);
			writeConstructors(entity, writer);
			writeGettersSetters(entity, writer, metadata.isUsingAnnotations());
			writeOverwrittenMethods(entity, writer);
			
			switch(entity.getImplInterface())
			{
			case Writable:
				writeWritableMethods(entity, writer, metadata.isUsingAnnotations()); //fallthrough
			case Readable:
				writeReadableMethods(entity, writer, metadata.isUsingAnnotations()); //fallthrough
			case Identifyable:
			}
			
			writer.write("}\n");
			System.out.println( "File created: " + fullpath);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}	

	private static void writeAnnotationHeader( Entity entity, Writer writer) throws IOException
	{
		writer.write("import javax.persistence.*;\n\n"
				+ "@Entity\n"
				+ "@Table(name=\"" + entity.getSimpleClassName() + "\")\n");
	}
	
	private static void writeClassDeclaration( Entity entity, Writer writer) throws IOException
	{
		writer.write("public class " + entity.getSimpleClassName() + " implements bn.blaszczyk.rose.model." 
					+ entity.getImplInterface() + ", Comparable<" + entity.getSimpleClassName() + ">\n");
	}
	
	private static void writeFieldDeclarations( Entity entity, Writer writer) throws IOException
	{
		// id
		writer.write("\tprivate int id = -1;\n");

		// primitive and enum fields
		for(Field field : entity.getFields())
			if( field instanceof PrimitiveField)
			{
				PrimitiveField primitiveField = (PrimitiveField) field;
				// declaration
				writer.write("\tprivate " + primitiveField.getType().getJavaname() + " "  );
				if(primitiveField.getDefValue() != null && primitiveField.getDefValue() != "" )
					writer.write(  primitiveField.getName() + " = " + String.format( primitiveField.getType().getDefFormat(), primitiveField.getDefValue() ) );
				else
					writer.write(  primitiveField.getName() + " = " + primitiveField.getType().getDefValue() );
				writer.write( ";\n" );
			}
			else if( field instanceof EnumField)
			{
				EnumField enumField = (EnumField) field;
				// enum variables
				writer.write("\tprivate " + enumField.getEnumType().getSimpleClassName() + " " + enumField.getName() + " = " 
						+ enumField.getEnumType().getSimpleClassName() + "." + enumField.getDefValue() + ";\n" );
			}
		// entity fields
		for(EntityField entityfield : entity.getEntityFields())
		{
			if(entityfield.getType().isSecondMany())
				writer.write("\tprivate java.util.Set<" + entityfield.getEntity().getSimpleClassName() + "> " + entityfield.getName() + "s = new java.util.TreeSet<>();\n");
			else
				writer.write("\tprivate " + entityfield.getEntity().getSimpleClassName() + " " + entityfield.getName() + ";\n");
		}
		
	}
	
	private static void writeConstructors(Entity entity, Writer writer) throws IOException
	{
		// default constructor
		writer.write("\n\tpublic " + entity.getSimpleClassName() + "()\n"
				+ "\t{\n"
				+ "\t}\n\n");
	}
	
	private static void writeGettersSetters(Entity entity, Writer writer, boolean usingAnnotations) throws IOException
	{
		// for id:
		if(usingAnnotations)
			writer.write("\t@Id\n"
					+ "\t@GeneratedValue\n"
					+ "\t@Column(name=\"" + entity.getObjectName() + "_id\")\n");
		writer.write("\t@Override\n"
				+ "\tpublic Integer getId()\n"
				+ "\t{\n"
				+ "\t\treturn id;\n"
				+ "\t}\n\n" );
		writer.write("\t@Override\n"
				+ "\tpublic void setId( Integer id )\n"
				+ "\t{\n"
				+ "\t\tthis.id = id;\n"
				+ "\t}\n\n");

		// primitive and enum Fields
		for(Field field : entity.getFields())
			if( field instanceof PrimitiveField)
				writePrimitiveGetterSetter((PrimitiveField) field, writer, usingAnnotations);
			else if( field instanceof EnumField )
				writeEnumGetterSetter((EnumField) field, writer, usingAnnotations);
		
		// entity Fields
		for(EntityField entityField : entity.getEntityFields())
			writeEntityGetterSetter(entityField, writer, usingAnnotations);
	}

	private static void writePrimitiveGetterSetter(PrimitiveField primitiveField, Writer writer, boolean usingAnnotations) throws IOException
	{
		// annotations
		if(usingAnnotations)
			writer.write("\t@Column(name=\"" + primitiveField.getName() + "\")\n");
		// getter
		writer.write("\tpublic " + primitiveField.getType().getJavaname() + " " + getGetterName(primitiveField) + "()\n"
				+ "\t{\n"
				+ "\t\treturn " + primitiveField.getName() + ";\n"
				+ "\t}\n\n" );
		// setter
		writer.write("\tpublic void " + getSetterName(primitiveField) + "( " + primitiveField.getType().getJavaname() + " " + primitiveField.getName() + " )\n"
				+ "\t{\n"
				+ "\t\tthis." + primitiveField.getName() + " = " + primitiveField.getName() + ";\n"
				+ "\t}\n\n" );
	}

	private static void writeEnumGetterSetter(EnumField enumField, Writer writer, boolean usingAnnotations) throws IOException
	{
		// annotations
		if(usingAnnotations)
			writer.write("\t@Column(name=\"" + enumField.getName() + "\")\n"
					+ "\t@Enumerated(EnumType.ORDINAL)\n");
		// getter
		writer.write("\tpublic " + enumField.getEnumType().getSimpleClassName() + " " + getGetterName(enumField) + "()\n"
				+ "\t{\n"
				+ "\t\treturn " + enumField.getName() + ";\n"
				+ "\t}\n\n" );
	
		// setter
		writer.write("\tpublic void " + getSetterName(enumField) + "( " + enumField.getEnumType().getSimpleClassName() + " " + enumField.getName() + " )\n"
				+ "\t{\n"
				+ "\t\tthis." + enumField.getName() + " = " + enumField.getName() + ";\n"
				+ "\t}\n\n" );
	}

	private static void writeEntityGetterSetter(EntityField entityField, Writer writer, boolean usingAnnotations) throws IOException
	{
		if(usingAnnotations)
			writeEntityAnnotations(entityField, writer);
		if(entityField.getType().isSecondMany())
			writeMultipleEntityGetterSetter(entityField, writer);
		else
			writeSingleEntityGetterSetter(entityField, writer);
	}
	

	private static void writeEntityAnnotations(EntityField entityField, Writer writer ) throws IOException
	{
		switch (entityField.getType())
		{
		case ONETOONE:
			if(entityField.getName().compareTo(entityField.getCounterName()) < 0 )
				writer.write("\t@OneToOne( fetch=FetchType." + FETCH_TYPE + " ) \n"
					+ "\t@JoinColumn( name=\"" + entityField.getName() + "_id\" )\n" );
			else
				writer.write("\t@OneToOne( fetch=FetchType." + FETCH_TYPE + ", mappedBy=\"" + 		entityField.getCounterName() + "\" )\n");	
			break;
		case ONETOMANY:
			writer.write("\t@OneToMany( fetch=FetchType." + FETCH_TYPE + ", mappedBy=\"" + 		entityField.getCounterName() + "\" )\n");
			break;
		case MANYTOONE:
			writer.write("\t@ManyToOne( fetch=FetchType." + FETCH_TYPE + " ) \n"
					+ "\t@JoinColumn( name=\"" + entityField.getName() + "_id\" )\n" );						
			break;
		case MANYTOMANY:
			if(entityField.getName().compareTo(entityField.getCounterName()) < 0 )
				writer.write("\t@ManyToMany( fetch = FetchType." + FETCH_TYPE + " )\n"
					+ "\t@JoinTable( name = \"" + SQLCreator.getManyToManyTableName(entityField) +  "\", "
					+ "\t\tjoinColumns = { @JoinColumn(name = \"" + entityField.getCounterName() + "_id\", nullable = false, updatable = false ) },"
					+ "\t\tinverseJoinColumns = { @JoinColumn(name = \"" + entityField.getName() + "_id\", nullable = false, updatable = false ) } )\n");
			else
				writer.write("\t@ManyToMany( fetch = FetchType." + FETCH_TYPE + ", mappedBy = \"" + entityField.getCounterName() + "s\" )\n");							
			break;
		}
	}

	private static void writeSingleEntityGetterSetter(EntityField entityField, Writer writer ) throws IOException
	{
		// getter
		writer.write("\tpublic " + entityField.getEntity().getSimpleClassName() + " " + getGetterName(entityField) + "()\n"
				+ "\t{\n"
				+ "\t\treturn " + entityField.getName() + ";\n"
				+ "\t}\n\n" );
		
		// setter
		writer.write("\tpublic void " + getSetterName(entityField) + "( " + entityField.getEntity().getSimpleClassName() + " " 	+ entityField.getName() + " )\n"
				+ "\t{\n"
				+ "\t\tthis." + entityField.getName() + " = " + entityField.getName() + ";\n"
				+ "\t}\n\n" );
	}

	private static void writeMultipleEntityGetterSetter(EntityField entityField, Writer writer ) throws IOException
	{
		// getter
		writer.write("\tpublic java.util.Set<" + entityField.getEntity().getSimpleClassName() + "> " + getGetterName(entityField)	+ "()\n"
				+ "\t{\n"
				+ "\t\treturn " + entityField.getName() + "s;\n"
				+ "\t}\n\n" );
	
		// setter
		writer.write("\tpublic void " + getSetterName(entityField) + "( java.util.Set<" + entityField.getEntity().getSimpleClassName() + "> " + entityField.getName() + "s )\n"
				+ "\t{\n"
				+ "\t\tthis." + entityField.getName() + "s = " + entityField.getName() + "s;\n"
				+ "\t}\n\n" );
	}
	
	private static void writeOverwrittenMethods(Entity entity, Writer writer ) throws IOException
	{
		// Comparable.compareto
		writer.write("\t@Override\n"
				+ "\tpublic int compareTo(" + entity.getSimpleClassName() + " that)\n"
				+ "\t{\n"
				+ "\t\treturn Integer.compare( this.id, that.id );\n"
				+ "\t}\n\n");
		
		// toString
		String toString = "\"" + entity.getToString() + "\"";
		for(Field field : entity.getFields() )
			toString = toString.replaceAll("\\%" + field.getName(), "\" + " + field.getName() + " + \"");	
		for(EntityField entityField : entity.getEntityFields() )
			toString = toString.replaceAll("\\%" + entityField.getName(), "\" + String.valueOf(" + entityField.getName() + ") + \"");			
		toString = toString.replaceAll("\\\"\\\" \\+ ", "").replaceAll(" \\+ \\\"\\\"", "");
		writer.write("\t@Override\n"
				+ "\tpublic String toString()\n"
				+ "\t{\n"
				+ "\t\treturn " + toString + ";\n"
				+ "\t}\n\n");
		
		// equals
		writer.write("\t@Override\n"
				+ "\tpublic boolean equals( Object that)\n"
				+ "\t{\n"
				+ "\t\tif(!(that instanceof " + entity.getSimpleClassName() + "))\n"
				+ "\t\t\treturn false;\n"
				+ "\t\treturn this.id == ((" + entity.getSimpleClassName() + ")that).id;\n"
				+ "\t}\n\n");
	}
	
	private static void writeReadableMethods(Entity entity, Writer writer, boolean usingAnnotations) throws IOException
	{
			
		// public String getEntityName();
		if(usingAnnotations)
			writeTransistenceAnnotation(writer);
		writer.write("\t@Override\n"
				+ "\tpublic String getEntityName()\n"
				+ "\t{\n"
				+ "\t\treturn \"" + entity.getSimpleClassName() + "\";\n"
				+ "\t}\n\n");
		
		// public int getFieldCount();
		if(usingAnnotations)
			writeTransistenceAnnotation(writer);
		writer.write("\t@Override\n"
				+ "\tpublic int getFieldCount()\n"
				+ "\t{\n"
				+ "\t\treturn " + entity.getFields().size() + ";\n"
				+ "\t}\n\n");
		
		
		// public String getFieldName( int index );
		if(usingAnnotations)
			writeTransistenceAnnotation(writer);
		writer.write("\t@Override\n"
				+ "\tpublic String getFieldName(int index)\n"
				+ "\t{\n"
				+ "\t\tswitch(index)\n"
				+ "\t\t{\n");
		int count = 0;
		for(Field field : entity.getFields())
			writer.write("\t\tcase " + count++ + ":\n"
					+ "\t\t\treturn \"" + field.getCapitalName() +  "\";\n" );
		writer.write("\t\t}\n"
				+ "\t\treturn \"\";\n"
				+ "\t}\n\n");
		
		// public Object getFieldValue( int index );
		if(usingAnnotations)
			writeTransistenceAnnotation(writer);
		writer.write("\t@Override\n"
				+ "\tpublic Object getFieldValue(int index)\n"
				+ "\t{\n"
				+ "\t\tswitch(index)\n"
				+ "\t\t{\n");
		count = 0;
		for(Field field : entity.getFields())
			writer.write("\t\tcase " + count++ + ":\n"
					+ "\t\t\treturn " + getGetterName(field) +  "();\n" );
		writer.write("\t\t}\n"
				+ "\t\treturn null;\n"
				+ "\t}\n\n");
		
		// public int getEntityCount();
		if(usingAnnotations)
			writeTransistenceAnnotation(writer);
		writer.write("\t@Override\n"
				+ "\tpublic int getEntityCount()\n"
				+ "\t{\n"
				+ "\t\treturn " + entity.getEntityFields().size() + ";\n"
				+ "\t}\n\n");
		
		// public Object getEntityValue( int index );
		if(usingAnnotations)
			writeTransistenceAnnotation(writer);
		writer.write("\t@Override\n"
				+ "\tpublic Object getEntityValue(int index)\n"
				+ "\t{\n"
				+ "\t\tswitch(index)\n"
				+ "\t\t{\n");
		count = 0;
		for(EntityField entityField : entity.getEntityFields())
			writer.write("\t\tcase " + count++ + ":\n"
					+ "\t\t\treturn " + getGetterName(entityField)+ "();\n" );
		writer.write("\t\t}\n"
				+ "\t\treturn null;\n"
				+ "\t}\n\n");
		
		// public String getEntityName( int index );
		if(usingAnnotations)
			writeTransistenceAnnotation(writer);
		writer.write("\t@Override\n"
				+ "\tpublic String getEntityName(int index)\n"
				+ "\t{\n"
				+ "\t\tswitch(index)\n"
				+ "\t\t{\n");
		count = 0;
		for(EntityField entityField : entity.getEntityFields())
			writer.write("\t\tcase " + count++ + ":\n"
					+ "\t\t\treturn \"" + entityField.getCapitalName() + ( entityField.getType().isSecondMany() ? "s" : "" ) 
					+  "\";\n" );
		writer.write("\t\t}\n"
				+ "\t\treturn \"\";\n"
				+ "\t}\n\n");
		
		// public RelationType getRelationType( int index );			
		if(usingAnnotations)
			writeTransistenceAnnotation(writer);
		writer.write("\t@Override\n"
				+ "\tpublic bn.blaszczyk.roseapp.model.RelationType getRelationType(int index)\n"
				+ "\t{\n"
				+ "\t\tswitch(index)\n"
				+ "\t\t{\n");
		count = 0;
		for(EntityField entityField : entity.getEntityFields())
			writer.write("\t\tcase " + count++ + ":\n"
					+ "\t\t\treturn bn.blaszczyk.roseapp.model.RelationType." + entityField.getType().name() + ";\n" );
		writer.write("\t\t}\n"
				+ "\t\treturn null;\n"
				+ "\t}\n\n");
		
		// public Class<?> getEntityClass( int index );	
		if(usingAnnotations)
			writeTransistenceAnnotation(writer);
		writer.write("\t@Override\n"
				+ "\tpublic Class<?> getEntityClass(int index)\n"
				+ "\t{\n"
				+ "\t\tswitch(index)\n"
				+ "\t\t{\n");
		count = 0;
		for(EntityField entityField : entity.getEntityFields())
			writer.write("\t\tcase " + count++ + ":\n"
					+ "\t\t\treturn " + entityField.getEntity().getSimpleClassName() + ".class;\n" );
		writer.write("\t\t}\n"
				+ "\t\treturn null;\n"
				+ "\t}\n\n");
		
		// public int getLength1( int index );			
		if(usingAnnotations)
			writeTransistenceAnnotation(writer);
		writer.write("\t@Override\n"
				+ "\tpublic int getLength1(int index)\n"
				+ "\t{\n"
				+ "\t\tswitch(index)\n"
				+ "\t\t{\n");
		count = 0;
		for(Field field : entity.getFields())
			if(field instanceof PrimitiveField)
			writer.write("\t\tcase " + count++ + ":\n"
					+ "\t\t\treturn " + ((PrimitiveField)field).getLength1() + ";\n" );
		writer.write("\t\t}\n"
				+ "\t\treturn 0;\n"
				+ "\t}\n\n");
		
		// public int getLength2( int index );			
		if(usingAnnotations)
			writeTransistenceAnnotation(writer);
		writer.write("\t@Override\n"
				+ "\tpublic int getLength2(int index)\n"
				+ "\t{\n"
				+ "\t\tswitch(index)\n"
				+ "\t\t{\n");
		count = 0;
		for(Field field : entity.getFields())
			if(field instanceof PrimitiveField)
			writer.write("\t\tcase " + count++ + ":\n"
					+ "\t\t\treturn " + ((PrimitiveField)field).getLength2() + ";\n" );
		writer.write("\t\t}\n"
				+ "\t\treturn 0;\n"
				+ "\t}\n\n");
	}
	
	private static void writeWritableMethods(Entity entity, Writer writer, boolean usingAnnotations) throws IOException
	{			
			// setField
			int count = 0;
			if(usingAnnotations)
				writeTransistenceAnnotation(writer);
			writer.write("\t@Override\n"
					+ "\tpublic void setField( int index, Object value )\n"
					+ "\t{\n" );
			writer.write("\t\tswitch( index )\n"
					+ "\t\t{\n");
			for(Field field : entity.getFields())
				if( field instanceof PrimitiveField)
				{
					PrimitiveField primitiveField = (PrimitiveField) field;
					writer.write("\t\tcase " + count++ + ":\n"
							+ "\t\t\t" + getSetterName(primitiveField) + "( " );
					switch(primitiveField.getType())
					{
					case VARCHAR:
					case CHAR:
						writer.write( "value.toString()" );
						break;
					case INT:
						writer.write( "(Integer) value" );
						break;
					case DATE:
						writer.write( "(java.util.Date) value" );
						break;
					case NUMERIC:
						writer.write( "(java.math.BigDecimal) value" );
						break;
					case BOOLEAN:
						writer.write( "(Boolean) value" ) ;
					}
					writer.write( " );\n"
							+ "\t\t\tbreak;\n" );
				}
				else if( field instanceof EnumField )
				{
					EnumField enumField = (EnumField) field;
					writer.write("\t\tcase " + count++ + ":\n"
							+ "\t\t\t\t" + getSetterName(enumField) + "( (" + enumField.getEnumType().getSimpleClassName() + ")value );\n" 
							+ "\t\t\tbreak;\n" );					
				}
			writer.write("\t\t}\n"
					+ "\t}\n\n");

			// setEntity
			count = 0;
			if(usingAnnotations)
				writeTransistenceAnnotation(writer);
			writer.write("\t@Override\n"
					+ "\tpublic void setEntity( int index, bn.blaszczyk.roseapp.model.Writable value )\n"
					+ "\t{\n"
					+ "\t\tswitch( index )\n"
					+ "\t\t{\n");
			for(EntityField entityField : entity.getEntityFields())
			{
				if( !entityField.getType().isSecondMany() )
				{
					writer.write("\t\tcase " + count + ":\n");
					if(entityField.getType().isFirstMany())
						writer.write( "\t\t\tif(" + getGetterName(entityField) + "() != null)\n"
								+ "\t\t\t\t" + getGetterName(entityField) + "()."
								+ getGetterName(entityField.getCouterpart()) + "().remove( this );\n"
								+ "\t\t\tif(value != null)\n"
								+ "\t\t\t\t((" + entityField.getEntity().getSimpleClassName() +  ")value)." 
								+ getGetterName(entityField.getCouterpart()) + "().add( this );\n");
					writer.write( "\t\t\t" + getSetterName(entityField) + "( (" + entityField.getEntity().getSimpleClassName() + ")value );\n"
							+ "\t\t\tbreak;\n" );
				}		
				count++;
			}
			writer.write("\t\t}\n"
					+ "\t}\n\n");

			// addEntity()
			count = 0;
			if(usingAnnotations)
				writeTransistenceAnnotation(writer);
			writer.write("\t@Override\n"
					+ "\tpublic void addEntity( int index,  bn.blaszczyk.roseapp.model.Writable value )\n"
					+ "\t{\n"
					+ "\t\tif( value == null)\n"
					+ "\t\t\treturn;\n"
					+ "\t\tswitch( index )\n"
					+ "\t\t{\n");
			for(EntityField entityField : entity.getEntityFields())
			{
				if( entityField.getType().isSecondMany() )
				{
					writer.write("\t\tcase " + count + ":\n");
					if(entityField.getType().isFirstMany())
						writer.write( "\t\t\t((" + entityField.getEntity().getSimpleClassName() +  ")value)." 
								+ getGetterName(entityField.getCouterpart()) + "().add( this );\n");
					else
						writer.write( "\t\t\tif( ((" + entityField.getEntity().getSimpleClassName() +  ")value)." 
									+ getGetterName(entityField.getCouterpart()) + "() != null)\n"
									+ "\t\t\t\t((" + entityField.getEntity().getSimpleClassName() +  ")value)." 
									+ getGetterName(entityField.getCouterpart()) + "()." + getGetterName(entityField)
									+ "().remove( (" + entityField.getEntity().getSimpleClassName() +  ")value );\n"
									+ "\t\t\t((" + entityField.getEntity().getSimpleClassName() +  ")value)." + getSetterName(entityField.getCouterpart()) + "( this );\n");
					writer.write( "\t\t\t" + getGetterName(entityField) + "().add( (" + entityField.getEntity().getSimpleClassName() + ")value );\n"
								+ "\t\t\tbreak;\n" );
				}	
				count++;
			}
			writer.write("\t\t}\n"
					+ "\t}\n\n");
			
			// removeEntity()
			count = 0;
			if(usingAnnotations)
				writeTransistenceAnnotation(writer);
			writer.write("\t@Override\n"
					+ "\tpublic void removeEntity( int index,  bn.blaszczyk.roseapp.model.Writable value )\n"
					+ "\t{\n"
					+ "\t\tif( value == null )\n"
					+ "\t\t\treturn;\n"
					+ "\t\tswitch( index )\n"
					+ "\t\t{\n");
			for(EntityField entityField : entity.getEntityFields())
			{
				if( entityField.getType().isSecondMany() )
				{
					writer.write("\t\tcase " + count + ":\n"
							+ "\t\t\tif( ! " + getGetterName(entityField) + "().contains( (" + entityField.getEntity().getSimpleClassName() + ")value ) )\n"
							+ "\t\t\t\treturn;\n");
					if(entityField.getType().isFirstMany())
						writer.write( "\t\t\t((" + entityField.getEntity().getSimpleClassName() +  ")value)." 
								+ getGetterName(entityField.getCouterpart()) + "().remove( this );\n");
					else
						writer.write( "\t\t\t((" + entityField.getEntity().getSimpleClassName() +  ")value)." + getSetterName(entityField.getCouterpart()) + "( null );\n");
					writer.write( "\t\t\t" + getGetterName(entityField) + "().remove( (" + entityField.getEntity().getSimpleClassName() + ")value );\n"
							+ "\t\t\tbreak;\n" );
				}	
				count++;
			}
			writer.write("\t\t}\n"
					+ "\t}\n\n");
	}
	
	private static void writeTransistenceAnnotation(Writer writer) throws IOException
	{
		writer.write("\t@Transient\n");
	}
	
	
}
