package template.einführung;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class JavaSqlBeispiel {

    public static void main(String[] args)
    {
        Connection connection = null;
        try
        {
            String connectionString = "jdbc:mysql://localhost:3306/mydatabase";
            connection = DriverManager.getConnection(connectionString, "root", "");

            System.out.println("Got it!");

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from mytable");
            while(rs.next())
            {
                String sb = rs.getInt("id") +
                        " " + rs.getString("name") +
                        " " + rs.getString("beschreibung") +
                        " " + rs.getDouble("wert");
                System.out.println(sb);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (connection != null)
                    connection.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }
}