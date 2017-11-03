package com.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.codec.binary.Base64;

public class EmailValidate {
	
	public static boolean checkUser(String username,String password) 
    {

	      boolean st =false;
	      Connection con=null;
	      
	      try {
			Class.forName("com.mysql.jdbc.Driver");
		   } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	      try {
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/palla", "root", "vedas");
			
			PreparedStatement ps =con.prepareStatement
                  ("select username,password from admin_login where username=? and password=?");
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet rs =ps.executeQuery();
			st = rs.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
		return st;
	
    }
	
	public static byte[] decodeImage(String imageDataString) {		
		return Base64.decodeBase64(imageDataString);
	}
	
}
