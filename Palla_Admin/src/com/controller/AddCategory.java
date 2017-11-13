package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

@WebServlet(urlPatterns = {"/addcategory"})
public class AddCategory extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://localhost:3306/palla";

	   static final String USER = "root";
	   static final String PASS = "vedas";
	
	public void init(ServletConfig config) {

}
	
public AddCategory() {
super();
// TODO Auto-generated constructor stub
}

Connection conn=null;
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	
	response.setContentType("application/json; charset=UTF-8");
	PrintWriter out=response.getWriter();
	request.setCharacterEncoding("UTF-8");
	
	String detailes=request.getParameter("category");
	System.out.println("categories.."+detailes);
	String  image_data = null;
	String category = null;
	String foodtype = "veg";
	String availability = null;
    int getValue = 0;
    int getValue1 =0;
    int getValue12 =0;
	try{
			
		final JSONObject obj = new JSONObject(detailes);
		
		  image_data = obj.getString("image_data");
	      category = obj.getString("category");
		  availability = obj.getString("availability");
		  foodtype = obj.getString("categoryname");
	}  catch(Exception e){
		
		
	} 
		      
	try {
		Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		
		e.printStackTrace();
	}
	
	try {
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
	} catch (SQLException e) {
		
		e.printStackTrace();
	}
	
	try {
		String sql = "select * from pallacategory where category=?";
		PreparedStatement st123 = conn.prepareStatement(sql);
		st123.setString(1,category);
		ResultSet rst = st123.executeQuery();
		if(rst.first()) {
			 System.out.println("This category is already exist in table.");
			 JSONObject jsonobj = new JSONObject();
			 String msg = "0";
       	     jsonobj.put("response", msg);
       	     jsonobj.put("message", "This category is already available in the category list.");
       	  	 out.println(jsonobj);
		}
		else {
			try {
				
				
				byte[] imageByteArray = EmailValidate.decodeImage(image_data);
				
				
				try{
					String query12 = "select count(id)+1 from veg";
					Statement st12 = conn.createStatement();
					ResultSet rs12 = st12.executeQuery(query12);
					if(rs12.next()) {
						getValue12 = Integer.parseInt(rs12.getString(1));
						System.out.println("get value..."+getValue12);
					}
					
					PreparedStatement ps=conn.prepareStatement(
							
					"insert into veg(id,image_data) values(?,?)");
					
					ps.setInt(1,getValue12);
					ps.setBytes(2, imageByteArray);
					
					int i=ps.executeUpdate();
					System.out.println(i+" records affected");
					
					try {
						String query = "select count(id) from veg";
						Statement st = conn.createStatement();
						ResultSet rs = st.executeQuery(query);
						if(rs.next()) {
							getValue = Integer.parseInt(rs.getString(1));
							String url = "http://localhost:8080/Image/veg?id="+getValue;
							System.out.println("image url.."+url);
							try{
								String query1 = "select count(cat_id)+1 from pallacategory";
								Statement st1 = conn.createStatement();
								ResultSet rs1 = st1.executeQuery(query1);
								if(rs1.next()) {
									getValue1 = Integer.parseInt(rs1.getString(1));
									System.out.println("get value..."+getValue1);
								}
								String feed = "cat_"+new SimpleDateFormat("ddMMyyy").format(new Date())+getValue1;
								System.out.println("category id..."+feed);
								PreparedStatement ps11=conn.prepareStatement("insert into pallacategory values(?,?,?,?,?)");
								ps11.setString(1, url);
								ps11.setString(2, category);
								ps11.setString(3, foodtype);
								ps11.setString(4, availability);
								ps11.setString(5, feed);
								
								int rowinsert = ps11.executeUpdate();
								
								if(rowinsert>0){
									 JSONObject jsonobj = new JSONObject();
									 String msg = "3";
						        	 jsonobj.put("response", msg);
						        	 jsonobj.put("category-id", feed);
								  	 out.println(jsonobj);
						        }
								else{
									String e= "0";
									JSONObject jsonobj = new JSONObject();
									
										jsonobj.put("response", e);
									
									out.println(jsonobj );
									
								}
							}catch(Exception e) {
								e.printStackTrace();
							}
							System.out.println("get value..."+getValue);
						}
					}catch(Exception e) {
						e.printStackTrace();
					}
					conn.close();
								
					}catch (Exception e) {e.printStackTrace();}
				
				
				System.out.println("Image Successfully Manipulated!");
			} catch (Exception ioe) {
				System.out.println("Exception while reading the Image " + ioe);
			}

			
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	

  if (conn != null) {
      
      try {
          conn.close();
      } catch (SQLException ex) {
          ex.printStackTrace();
      }
  }
  
}
}

