/*
    Bücherverleih
    Bücher werden gespeichert mit ID und Titel. Jedes Buch ist nur ein Mal im Bestand.
    Kunden haben Nummer, Name und Guthaben.
    Ausleihe ist gespeichert mit Buch, Kunde, Datum, Dauer.
    Wird ein Buch ausgeliehen, wird es in Ausleihe eingetragen und für die Dauer in Tagen je 1€ vom Guthaben des Kunden abgezogen.
    Wird das Buch zurückgebracht, wird es aus der Ausleihe gelöscht.

    Um ein Buch ausleihen zu können, muss geprüft werden, ob das Buch bereits ausgeliehen ist.
    Dann muss geprüft werden, ob Kunde genug Guthaben für die Dauer hat.
    Ist beides Okay, kann das Buch ausgeliehen werden.

    Dieser Vorgang soll innerhalb einer Transaktion stattfinden. Tritt ein Fehler auf, wird der Vorgang rückgängig gemacht.


 */

package aufgaben.lösung_1;

import java.sql.*;
import java.time.LocalDate;

public class Buchverleih
{

    public static void main(String[] args)
    {
        // import OP3._20_java_und_mysql.MySQL;
        MySQL.setConnectionString("jdbc:mysql://localhost:3306/buchverleih");

        selectKunde();
        selectBuch();
        selectAusleihe();

        try
        {
            if (ausleihen(Buch.bücher.get(1), Kunde.kunden.get(1), LocalDate.now(), 5) != null)
                System.out.println("Erfolg!");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        for (Ausleihe a : Ausleihe.ausleihen.values())
            System.out.println(a);

    }

    /**
     * Lädt alle Kunden aus der Datenbank.
     */
    public static void selectKunde()
    {
        try (Connection connection = MySQL.getConnection();
            Statement statement = connection.createStatement())
        {
            ResultSet rs = statement.executeQuery("SELECT * FROM kunde");

            while (rs.next())
            {
                new Kunde(rs.getInt("nummer"), rs.getString("name"), rs.getInt("guthaben"));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Lädt alle Bücher aus der Datenbank.
     */
    public static void selectBuch()
    {
        try (Connection connection = MySQL.getConnection();
             Statement statement = connection.createStatement())
        {
            ResultSet rs = statement.executeQuery("SELECT * FROM buch");

            while (rs.next())
            {
                new Buch(rs.getInt("id"), rs.getString("titel"));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Lädt alle Ausleihen aus der Datenbank.
     */
    public static void selectAusleihe()
    {
        try (Connection connection = MySQL.getConnection();
             Statement statement = connection.createStatement())
        {
            ResultSet rs = statement.executeQuery("SELECT * FROM ausleihe");

            while (rs.next())
            {
                Buch b = Buch.bücher.get(rs.getInt("buch"));
                Kunde k = Kunde.kunden.get(rs.getInt("kunde"));
                LocalDate ld = rs.getObject("datum", LocalDate.class);
                int d = rs.getInt("dauer");
                new Ausleihe(b, k, ld, d);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Erstellt eine neue Ausleihe.
     * Es wird geprüft, ob das Buch ausgeliehen werden kann und ob Kunde genug Guthaben hat.
     * Der Vorgang findet in einer SQL-Transaktion statt.
     * @return Das erstellte Ausleihe-Objekt.
     */
    public static Ausleihe ausleihen(Buch buch, Kunde kunde, LocalDate datum, int dauer) throws SQLException
    {
        // Befindet sich das Buch in der HashMap, ist es bereits ausgeliehen.
        if (Ausleihe.ausleihen.get(buch) == null && kunde.getGuthaben() - dauer >= 0)
        {
            // Connection erstellen
            try (Connection connection = MySQL.getConnection())
            {
                // AutoCommit deaktivieren
                connection.setAutoCommit(false);

                // Versuch, Ausleihe der Datenbank hinzuzufügen.
                try (PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO ausleihe (buch, kunde, datum, dauer) VALUES (?, ?, ?, ?)"))
                {
                    statement.setInt(1, buch.getId());
                    statement.setInt(2, kunde.getNummer());
                    statement.setObject(3, datum);
                    statement.setInt(4, dauer);

                    if (statement.executeUpdate() != 1)
                    {
                        // Rollback bei Fehler
                        connection.rollback();
                        return null;
                    }
                }
                catch (SQLException e)
                {
                    // Rollback bei Fehler
                    connection.rollback();
                    throw e;
                }

                // Versuch, dem Kunden das Guthaben zu verringern.
                try (PreparedStatement statement = connection.prepareStatement("UPDATE kunde SET guthaben = guthaben - ? WHERE nummer = ?"))
                {
                    statement.setInt(1, dauer);
                    statement.setInt(2, kunde.getNummer());

                    if (statement.executeUpdate() != 1)
                    {
                        // Rollback bei Fehler
                        connection.rollback();
                        return null;
                    }
                }
                catch (SQLException e)
                {
                    // Rollback bei Fehler
                    connection.rollback();
                    throw e;
                }

                // Hat bis hier hin alles geklappt, erstellen wir das Objekt
                Ausleihe a = new Ausleihe(buch, kunde, datum, dauer);
                // und reduzieren das Guthaben des Kunden
                kunde.addGuthaben(-dauer);

                // commit nicht vergessen!
                connection.commit();
                return a;
            }
        }
        else
            return null;
    }
}


