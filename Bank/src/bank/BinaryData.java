package bank;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.Scanner;

public class BinaryData {
    private static final String connectionString = "jdbc:mysql://127.0.0.1:3306/bank";


    public static void main(String[] args) {
        System.out.print("Dateipfad eingeben: ");
        Scanner sc = new Scanner(System.in);
        //String dateipfad = sc.nextLine();
        String dateipfad = "R:\\WBS\\OP3\\Bank\\resources\\WBS-BildmarkeABN.png";

//        if(updateKundeMitBild(dateipfad,1))
//            System.out.println("Erfolgreich");
        if(selectBildVonKunde(dateipfad,1))
            System.out.println("Erfolgreich");
    }

    private static boolean updateKundeMitBild(String dateipfad, int kunde){

        try (FileInputStream fileInputStream = new FileInputStream(dateipfad)){

            try (Connection connection = DriverManager.getConnection(connectionString,"root","");
                 PreparedStatement statement = connection.prepareStatement(
                         "UPDATE kunde SET bild = ? WHERE  id = ?"
                 )){

                statement.setBinaryStream(1,fileInputStream);
                statement.setInt(2, kunde);

                return  statement.executeUpdate() > 0;

            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    private static boolean selectBildVonKunde(String dateipfad, int kunde){
        try (FileOutputStream fileOutputStream = new FileOutputStream(dateipfad)){
            try (Connection connection = DriverManager.getConnection(connectionString,"root","");
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT bild FROM kunde WHERE  id = ?"
            )){
                statement.setInt(1, kunde);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()){
                    Blob blob = resultSet.getBlob(1);

                    fileOutputStream.write(blob.getBinaryStream().readAllBytes());

//                    byte[] bytes = blob.getBytes(1,(int)blob.length());
//                    fileOutputStream.write(bytes);

                    return true;
                }
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
