package aufgaben_insert_update.lösung_2;

import template.MySQL;

import java.sql.*;
import java.util.ArrayList;

public class Einsatz
{
    public static final ArrayList<Einsatz> einsätze = new ArrayList<>();

    private final Mitarbeiter mitarbeiter;
    private final Baustelle baustelle;

    private Integer stunden;


    public void setStunden(Integer stunden)
    {
        this.stunden = stunden;
        updateEinsatz(this, "stunden", stunden);
    }

    public Einsatz(Mitarbeiter mitarbeiter, Baustelle baustelle, Integer stunden)
    {
        this.mitarbeiter = mitarbeiter;
        this.baustelle = baustelle;
        this.stunden = stunden;

        einsätze.add(this);
    }

    @Override
    public String toString()
    {
        return "Einsatz{" +
                "mitarbeiter='" + mitarbeiter + '\'' +
                ", baustelle='" + baustelle + '\'' +
                ", stunden=" + stunden +
                '}';
    }

    /**
     * Fragt von der Datenbank die Einsätze ab und erzeugt die Model-Objekte.
     */
    public static void selectEinsatz()
    {
        try (Connection connection = MySQL.getConnection();
             Statement statement = connection.createStatement())
        {
            ResultSet rs = statement.executeQuery("select * from einsatz");

            while(rs.next())
            {
                Mitarbeiter mitarbeiter = Mitarbeiter.mitarbeiter.get(rs.getString("mitarbeiternummer"));
                Baustelle baustelle = Baustelle.baustellen.get(rs.getString("baustellennummer"));

                Integer stunden = rs.getInt("stunden");

                new Einsatz(mitarbeiter, baustelle, stunden);
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Fügt einen Einsatz der Datenbank hinzu und wenn dies erfolgreich war, wird ein Objekt der Model-Klasse erstellt.
     * @return Das erstellte Objekt der Mode-Klasse.
     */
    public static Einsatz createEinsatz(Mitarbeiter mitarbeiter, Baustelle baustelle, Integer stunden)
    {
        Einsatz einsatz = null;
        try (Connection connection = MySQL.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO einsatz (mitarbeiternummer, baustellennummer, stunden) VALUES (?, ?, ?)"))
        {
            statement.setString(1, mitarbeiter.getMitarbeiternummer());
            statement.setString(2, baustelle.getBaustellennummer());
            statement.setInt(3, stunden);

            if (statement.executeUpdate() > 0)
                einsatz = new Einsatz(mitarbeiter, baustelle, stunden);

            return einsatz;
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
    private static boolean updateEinsatz(Einsatz einsatz, String attribut, Object wert)
    {
        try (Connection connection = MySQL.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE einsatz SET " + attribut + " = ? " +
                             "WHERE mitarbeiternummer = '" + einsatz.mitarbeiter.getMitarbeiternummer() + "' " +
                             "AND baustellennummer = '" + einsatz.baustelle.getBaustellennummer() + "'"))
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
