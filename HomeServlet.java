
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/HomeServlet")
public class HomeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        // JDBC Database Connection
        String jdbcUrl = "jdbc:mysql://localhost:3306/userdb";
        String dbUser = "root";
        String dbPassword = "admin1";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
                String sql = "SELECT id, username FROM users ORDER BY id ASC";
                try (PreparedStatement statement = connection.prepareStatement(sql);
                     ResultSet resultSet = statement.executeQuery()) {

                    // HTML formatting for displaying data
                    response.getWriter().println("<html>");
                    response.getWriter().println("<head>");
                    response.getWriter().println("<title>Home Page</title>");
                    response.getWriter().println("</head>");
                    response.getWriter().println("<body>");
                    response.getWriter().println("<h2>User Data</h2>");
                    response.getWriter().println("<table border='1'>");
                    response.getWriter().println("<tr><th>ID</th><th>Email</th></tr>");

                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String username = resultSet.getString("username");

                        response.getWriter().println("<tr><td>" + id + "</td><td>" + username + "</td></tr>");
                    }

                    response.getWriter().println("</table>");
                    response.getWriter().println("</body>");
                    response.getWriter().println("</html>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
