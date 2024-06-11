package aufgaben_insert_update.lösung_2;

import template.MySQL;

import java.sql.*;
import java.util.HashMap;

public class Baustelle
{
    public static final HashMap<String, Baustelle> baustellen = new HashMap<>();

    private final String baustellennummer;

    private String baustellenbezeichnung;


    public String getBaustellennummer()
    {
        return baustellennummer;
    }

    public void setBaustellenbezeichnung(String baustellenbezeichnung)
    {
        this.baustellenbezeichnung = baustellenbezeichnung;
    }

    public Baustelle(String baustellennummer, String baustellenbezeichnung)
    {
        this.baustellennummer = baustellennummer;
        this.baustellenbezeichnung = baustellenbezeichnung;

        baustellen.put(baustellennummer, this);
    }

    @Override
    public String toString()
    {
        return "Baustelle{" +
                "baustellennummer='" + baustellennummer + '\'' +
                ", baustellenbezeichnung='" + baustellenbezeichnung + '\'' +
                '}';
    }

    /**
     * Fragt von der Datenbank die Baustellen ab und erzeugt die Model-Objekte.
     */
    public static void selectBaustelle()
    {
        try (Connection connection = MySQL.getConnection();
             Statement statement = connection.createStatement())
        {
            ResultSet rs = statement.executeQuery("select * from baustelle");

            while(rs.next())
            {
                String baustellennummer = rs.getString("baustellennummer");
                String baustellenbezeichnung = rs.getString("baustellenbezeichnung");

                new Baustelle(baustellennummer, baustellenbezeichnung);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Fügt eine Baustelle der Datenbank hinzu und wenn dies erfolgreich war, wird ein Objekt der Model-Klasse erstellt.
     * @return Das erstellte Objekt der Mode-Klasse.
     */
    public static Baustelle createBaustelle(String baustellennummer, String baustellenbezeichnung)
    {
        Baustelle b = null;
        try (Connection connection = MySQL.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO baustelle (baustellennummer, baustellenbezeichnung) VALUES (?, ?)"))
        {
            statement.setString(1, baustellennummer);
            statement.setString(2, baustellenbezeichnung);

            if (statement.executeUpdate() > 0)
                b = new Baustelle(baustellennummer, baustellenbezeichnung);

            return b;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Wird durch einen Setter in Abteilung aufgerufen.
     * Updatet die Datenbank mit den übergebenen Werten.
     * @return True, wenn genau 1 Datensatz geändert wurde, sonst False.
     */
    private static boolean updateBaustelle(Baustelle baustelle, String attribut, Object wert)
    {
        try (Connection connection = MySQL.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE baustelle SET " + attribut + " = ? WHERE baustellennummer = '" + baustelle.baustellennummer + "'"))
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
