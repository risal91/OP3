package aufgaben_insert_update.lösung_3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Service
{
    private static final String connectionString = "jdbc:mysql://127.0.0.1:3306/filmsammlung";

    /**
     * Erstellt mit dem DriverManager eine Connection zur Datenbank.
     * @return Die geöffnete Verbindung.
     */
    private static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(connectionString, "root", "");

    }

    /**
     * Fragt alle Filme aus der Datenbank ab und erstellt die Model-Objekte.
     */
    public static void selectFilme()
    {
        // getConnection() gibt uns die Connection zurück, die wir hier im Try-With-Resources als Variable hinterlegen.
        // Dadurch wird die Connection bei Verlassen des Try-Blocks automatisch geschlossen! Wir sparen uns hier den Finally-Block!
        try (Connection connection = getConnection(); // Im Try-With-Resources können mehrere Befehle durch Semikolon getrennt aufgelistet werden.
             Statement statement = connection.createStatement())
        {
            ResultSet rs = statement.executeQuery("select * from film");

            while (rs.next())
            {
                int id = rs.getInt("id");
                String titel = rs.getString("titel");
                String lagerort = rs.getString("lagerort");
                int spieldauer = rs.getInt("spieldauer");
                String bonusFeatures = rs.getString("bonusFeatures");
                String genre = rs.getString("genre");

                new Film(id, titel, lagerort, spieldauer, bonusFeatures, genre);
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Fragt alle Lagerorte aus der Datenbank ab.
     * @return Eine Liste mit den Lagerorten.
     */
    public static List<String> selectLagerorte()
    {
        // Mit dieser Schreibweise wird die Connection automatisch wieder geschlossen!
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement())
        {
            List<String> lagerorte = new ArrayList<>();

            ResultSet rs = statement.executeQuery("SELECT DISTINCT lagerort FROM film ORDER BY lagerort");

            while (rs.next())
            {
                lagerorte.add(rs.getString(1));
            }

            return lagerorte;

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Fügt einen neuen Film der Datenbank hinzu und erstellt das Model-Objekt.
     * @return Ein Objekt des erstellten Films.
     */
    public static Film createFilm(String titel, String lagerort, int spieldauer, String bonusFeatures, String genre)
    {
        Film f = null;

        // Möchten wir Daten in der Datenbank hinzufügen oder ändern, sollten wir ein Prepared Statement verwenden.
        // Anstatt dass wir die Werte zum Hinzufügen als Klartext in den SQL Befehl schreiben,
        // nutzen wir die positionierten Parameter des Prepared Statements.
        // Dadurch vermeiden wir SQL Injections, da die Werte, die wir über Parameter dem SQL Befehl hinzufügen,
        // so modifiziert werden, dass darin enthaltene Befehle nicht ausgeführt werden.
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO film (titel, lagerort, spieldauer, bonusFeatures, genre) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS))
        {
            // Wir sagen dem Prepared Statement hier noch, dass wir anschließend die automatisch generierten Keys haben möchten.
            // Dies tun wir hier, da wir in SQL für die ID AUTO_INCREMENT verwendet haben.

            // ACHTUNG: Die Parameter beginnen mit Index 1!
            statement.setString(1, titel);
            statement.setString(2, lagerort);
            statement.setInt(3, spieldauer);
            statement.setString(4, bonusFeatures);
            statement.setString(5, genre);

            statement.executeUpdate();

            // Über getGeneratedKeys() bekommen wir nun die erzeugten Keys zurück.
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next())
            {
                int id = rs.getInt(1);

                f = new Film(id, titel, lagerort, spieldauer, bonusFeatures, genre);
            }

            return f;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return f;
        }
    }

    /**
     * Aktualisiert einen Film in der Datenbank.
     *
     * @return True, wenn die Aktualisierung geklappt hat, sonst False.
     */
    public static boolean updateFilm(Film film, String attribut, Object wert)
    {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE film SET " + attribut + " = ? WHERE id = " + film.getId()))
        {
            statement.setObject(1, wert);

            return statement.executeUpdate() == 1;

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
