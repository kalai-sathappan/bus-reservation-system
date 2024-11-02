import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/confirmticket"})
public class confirmticket extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();  

        int id = Integer.parseInt(request.getParameter("id")); 
        String busNumber = request.getParameter("busNumber");
        int seat = Integer.parseInt(request.getParameter("seat"));
        String status = request.getParameter("status"); 
        String date = request.getParameter("date");  
        String route = request.getParameter("route"); 

        // Add CSS for styling the response
        out.println("<style>");
        out.println("body { font-family: 'Arial', sans-serif; background-color: #f0f2f5; padding: 20px; text-align: center; color: #333; }");
        out.println(".container { background-color: #fff; border-radius: 8px; padding: 30px; box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); max-width: 500px; margin: auto; }");
        out.println("h2 { color: #28a745; }");
        out.println("h3 { color: red; }");
        out.println("p { font-size: 18px; margin: 10px 0; }");
        out.println(".button { background-color: #007bff; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; font-size: 16px; }");
        out.println(".button:hover { background-color: #0056b3; }");
        out.println("</style>");

        try { 
            Class.forName("com.mysql.cj.jdbc.Driver");  // Updated driver
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/login?useSSL=false", "root", "root");

            // Retrieve busId based on busNumber
            String getBusIdQuery = "SELECT busId, totalSeats FROM Buses WHERE busNumber = ?";
            PreparedStatement getBusIdStmt = con.prepareStatement(getBusIdQuery);
            getBusIdStmt.setString(1, busNumber);
            ResultSet busIdResult = getBusIdStmt.executeQuery();
            
            int busId = -1;
            int seatcount = -1;
            if (busIdResult.next()) {
                busId = busIdResult.getInt("busId"); 
                seatcount = busIdResult.getInt("totalSeats");
            } else {
                out.println("<div class='container'>");
                out.println("<h3>Error: Bus number " + busNumber + " not found!</h3>");
                out.println("<p>Please check the bus number and try again.</p>");
                out.println("<button class='button' onclick='window.history.back()'>Go Back</button>");
                out.println("</div>");
                return;
            }

            // Check if the bus is full for the selected date
            String busFull = "SELECT COUNT(*) as total FROM Tickets WHERE busId = ? AND travelDate = ?"; 
            PreparedStatement ps1 = con.prepareStatement(busFull); 
            ps1.setInt(1, busId);
            ps1.setString(2, date);
            ResultSet rs1 = ps1.executeQuery();
            
            int seatbooked = 0;
            if (rs1.next()) { 
                seatbooked = rs1.getInt("total");
            }
            
            if (seatbooked >= seatcount) { 
                out.println("<div class='container'>");
                out.println("<h3>Bus is full for the selected date. Please choose another bus or date.</h3>");
                out.println("<button class='button' onclick='window.history.back()'>Go Back</button>");
                out.println("</div>");
            } else {
                // Check if the seat is already booked
                String checkSeatQuery = "SELECT * FROM Tickets WHERE busId = ? AND seatNumber = ? AND travelDate = ?";
                PreparedStatement checkSeatStmt = con.prepareStatement(checkSeatQuery);
                checkSeatStmt.setInt(1, busId);
                checkSeatStmt.setInt(2, seat);
                checkSeatStmt.setString(3, date);

                ResultSet rs = checkSeatStmt.executeQuery();
                
                if (rs.next()) {
                    // Seat is already booked
                    out.println("<div class='container'>");
                    out.println("<h3>Seat " + seat + " is already booked for Bus " + busNumber + " on " + date + ".</h3>");
                    out.println("<p>Please choose another seat.</p>");
                    out.println("<button class='button' onclick='window.history.back()'>Go Back</button>");
                    out.println("</div>");
                } else {
                    // Insert the booking details into the Tickets table
                    String insertQuery = "INSERT INTO Tickets (id, busId, seatNumber, status, travelDate, route) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement insertStmt = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                    insertStmt.setInt(1, id);
                    insertStmt.setInt(2, busId);
                    insertStmt.setInt(3, seat);
                    insertStmt.setString(4, status);
                    insertStmt.setString(5, date);
                    insertStmt.setString(6, route);

                    int rowsInserted = insertStmt.executeUpdate(); 
                    
                    if (rowsInserted > 0) {
                        // Retrieve the auto-generated ticketId
                        ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                        int ticketId = -1;
                        if (generatedKeys.next()) {
                            ticketId = generatedKeys.getInt(1); // Get the ticketId
                        }

                        out.println("<div class='container'>");
                        out.println("<h2>Ticket Confirmation</h2>");
                        out.println("<p><strong>Ticket ID:</strong> " + ticketId + "</p>");
                        out.println("<p><strong>Bus Number:</strong> " + busNumber + "</p>");
                        out.println("<p><strong>Travel Date:</strong> " + date + "</p>");
                        out.println("<p><strong>Route:</strong> " + route + "</p>");
                        out.println("<p><strong>Seat Number:</strong> " + seat + "</p>"); 
                        out.println("<h2>Happy Journey!!!</h2>");
                        out.println("<button class='button' onclick='window.location.href=\"/WebApplication3\"'>Home</button>");
                        out.println("</div>");
                    } else {
                        out.println("<div class='container'>");
                        out.println("<h3>Error: Unable to book the ticket. Please try again.</h3>");
                        out.println("<button class='button' onclick='window.history.back()'>Go Back</button>");
                        out.println("</div>");
                    }
                } 
            }

            // Close resources
            con.close();

        } catch (Exception ex) {
            out.println("<div class='container'>");
            out.println("<h3>Error occurred: " + ex.getMessage() + "</h3>");
            out.println("<button class='button' onclick='window.history.back()'>Go Back</button>");
            out.println("</div>");
        }
    }
}
