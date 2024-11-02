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

@WebServlet(urlPatterns = {"/viewticket"})
public class viewticket extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        int ticketId = Integer.parseInt(request.getParameter("ticketId"));
        int userId = Integer.parseInt(request.getParameter("userId"));
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/login?useSSL=false","root","root");

            // Fetch user name from 'logintable' based on userId
            String q1 = "SELECT username FROM logintable WHERE id = ?"; 
            PreparedStatement ps1 = con.prepareStatement(q1); 
            ps1.setInt(1, userId); 
            ResultSet rs1 = ps1.executeQuery(); 

            if (rs1.next()) { 
                String userName = rs1.getString("username");

                // Fetch ticket details from 'Tickets' table based on ticketId and userId
                String q2 = "SELECT * FROM Tickets WHERE ticketId = ? AND id = ?"; 
                PreparedStatement ps2 = con.prepareStatement(q2);
                ps2.setInt(1, ticketId); 
                ps2.setInt(2, userId); 
                ResultSet rs2 = ps2.executeQuery();

                if (rs2.next()) {
                    int busId = rs2.getInt("busId"); 
                    int seat = rs2.getInt("seatNumber"); 
                    String status = rs2.getString("status"); 
                    String route = rs2.getString("route"); 
                    java.sql.Date bookingDate = rs2.getDate("bookingDate");

                    // Fetch bus number from 'Buses' table based on busId
                    String q3 = "SELECT busNumber FROM Buses WHERE busId = ?"; 
                    PreparedStatement ps3 = con.prepareStatement(q3);
                    ps3.setInt(1, busId); 
                    ResultSet rs3 = ps3.executeQuery();

                    if (rs3.next()) {
                        int busNumber = rs3.getInt("busNumber");

                        // Add CSS styling and display ticket and user details in a ticket slip format
                        out.println("<html><head><style>");
                        out.println("body { font-family: Arial, sans-serif; background-color: #f0f8ff; padding: 120px; text-align: center; }");
                        out.println(".ticket { border: 2px dashed #007BFF; padding: 20px; border-radius: 10px; display: inline-block; margin: auto; width: 300px; box-shadow: 2px 2px 12px rgba(0,0,0,0.2); }");
                        out.println("h2 { color: #007BFF; }"); 
                        out.println(".button { background-color: #007bff; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; font-size: 16px; }");
                        out.println(".button:hover { background-color: #0056b3; }");
                        out.println("p { margin: 5px 0; }");
                        out.println(".highlight { font-weight: bold; color: #007BFF; }");
                        out.println("</style></head><body>");
                        out.println("<div class='ticket'>");
                        out.println("<h2>Ticket Slip</h2>");
                        out.println("<p><span class='highlight'>User ID:</span> " + userId + "</p>");
                        out.println("<p><span class='highlight'>User Name:</span> " + userName + "</p>");
                        out.println("<p><span class='highlight'>Ticket ID:</span> " + ticketId + "</p>");
                        out.println("<p><span class='highlight'>Booking Date:</span> " + bookingDate + "</p>");
                        out.println("<p><span class='highlight'>Seat Number:</span> " + seat + "</p>");
                        out.println("<p><span class='highlight'>Bus Number:</span> " + busNumber + "</p>");
                        out.println("<p><span class='highlight'>Route:</span> " + route + "</p>"); 
                        out.println("<button class='button' onclick='window.location.href=\"/WebApplication3\"'>Home</button>");
                        out.println("</div>");
                        out.println("</body></html>");
                    } else {
                        displayErrorMessage(out, "No bus details found for busId: " + busId);
                    }
                } else {
                    displayErrorMessage(out, "Sorry, No Such Ticket Found");
                }
            } else {
                displayErrorMessage(out, "Sorry, No Such User Found");
            }

        } catch (Exception ex) {
            Logger.getLogger(viewticket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void displayErrorMessage(PrintWriter out, String message) {
        out.println("<html><head><style>");
        out.println("body { font-family: Arial, sans-serif; background-color: #f0f8ff; height: 100vh; display: flex; justify-content: center; align-items: center; margin: 0; }");
        out.println(".error { background-color: #ffbaba; border: 2px solid #d8000c; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2); text-align: center; }");
        out.println(".error h3 { color: #d8000c; font-size: 24px; margin: 0; }");
        out.println(".error p { color: #d8000c; font-size: 18px; margin: 10px 0 0; }");
        out.println("</style></head><body>");
        out.println("<div class='error'><h3>" + message + "</h3><p>No such record found.</p></div>");
        out.println("</body></html>");
    }
}
