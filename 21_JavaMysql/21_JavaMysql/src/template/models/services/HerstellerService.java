package template.models.services;

import template.MySQL;
import template.models.Hersteller;

import java.sql.*;

/**
 * Service-Klasse für Hersteller.
 */
public class HerstellerService
{
    /**
     * Fragt alle Hersteller aus der Datenbank ab und erstellt die Mode-Objekte.
     */
    public static void selectHersteller()
    {
        // Über das Connection-Objekt können wir mit der Datenbank kommunizieren.
        // Wir erstellen ein einfaches Statement.
        // Try-With-Resources = Nach dem Try-Block werden reservierte Ressourcen freigegeben. Offene Verbindungen werden geschlossen.
        try (Connection connection = MySQL.getConnection(); // Verwendung der eigenen MySQL-Klasse zur Erzeugung der Connection.
             Statement statement = connection.createStatement())
        {
            // Mit executeQuery können wir eine Query ausführen und bekommen dabei ein ResultSet zurück.
            ResultSet rs = statement.executeQuery("SELECT * FROM hersteller");

            // Im ResultSet befinden sich die Daten, die wir von der Datenbank erhalten haben.
            while (rs.next()) // mit next() schieben wir den Lesezeiger auf den nächsten Datensatz.
            {
                // Die Daten fragen wir über den Bezeichner der Spalte oder den Index der Spalte ab.
                int nummer = rs.getInt("nummer");
                String name = rs.getString("name");

                new Hersteller(nummer, name); // Und können dann mit diesen Daten das Model-Objekt erzeugen.
            }
        }
        // Fehler abfangen und ausgeben.
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Fügt einen Hersteller der Datenbank hinzu und erstellt anschließend das Objekt.
     * @return Der erstellte Hersteller, oder Null, wenn es zu einem Fehler kam.
     */
    public static Hersteller createHersteller(String name)
    {
        Hersteller h = null;

        // Möchten wir Daten in der Datenbank hinzufügen oder ändern, sollten wir ein Prepared Statement verwenden.
        // Das gilt auch, wenn wir Variablen in einem Select-Befehl verwenden.
        // Anstatt die Werte zum Hinzufügen als Klartext in den SQL Befehl zu schreiben, nutzen wir die positionierten Parameter des Prepared Statements.
        // Dadurch vermeiden wir SQL Injections, da die Werte, die wir über Parameter dem SQL Befehl hinzufügen,
        // so modifiziert werden, dass darin enthalten Befehle nicht ausgeführt werden.
        try (Connection connection = MySQL.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO hersteller (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS))
        {
            // Wir sagen dem Prepared Statement hier noch, dass wir anschließend die automatisch generierten Keys haben möchten.
            // Dies tun wir hier, da wir in SQL für die Nummer AUTO_INCREMENT verwendet haben.

            statement.setString(1, name); // Achtung: Parameter beginnen mit Index 1!

            // Anschließend führen wir das Statement mit executeUpdate() aus.
            // executeUpdate verwenden wir für Inserts, Updates und Deletes.
            statement.executeUpdate();

            // Über getGeneratedKeys() bekommen wir nun alle erzeugen Keys zurück.
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) // next() müssen wir einmal ausführen, um auf den ersten Datensatz zu kommen.
            {
                int nummer = rs.getInt(1);
                // Jetzt haben wir alle Daten, um das Hersteller-Objekt zu erzeugen.
                h = new Hersteller(nummer, name);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        // Den Hersteller zurückgeben, oder Null, wenn es einen Fehler gab.
        return h;
    }

    /**
     * Aktualisiert einen Hersteller in der Datenbank.
     * @return True, wenn erfolgreich, sonst False.
     */
    public static boolean updateHersteller(Hersteller hersteller, String attribut, Object wert)
    {
        // Alle Werte, die durch einen Benutzer beeinflusst werden könnten, sollten als Parameter eingefügt werden.
        // "attribut" ist durch unseren Programmcode vorgegeben. Aber "wert" ist etwas, was ein Benutzer theoretisch eingeben könnte.
        // getConnection() gibt uns die Connection zurück, die wir hier im Try-With-Resources als Variable hinterlegen.
        // Dadurch wird die Connection bei Verlassen des Try-Blocks automatisch geschlossen! Wir sparen uns hier den Finally-Block!
        try (Connection connection = MySQL.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "UPDATE hersteller SET " + attribut + " = ? WHERE nummer = " + hersteller.getNummer()))
        {
            statement.setObject(1, wert);

            // executeUpdate gibt zurück, wie viele Zeilen betroffen wurden.
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
