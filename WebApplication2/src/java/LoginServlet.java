import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

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
            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
                String sql = "SELECT * FROM userdb.users WHERE username=? AND password=?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, email);
                statement.setString(2, password);
                
                ResultSet resultSet = statement.executeQuery();
                
                if (resultSet.next()) {
                    // Successful login
                    response.sendRedirect("index.html");
                } else {
                    // Failed login
                    PrintWriter out = response.getWriter();
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('Invalid email or password. Please try again.');");
                    out.println("location='login.html';");
                    out.println("</script>");
                }
            }
        } catch (Exception e) {
            // Log the exception to a log file or console
            log("Error in Login Servlet", e);

            // Redirect to an error page with a message
            response.sendRedirect("error.html?message=Database error");

            // If you are running this on a development environment, you can also print the stack trace to the response
            // This is for debugging purposes and should be avoided in a production environment
            // PrintWriter out = response.getWriter();
            // e.printStackTrace(new PrintWriter(out));
        }
    }
}
