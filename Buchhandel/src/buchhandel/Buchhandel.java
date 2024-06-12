package buchhandel;

import buchhandel.models.Ausleihe;
import buchhandel.models.Buch;
import buchhandel.models.Kunde;

import java.sql.*;
import java.time.LocalDate;

public class Buchhandel {

    public static void ausgabe(String tabelle){
        try (Connection connection = MySQL.getConnection();
        Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("SELECT * FROM" + tabelle);

            while (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    System.out.print(resultSet.getString(i) + "\t");
                }
                System.out.println("");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static Ausleihe ausleihVorgang(Buch buch,Kunde kunde,LocalDate datum, int dauer)throws SQLException{
        if(Ausleihe.ausleihen.get(buch) == null && kunde.getGuthaben() - dauer >= 0){
            try (Connection connection = MySQL.getConnection()){
                connection.setAutoCommit(false);
                try(PreparedStatement statement = connection.prepareStatement("INSERT INTO ausleihe(buch,kunde,datum,dauer) VALUES (?,?,?,?)")) {
                    statement.setInt(1,buch.getId());
                    statement.setInt(2,kunde.getNummer());
                    statement.setObject(3,datum);
                    statement.setInt(4,dauer);

                    if(statement.executeUpdate() != 1){
                        connection.rollback();
                        return null;
                    }
                }catch (SQLException e){
                    connection.rollback();
                }
                try (PreparedStatement statement = connection.prepareStatement(
                        "UPDATE kunde SET guthaben = guthaben - ? WHERE nummer = ?"
                )){
                    statement.setInt(1,dauer);
                    statement.setInt(2,kunde.getNummer());

                    if(statement.executeUpdate() != 1){
                        connection.rollback();
                        return null;
                    }

                }catch (SQLException e){
                    connection.rollback();
                    throw  e;
                }
                connection.commit();
                return new Ausleihe(buch,kunde,datum,dauer);
            }
        }else {
            return null;
        }
    }

    public static void main(String[] args) {

        MySQL.setConnectionString("jdbc:mysql://127.0.0.1:3306/buchverleih");

        selectKunde();
    }

    public static void selectKunde(){
        try (Connection connection = MySQL.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM Kunde");

            while (resultSet.next()){


                new Kunde(resultSet.getInt(1),resultSet.getString(2),resultSet.getInt(3));
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void selectBuch()
    {
        try (Connection connection = MySQL.getConnection();
             Statement statement = connection.createStatement())
        {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM buch");

            while (resultSet.next())
            {
                new Buch(resultSet.getInt("id"), resultSet.getString("titel"));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void selectAusleihe()
    {
        try (Connection connection = MySQL.getConnection();
             Statement statement = connection.createStatement())
        {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ausleihe");

            while (resultSet.next())
            {
                Buch b = Buch.bücher.get(resultSet.getInt("buch"));
                Kunde k = Kunde.kunden.get(resultSet.getInt("kunde"));
                LocalDate ld = resultSet.getObject("datum", LocalDate.class);
                int d = resultSet.getInt("dauer");
                new Ausleihe(b, k, ld, d);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
