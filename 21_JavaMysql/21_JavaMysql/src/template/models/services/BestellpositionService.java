package template.models.services;

import template.models.*;
import template.MySQL;

import java.sql.*;

public class BestellpositionService
{
    public static void selectBestellposition()
    {
        try (Connection connection = MySQL.getConnection();
             Statement statement = connection.createStatement())
        {
            ResultSet rs = statement.executeQuery("select * from bestellposition");
            while (rs.next())
            {
                Bestellung bestellung = Bestellung.bestellungen.get(rs.getInt("bestellung"));
                Artikel artikel = Artikel.artikel.get(rs.getInt("artikel"));

                int anzahl = rs.getInt("anzahl");

                new Bestellposition(bestellung, artikel, anzahl);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Erstellt eine neue Bestellposition und fügt diese in der Datenbank ein.
     * @return Die erstellte Bestellposition.
     */
    public static Bestellposition createBestellposition(Bestellung bestellung, Artikel artikel, int anzahl)
    {
        Bestellposition bp = null;
        try (Connection connection = MySQL.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO bestellposition (bestellung, artikel, anzahl) VALUES (?, ?, ?)"))
        {
            statement.setInt(1, bestellung.getNummer());
            statement.setInt(2, artikel.getNummer());
            statement.setInt(3, anzahl);

            if (statement.executeUpdate() > 0)
            {
                bp = new Bestellposition(bestellung, artikel, anzahl);
            }

            return bp;
        }

        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Aktualisiert eine Bestellposition in der Datenbank.
     * @param attribut Das zu aktualisierende Attribut.
     * @param wert Der neue Wert.
     * @return True, wenn erfolgreich, sonst False.
     */
    public static boolean updateBestellposition(Bestellposition bestellposition, String attribut, Object wert)
    {
        try (Connection connection = MySQL.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                    "UPDATE bestellposition SET " + attribut + " = ? " +
                            "WHERE bestellung = " + bestellposition.getBestellung().getNummer() + " " +
                            "AND artikel = " + bestellposition.getArtikel().getNummer()))
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
