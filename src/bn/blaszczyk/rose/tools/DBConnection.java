package bn.blaszczyk.rose.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import bn.blaszczyk.rose.creators.CreateException;

public class DBConnection
{
	/*
	 * DataBase Connection
	 */
	private static Connection dbConn;

	/*
	 * Static DB Access Methods
	 */
	public static void connectToDatabase(final String driverClass, final String connectionString, final String userID, final String passWord) throws CreateException
	{
		try
		{
			Class.forName(driverClass).newInstance();
			dbConn = DriverManager.getConnection(connectionString, userID, passWord);
		}
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e)
		{
			dbConn = null;
			throw new CreateException("error connecting to DB", e);
		}
	}
	
	public static void closeConnection()
	{
		if (dbConn == null)
			return;
		try
		{
			dbConn.close();
		}
		catch (SQLException e)
		{}
		dbConn = null;
	}

	public static Connection getConnection()
	{
		return dbConn;
	}

	public static int executeUpdate(final String sql) throws CreateException
	{
		final Statement stmt;
		if (dbConn != null)
			try
			{
				stmt = dbConn.createStatement();
				return stmt.executeUpdate(sql);
			}
			catch (SQLException e)
			{
				throw new CreateException("error executing update with '" + sql + "'", e);
			}
		return -1;
	}

	
	public static boolean databaseExists(final String dbName) throws CreateException
	{
		final ResultSet rs;
		try
		{
			rs = DBConnection.getConnection().getMetaData().getCatalogs();
			while(rs.next())
				if(rs.getString(1).equals(dbName))
					return true;
		}
		catch (SQLException e)
		{
			throw new CreateException("Fehler beim Zugriff auf SQL Server",e);
		}
		return false;
	}
	
	public static void createDatabase(final String dbName) throws CreateException
	{
		String sql = "CREATE DATABASE " + dbName;
		DBConnection.executeUpdate(sql);
	}
	
}
