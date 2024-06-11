package aufgaben_insert_update.lösung_2;

import template.MySQL;

import java.sql.*;
import java.util.HashMap;

/**
 * Model-Klasse für Abteilungen mit select-, create (insert) und update-Methoden.
 */
public class Abteilung
{
    /**
     * Speichert die Model-Objekte mit dem primaryKey als Key.
     */
    public static final HashMap<Integer, Abteilung> abteilungen = new HashMap<>();

    private final int abteilungsnummer;
    private String abteilungsname;

    public int getAbteilungsnummer()
    {
        return abteilungsnummer;
    }

    /**
     * Ändert den Abteilungsnamen und ruft updateAbteilung auf.
     */
    public void setAbteilungsname(String abteilungsname)
    {
        this.abteilungsname = abteilungsname;
        updateAbteilung(this, "abteilungsname", abteilungsname);
    }

    public Abteilung(int abteilungsnummer, String abteilungsname)
    {
        this.abteilungsnummer = abteilungsnummer;
        this.abteilungsname = abteilungsname;

        abteilungen.put(abteilungsnummer, this);
    }

    @Override
    public String toString()
    {
        return "Abteilung{" +
                "abteilungsnummer=" + abteilungsnummer +
                ", abteilungsname='" + abteilungsname + '\'' +
                '}';
    }

    /**
     * Fragt von der Datenbank die Abteilungen ab und erzeugt die Model-Objekte.
     */
    public static void selectAbteilung()
    {
        try (Connection connection = MySQL.getConnection();
             Statement statement = connection.createStatement())
        {
            ResultSet rs = statement.executeQuery("select * from abteilung");

            while (rs.next())
            {
                int abteilungsnummer = rs.getInt("abteilungsnummer");
                String abteilungsname = rs.getString("abteilungsname");

                new Abteilung(abteilungsnummer, abteilungsname);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Fügt eine Abteilung der Datenbank hinzu und wenn dies erfolgreich war, wird ein Objekt der Model-Klasse erstellt.
     * @return Das erstellte Objekt der Mode-Klasse.
     */
    public static Abteilung createAbteilung(int abteilungsnummer, String abteilungsname)
    {
        Abteilung a = null;
        try (Connection connection = MySQL.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO abteilung (abteilungsnummer, abteilungsname) VALUES (?, ?)"))
        {
            statement.setInt(1, abteilungsnummer);
            statement.setString(2, abteilungsname);

            if (statement.executeUpdate() > 0)
                a = new Abteilung(abteilungsnummer, abteilungsname);

            return a;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    // Private, weil wird nur über die Setter aufgerufen
    /**
     * Wird durch einen Setter in Abteilung aufgerufen.
     * Updatet die Datenbank mit den übergebenen Werten.
     * @return True, wenn genau 1 Datensatz geändert wurde, sonst False.
     */
    private static boolean updateAbteilung(Abteilung abteilung, String attribut, Object wert)
    {
        try (Connection connection = MySQL.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE abteilung SET " + attribut + " = ? WHERE abteilungsnummer = " + abteilung.abteilungsnummer))
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
