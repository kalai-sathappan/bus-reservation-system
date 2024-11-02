import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/newbusservlet"})
public class newbusservlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String busnumber = request.getParameter("busnumber");
        String route = request.getParameter("route");
        int seat = Integer.parseInt(request.getParameter("seat"));

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
                       Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/login?useSSL=false","root","root");
            String query = "select * from Buses where busNumber = ?"; 
            PreparedStatement ps1 = con.prepareStatement(query); 
            ps1.setString(1 , busnumber);  
            ResultSet rs = ps1.executeQuery(); 
            if(rs.next()){ 
                out.print("Same bus at this route already available"); 
            } 
            else{
            String q1 = "INSERT INTO Buses (busNumber, route, totalseats) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(q1);

            // Parameter indices should start from 1
            ps.setString(1, busnumber);
            ps.setString(2, route);
            ps.setInt(3, seat);

            int z = ps.executeUpdate();
            if (z > 0) {
                out.print("Bus is successfully added");
            } else {
                out.print("Bus is not added, contact the office");
            } 
            }
        } catch (Exception ex) {
           
            out.print("An error occurred: " + ex.getMessage());
        }
    }
}
