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

@WebServlet(urlPatterns = { "/addproduct" })
public class AddProduct extends HttpServlet{

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
	
public AddProduct() {
super();
//TODO Auto-generated constructor stub
}

Connection conn=null;
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	
	response.setContentType("application/json; charset=UTF-8");
	PrintWriter out=response.getWriter();
	request.setCharacterEncoding("UTF-8");
	
	String detailes=request.getParameter("product");
	System.out.println("categories.."+detailes);
	String image_data = null;
	String cat_id = null;
	String itemname = null;
	String availability = null;
	String cost = null;
	String discount = null;
	
    int getValue = 0;
    int getValue1 =0;
    int getValue12 =0;
	try{
			
		final JSONObject obj = new JSONObject(detailes);
		
		  image_data = obj.getString("image_data");
	      cat_id = obj.getString("category-id");
		  availability = obj.getString("availability");
		  itemname=obj.getString("itemname");
		  cost = obj.getString("cost");
		  discount = obj.getString("discount");
			
	}  catch(Exception e){
		
		e.printStackTrace();
	} 
	System.out.println("itemname.."+itemname);
	System.out.println("itemname.."+cat_id);
	System.out.println("itemname.."+availability);
	System.out.println("itemname.."+cost);
	System.out.println("itemname.."+discount);        
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
						String query1 = "select count(product_id)+1 from palla";
						Statement st1 = conn.createStatement();
						ResultSet rs1 = st1.executeQuery(query1);
						if(rs1.next()) {
							getValue1 = Integer.parseInt(rs1.getString(1));
							System.out.println("get value..."+getValue1);
						}
						String feed = "pro_"+new SimpleDateFormat("ddMMyyy").format(new Date())+getValue1;
						System.out.println("product id..."+feed);
						PreparedStatement ps11=conn.prepareStatement("insert into palla values(?,?,?,?,?,?,?)");
						//System.out.println("before inserting..."+feed);
						ps11.setString(1, feed);
						ps11.setString(2, url);
						ps11.setString(3, itemname);
						ps11.setString(4, cost);
						ps11.setString(5, discount);
						ps11.setString(6, availability);
						ps11.setString(7, cat_id);
						
						int rowinsert = ps11.executeUpdate();
						
						if(rowinsert>0){
							 JSONObject jsonobj = new JSONObject();
							 String msg = "3";
				        	 jsonobj.put("response", msg);
				        	 jsonobj.put("product-id", feed);
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



  if (conn != null) {
      
      try {
          conn.close();
      } catch (SQLException ex) {
          ex.printStackTrace();
      }
  }
  
}	

}
