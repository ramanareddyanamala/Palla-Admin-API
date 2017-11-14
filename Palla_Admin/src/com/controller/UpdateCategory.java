package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

@WebServlet(urlPatterns = {"/updatecategory"})
public class UpdateCategory extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://localhost:3306/palla";

	   static final String USER = "root";
	   static final String PASS = "vedas";
	
	public void init(ServletConfig config) {

}
	
public UpdateCategory() {
super();
//TODO Auto-generated constructor stub
}

Connection conn=null;
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	
	response.setContentType("application/json; charset=UTF-8");
	PrintWriter out=response.getWriter();
	request.setCharacterEncoding("UTF-8");
	
	String detailes=request.getParameter("updatecategory");
	System.out.println("categories.."+detailes);
	String image_data = null;
	String category = null;
	String availability = null;
	String cat_id = null;
  
	try{
			
		final JSONObject obj = new JSONObject(detailes);
		
		  image_data = obj.getString("image_data");
		  cat_id = obj.getString("cat_id");
	      category = obj.getString("category");
		  availability = obj.getString("availability");
		 
	}  catch(Exception e){
		e.printStackTrace();
		
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
			 String cat_id1 = rst.getString(4);
			 System.out.println("cat-id value.."+cat_id1);//equalsIgnoreCase()
			 if(cat_id1.equalsIgnoreCase(cat_id)) {
				 System.out.println("you can update your category");
				 try {
						String sql1 = "select * from pallacategory where cat_id =?";
						PreparedStatement st1234 = conn.prepareStatement(sql1);
						st1234.setString(1,cat_id);
						ResultSet rst123 = st1234.executeQuery();
						if(rst123.first()) {
							
							String cat = rst123.getString(2);
							System.out.println("category name..."+cat);
							
							
							String imageUrl = rst.getString(1);
							System.out.println("image url..."+imageUrl);
							String[] parts = imageUrl.split("=");
							String part1 = parts[0]; 
							System.out.println("url..."+part1);
							String part2 = parts[1]; 
							System.out.println("image id..."+part2);
						
						   byte[] imageByteArray = EmailValidate.decodeImage(image_data);
							try {
								PreparedStatement ps11=conn.prepareStatement("update veg set IMAGE_DATA =? where id =?");
								ps11.setBytes(1, imageByteArray);
								ps11.setString(2, part2);
								int rowinsert = ps11.executeUpdate();
								
								if(rowinsert>0){
									PreparedStatement ps1=conn.prepareStatement("update pallacategory set url =?, category =?,availability=? where cat_id =?");
									ps1.setString(1, imageUrl);
									ps1.setString(2, category);
									ps1.setString(3, availability);
									ps1.setString(4, cat_id);
									int rowinsert1 = ps1.executeUpdate();
									if(rowinsert1>0) {
										 JSONObject jsonobj = new JSONObject();
										 String msg = "3";
							        	 jsonobj.put("response", msg);
							        	 out.println(jsonobj);
									}
									else {
										String e= "0";
										JSONObject jsonobj = new JSONObject();
										
											jsonobj.put("response", e);
										
										out.println(jsonobj );
									}
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
						
						}else {
							System.out.println("not valid cat_id");
						}
						
					}catch(Exception e) {
						e.printStackTrace();
					}
			 }else {
				 System.out.println("This category is already exist in table.");
				 String e= "0";
					JSONObject jsonobj = new JSONObject();
					jsonobj.put("response", e);
					jsonobj.put("message", "This category is already available. Please choose other category");
					out.println(jsonobj );
				 
			 }
			 
		}else {
			try {
				String sql1 = "select * from pallacategory where cat_id =?";
				PreparedStatement st1234 = conn.prepareStatement(sql1);
				st1234.setString(1,cat_id);
				ResultSet rst123 = st1234.executeQuery();
				if(rst123.first()) {
					
					String cat = rst123.getString(2);
					System.out.println("category name..."+cat);
					
					
					String imageUrl = rst123.getString(1);
					System.out.println("image url..."+imageUrl);
					String[] parts = imageUrl.split("=");
					String part1 = parts[0]; 
					System.out.println("url..."+part1);
					String part2 = parts[1]; 
					System.out.println("image id..."+part2);
				
				   byte[] imageByteArray = EmailValidate.decodeImage(image_data);
					try {
						PreparedStatement ps11=conn.prepareStatement("update veg set IMAGE_DATA =? where id =?");
						ps11.setBytes(1, imageByteArray);
						ps11.setString(2, part2);
						int rowinsert = ps11.executeUpdate();
						
						if(rowinsert>0){
							PreparedStatement ps1=conn.prepareStatement("update pallacategory set url =?, category =?,availability=? where cat_id =?");
							ps1.setString(1, imageUrl);
							ps1.setString(2, category);
							ps1.setString(3, availability);
							ps1.setString(4, cat_id);
							int rowinsert1 = ps1.executeUpdate();
							if(rowinsert1>0) {
								 JSONObject jsonobj = new JSONObject();
								 String msg = "3";
					        	 jsonobj.put("response", msg);
					        	 out.println(jsonobj);
							}
							else {
								String e= "0";
								JSONObject jsonobj = new JSONObject();
								
									jsonobj.put("response", e);
								
								out.println(jsonobj );
							}
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
				
				}else {
					System.out.println("not valid cat_id");
				}
				
			}catch(Exception e) {
				e.printStackTrace();
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
