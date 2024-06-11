package template.models.services;

import template.models.Adresse;
import template.models.Bestellung;
import template.models.Kunde;
import template.MySQL;

import java.sql.*;
import java.time.LocalDateTime;

public class BestellungService
{
    public static void selectBestellung()
    {
        try (Connection connection = MySQL.getConnection();
             Statement statement = connection.createStatement())
        {
            ResultSet rs = statement.executeQuery("select * from bestellung");
            while (rs.next())
            {
                int nummer = rs.getInt("nummer");
                LocalDateTime datum = rs.getObject("datum", LocalDateTime.class);

                Kunde kunde = Kunde.kunden.get(rs.getInt("kunde"));
                Adresse rechnungsadresse = Adresse.adressen.get(rs.getInt("rechnungsadresse"));
                Adresse lieferadresse = Adresse.adressen.get(rs.getInt("lieferadresse"));

                new Bestellung(nummer, datum, kunde, rechnungsadresse, lieferadresse);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Erstellt eine neue Bestellung und fügt diese in der Datenbank ein.
     * @return Die erstellte Bestellung.
     */
    public static Bestellung createBestellung(LocalDateTime datum, Kunde kunde, Adresse rechnungsadresse, Adresse lieferadresse)
    {
        Bestellung b = null;
        try (Connection connection = MySQL.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO bestellung (datum, kunde, rechnungsadresse, lieferadresse) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS))
        {
            statement.setObject(1, datum);
            statement.setInt(2, kunde.getNummer());
            statement.setInt(3, rechnungsadresse.getId());
            statement.setInt(4, lieferadresse.getId());

            statement.executeUpdate();

            // Über getGeneratedKeys() bekommen wir nun alle erzeugten Keys zurück.
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next())
            {
                int nummer = rs.getInt(1);

                b = new Bestellung(nummer, datum, kunde, rechnungsadresse, lieferadresse);
            }

            return b;

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }

    }
}
