package template.models.services;

import template.MySQL;
import template.models.Hersteller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
}
