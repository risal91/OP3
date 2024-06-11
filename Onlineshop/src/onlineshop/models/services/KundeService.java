package onlineshop.models.services;

import onlineshop.MySQL;
import onlineshop.models.Kunde;

import java.sql.*;

public class KundeService {

    public static void selectKunde(){

        try (Connection connection = MySQL.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT  * from kunde");

            while (resultSet.next()){
                int nummer = resultSet.getInt(1);
                String name = resultSet.getString(2);

                new Kunde(nummer,name);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static  Kunde creatKunde(String name){
        Kunde k = null;
                try (Connection connection = MySQL.getConnection();
                     PreparedStatement statement = connection.prepareStatement(
                             "INSERT INTO kunde(name) VALUES (?)",Statement.RETURN_GENERATED_KEYS)){
                    statement.setString(1,name);
                    statement.executeUpdate();

                    ResultSet resultSet = statement.getGeneratedKeys();

                    if(resultSet.next()){
                        int nummer = resultSet.getInt(1);

                        k = new Kunde(nummer,name);
                    }

                }catch (SQLException e){
                    e.printStackTrace();
                }

        return k;
    }
}
