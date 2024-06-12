package modules.service;

import modules.Benutzer;

import java.sql.*;

public class BenutzerService {
    public static void selectBenutzer(){

        try (Connection connection = DriverManager.getConnection("Jdbc:mysql://127.0.0.1:3306/bildverarbeitung", "root","");
             Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("SELECT * FROM benutzer");

            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String benutzer = resultSet.getString("benutzer");

                Benutzer user = Benutzer.benutzerMap.get(resultSet.getInt("id"));

                new Benutzer(id,benutzer);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Benutzer creatBenutzer(String benutzer){
        Benutzer b = null;
        try (Connection connection = DriverManager.getConnection("Jdbc:mysql://127.0.0.1:3306/bildverarbeitung", "root","");
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO benutzer(benutzer) VALUES (?)",
                     Statement.RETURN_GENERATED_KEYS
             )){

            statement.setString(1, benutzer);
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()){
                int id = resultSet.getInt("id");
                b = new Benutzer(id, benutzer);
            }
            return b;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static boolean updateBenutzer(Benutzer benutzer, Object wert){
        try (Connection connection = DriverManager.getConnection("Jdbc:mysql://127.0.0.1:3306/bildverarbeitung", "root","");
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE benutzer SET benutzer = ? WHERE id = "+benutzer.getId()
             )){
            statement.setObject(1,wert);

            int betroffenerDatensatz = statement.executeUpdate();

            selectBenutzer();
            return betroffenerDatensatz == 1;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
