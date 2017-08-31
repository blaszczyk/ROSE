package bn.blaszczyk.rose.parser;

import java.text.ParseException;
import java.util.Scanner;

import bn.blaszczyk.rose.MetaData;
import bn.blaszczyk.rose.model.*;

public class EntityParser {
	
	private final MetaData metadata;
	
	public EntityParser(MetaData metadata)
	{
		this.metadata = metadata;
	}
	

	public EntityModel parseEntity(String args, Scanner scanner ) throws ParseException
	{
		String[] split = args.split(":");
		ImplInterface implInterface = ImplInterface.NONE;
		if(split.length > 1)
			if(split[1].toLowerCase().contains("p"))
				implInterface = ImplInterface.REPRESENTABLE;
			else if(split[1].toLowerCase().contains("w"))
				implInterface = ImplInterface.WRITABLE;
			else if(split[1].toLowerCase().contains("r"))
				implInterface = ImplInterface.READABLE;
			else if(split[1].toLowerCase().contains("i"))
				implInterface = ImplInterface.IDENTIFYABLE;
		EntityModel entityModel = new EntityModel(split[0].trim(),metadata.getModelpackage(), implInterface);
		String line;
		while(scanner.hasNextLine() && !( line = scanner.nextLine().trim() ).startsWith( "end entity" ) )
		{
			split = line.split("\\s+",2);
			if(split.length == 1 )
				continue;
			String command = split[0];
			if( "enum".equalsIgnoreCase(command) )
			{
				split = split[1].split("\\s+");
				if( split.length == 1) 
					entityModel.addField(new EnumField(split[0]));
				else if( split.length == 2)
					entityModel.addField(new EnumField(split[0], split[1]));
				else
					entityModel.addField(new EnumField(split[0], split[1],split[2]));
				continue;
			}
			if( isPrimitiveType(command) )
			{
				split = split[1].split("\\s+",2);
				if(split.length == 2)
					entityModel.addField(new PrimitiveField(command, split[0], split[1]));
				else
					entityModel.addField(new PrimitiveField(command, split[0]));
			}
			else if( isRelationType(command) )
			{
				split = split[1].split("\\s+", 3);
					if(split.length == 3)
						entityModel.addEntityField(new EntityField(split[0], getRelationType(command), split[1], split[2]) );	
					else if(split.length == 2)
						entityModel.addEntityField(new EntityField(split[0], getRelationType(command), split[1]) );					
					else
						entityModel.addEntityField(new EntityField(split[0], getRelationType(command) ) );			
			}
			else if( "tostring".equalsIgnoreCase(command))
			{
				if(split.length == 2)
					entityModel.setToString(split[1]);
			}
			else 
				System.out.println("Invalid Field: " + line);
		}
		return entityModel;
	}

	private static boolean isPrimitiveType(String sqltype)
	{
		for(PrimitiveType primitiveType : PrimitiveType.values())
			if( sqltype.toLowerCase().startsWith( primitiveType.getSqlname().toLowerCase() ) )
				return true;
		return false;
	}

	private static boolean isRelationType(String name)
	{
		for(RelationType type : RelationType.values())
			if( name .equalsIgnoreCase( type.name() ) )
				return true;
		return false;
	}
	
	private static RelationType getRelationType(String typeName)
	{
		for( RelationType type : RelationType.values() )
			if(type.name().equalsIgnoreCase(typeName))
				return type;
		return null;
	}
	
}
