import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/displayroute")
public class displayroute extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/login?useSSL=false", "root", "root");
            Statement st = con.createStatement();
            String query = "SELECT busNumber, route, totalseats FROM Buses";
            ResultSet rs = st.executeQuery(query);

            out.println("<html><head><title>Available Buses</title></head><body><center>");
            out.println("<h2>Available Buses</h2>");
            out.println("<table border='1' cellpadding='10' cellspacing='0'>");
            out.println("<tr><th>Bus Number</th><th>Route</th><th>Total Seats</th></tr>");

            while (rs.next()) {
                out.println("<tr><td>" + rs.getString("busNumber") + "</td>"
                        + "<td>" + rs.getString("route") + "</td>"
                        + "<td>" + rs.getInt("totalseats") + "</td></tr>");
            }

            out.println("</table></center></body></html>");
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
