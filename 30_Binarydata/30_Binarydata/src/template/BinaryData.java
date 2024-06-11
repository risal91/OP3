package template;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.Scanner;

public class BinaryData
{
    private static final String connectionString = "jdbc:mysql://localhost:3306/bank";


    public static void main(String[] args)
    {
        System.out.println("Dateipfad eingeben:");
        Scanner scanner = new Scanner(System.in);
        String dateipfad = scanner.nextLine();

		/*if (updateKundeMitBild(dateipfad, 1))
			System.out.println("Erfolgreich");*/

        if (selectBildVonKunde(dateipfad, 1))
            System.out.println("Erfolgreich");

    }

    /**
     * Fügt einem Kunden in der Datenbank das Bild hinzu.
     */
    private static boolean updateKundeMitBild(String dateipfad, int kunde)
    {
        // FileStream um die Datei lesen zu können.
        try (FileInputStream fileInputStream = new FileInputStream(dateipfad))
        {
            // PreparedStatement zum Einfügen der Bild-Datei.
            try (Connection connection = DriverManager.getConnection(connectionString, "root", "");
                 PreparedStatement statement = connection.prepareStatement(
                         "UPDATE kunde SET bild = ? WHERE id = ?"))
            {
                // Wir können den FileStream direkt übergeben, ohne die Daten manuell einlesen zu müssen.
                statement.setBinaryStream(1, fileInputStream);
                statement.setInt(2, kunde);

                return statement.executeUpdate() > 0;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Fragt das Bild eines Kunden aus der Datenbank ab und speichert dieses lokal.
     */
    private static boolean selectBildVonKunde(String dateipfad, int kunde)
    {
        // Das Bild soll aus der Datenbank als Datei auf der Festplatte gespeichert werden.
        // Durch diese Schreibweise im try werden externe Ressourcen automatisch wieder freigegeben, Verbindungen werden geschlossen.
        try (FileOutputStream fileOutputStream = new FileOutputStream(dateipfad))
        {
            try (Connection connection = DriverManager.getConnection(connectionString, "root", "");
                 PreparedStatement statement = connection.prepareStatement(
                         "SELECT bild FROM kunde WHERE id = ?"))
            {
                statement.setInt(1, kunde);

                ResultSet rs = statement.executeQuery();

                if (rs.next())
                {
                    // Das Bild können wir als Blob abfragen, oder direkt als BinaryStream
                    Blob blob = rs.getBlob(1);
                    //fileOutputStream.write(blob.getBinaryStream().readAllBytes()); // Ab Java 9

                    // Die write-Methode des FileOutputStreams möchte ein Byte-Array, also fragen wir aus dem blob mit getBytes() das Byte-Array ab.
                    byte[] bytes = blob.getBytes(1, (int)blob.length());
                    fileOutputStream.write(bytes);

                    return true;
                }
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

}
