import modules.Avatar;
import modules.Benutzer;
import modules.service.AvatarService;
import modules.service.BenutzerService;

import java.sql.*;

public class UserProfile {

    public static void ausgabe(String tabelle){

        try (Connection connection = DriverManager.getConnection("Jdbc:mysql://127.0.0.1:3306/bildverarbeitung", "root","");
             Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tabelle);

            while (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    if (resultSet.getMetaData().getColumnType(i) != -4){
                        System.out.print(resultSet.getString(i) + "\t");
                    }else {
                        System.out.println("Bild"+"\t");
                    }

                }
                System.out.println("");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        BenutzerService.selectBenutzer();
        AvatarService.selectAvatar();

        //BenutzerService.updateBenutzer(Benutzer.benutzerMap.get(1),"KleberKlaus");
        AvatarService.updateAvatar(Avatar.avatarMap.get(Benutzer.benutzerMap.get(1)),"Bildverarbeitung/resources/WBS-Bildmarke.png");



        ausgabe("benutzer");
        ausgabe("avatar");


    }
}
