package onlineshop.models.services;

import onlineshop.MySQL;
import onlineshop.models.Artikel;
import onlineshop.models.Bestellposition;
import onlineshop.models.Bestellung;

import java.sql.*;

public class BerstellpositionService {

    public static void selectBestellposition(){

        try (Connection connection = MySQL.getConnection();
             Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery("SELECT * FROM bestellposition");

            while (resultSet.next()){
                Bestellung bestellung = Bestellung.bestellungMap.get(resultSet.getInt("bestellung"));
                Artikel artikel = Artikel.artikelMap.get(resultSet.getInt("artikel"));
                int anzahl = resultSet.getInt("anzahl");

                new Bestellposition(bestellung,artikel,anzahl);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static Bestellposition createbestellposition(Bestellung bestellung, Artikel artikel, int anzahl){
        Bestellposition bp = null;
        try (Connection connection = MySQL.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO bestellposition(bestellung, artikel, anzahl) VALUES (?,?,?)"
             )){
            statement.setInt(1,bestellung.getNummer());
            statement.setInt(2,artikel.getNummer());
            statement.setInt(3,anzahl);

            statement.executeUpdate();

            bp = new Bestellposition(bestellung,artikel,anzahl);

        }catch (SQLException e){
            e.printStackTrace();
        }


       return bp;
    }
}
