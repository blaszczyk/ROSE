/*
 * MetaDataParser.java
 * generated by rose
 */

package bn.blaszczyk.rose;

import java.text.ParseException;

public class MetaDataParser
{
	public static void parseMember( MetaData metaData, String name, String value ) throws ParseException
	{
		switch( name.toLowerCase() )
		{
		case "metadata_id":
			metaData.setMetaData_id( Integer.parseInt( value ) );
			break;
		case "srcpath":
			metaData.setSrcpath( value );
			break;
		case "sqlpath":
			metaData.setSqlpath( value );
			break;
		case "usingforeignkeys":
			metaData.setUsingForeignKeys( Boolean.parseBoolean( value ) );
			break;
		case "dbtype":
			metaData.setDbtype( value );
			break;
		case "dbuser":
			metaData.setDbuser( value );
			break;
		case "dbpassword":
			metaData.setDbpassword( value );
			break;
		case "dbserver":
			metaData.setDbserver( value );
			break;
		case "dbport":
			metaData.setDbport( value );
			break;
		case "dbname":
			metaData.setDbname( value );
			break;
		case "modelpackage":
			metaData.setModelpackage( value );
			break;
		case "usingannotations":
			metaData.setUsingAnnotations( Boolean.parseBoolean( value ) );
			break;
		case "parserpackage":
			metaData.setParserpackage( value );
			break;
		case "parserformat":
			metaData.setParserformat( value );
			break;
		case "controllerpackage":
			metaData.setControllerpackage( value );
			break;
		case "controllerclass":
			metaData.setControllerclass( value );
			break;
		case "entitymodelpackage":
			metaData.setEntitymodelpackage( value );
			break;
		case "entitymodelformat":
			metaData.setEntitymodelformat( value );
			break;
		case "entitymodelfactoryclass":
			metaData.setEntitymodelfactoryclass( value );
			break;
		default:
			System.out.println( "Unknown Member: " + name + " in MetaData");
		}
	}

	public static void setMember( MetaData metaData, String name, Object value ) throws ParseException
	{
		switch( name.toLowerCase() )
		{
		case "metadata_id":
			metaData.setMetaData_id( (Integer) value );
			break;
		case "srcpath":
			metaData.setSrcpath( value.toString() );
			break;
		case "sqlpath":
			metaData.setSqlpath( value.toString() );
			break;
		case "usingforeignkeys":
			metaData.setUsingForeignKeys( (Boolean) value );
			break;
		case "dbtype":
			metaData.setDbtype( value.toString() );
			break;
		case "dbuser":
			metaData.setDbuser( value.toString() );
			break;
		case "dbpassword":
			metaData.setDbpassword( value.toString() );
			break;
		case "dbserver":
			metaData.setDbserver( value.toString() );
			break;
		case "dbport":
			metaData.setDbport( value.toString() );
			break;
		case "dbname":
			metaData.setDbname( value.toString() );
			break;
		case "modelpackage":
			metaData.setModelpackage( value.toString() );
			break;
		case "usingannotations":
			metaData.setUsingAnnotations( (Boolean) value );
			break;
		case "parserpackage":
			metaData.setParserpackage( value.toString() );
			break;
		case "parserformat":
			metaData.setParserformat( value.toString() );
			break;
		case "controllerpackage":
			metaData.setControllerpackage( value.toString() );
			break;
		case "controllerclass":
			metaData.setControllerclass( value.toString() );
			break;
		case "entitymodelpackage":
			metaData.setEntitymodelpackage( value.toString() );
			break;
		case "entitymodelformat":
			metaData.setEntitymodelformat( value.toString() );
			break;
		case "entitymodelfactoryclass":
			metaData.setEntitymodelfactoryclass( value.toString() );
			break;
		default:
			System.out.println( "Unknown Member: " + name + " in MetaData");
		}
	}

	public static void setEntity( MetaData metaData, String name, Object value ) throws ParseException
	{
		switch( name.toLowerCase() )
		{
		default:
			System.out.println( "Unknown Single Entitymember: " + name + " in MetaData");
		}
	}

	public static void addEntity( MetaData metaData, String name, Object value ) throws ParseException
	{
		switch( name.toLowerCase() )
		{
		default:
			System.out.println( "Unknown Multiple Entitymember: " + name + " in MetaData");
		}
	}

	public static void deleteEntity( MetaData metaData, String name, Object value ) throws ParseException
	{
		switch( name.toLowerCase() )
		{
		default:
			System.out.println( "Unknown Multiple Entitymember: " + name + " in MetaData");
		}
	}

}
