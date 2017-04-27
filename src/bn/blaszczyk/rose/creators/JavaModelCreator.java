package bn.blaszczyk.rose.creators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import bn.blaszczyk.rose.*;
import bn.blaszczyk.rose.model.*;

public class JavaModelCreator {
	
	private static final String ONE_FETCH_TYPE = "LAZY";
	private static final String MANY_FETCH_TYPE = "LAZY";

	public static String getGetterName(Field field)
	{
		if(field instanceof PrimitiveField && ((PrimitiveField)field).getType().equals(PrimitiveType.BOOLEAN))
			return "is" + field.getCapitalName();
		return "get" + field.getCapitalName();
	}
	
	public static String getSetterName(Field field)
	{
		return "set" + field.getCapitalName();
	}
	
	/*
	 * create Entity
	 */
	public static void create(Entity entity, MetaData metadata)
	{
		boolean isImplementation = metadata.isUsingInterfaces();
		String optionalImpl = isImplementation ? "Impl" : "";
		String fullpath = metadata.getSrcpath() + metadata.getModelpackage().replaceAll("\\.", "/") + "/" + entity.getSimpleClassName() + optionalImpl +".java";
		File file = new File(fullpath);
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		try(Writer writer = new FileWriter(file))
		{
			// initial comment
			writer.write("/*\r\n * " + entity.getSimpleClassName() + optionalImpl +".java\r\n * generated by rose\r\n */\r\n");
			
			// package declaration
			writer.write("package " + metadata.getModelpackage() + ";\r\n\r\n");
			
			if(metadata.isUsingAnnotations())
				writeAnnotationHeader(entity, writer);
			
			writeClassDeclaration(entity, metadata.isUsingTimestamp(), isImplementation, writer);			
			
			writer.write("{\r\n");
			
			if(entity.getImplInterface().doesExtend(ImplInterface.IDENTIFYABLE))
				writeIdDeclarations(writer);
				
			writeFieldDeclarations(entity, writer, metadata.isUsingTimestamp());
			writeConstructors(entity, optionalImpl, writer);

			if(entity.getImplInterface().doesExtend(ImplInterface.IDENTIFYABLE))
				writeIdGettersSetters(entity, writer, metadata.isUsingAnnotations());

			writeGettersSetters(entity, optionalImpl,  writer, metadata.isUsingAnnotations());
			if(metadata.isUsingTimestamp())
				writeTimestampGetterSetter(writer, metadata.isUsingAnnotations());
			writeToString(entity, writer);			

			if(entity.getImplInterface().doesExtend(ImplInterface.IDENTIFYABLE))
				writeCompareEqualsById(entity, writer);
			
			if(entity.getImplInterface().doesExtend(ImplInterface.READABLE))
				writeReadableMethods(entity, writer, metadata.isUsingAnnotations());
			
			if(entity.getImplInterface().doesExtend(ImplInterface.WRITABLE))
				writeWritableMethods(entity, writer, metadata.isUsingAnnotations()); 

			writer.write("}\r\n");
			System.out.println( "File created: " + fullpath);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static void writeAnnotationHeader( Entity entity, Writer writer) throws IOException
	{
		writer.write("import javax.persistence.*;\r\n\r\n"
				+ "@Entity\r\n"
				+ "@Table(name=\"" + entity.getObjectName().toLowerCase() + "\")\r\n");
	}
	
	private static void writeClassDeclaration( Entity entity, boolean timestamped, boolean isImplementation, Writer writer) throws IOException
	{
		writer.write("public class " + entity.getSimpleClassName());
		if(isImplementation)
			writer.write("Impl implements " + entity.getSimpleClassName());
		if(entity.getImplInterface() != ImplInterface.NONE)
		{
			if(!isImplementation)
				writer.write(" implements bn.blaszczyk.rose.model." + entity.getImplInterface().getIdentifyer());
			writer.write(", Comparable<" + entity.getSimpleClassName() + ">");
			if(timestamped)
				writer.write(", " + Timestamped.class.getName());
		}
		writer.write( "\r\n");
	}
	
	private static void writeIdDeclarations(  Writer writer) throws IOException
	{
		writer.write("\tprivate int id = -1;\r\n");
	}
		
	private static void writeFieldDeclarations( Entity entity, Writer writer, boolean usingTimestamp) throws IOException
	{
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
				writer.write( ";\r\n" );
			}
			else if( field instanceof EnumField)
			{
				EnumField enumField = (EnumField) field;
				// enum variables
				writer.write("\tprivate " + enumField.getEnumType().getSimpleClassName() + " " + enumField.getName() + " = " 
						+ enumField.getEnumType().getSimpleClassName() + "." + enumField.getDefValue() + ";\r\n" );
			}
		// entity fields
		for(EntityField entityfield : entity.getEntityFields())
		{
			if(entityfield.getType().isSecondMany())
				writer.write("\tprivate java.util.Set<" + entityfield.getEntity().getSimpleClassName() + "> " + entityfield.getName() + " = new java.util.TreeSet<>();\r\n");
			else
				writer.write("\tprivate " + entityfield.getEntity().getSimpleClassName() + " " + entityfield.getName() + ";\r\n");
		}
		if(usingTimestamp)
			writer.write("\tprivate java.util.Date timestamp = new java.util.Date();\r\n");
	}
	
	private static void writeConstructors(Entity entity, String optionalImpl, Writer writer) throws IOException
	{
		// default constructor
		writer.write("\r\n\tpublic " + entity.getSimpleClassName() + optionalImpl + "()\r\n"
				+ "\t{\r\n"
				+ "\t}\r\n\r\n");
	}
	
	private static void writeIdGettersSetters(Entity entity, Writer writer, boolean usingAnnotations) throws IOException
	{
		if(usingAnnotations)
			writer.write("\t@Id\r\n"
					+ "\t@GeneratedValue\r\n"
					+ "\t@Column(name=\"" + entity.getObjectName() + "_id\")\r\n");
		writer.write("\t@Override\r\n"
				+ "\tpublic Integer getId()\r\n"
				+ "\t{\r\n"
				+ "\t\treturn id;\r\n"
				+ "\t}\r\n\r\n" );
		writer.write("\t@Override\r\n"
				+ "\tpublic void setId( Integer id )\r\n"
				+ "\t{\r\n"
				+ "\t\tthis.id = id;\r\n"
				+ "\t}\r\n\r\n");
	}
	
	private static void writeGettersSetters(Entity entity, String optionalImpl,  Writer writer, boolean usingAnnotations) throws IOException
	{
		// primitive and enum Fields
		for(Field field : entity.getFields())
			if( field instanceof PrimitiveField)
				writePrimitiveGetterSetter((PrimitiveField) field, writer, usingAnnotations);
			else if( field instanceof EnumField )
				writeEnumGetterSetter((EnumField) field, writer, usingAnnotations);
		
		// entity Fields
		for(EntityField entityField : entity.getEntityFields())
			writeEntityGetterSetter(entityField,  optionalImpl, writer, usingAnnotations);
	}

	private static void writePrimitiveGetterSetter(PrimitiveField primitiveField, Writer writer, boolean usingAnnotations) throws IOException
	{
		// annotations
		if(usingAnnotations)
			writer.write("\t@Column(name=\"" + primitiveField.getName() + "\")\r\n");
		// getter
		writer.write("\tpublic " + primitiveField.getType().getJavaname() + " " + getGetterName(primitiveField) + "()\r\n"
				+ "\t{\r\n"
				+ "\t\treturn " + primitiveField.getName() + ";\r\n"
				+ "\t}\r\n\r\n" );
		// setter
		writer.write("\tpublic void " + getSetterName(primitiveField) + "( " + primitiveField.getType().getJavaname() + " " + primitiveField.getName() + " )\r\n"
				+ "\t{\r\n"
				+ "\t\tthis." + primitiveField.getName() + " = " + primitiveField.getName() + ";\r\n"
				+ "\t}\r\n\r\n" );
	}

	private static void writeEnumGetterSetter(EnumField enumField, Writer writer, boolean usingAnnotations) throws IOException
	{
		// annotations
		if(usingAnnotations)
			writer.write("\t@Column(name=\"" + enumField.getName() + "\")\r\n"
					+ "\t@Enumerated(EnumType.ORDINAL)\r\n");
		// getter
		writer.write("\tpublic " + enumField.getEnumType().getSimpleClassName() + " " + getGetterName(enumField) + "()\r\n"
				+ "\t{\r\n"
				+ "\t\treturn " + enumField.getName() + ";\r\n"
				+ "\t}\r\n\r\n" );
	
		// setter
		writer.write("\tpublic void " + getSetterName(enumField) + "( " + enumField.getEnumType().getSimpleClassName() + " " + enumField.getName() + " )\r\n"
				+ "\t{\r\n"
				+ "\t\tthis." + enumField.getName() + " = " + enumField.getName() + ";\r\n"
				+ "\t}\r\n\r\n" );
	}

	private static void writeEntityGetterSetter(EntityField entityField, String optionalImpl,  Writer writer, boolean usingAnnotations) throws IOException
	{
		if(usingAnnotations)
			writeEntityAnnotations(entityField, optionalImpl, writer);
		if(entityField.getType().isSecondMany())
			writeMultipleEntityGetterSetter(entityField, writer);
		else
			writeSingleEntityGetterSetter(entityField, writer);
	}
	

	private static void writeEntityAnnotations(EntityField entityField, String optionalImpl, Writer writer ) throws IOException
	{
		switch (entityField.getType())
		{
		case ONETOONE:
			if(entityField.getName().compareTo(entityField.getCounterName()) < 0 )
				writer.write("\t@OneToOne( targetEntity=" + entityField.getEntity().getSimpleClassName() + optionalImpl + ".class, fetch=FetchType." + ONE_FETCH_TYPE + " ) \r\n"
					+ "\t@JoinColumn( name=\"" + entityField.getName() + "_id\" )\r\n" );
			else
				writer.write("\t@OneToOne( targetEntity=" + entityField.getEntity().getSimpleClassName() + optionalImpl + ".class, fetch=FetchType." + ONE_FETCH_TYPE + ", mappedBy=\"" + 		entityField.getCounterName() + "\" )\r\n");	
			break;
		case ONETOMANY:
			writer.write("\t@OneToMany( targetEntity=" + entityField.getEntity().getSimpleClassName() + optionalImpl + ".class, fetch=FetchType." + MANY_FETCH_TYPE + ", mappedBy=\"" + 		entityField.getCounterName() + "\" )\r\n");
			break;
		case MANYTOONE:
			writer.write("\t@ManyToOne( targetEntity=" + entityField.getEntity().getSimpleClassName() + optionalImpl + ".class, fetch=FetchType." + ONE_FETCH_TYPE + " ) \r\n"
					+ "\t@JoinColumn( name=\"" + entityField.getName() + "_id\" )\r\n" );						
			break;
		case MANYTOMANY:
			if(entityField.getName().compareTo(entityField.getCounterName()) < 0 )
				writer.write("\t@ManyToMany( targetEntity=" + entityField.getEntity().getSimpleClassName() + optionalImpl + ".class, fetch = FetchType." + MANY_FETCH_TYPE + " )\r\n"
					+ "\t@JoinTable( name = \"" + SQLCreator.getManyToManyTableName(entityField) +  "\", "
					+ "\t\tjoinColumns = { @JoinColumn(name = \"" + entityField.getCounterName() + "_id\", nullable = false, updatable = false ) },"
					+ "\t\tinverseJoinColumns = { @JoinColumn(name = \"" + entityField.getName() + "_id\", nullable = false, updatable = false ) } )\r\n");
			else
				writer.write("\t@ManyToMany( targetEntity=" + entityField.getEntity().getSimpleClassName() + optionalImpl + ".class, fetch = FetchType." + MANY_FETCH_TYPE + ", mappedBy = \"" + entityField.getCounterName() + "\" )\r\n");							
			break;
		}
	}

	private static void writeSingleEntityGetterSetter(EntityField entityField, Writer writer ) throws IOException
	{
		// getter
		writer.write("\tpublic " + entityField.getEntity().getSimpleClassName() + " " + getGetterName(entityField) + "()\r\n"
				+ "\t{\r\n"
				+ "\t\treturn " + entityField.getName() + ";\r\n"
				+ "\t}\r\n\r\n" );
		
		// setter
		writer.write("\tpublic void " + getSetterName(entityField) + "( " + entityField.getEntity().getSimpleClassName() + " " 	+ entityField.getName() + " )\r\n"
				+ "\t{\r\n"
				+ "\t\tthis." + entityField.getName() + " = " + entityField.getName() + ";\r\n"
				+ "\t}\r\n\r\n" );
	}

	private static void writeMultipleEntityGetterSetter(EntityField entityField, Writer writer ) throws IOException
	{
		// getter
		writer.write("\tpublic java.util.Set<" + entityField.getEntity().getSimpleClassName() + "> " + getGetterName(entityField)	+ "()\r\n"
				+ "\t{\r\n"
				+ "\t\treturn " + entityField.getName() + ";\r\n"
				+ "\t}\r\n\r\n" );
	
		// setter
		writer.write("\tpublic void " + getSetterName(entityField) + "( java.util.Set<" + entityField.getEntity().getSimpleClassName() + "> " + entityField.getName() + " )\r\n"
				+ "\t{\r\n"
				+ "\t\tthis." + entityField.getName() + " = " + entityField.getName() + ";\r\n"
				+ "\t}\r\n\r\n" );
	}

	private static void writeTimestampGetterSetter(Writer writer, Boolean usingAnnotations) throws IOException
	{
		if(usingAnnotations)
			writer.write("\t@Temporal(TemporalType.TIMESTAMP)\r\n"
					+ "\t@Column(name=\"timestamp_update\")\r\n");
		writer.write("\t@Override\r\n"
				+ "\tpublic java.util.Date getTimestamp()\r\n"
				+ "\t{\r\n"
				+ "\t\treturn timestamp;\r\n"
				+ "\t}\r\n\r\n" );
		writer.write("\t@Override\r\n"
				+ "\tpublic void setTimestamp( java.util.Date timestamp )\r\n"
				+ "\t{\r\n"
				+ "\t\tthis.timestamp = timestamp;\r\n"
				+ "\t}\r\n\r\n");
	}

	private static void writeCompareEqualsById(Entity entity, Writer writer ) throws IOException
	{
		// Comparable.compareto
		writer.write("\t@Override\r\n"
				+ "\tpublic int compareTo(" + entity.getSimpleClassName() + " that)\r\n"
				+ "\t{\r\n"
				+ "\t\treturn Integer.compare( this.getId(), that.getId() );\r\n"
				+ "\t}\r\n\r\n");
		
		// equals
//		writer.write("\t@Override\r\n"
//				+ "\tpublic boolean equals( Object that)\r\n"
//				+ "\t{\r\n"
//				+ "\t\tif(!(that instanceof " + entity.getSimpleClassName() + "))\r\n"
//				+ "\t\t\treturn false;\r\n"
//				+ "\t\treturn this.id == ((" + entity.getSimpleClassName() + ")that).id;\r\n"
//				+ "\t}\r\n\r\n");	
	}
	
	private static void writeToString(Entity entity, Writer writer ) throws IOException
	{
		String toString = "\"" + entity.getToString() + "\"";
		for(Field field : entity.getFields() )
			toString = toString.replaceAll("\\%" + field.getName(), "\" + " + field.getName() + " + \"");	
		for(EntityField entityField : entity.getEntityFields() )
			toString = toString.replaceAll("\\%" + entityField.getName(), "\" + String.valueOf(" + entityField.getName() + ") + \"");			
		toString = toString.replaceAll("\\\"\\\" \\+ ", "").replaceAll(" \\+ \\\"\\\"", "");
		writer.write("\t@Override\r\n"
				+ "\tpublic String toString()\r\n"
				+ "\t{\r\n"
				+ "\t\treturn " + toString + ";\r\n"
				+ "\t}\r\n\r\n");
	}
	
	private static void writeReadableMethods(Entity entity, Writer writer, boolean usingAnnotations) throws IOException
	{
			
		// public String getEntityName();
		if(usingAnnotations)
			writeTransistenceAnnotation(writer);
		writer.write("\t@Override\r\n"
				+ "\tpublic String getEntityName()\r\n"
				+ "\t{\r\n"
				+ "\t\treturn \"" + entity.getSimpleClassName() + "\";\r\n"
				+ "\t}\r\n\r\n");
		
		// public int getFieldCount();
		if(usingAnnotations)
			writeTransistenceAnnotation(writer);
		writer.write("\t@Override\r\n"
				+ "\tpublic int getFieldCount()\r\n"
				+ "\t{\r\n"
				+ "\t\treturn " + entity.getFields().size() + ";\r\n"
				+ "\t}\r\n\r\n");
		
		
		// public String getFieldName( int index );
		if(usingAnnotations)
			writeTransistenceAnnotation(writer);
		writer.write("\t@Override\r\n"
				+ "\tpublic String getFieldName(int index)\r\n"
				+ "\t{\r\n"
				+ "\t\tswitch(index)\r\n"
				+ "\t\t{\r\n");
		int count = 0;
		for(Field field : entity.getFields())
			writer.write("\t\tcase " + count++ + ":\r\n"
					+ "\t\t\treturn \"" + field.getCapitalName() +  "\";\r\n" );
		writer.write("\t\t}\r\n"
				+ "\t\treturn \"\";\r\n"
				+ "\t}\r\n\r\n");
		
		// public Object getFieldValue( int index );
		if(usingAnnotations)
			writeTransistenceAnnotation(writer);
		writer.write("\t@Override\r\n"
				+ "\tpublic Object getFieldValue(int index)\r\n"
				+ "\t{\r\n"
				+ "\t\tswitch(index)\r\n"
				+ "\t\t{\r\n");
		count = 0;
		for(Field field : entity.getFields())
			writer.write("\t\tcase " + count++ + ":\r\n"
					+ "\t\t\treturn " + getGetterName(field) +  "();\r\n" );
		writer.write("\t\t}\r\n"
				+ "\t\treturn null;\r\n"
				+ "\t}\r\n\r\n");
		
		// public int getEntityCount();
		if(usingAnnotations)
			writeTransistenceAnnotation(writer);
		writer.write("\t@Override\r\n"
				+ "\tpublic int getEntityCount()\r\n"
				+ "\t{\r\n"
				+ "\t\treturn " + entity.getEntityFields().size() + ";\r\n"
				+ "\t}\r\n\r\n");

		// public Object getEntityValueOne( int index );
		if(usingAnnotations)
			writeTransistenceAnnotation(writer);
		writer.write("\t@Override\r\n"
				+ "\tpublic bn.blaszczyk.rose.model.Readable getEntityValueOne(int index)\r\n"
				+ "\t{\r\n"
				+ "\t\tswitch(index)\r\n"
				+ "\t\t{\r\n");
		count = 0;
		for(EntityField entityField : entity.getEntityFields())
		{
			if(!entityField.getType().isSecondMany())
				writer.write("\t\tcase " + count + ":\r\n"
						+ "\t\t\treturn " + getGetterName(entityField)+ "();\r\n" );
			count++;
		}
		writer.write("\t\t}\r\n"
				+ "\t\treturn null;\r\n"
				+ "\t}\r\n\r\n");

		// public Object getEntityValueMany( int index );
		if(usingAnnotations)
			writeTransistenceAnnotation(writer);
		writer.write("\t@Override\r\n"
				+ "\tpublic java.util.Set<? extends bn.blaszczyk.rose.model.Readable> getEntityValueMany(int index)\r\n"
				+ "\t{\r\n"
				+ "\t\tswitch(index)\r\n"
				+ "\t\t{\r\n");
		count = 0;
		for(EntityField entityField : entity.getEntityFields())
		{
			if(entityField.getType().isSecondMany())
				writer.write("\t\tcase " + count + ":\r\n"
						+ "\t\t\treturn " + getGetterName(entityField)+ "();\r\n" );
			count++;
		}
		writer.write("\t\t}\r\n"
				+ "\t\treturn null;\r\n"
				+ "\t}\r\n\r\n");
		
		// public String getEntityName( int index );
		if(usingAnnotations)
			writeTransistenceAnnotation(writer);
		writer.write("\t@Override\r\n"
				+ "\tpublic String getEntityName(int index)\r\n"
				+ "\t{\r\n"
				+ "\t\tswitch(index)\r\n"
				+ "\t\t{\r\n");
		count = 0;
		for(EntityField entityField : entity.getEntityFields())
			writer.write("\t\tcase " + count++ + ":\r\n"
					+ "\t\t\treturn \"" + entityField.getCapitalName()	+ "\";\r\n" );
		writer.write("\t\t}\r\n"
				+ "\t\treturn \"\";\r\n"
				+ "\t}\r\n\r\n");
		
		// public RelationType getRelationType( int index );			
		if(usingAnnotations)
			writeTransistenceAnnotation(writer);
		writer.write("\t@Override\r\n"
				+ "\tpublic bn.blaszczyk.rose.model.RelationType getRelationType(int index)\r\n"
				+ "\t{\r\n"
				+ "\t\tswitch(index)\r\n"
				+ "\t\t{\r\n");
		count = 0;
		for(EntityField entityField : entity.getEntityFields())
			writer.write("\t\tcase " + count++ + ":\r\n"
					+ "\t\t\treturn bn.blaszczyk.rose.model.RelationType." + entityField.getType().name() + ";\r\n" );
		writer.write("\t\t}\r\n"
				+ "\t\treturn null;\r\n"
				+ "\t}\r\n\r\n");
		
		// public Class<?> getEntityClass( int index );	
		if(usingAnnotations)
			writeTransistenceAnnotation(writer);
		writer.write("\t@Override\r\n"
				+ "\tpublic Class<? extends bn.blaszczyk.rose.model.Readable> getEntityClass(int index)\r\n"
				+ "\t{\r\n"
				+ "\t\tswitch(index)\r\n"
				+ "\t\t{\r\n");
		count = 0;
		for(EntityField entityField : entity.getEntityFields())
			writer.write("\t\tcase " + count++ + ":\r\n"
					+ "\t\t\treturn " + entityField.getEntity().getSimpleClassName() + ".class;\r\n" );
		writer.write("\t\t}\r\n"
				+ "\t\treturn null;\r\n"
				+ "\t}\r\n\r\n");
		
	}
	
	private static void writeWritableMethods(Entity entity, Writer writer, boolean usingAnnotations) throws IOException
	{			
			// setField
			int count = 0;
			if(usingAnnotations)
				writeTransistenceAnnotation(writer);
			writer.write("\t@Override\r\n"
					+ "\tpublic void setField( int index, Object value )\r\n"
					+ "\t{\r\n" );
			writer.write("\t\tswitch( index )\r\n"
					+ "\t\t{\r\n");
			for(Field field : entity.getFields())
				if( field instanceof PrimitiveField)
				{
					PrimitiveField primitiveField = (PrimitiveField) field;
					writer.write("\t\tcase " + count++ + ":\r\n"
							+ "\t\t\t" + getSetterName(primitiveField) + "( (" + primitiveField.getType().getJavaname() + ") value );\r\n"
							+ "\t\t\tbreak;\r\n" );
				}
				else if( field instanceof EnumField )
				{
					EnumField enumField = (EnumField) field;
					writer.write("\t\tcase " + count++ + ":\r\n"
							+ "\t\t\t" + getSetterName(enumField) + "( (" + enumField.getEnumType().getSimpleClassName() + ")value );\r\n" 
							+ "\t\t\tbreak;\r\n" );					
				}
			writer.write("\t\t}\r\n"
					+ "\t}\r\n\r\n");

			// setEntity
			count = 0;
			if(usingAnnotations)
				writeTransistenceAnnotation(writer);
			writer.write("\t@Override\r\n"
					+ "\tpublic void setEntity( int index, bn.blaszczyk.rose.model.Writable value )\r\n"
					+ "\t{\r\n"
					+ "\t\tswitch( index )\r\n"
					+ "\t\t{\r\n");
			for(EntityField entityField : entity.getEntityFields())
			{
				if( !entityField.getType().isSecondMany() )
				{
					writer.write("\t\tcase " + count + ":\r\n");
					if(entityField.getType().isFirstMany())
						writer.write( "\t\t\tif(" + getGetterName(entityField) + "() != null)\r\n"
								+ "\t\t\t\t" + getGetterName(entityField) + "()."
									+ getGetterName(entityField.getCouterpart()) + "().remove( this );\r\n"
								+ "\t\t\tif(value != null)\r\n"
								+ "\t\t\t\t((" + entityField.getEntity().getSimpleClassName() +  ")value)." 
									+ getGetterName(entityField.getCouterpart()) + "().add( this );\r\n");
					else
						writer.write( "\t\t\tif(" + getGetterName(entityField) + "() == " + "value" + ")\r\n"
								+ "\t\t\t\treturn;\r\n"
								+ "\t\t\tif(" + getGetterName(entityField) + "() != null)\r\n"
								+ "\t\t\t\t" + getGetterName(entityField) + "()." + getSetterName(entityField.getCouterpart()) + "(null);\r\n"
								+ "\t\t\tif(value != null)\r\n"
								+ "\t\t\t{\r\n"
								+ "\t\t\t\tif( ((" + entityField.getEntity().getSimpleClassName() + ")value)." + getGetterName(entityField.getCouterpart()) + "() != null)\r\n"
								+ "\t\t\t\t\t((" + entityField.getEntity().getSimpleClassName() + ")value)." + getGetterName(entityField.getCouterpart()) + "()." 
									+ getSetterName(entityField) + "(null);\r\n"
								+ "\t\t\t\t((" + entityField.getEntity().getSimpleClassName() + ")value)." + getSetterName(entityField.getCouterpart()) 
								+ "(this);\r\n"
								+ "\t\t\t}\r\n");
					writer.write( "\t\t\t" + getSetterName(entityField) + "( (" + entityField.getEntity().getSimpleClassName() + ")value );\r\n"
							+ "\t\t\tbreak;\r\n" );
				}		
				count++;
			}
			writer.write("\t\t}\r\n"
					+ "\t}\r\n\r\n");

			// addEntity()
			count = 0;
			if(usingAnnotations)
				writeTransistenceAnnotation(writer);
			writer.write("\t@Override\r\n"
					+ "\tpublic void addEntity( int index,  bn.blaszczyk.rose.model.Writable value )\r\n"
					+ "\t{\r\n"
					+ "\t\tif( value == null)\r\n"
					+ "\t\t\treturn;\r\n"
					+ "\t\tswitch( index )\r\n"
					+ "\t\t{\r\n");
			for(EntityField entityField : entity.getEntityFields())
			{
				if( entityField.getType().isSecondMany() )
				{
					writer.write("\t\tcase " + count + ":\r\n");
					if(entityField.getType().isFirstMany())
						writer.write( "\t\t\t((" + entityField.getEntity().getSimpleClassName() +  ")value)." 
								+ getGetterName(entityField.getCouterpart()) + "().add( this );\r\n");
					else
						writer.write( "\t\t\tif( ((" + entityField.getEntity().getSimpleClassName() +  ")value)." 
									+ getGetterName(entityField.getCouterpart()) + "() != null)\r\n"
									+ "\t\t\t\t((" + entityField.getEntity().getSimpleClassName() +  ")value)." 
									+ getGetterName(entityField.getCouterpart()) + "()." + getGetterName(entityField)
									+ "().remove( (" + entityField.getEntity().getSimpleClassName() +  ")value );\r\n"
									+ "\t\t\t((" + entityField.getEntity().getSimpleClassName() +  ")value)." + getSetterName(entityField.getCouterpart()) + "( this );\r\n");
					writer.write( "\t\t\t" + getGetterName(entityField) + "().add( (" + entityField.getEntity().getSimpleClassName() + ")value );\r\n"
								+ "\t\t\tbreak;\r\n" );
				}	
				count++;
			}
			writer.write("\t\t}\r\n"
					+ "\t}\r\n\r\n");

			// removeEntity()
			count = 0;
			if(usingAnnotations)
				writeTransistenceAnnotation(writer);
			writer.write("\t@Override\r\n"
					+ "\tpublic void removeEntity( int index,  bn.blaszczyk.rose.model.Writable value )\r\n"
					+ "\t{\r\n"
					+ "\t\tif( value == null )\r\n"
					+ "\t\t\treturn;\r\n"
					+ "\t\tswitch( index )\r\n"
					+ "\t\t{\r\n");
			for(EntityField entityField : entity.getEntityFields())
			{
				if( entityField.getType().isSecondMany() )
				{
					writer.write("\t\tcase " + count + ":\r\n"
							+ "\t\t\tif( ! " + getGetterName(entityField) + "().contains( (" + entityField.getEntity().getSimpleClassName() + ")value ) )\r\n"
							+ "\t\t\t\treturn;\r\n");
					if(entityField.getType().isFirstMany())
						writer.write( "\t\t\t((" + entityField.getEntity().getSimpleClassName() +  ")value)." 
								+ getGetterName(entityField.getCouterpart()) + "().remove( this );\r\n");
					else
						writer.write( "\t\t\t((" + entityField.getEntity().getSimpleClassName() +  ")value)." + getSetterName(entityField.getCouterpart()) + "( null );\r\n");
					writer.write( "\t\t\t" + getGetterName(entityField) + "().remove( (" + entityField.getEntity().getSimpleClassName() + ")value );\r\n"
							+ "\t\t\tbreak;\r\n" );
				}	
				count++;
			}
			writer.write("\t\t}\r\n"
					+ "\t}\r\n\r\n");
	}
	
	private static void writeTransistenceAnnotation(Writer writer) throws IOException
	{
		writer.write("\t@Transient\r\n");
	}
	
	
}
