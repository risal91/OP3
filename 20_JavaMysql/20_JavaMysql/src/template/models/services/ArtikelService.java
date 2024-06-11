package template.models.services;

import template.MySQL;
import template.models.Artikel;
import template.models.Hersteller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// DAO (Data Access Object) Klassen bieten Schnittstellen zur Interaktion mit der Datenbank.
// Service-Klassen bieten Interaktion mit der Datenbank und weitere Geschäftslogik (wie das Erstellen von Objekten).


/**
 * Service-Klasse für Artikel.
 */
public class ArtikelService
{
    /**
     * Fragt alle Artikel aus der Datenbank ab und erstellt die Model-Objekte.
     */
    public static void selectArtikel()
    {
        // Try-With-Resources = Nach dem Try-Block werden reservierte Ressourcen freigegeben. Offene Verbindungen werden geschlossen.
        try (Connection connection = MySQL.getConnection();
             Statement statement = connection.createStatement())
        {
            ResultSet rs = statement.executeQuery("SELECT * FROM artikel");

            while (rs.next())
            {
                int nummer = rs.getInt("nummer");
                String bezeichnung = rs.getString("bezeichnung");
                BigDecimal preis = rs.getBigDecimal("preis");

                // Und hier wird die HashMap praktisch, denn wenn wir die Nummer des Herstellers aus der Datenbank abfragen,
                // können wir das als Key der HashMap nutzen, um das Hersteller-Objekt zu finden.
                Hersteller hersteller = Hersteller.hersteller.get(rs.getInt("hersteller"));

                new Artikel(nummer, bezeichnung, preis, hersteller);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
