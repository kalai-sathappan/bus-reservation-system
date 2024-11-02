import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/cancelticket"})
public class cancelticket extends HttpServlet {

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
            
            // SQL query to delete ticket by ticketId and userId
            String q1 = "DELETE FROM Tickets WHERE ticketId = ? AND id = ?";
            PreparedStatement ps1 = con.prepareStatement(q1); 
            ps1.setInt(1, ticketId); 
            ps1.setInt(2, userId);
            
            // Execute update and check if the ticket was found and deleted
            int rs = ps1.executeUpdate();  

            out.println("<html><head><style>");
            out.println("body { font-family: Arial, sans-serif; background-color: #f4f4f9; color: #333; text-align: center; padding: 50px; }");
            out.println(".container { background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2); max-width: 500px; margin: auto; }");
            out.println(".message { font-size: 22px; font-weight: bold; margin-bottom: 15px; }");
            out.println(".success-message { color: #28a745; }");
            out.println(".error-message { color: #ff6b6b; }");
            out.println(".description { font-size: 18px; color: #666; margin-top: 10px; margin-bottom: 20px; }");
            out.println(".button { background-color: #007bff; color: white; padding: 12px 25px; border: none; border-radius: 5px; cursor: pointer; font-size: 16px; text-decoration: none; }");
            out.println(".button:hover { background-color: #0056b3; }");
            out.println("</style></head><body>");

            if (rs > 0) { 
                // Ticket was found and deleted successfully
                out.println("<div class='container'>");
                out.println("<p class='message success-message'>Your ticket has been cancelled successfully!</p>");
                out.println("<p class='description'>Thank you! Visit us for future travels.</p>");
                out.println("<button class='button' onclick='window.location.href=\"/WebApplication3\"'>Home</button>");
                out.println("</div>");
            } else { 
                // No matching ticket found
                out.println("<div class='container'>");
                out.println("<p class='message error-message'>No Ticket Found</p>");
                out.println("<p class='description'>Sorry, we couldn't find a ticket with the provided details. Please check and try again.</p>");
                out.println("<button class='button' onclick='window.history.back()'>Go Back</button>");
                out.println("</div>");
            }

            out.println("</body></html>");
            con.close(); // Closing the connection after use

        } catch (Exception ex) {
            Logger.getLogger(cancelticket.class.getName()).log(Level.SEVERE, null, ex);
            out.println("<html><body><div class='container'><p class='error-message'>An error occurred while processing your request. Please try again later.</p></div></body></html>");
        }
    }
}
