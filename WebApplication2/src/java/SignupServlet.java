import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // JDBC Database Connection
        String jdbcUrl = "jdbc:mysql://localhost:3306/userdb";
        String dbUser = "root";
        String dbPassword = "admin1";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (java.sql.Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
                String sql = "INSERT INTO userdb.users (username, password) VALUES (?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, email);
                statement.setString(2, password);
                int rows = statement.executeUpdate();
                
                if (rows > 0) {
                    // Registration successful
                    response.sendRedirect("index.html");
                } else {
                    // Registration failed
                    PrintWriter out = response.getWriter();
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('Username already exists. Please choose a different username.');");
                    out.println("location='login.html';");
                    out.println("</script>");
                }
            }
            
        } catch (Exception e) {
            // Log the exception to a log file or console
            log("Error in Login Servlet", e);
            
            // Redirect to an error page with a message
            response.sendRedirect("error.html?message=Database error");
        }
    }
}
