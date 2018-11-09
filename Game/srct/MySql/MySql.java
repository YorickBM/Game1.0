package MySql;

import java.sql.*;

public class MySql {
	
	public static String ConnectionUser = "UserName";
	public static String ConnectionPassword = "Password";
	public static String ConnectionUrl = "jdbc:mysql://localhost:3306/KnightsOfRye";
	
	public static void getData(int value, String table, String data){	
		
		try {
			Connection myConn;
			PreparedStatement myStmt;
			ResultSet myRs = null;
			
			myConn = DriverManager.getConnection(ConnectionUrl, ConnectionUser, ConnectionPassword);
			
			myStmt = myConn.prepareStatement("select * from Users where " + table + " = ?");
			myStmt.setDouble(1, value);
			
			myRs = myStmt.executeQuery();
			
			while(myRs.next()) {
				System.out.println(myRs.getString("" + data));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static String getData(String value, String table, String data){
		
		Connection myConn;
		PreparedStatement myStmt;
		ResultSet myRs = null;
		String ReturnData = null;
		
		try {
			
			myConn = DriverManager.getConnection(ConnectionUrl, ConnectionUser, ConnectionPassword);
			
			myStmt = myConn.prepareStatement("select * from Users where " + table + " = ?");
			myStmt.setString(1, value);
			
			myRs = myStmt.executeQuery();
			
			while(myRs.next()) {
				ReturnData = myRs.getString("" + data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ReturnData;
		
	}
	
	public static int getIntData(String value, String table, String data){
		
		Connection myConn;
		PreparedStatement myStmt;
		ResultSet myRs = null;
		int ReturnData = 0;
		
		try {
			
			myConn = DriverManager.getConnection(ConnectionUrl, ConnectionUser, ConnectionPassword);
			
			myStmt = myConn.prepareStatement("select * from Users where " + table + " = ?");
			myStmt.setString(1, value);
			
			myRs = myStmt.executeQuery();
			
			while(myRs.next()) {
				ReturnData = myRs.getInt("" + data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ReturnData;
		
	}
	
	public static void createUser(String Username, String Password){
		
		String InsertableSQL = "insert into Users (Username, password) VALUES (?, ?)";
		
		try {
			Connection myConn;
			PreparedStatement myStmt;
			
			myConn = DriverManager.getConnection(ConnectionUrl, ConnectionUser, ConnectionPassword);
			
			myStmt = myConn.prepareStatement(InsertableSQL);
			myStmt.setString(1, Username);
			myStmt.setString(2, Password);
			//myStmt.setTimestamp(3, getCurrentTimeStamp());
			
			
			myStmt.executeUpdate();
			
			System.out.println("[MySql] Succesfully created the account of " + Username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void updateData(String data, float valueData, String tabel, String valueTable){
		
		String InsertableSQL = "update Users set " + data + " = ? where "+ tabel + " = ?";
		
		try {
			Connection myConn;
			PreparedStatement myStmt;
			
			myConn = DriverManager.getConnection(ConnectionUrl, ConnectionUser, ConnectionPassword);
			
			myStmt = myConn.prepareStatement(InsertableSQL);
			myStmt.setFloat(1, valueData);
			myStmt.setString(2, valueTable);
			
			
			myStmt.executeUpdate();			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void updateData() {
		
		try {
			Connection myConn;
			Statement myStmt;
			
			myConn = DriverManager.getConnection(ConnectionUrl, ConnectionUser, ConnectionPassword);
			
			myStmt = myConn.createStatement();
			String sql = "update Users " +
						" set Username='Paul'" +
						" where ID='1'";
			
			int rowsAffected = myStmt.executeUpdate(sql);
			System.out.println("[MySql] Succesfully updated " + rowsAffected + " row(s)");
			
			myStmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void deleteData() {
		
		try {
			Connection myConn;
			Statement myStmt;
			
			myConn = DriverManager.getConnection(ConnectionUrl, ConnectionUser, ConnectionPassword);
			
			myStmt = myConn.createStatement();
			String sql = "delete from Users where ID='1'";
			
			int rowsAffected = myStmt.executeUpdate(sql);
			System.out.println("[MySql] Succesfully deleted " + rowsAffected + " row(s)");
			
			myStmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void setConnectionUser(String User) {
		ConnectionUser = User;
	}
	
	public static void setConnectionPassword(String Password) {
		ConnectionPassword = Password;
	}
	
	public static void setConnectionUrl(String Url) {
		ConnectionUrl = Url;
	}
}
