package onlineshop.models.services;

import onlineshop.MySQL;
import onlineshop.models.Artikel;
import onlineshop.models.Hersteller;

import java.math.BigDecimal;
import java.sql.*;

public class ArtikelService {
    public static void selectArtikel(){

        try (Connection connection = MySQL.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM artikel");

            while (resultSet.next()){
                int nummer = resultSet.getInt(1);
                String bezeichnung = resultSet.getString("bezeichnung");
                BigDecimal preis = resultSet.getBigDecimal("preis");

                Hersteller hersteller = Hersteller.herstellerMap.get(resultSet.getInt("hersteller"));

                new Artikel(nummer,bezeichnung,preis,hersteller);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static Artikel creatArtikel(String bezeichnung, BigDecimal preis, Hersteller hersteller){
        Artikel a = null;
        try (Connection connection = MySQL.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO artikel(bezeichnung,preis,hersteller) VALUES (?,?,?)",
                     Statement.RETURN_GENERATED_KEYS
             )){
            statement.setString(1,bezeichnung);
            statement.setBigDecimal(2,preis);
            statement.setInt(3,hersteller.getNummer());

            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();

            if(resultSet.next()){
                int nummer = resultSet.getInt(1);

                a = new Artikel(nummer, bezeichnung,preis,hersteller);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return a;
    }

}
