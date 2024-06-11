package template.models.services;

import template.MySQL;
import template.models.Adresse;
import template.models.Kunde;

import java.sql.*;

public class AdresseService
{
    public static void selectAdresse()
    {
        try (Connection connection = MySQL.getConnection();
             Statement statement = connection.createStatement())
        {

            ResultSet rs = statement.executeQuery("select * from adresse");
            while (rs.next())
            {
                int id = rs.getInt("id");
                String straßeNr = rs.getString("straßeNr");
                String plz = rs.getString("plz");
                String ort = rs.getString("ort");

                Kunde kunde  = Kunde.kunden.get(rs.getInt("kunde"));

                new Adresse(id, straßeNr, plz, ort, kunde);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    public static Adresse createAdresse(String straßeNr, String plz, String ort, Kunde kunde)
    {
        Adresse a = null;

        try (Connection connection = MySQL.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO adresse (straßeNr, plz, ort, kunde) " +
                        "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS))
        {

            statement.setString(1, straßeNr);
            statement.setString(2, plz);
            statement.setString(3, ort);
            statement.setInt(4, kunde.getNummer());

            statement.executeUpdate();

            // Über getGeneratedKeys() bekommen wir nun alle erzeugten Keys zurück.
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next())
            {
                int id = rs.getInt(1);
                // Jetzt haben wir alle Daten, um auch das Artikel-Objekt zu erzeugen
                a = new Adresse(id, straßeNr, plz, ort, kunde);
            }

            // Den neu erstellten Artikel können wir zurückgeben.
            return a;

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
