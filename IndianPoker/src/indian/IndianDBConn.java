package indian;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class IndianDBConn {
	private Connection con;
	public Connection getConnection(){
		return con;
	}
	
	public void closeConnection() throws SQLException{
		System.out.println("DB연결해제");
		con.close();
	}
	
	public IndianDBConn() throws ClassNotFoundException, SQLException{
		String user = "hr";
		String pw = "hr";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";

		Class.forName("oracle.jdbc.driver.OracleDriver");
		con = DriverManager.getConnection(url,user,pw);
	}
}
