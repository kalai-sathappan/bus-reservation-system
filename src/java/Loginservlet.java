/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//import java.beans.Statement;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author user
 */
@WebServlet(urlPatterns = {"/Loginservlet"})
public class Loginservlet extends HttpServlet {

    
   

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       response.setContentType("type/html");
       PrintWriter out = response.getWriter(); 
       //accept user and password
       String username = request.getParameter("username");
       String password = request.getParameter("password");
       
       //database insertion
       
       try{
           //connection establishment
          Class.forName("com.mysql.cj.jdbc.Driver");
           Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/login?useSSL=false","root","root");
           //login db name , root username , root password
           
           Statement st = con.createStatement();
           String query =" select * from logintable where username = '"+username+"' and password = '"+password+"' ";
           ResultSet rs = st.executeQuery(query);
           if(rs.next()){ 
               int id = rs.getInt("id"); 
               String name = rs.getString("username");
               response.sendRedirect("optionpage.html?id=" + id + "&username=" + name);
           }
           else{
               response.sendRedirect("wronglogin.html");
           }
           con.close();
       } 
       catch(Exception e){ 
           System.out.println(e.getMessage());
       }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
 

}
