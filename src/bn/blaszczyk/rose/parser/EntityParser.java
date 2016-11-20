package bn.blaszczyk.rose.parser;

import java.text.ParseException;
import java.util.Scanner;

import bn.blaszczyk.rose.MetaData;
import bn.blaszczyk.rose.model.Entity;
import bn.blaszczyk.rose.model.EntityField;
import bn.blaszczyk.rose.model.EnumField;
import bn.blaszczyk.rose.model.EnumType;
import bn.blaszczyk.rose.model.ImplInterface;
import bn.blaszczyk.rose.model.PrimitiveField;
import bn.blaszczyk.rose.model.PrimitiveType;
import bn.blaszczyk.rose.model.RelationType;

public class EntityParser {
	
	private final MetaData metadata;
	private final RoseParser roseParser;
	
	public EntityParser(MetaData metadata, RoseParser roseParser)
	{
		this.metadata = metadata;
		this.roseParser = roseParser;
	}
	

	public Entity parseEntity(String args, Scanner scanner ) throws ParseException
	{
		String[] split = args.split(":");
		ImplInterface implInterface = ImplInterface.Identifyable;
		if(split.length > 1)
			if(split[1].toLowerCase().contains("w"))
				implInterface = ImplInterface.Writable;
			else if(split[1].toLowerCase().contains("r"))
				implInterface = ImplInterface.Readable;
		Entity entity = new Entity(split[0].trim(),metadata.getModelpackage(), implInterface);
		String line;
		while(scanner.hasNextLine() && !( line = scanner.nextLine().trim() ).startsWith( "end entity" ) )
		{
			split = line.split("\\s+",2);
			String command = split[0];
			EnumType subenum;
			if( (subenum = roseParser.getEnumType(command)) != null )
			{
				if( split.length == 1) 
					entity.addField(new EnumField(subenum));
				else 
				{
					split = line.split("\\s+",2);
					if( split.length == 1)
						entity.addField(new EnumField(subenum, split[0]));
					else
						entity.addField(new EnumField(subenum, split[0],split[1]));
				}
				continue;
			}
			else if(split.length == 1 )
				continue;
			if( isPrimitiveType(command) )
			{
				split = split[1].split("\\s+",2);
				if(split.length == 2)
					entity.addField(new PrimitiveField(command, split[0], split[1]));
				else
					entity.addField(new PrimitiveField(command, split[0]));
			}
			else if( isRelationType(command) )
			{
				split = split[1].split("\\s+", 3);
				Entity subentity;
				if( (subentity = roseParser.getEntityType(split[0])) != null )
					if(split.length == 3)
						entity.addEntityField(new EntityField(subentity, getRelationType(command), split[1],split[2]), true);	
					else if(split.length == 2)
						entity.addEntityField(new EntityField(subentity, getRelationType(command), split[1],split[1]), true);					
					else
						entity.addEntityField(new EntityField(subentity, getRelationType(command),subentity.getObjectName(),entity.getObjectName()), true);			
			}
			else if( command.equalsIgnoreCase("tostring"))
			{
				if(split.length == 2)
					entity.setToString(split[1]);
			}
			else 
				System.out.println("Invalid Field: " + line);
		}
		return entity;
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
