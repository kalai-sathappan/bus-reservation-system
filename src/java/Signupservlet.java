/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author user
 */
@WebServlet(urlPatterns = {"/Signupservlet"})
public class Signupservlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter(); 
        String name = request.getParameter("username"); 
        String password = request.getParameter("password"); 
         
        try {
            //connnection establisghh
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/login?useSSL=false","root","root");
            if(name.length()>2 && password.length()>2 && !name.contains("  ") && !password.contains("  ")){ 
                
            String query1 = "select * from logintable where username = ?";
            PreparedStatement ps1 = con.prepareStatement(query1);  
            ps1.setString(1 , name);
            ResultSet rs = ps1.executeQuery(); 
            if(rs.next()){  // to check wheteher username already exists or not
                response.sendRedirect("usernotcreated.html");
            }
            else{    
            String query = "INSERT INTO logintable (username, password) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, password);
            
            int ans = ps.executeUpdate();  
            if(ans > 0){ 
                response.sendRedirect("usercreated.html");
            } else { 
                response.sendRedirect("usernotcreated.html");
            } 
            } 
            }
            else{ 
                response.sendRedirect("usernotcreated.html");
            }
           con.close();
        } catch (Exception e) {
           System.out.println(e);
        }
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    
}
