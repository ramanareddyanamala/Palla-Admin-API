package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

@WebServlet(urlPatterns = { "/login" })
public class UserLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public UserLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

    Connection conn=null;
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		String logindetailes=request.getParameter("logindetailes");
		
		String [] split=logindetailes.split(",");
		
		String username=split[0];
		String password=split[1];
		
		System.out.println("mailid.."+username);
		System.out.println("pass.."+password);
		try {
			Class.forName("com.mysql.jdbc.Driver");
		    } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/palla", "root", "vedas");
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
		if(EmailValidate.checkUser(username, password)){
			 try {
				
				String query="select username,password from admin_login where username=?";
				PreparedStatement ps=conn.prepareStatement(query);
				ps.setString(1, username);
				ResultSet rs = ps.executeQuery();
				while(rs.next())
				{
					
					String username1 =rs.getString("username");
					String password1 = rs.getString("password");
					
					
					String message1="3";
		
					JSONObject jsonobj2 = new JSONObject();
					jsonobj2.put("userrname", username1);
					jsonobj2.put("password", password1);
					jsonobj2.put("response", message1);
					out.println(jsonobj2);
				
				}
				
				
			 } catch (JSONException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		 }
		
	
		 else{
			 try {
				 String message="0";
				 JSONObject jsonobj = new JSONObject();
				
				jsonobj.put("response", message);
				out.println(jsonobj);
				
				
			 } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 }
	}

}
