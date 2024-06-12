package modules.service;

import modules.Avatar;
import modules.Benutzer;

import java.io.FileInputStream;
import java.sql.*;

public class AvatarService {

    public static void selectAvatar() {

        try (Connection connection = DriverManager.getConnection("Jdbc:mysql://127.0.0.1:3306/bildverarbeitung", "root", "");
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM avatar");

            while (resultSet.next()) {
                Benutzer benutzer = Benutzer.benutzerMap.get(resultSet.getInt(1));
                Blob bild = resultSet.getBlob("bild");

                new Avatar(benutzer, bild);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean creatAvatar(Benutzer benutzer, String bild) {
        try (FileInputStream fileInputStream = new FileInputStream(bild)) {


            try (Connection connection = DriverManager.getConnection("Jdbc:mysql://127.0.0.1:3306/bildverarbeitung", "root", "");
                 PreparedStatement statement = connection.prepareStatement(
                         "INSERT INTO avatar(benutzer,bild) VALUES (?,?)"
                 )) {

                statement.setInt(1, benutzer.getId());
                statement.setBinaryStream(2, fileInputStream);

                statement.executeUpdate();

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateAvatar(Avatar avatar, Object wert) {
        try (FileInputStream fileInputStream = new FileInputStream(String.valueOf(wert))) {
            try (Connection connection = DriverManager.getConnection("Jdbc:mysql://127.0.0.1:3306/bildverarbeitung", "root", "");
                 PreparedStatement statement = connection.prepareStatement(
                         "UPDATE avatar SET bild = ? WHERE benutzer = "+avatar.getBenutzer().getId()
                 )) {
                statement.setBinaryStream(1, fileInputStream);


                int betroffenerDatensatz = statement.executeUpdate();

                return betroffenerDatensatz == 1;
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
