package template.models.services;

import template.MySQL;
import template.models.Artikel;
import template.models.Hersteller;

import java.math.BigDecimal;
import java.sql.*;

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

    /**
     * Fügt einen Artikel der Datenbank hinzu und erstellt anschließend das Objekt.
     * @param bezeichnung Bezeichnung des Artikels.
     * @param preis Preis des Artikels.
     * @param hersteller Hersteller des Artikels.
     * @return Der erstellte Artikel, oder Null, wenn es einen Fehler gab.
     */
    public static Artikel createArtikel(String bezeichnung, BigDecimal preis, Hersteller hersteller)
    {
        Artikel a = null;

        try (Connection connection = MySQL.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 "INSERT INTO artikel (bezeichnung, preis, hersteller)" +
                     "VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS))
        {
            // Achtung: Die Parameter beginnen mit Index 1!
            statement.setString(1, bezeichnung);
            statement.setBigDecimal(2, preis);
            statement.setInt(3, hersteller.getNummer());

            statement.executeUpdate();

            // getGeneratedKeys(), da wir die Primary Keys über AUTO_INCREMENT erzeugen lassen.
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next())
            {
                int nummer = rs.getInt(1);
                a = new Artikel(nummer, bezeichnung, preis, hersteller);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return a;
    }

    // Mit dieser Update-Methode möchten wir ein bestimmtes Attribut eines Artikels updaten können.
    // Diese Methode wird dann aufgerufen, wenn über einen Setter der Model-Klasse ein Wert geändert wird.

    /**
     * Aktualisiert einen Artikel in der Datenbank.
     * @param artikel Der zu aktualisierende Artikel.
     * @param attribut Das zu aktualisierende Attribut.
     * @param wert Der neue Wert.
     * @return True, wenn erfolgreich, sonst False.
     */
    public static boolean updateArtikel(Artikel artikel, String attribut, Object wert)
    {
        try (Connection connection = MySQL.getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "UPDATE artikel SET " + attribut + " = ? WHERE nummer = " + artikel.getNummer()))
        {
            statement.setObject(1, wert);

            int anzahl = statement.executeUpdate();

            return anzahl == 1;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
