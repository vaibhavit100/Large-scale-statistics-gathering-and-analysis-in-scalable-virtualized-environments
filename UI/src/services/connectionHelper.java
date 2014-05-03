package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectionHelper {

	static Connection con; 
	static String url; 
	static String URL ="jdbc:mysql://localhost:3306/cmpe283_sql";
	static String username= "root" ;
	static String password = "root";
	public static Connection getConnection() 
	{ 
		
		try
		{
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			con=(Connection) DriverManager.getConnection(URL,username,password);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	//	log.info("Exit from getConnection in DataBaseConnection");
		
		return con;
	}
}
