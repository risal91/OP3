package template.models.services;

import template.models.Kunde;
import template.MySQL;

import java.sql.*;

public class KundeService
{
    public static void selectKunde()
    {
        try (Connection connection = MySQL.getConnection();
             Statement statement = connection.createStatement())
        {

            ResultSet rs = statement.executeQuery("select * from kunde");
            while (rs.next())
            {
                int nummer = rs.getInt("nummer");
                String name = rs.getString("name");

                new Kunde(nummer, name);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Erstellt einen neuen Kunden und fügt diesen in der Datenbank ein.
     * @param name Name des Kunden
     * @return Der erstellte Kunde
     */
    public static Kunde createKunde(String name)
    {
        Kunde k = null;

        try (Connection connection = MySQL.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO kunde (name) " +
                        "VALUES (?)", Statement.RETURN_GENERATED_KEYS))
        {
            // Wir sagen dem Prepared Statement hier noch, dass wir anschließend die automatisch generierten Keys haben möchten.
            // Dies tun wir hier, da wir in SQL für die Nummer AUTO_INCREMENT verwendet haben

            // ACHTUNG: Die Parameter beginnen mit Index 1!
            statement.setString(1, name);

            // executeUpdate verwenden wir für Inserts, Updates und Deletes.
            statement.executeUpdate();

            // Über getGeneratedKeys() bekommen wir nun alle erzeugten Keys zurück.
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) // next() müssen wir einmal ausführen, um auf den ersten Datensatz zu kommen
            {
                int nummer = rs.getInt(1);
                // Jetzt haben wir alle Daten, um auch das Kunde-Objekt zu erzeugen
                k = new Kunde(nummer, name);
            }

            // Den neu erstellten Kunden können wir zurückgeben.
            return k;

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Aktualisiert einen Kunden in der Datenbank.
     * @param kunde Der zu aktualisierende Kunde
     * @param attribut Das zu aktualisierende Attribut.
     * @param wert Der neue Wert.
     * @return True, wenn erfolgreich, sonst False.
     */
    public static boolean updateKunde(Kunde kunde, String attribut, Object wert)
    {

        // Alle Werte, die durch einen Benutzer beeinflusst werden könnten, sollten als Parameter eingefügt werden.
        // "attribut" ist durch unseren Programmcode vorgegeben. Aber "wert" ist etwas, was ein Benutzer theoretisch eingeben könnte.
        // Durch diese Schreibweise im try werden externe Ressourcen automatisch wieder freigegeben.
        try (Connection connection = MySQL.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                "UPDATE kunde SET " + attribut + " = ? WHERE nummer = " + kunde.getNummer()))
        {
            // ACHTUNG: Die Parameter beginnen mit Index 1!
            statement.setObject(1, wert);

            // executeUpdate gibt zurück, wie viele Zeilen betroffen wurden
            int anzahl = statement.executeUpdate();

            // haben wir genau 1 betroffene Zeile (Datensatz), hat Update geklappt.
            return anzahl == 1;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }

    }
}
