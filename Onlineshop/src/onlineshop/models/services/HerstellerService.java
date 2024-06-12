package onlineshop.models.services;

import onlineshop.MySQL;
import onlineshop.models.Hersteller;
import onlineshop.models.Kunde;

import java.sql.*;

public class HerstellerService {

    public static void selectHersteller(){

        try(Connection connection = MySQL.getConnection();
            Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery("SELECT * FROM hersteller");

            while (resultSet.next()){
                int nummer = resultSet.getInt("nummer");
                String name = resultSet.getString("name");

                new Hersteller(nummer, name);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static Hersteller createHersteller(String name){
            Hersteller h = null;

            try (Connection connection =MySQL.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "INSERT INTO hersteller(name) VALUES (?)",Statement.RETURN_GENERATED_KEYS)){
                statement.setString(1,name);
                statement.executeUpdate();

                ResultSet resultSet = statement.getGeneratedKeys();

                if (resultSet.next()){
                    int nummer = resultSet.getInt(1);

                    h = new Hersteller(nummer,name);
                }

            }catch (SQLException e){
                e.printStackTrace();
            }


        return h;

    }

    public static boolean updateHersteller(Hersteller hersteller, String attribut, Object wert){
        try (Connection connection = MySQL.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATe hersteller SET "+ attribut + " = ? WHERE nummer = "+ hersteller.getNummer()
             )){
            statement.setObject(1,wert);

            int betroffenerDatensatz =  statement.executeUpdate();

            return betroffenerDatensatz == 1;

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
