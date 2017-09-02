package bn.blaszczyk.rose.parser;

import java.util.Scanner;

import bn.blaszczyk.rose.MetaData;
import bn.blaszczyk.rose.RoseException;
import bn.blaszczyk.rose.model.*;

public class EntityParser {
	
	private static final String TO_STRING = "tostring";
	private static final String ENUM_TYPE = "enum";
	
	private static final String COMMENT_INIT_CHARS = "#-/\\";
	
	private final MetaData metadata;
	
	public EntityParser(final MetaData metadata)
	{
		this.metadata = metadata;
	}

	public EntityModel parseEntity(final String args, final Scanner scanner ) throws RoseException
	{
		String[] split = args.split(":");
		final ImplInterface implInterface = parseInterface(split);
		final EntityModel entityModel = new EntityModel(split[0].trim(),metadata.getModelpackage(), implInterface);
		
		while(scanner.hasNextLine())
		{
			final String line = scanner.nextLine().trim();
			if(line.startsWith( "end entity" ))
				return entityModel;
			split = line.split("\\s+",2);
			if(isComment(split))
				continue;
			final String command = split[0];
			if( isPrimitiveType(command) )
				entityModel.addField(parsePrimitiveField(split[1], command));
			else if( ENUM_TYPE.equalsIgnoreCase(command) )
				entityModel.addField(parseEnumField(split[1]));
			else if( isRelationType(command) )
				entityModel.addEntityField(parseEntityField(split[1], command));
			else if( TO_STRING.equalsIgnoreCase(command))
				entityModel.setToString(split[1]);
		}
		throw new RoseException("no end entity found for " + entityModel.getClassName());
	}

	private static boolean isComment(final String[] split)
	{
		return split.length < 2 || COMMENT_INIT_CHARS.contains(split[0].substring(0, 1));
	}

	private static ImplInterface parseInterface(final String[] split)
	{
		if(split.length > 1)
		{
			if(split[1].toLowerCase().contains("p"))
				return ImplInterface.REPRESENTABLE;
			else if(split[1].toLowerCase().contains("w"))
				return ImplInterface.WRITABLE;
			else if(split[1].toLowerCase().contains("r"))
				return ImplInterface.READABLE;
			else if(split[1].toLowerCase().contains("i"))
				return ImplInterface.IDENTIFYABLE;
		}
		return ImplInterface.NONE;
	}
	
	private static PrimitiveField parsePrimitiveField(final String args, final String type) throws RoseException
	{
		final String[] split = args.split("\\s+",2);
		if(split.length == 2)
			return new PrimitiveField(type, split[0], split[1]);
		else
			return new PrimitiveField(type, split[0]);
	}

	private static EnumField parseEnumField(final String args)
	{
		final String[] split = args.split("\\s+");
		if( split.length == 1) 
			return new EnumField(split[0]);
		else if( split.length == 2)
			return new EnumField(split[0], split[1]);
		else
			return new EnumField(split[0], split[1],split[2]);
	}

	private static EntityField parseEntityField(final String args, final String relationType)
	{
		final String[] split = args.split("\\s+", 3);
		if(split.length == 3)
			return new EntityField(split[0], getRelationType(relationType), split[1], split[2]);
		else if(split.length == 2)
			return new EntityField(split[0], getRelationType(relationType), split[1]);
		else
			return new EntityField(split[0], getRelationType(relationType) );
	}

	private static boolean isPrimitiveType(final String sqltype)
	{
		for(final PrimitiveType primitiveType : PrimitiveType.values())
			if( sqltype.toLowerCase().startsWith( primitiveType.getSqlname().toLowerCase() ) )
				return true;
		return false;
	}

	private static boolean isRelationType(final String name)
	{
		for(final RelationType type : RelationType.values())
			if( name .equalsIgnoreCase( type.name() ) )
				return true;
		return false;
	}
	
	private static RelationType getRelationType(final String typeName)
	{
		for(final RelationType type : RelationType.values() )
			if(type.name().equalsIgnoreCase(typeName))
				return type;
		return null;
	}
	
}
