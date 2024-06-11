package onlineshop.models.services;

import onlineshop.MySQL;
import onlineshop.models.Adresse;
import onlineshop.models.Bestellung;
import onlineshop.models.Kunde;

import java.sql.*;

public class BestellungService {

    public static void selectBestellung(){
        try (Connection connection = MySQL.getConnection();
             Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery("SELECT * FROM bestellung");

            while (resultSet.next()){
                int nummer = resultSet.getInt("nummer");
                Date datum = resultSet.getDate("datum");
                Kunde kunde = Kunde.kundeMap.get(resultSet.getInt("kunde"));
                Adresse rechnungsadresse = Adresse.adresseMap.get(resultSet.getInt("rechnungsadresse"));
                Adresse lieferadresse = Adresse.adresseMap.get(resultSet.getInt("lieferadresse"));

                new Bestellung(nummer,datum,kunde,rechnungsadresse,lieferadresse);

            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static Bestellung createBestellung(Date datum, Kunde kunde, Adresse rechnungsadresse, Adresse lieferadresse){
        Bestellung b = null;

        try (Connection connection = MySQL.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO bestellung(datum, kunde, rechnungsadresse, lieferadresse) VALUES (?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS
        )){
            statement.setDate(1,datum);
            statement.setInt(2,kunde.getNummer());
            statement.setInt(3,rechnungsadresse.getId());
            statement.setInt(4,lieferadresse.getId());

            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();

            if(resultSet.next()){
                int nummer = resultSet.getInt(1);
                b = new Bestellung(nummer,datum,kunde,rechnungsadresse,lieferadresse);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return b;
    }
}
