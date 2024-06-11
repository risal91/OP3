/* Ticketautomat (zum Beispiel für Paketboxen der DHL, wo sich Kunden mit einer generierten Nummer identifizieren müssen um Pakete abholen zu können)

    Tickets werden gespeichert mit einer ID (Auto_Increment) und einer zufällig erzeugten Nummer zwischen 1000 und 9999.

    Der Automat kann Tickets erstellen. Dabei wird eine Nummer zufällig erzeugt und in der Datenbank eingetragen.
    Zum Login von Benutzern muss die ID des Tickets und die erzeugte Nummer übergeben werden.
    Die übergebene Nummer wird mit der Nummer in der Datenbank verglichen und wenn sie übereinstimmen, war der Login erfolgreich.

    Folgende Methoden sollen implementiert werden:
    public static Ticket createTicket()
    public static boolean login(int id, int nummer)

    Testen Sie Ihr Programm, indem Sie ein Ticket erstellen lassen und dann durch manuelle Eingabe der Daten den Login versuchen.

    Hinweis: Für den Login könnte die Nummer per SELECT aus der Datenbank abgefragt und anschließend mit der übergebenen Nummer verglichen werden.

 */


package aufgaben_insert_update.lösung_4;


import java.sql.*;
import java.util.Random;
import java.util.Scanner;

class Ticket
{
    private final int id;
    private final int nummer;

    public Ticket(int id, int nummer)
    {
        this.id = id;
        this.nummer = nummer;
    }

    @Override
    public String toString()
    {
        return "Ticket{" +
                "id=" + id +
                ", nummer=" + nummer +
                '}';
    }
}

class Automat
{
    private static String connectionString = "jdbc:mysql://127.0.0.1:3306/ticketautomat";

    /**
     * Erstellt ein Ticket in der Datenbank.
     * @return Das erstellte Ticket.
     */
    public static Ticket createTicket()
    {
        // Zufallszahl von 1000 bis 9999.
        int nummer = new Random().nextInt(9000)+1000;

        // Try-With-Resources: Connection öffnen und wird bei Verlassen des Try-Blocks automatisch wieder geschlossen.
        try (Connection connection = DriverManager.getConnection(connectionString, "root", "");
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO ticket (nummer) VALUES (?)", Statement.RETURN_GENERATED_KEYS))
        {
            // Parameter hinzufügen
            statement.setInt(1, nummer);

            // DML Commands mit executeUpdate ausführen (INSERT, UPDATE, DELETE)
            statement.executeUpdate();

            // Die automatisch erstellte ID abfragen
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next())
            {
                int id = rs.getInt(1);

                // Ticket-Objekt erstellen
                return new Ticket(id, nummer);
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
     * Vergleicht die übergebene Nummer mit der Nummer des Tickets in der Datenbank.
     * @return True, wenn Nummer mit der Datenbank übereinstimmt, sonst False.
     */
    public static boolean login(int id, int nummer)
    {
        try (Connection connection = DriverManager.getConnection(connectionString, "root", "");
            PreparedStatement statement = connection.prepareStatement("SELECT nummer FROM ticket WHERE id = ?"))
        {
            statement.setInt(1, id);

            // SELECT-Befehle mit executeQuery ausführen
            ResultSet rs = statement.executeQuery();
            if (rs.next())
            {
                // Übergebene Nummer mit der Nummer in der Datenbank vergleichen
                if (nummer == rs.getInt("nummer"))
                    return true; // True, wenn sie identisch sind
                else
                    return  false; // Sonst False
            }
            return false;

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}

public class Lösung_4
{
    public static void main(String[] args)
    {
        // Ticket erstellen
        Ticket t = Automat.createTicket();
        System.out.println(t);

        Scanner s = new Scanner(System.in);
        System.out.print("ID: ");
        int id = Integer.parseInt(s.nextLine());
        System.out.print("Nummer: ");
        int nummer = Integer.parseInt(s.nextLine());

        // Login versuchen
        if (Automat.login(id, nummer))
            System.out.println("Erfolgreich!");
        else
            System.out.println("Fehler!");

    }
}
