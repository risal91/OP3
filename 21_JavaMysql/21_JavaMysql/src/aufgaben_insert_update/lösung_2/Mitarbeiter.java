package aufgaben_insert_update.lösung_2;

import template.MySQL;

import java.sql.*;
import java.util.HashMap;

public class Mitarbeiter
{
    public static final HashMap<String, Mitarbeiter> mitarbeiter = new HashMap<>();

    private final String mitarbeiternummer;

    private boolean maschinenberechtigung;
    private String mitarbeitername;
    private String mitarbeiterPLZ;
    private Abteilung abteilung;

    public String getMitarbeiternummer()
    {
        return mitarbeiternummer;
    }

    public void setMaschinenberechtigung(boolean maschinenberechtigung)
    {
        this.maschinenberechtigung = maschinenberechtigung;
        updateMitarbeiter(this, "maschinenberechtigung", maschinenberechtigung);
    }

    public void setMitarbeitername(String mitarbeitername)
    {
        this.mitarbeitername = mitarbeitername;
        updateMitarbeiter(this, "mitarbeitername", mitarbeitername);
    }

    public void setMitarbeiterPLZ(String mitarbeiterPLZ)
    {
        this.mitarbeiterPLZ = mitarbeiterPLZ;
        updateMitarbeiter(this, "mitarbeiterPLZ", mitarbeiterPLZ);
    }

    public void setAbteilung(Abteilung abteilung)
    {
        this.abteilung = abteilung;
        // Hier aufpassen, weil in der Datenbank für Mitarbeiter nur "abteilungsnummer" abgespeichert wird!
        updateMitarbeiter(this, "abteilungsnummer", abteilung.getAbteilungsnummer());
    }

    public Mitarbeiter(String mitarbeiternummer, boolean maschinenberechtigung, String mitarbeitername, String mitarbeiterPLZ, Abteilung abteilung)
    {
        this.mitarbeiternummer = mitarbeiternummer;
        this.maschinenberechtigung = maschinenberechtigung;
        this.mitarbeitername = mitarbeitername;
        this.mitarbeiterPLZ = mitarbeiterPLZ;
        this.abteilung = abteilung;

        mitarbeiter.put(mitarbeiternummer, this);
    }

    @Override
    public String toString()
    {
        return "Mitarbeiter{" +
                "mitarbeiternummer='" + mitarbeiternummer + '\'' +
                ", maschinenberechtigung=" + maschinenberechtigung +
                ", mitarbeitername='" + mitarbeitername + '\'' +
                ", mitarbeiterPLZ='" + mitarbeiterPLZ + '\'' +
                ", abteilung=" + abteilung +
                '}';
    }

    /**
     * Fragt von der Datenbank die Mitarbeiter ab und erzeugt die Model-Objekte.
     */
    public static void selectMitarbeiter()
    {
        try (Connection connection = MySQL.getConnection();
             Statement statement = connection.createStatement())
        {
            ResultSet rs = statement.executeQuery("select * from mitarbeiter");

            while(rs.next())
            {
                String mitarbeiternummer = rs.getString("mitarbeiternummer");
                boolean maschinenberechtigung = rs.getBoolean("maschinenberechtigung");
                String mitarbeitername = rs.getString("mitarbeitername");
                String mitarbeiterPLZ = rs.getString("mitarbeiterPLZ");

                // Weil wir Abteilungen in einer HashMap speichern, können wir die passende Abteilung ganz einfach über die Keys herausfinden
                Abteilung abteilung = Abteilung.abteilungen.get(rs.getInt("abteilungsnummer"));

                new Mitarbeiter(mitarbeiternummer, maschinenberechtigung, mitarbeitername, mitarbeiterPLZ, abteilung);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Fügt einen Mitarbeiter der Datenbank hinzu und wenn dies erfolgreich war, wird ein Objekt der Model-Klasse erstellt.
     * @return Das erstellte Objekt der Mode-Klasse.
     */
    public static Mitarbeiter createMitarbeiter(String mitarbeiternummer, boolean maschinenberechtigung, String mitarbeitername, String mitarbeiterPLZ, Abteilung abteilung)
    {
        Mitarbeiter m = null;
        try (Connection connection = MySQL.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO mitarbeiter (mitarbeiternummer, maschinenberechtigung, mitarbeitername, mitarbeiterPLZ, abteilungsnummer) VALUES (?, ?, ?, ?, ?)"))
        {
            statement.setString(1, mitarbeiternummer);
            statement.setBoolean(2, maschinenberechtigung);
            statement.setString(3, mitarbeitername);
            statement.setString(4, mitarbeiterPLZ);
            statement.setInt(5, abteilung.getAbteilungsnummer());

            if (statement.executeUpdate() > 0)
                m = new Mitarbeiter(mitarbeiternummer, maschinenberechtigung, mitarbeitername, mitarbeiterPLZ, abteilung);

            return m;
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
    private static boolean updateMitarbeiter(Mitarbeiter mitarbeiter, String attribut, Object wert)
    {
        try (Connection connection = MySQL.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE mitarbeiter SET " + attribut + " = ? WHERE mitarbeiternummer = '" + mitarbeiter.mitarbeiternummer + "'"))
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
