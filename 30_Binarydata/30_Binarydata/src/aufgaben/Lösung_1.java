/* Erstellen Sie eine Konsolenanwendung zur Verwaltung von Benutzeraccounts.
    In diesem Beispiel werden Benutzer nur mit Benutzername und Profilbild gespeichert.
    Erstellen Sie eine Datenbank mit passender Tabelle für Benutzer.
    Erstellen Sie in Java eine passende Model-Klasse mit Methoden zum Erstellen von Accounts und dem Aktualisieren mit Profilbild.
    Testen Sie Ihr Programm, indem Sie einen Benutzer erstellen und anschließend diesen mit einem Bild (z.B. der WBS Bildmarke) aktualisieren.

DROP DATABASE IF EXISTS benutzerverwaltung1;
CREATE DATABASE benutzerverwaltung1;

USE benutzerverwaltung1;

CREATE TABLE benutzer (
    benutzername VARCHAR(100) PRIMARY KEY,
    profilbild BLOB
);
*/

package aufgaben;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Scanner;

public class Lösung_1
{
    public static void main(String[] args)
    {
        System.out.println("Benutzername eingeben:");
        Scanner scanner = new Scanner(System.in);
        String benutzername = scanner.nextLine();

        Benutzer b = Controller.createBenutzer(benutzername, new byte[] {});

        if (b != null)
        {
            System.out.println("Dateipfad eingeben:");

            String dateipfad = scanner.nextLine();

            try
            {
                // Ich lese die Datei hier über die Files-Klasse ein, da Java 8 noch kein readAllBytes() kann.
                // Zur Files-Klasse erfahren Sie mehr in OP4.
                byte[] profilbildBytes = Files.readAllBytes(Paths.get(dateipfad));
                b.setProfilbild(profilbildBytes);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}

class Benutzer
{
    private final String benutzername;

    private byte[] profilbild;

    public Benutzer(String benutzername, byte[] profilbild)
    {
        this.benutzername = benutzername;
        this.profilbild = profilbild;
    }

    public String getBenutzername()
    {
        return benutzername;
    }

    public byte[] getProfilbild()
    {
        return profilbild;
    }

    public void setProfilbild(byte[] profilbild)
    {
        if (Controller.updateProfilbild(benutzername, profilbild))
            this.profilbild = profilbild;
    }

}

class Controller
{
    private static final String connectionString = "jdbc:mysql://localhost:3306/benutzerverwaltung1";

    /**
     * Erstellt einen Benutzer in der Datenbank.
     * @param profilbildBytes Profilbild als Byte-Array
     * @return Der erstellte Benutzer.
     */
    public static Benutzer createBenutzer(String benutzername, byte[] profilbildBytes)
    {
        try (Connection connection = DriverManager.getConnection(connectionString, "root", "");
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO benutzer (benutzername, profilbild) VALUES (?, ?)"))
        {
            // das Profilbild wird mir als Byte-Array übergeben.
            // Über die Connection kann ich ein Blob erzeugen und dieses mit setBytes() befüllen.
            Blob profilbild = connection.createBlob();
            profilbild.setBytes(1, profilbildBytes);

            statement.setString(1, benutzername);
            statement.setBlob(2, profilbild);

            if (statement.executeUpdate() == 1)
                return new Benutzer(benutzername, profilbildBytes);

            return null;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    // Ich habe hier eine Select-Methode erstellt, auch wenn ich sie nicht wirklich verwende
    public static Benutzer selectBenutzer(String benutzername)
    {
        try (Connection connection = DriverManager.getConnection(connectionString, "root", "");
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT benutzername, profilbild FROM benutzer WHERE benutzername = ?"))
        {
            statement.setString(1, benutzername);

            ResultSet rs = statement.executeQuery();
            if (rs.next())
            {
                Blob profilbild = rs.getBlob(2);
                return new Benutzer(benutzername, profilbild.getBytes(1, (int)profilbild.length()));
            }
            return null;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Aktualisiert das Profilbild in der Datenbank.
     * @param profilbildBytes Profilbild als Byte-Array
     * @return True, wenn erfolgreich, sonst False.
     */
    public static boolean updateProfilbild(String benutzername, byte[] profilbildBytes)
    {
        try (Connection connection = DriverManager.getConnection(connectionString, "root", "");
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE benutzer SET profilbild = ? WHERE benutzername = ?"))
        {
            // das Profilbild wird mir als Byte-Array übergeben.
            // Über die Connection kann ich ein Blob erzeugen und dieses mit setBytes() befüllen.
            Blob profilbild = connection.createBlob();
            profilbild.setBytes(1, profilbildBytes);

            statement.setBlob(1, profilbild);
            statement.setString(2, benutzername);

            return statement.executeUpdate() == 1;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}