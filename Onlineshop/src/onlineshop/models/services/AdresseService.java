package onlineshop.models.services;

import onlineshop.MySQL;
import onlineshop.models.Adresse;
import onlineshop.models.Hersteller;
import onlineshop.models.Kunde;

import java.sql.*;

public class AdresseService {
    public static void selectAdresse(){
        try (Connection connection = MySQL.getConnection();
             Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery("SELECT * FROM adresse");

            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String straßeNr = resultSet.getString("straßeNr");
                String plz = resultSet.getString("plz");
                String ort = resultSet.getString("ort");

                Kunde kunde = Kunde.kundeMap.get(resultSet.getInt("kunde"));

                new Adresse(id,straßeNr, plz,ort,kunde);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static Adresse creatadresse(String straßeNr,String plz, String ort, Kunde kunde){
        Adresse a = null;

        try(Connection connection = MySQL.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO adresse(straßeNr, plz, ort, kunde) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
            statement.setString(1,straßeNr);
            statement.setString(2,plz);
            statement.setString(3,ort);
            statement.setInt(4,kunde.getNummer());

            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();

            if(resultSet.next()){
                int id = resultSet.getInt(1);
                a= new Adresse(id,straßeNr,plz,ort,kunde);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return a;
    }

    public static boolean updateAdresse(Adresse adresse, String attribut, Object wert){
        try (Connection connection = MySQL.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATe adresse SET "+ attribut + " = ? WHERE id = "+ adresse.getId()
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
