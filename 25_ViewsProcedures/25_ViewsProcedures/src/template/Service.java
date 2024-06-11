package template;

import java.math.BigDecimal;
import java.sql.*;

public class Service
{
    /**
     * Führt die View "selectAlles" aus und gibt das Ergebnis als String zurück.
     * @return Das Ergebnis der View-Abfrage als String.
     */
    public static String selectAlles()
    {
        // getConnection() gibt uns die Connection zurück, die wir hier im Try-With-Resources als Variable hinterlegen.
        // Dadurch wird die Connection bei Verlassen des Try-Blocks automatisch geschlossen! Wir sparen uns hier den Finally-Block!
        try (Connection connection = MySQL.getConnection(); // Im Try-With-Resources können mehrere Befehle durch Semikolon getrennt aufgelistet werden.
             Statement statement = connection.createStatement())
        {
            // Views können einfach wie Tabellen abgefragt werden.
            ResultSet rs = statement.executeQuery("select * from selectAlles");

            // Wir bauen uns eine rudimentäre Ausgabe zusammen. Nicht hübsch, aber erledigt den Job.
            StringBuilder sb = new StringBuilder();

            //sb.append("Kunde - Kunde_Name - Bestellung - Datum - Rechnung - Lieferung - Artikel - Bezeichnung - Anzahl - Hersteller - Hersteller_Name\n");

            // Das Ganze dynamischer:
            sb.append(" | ");
            for(int i = 1; i <= rs.getMetaData().getColumnCount() ; i++){
                sb.append( rs.getMetaData().getColumnName(i) ).append(" | ");
            }
            sb.append("\n");

            while (rs.next())
            {
                /*
                sb.append(rs.getString("kunde")).append(" - ");
                sb.append(rs.getString("kunde_name")).append(" - ");
                sb.append(rs.getString("bestellung")).append(" - ");
                sb.append(rs.getString("datum")).append(" - ");
                sb.append(rs.getString("rechnung")).append(" - ");
                sb.append(rs.getString("lieferung")).append(" - ");
                sb.append(rs.getString("artikel")).append(" - ");
                sb.append(rs.getString("bezeichnung")).append(" - ");
                sb.append(rs.getString("anzahl")).append(" - ");
                sb.append(rs.getString("hersteller")).append(" - ");
                sb.append(rs.getString("hersteller_name")).append("\n");
                */

                // Das Ganze dynamischer:
                 sb.append(" | ");
                for(int i = 1; i <= rs.getMetaData().getColumnCount() ; i++){
                    sb.append( rs.getString(i) ).append(" | ");
                }
                sb.append("\n");
            }

            return sb.toString();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Führt die Prozedur "insertArtikel" aus und gibt die durch AUTO_INCREMENT erstellte Artikelnummer zurück.
     * @return Die erzeugte Artikelnummer.
     */
    public static int insertArtikel(String bezeichnung, BigDecimal preis, int hersteller)
    {
        // Um StoredProcedures auszuführen brauchen wir CallableStatement.
        try (Connection connection = MySQL.getConnection();
                CallableStatement statement = connection.prepareCall("{CALL insertArtikel(?,?,?,?)}"))
        {
            // Parameter setzen
            statement.setString(1, bezeichnung);
            statement.setBigDecimal(2, preis);
            statement.setInt(3, hersteller);

            // Den Out-Parameter müssen wir registrieren und den SQL-Datentypen angeben (machen wir hier über JDBCType)
            statement.registerOutParameter(4, JDBCType.INTEGER);

            // Wenn das Insert in der Prozedur klappt (das heißt, executeUpdate gibt einen positiven Wert zurück)
            if (statement.executeUpdate() > 0)
                // können wir den Out-Parameter abfragen.
                return statement.getInt(4);

            return -1;

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Führt die Prozedur "updateArtikelPreis" aus und gibt die Anzahl der betroffenen Datensätze zurück.
     * @return Die Anzahl der betroffenen Datensätze.
     */
    public static int updateArtikelPreis(int nummer, BigDecimal preis)
    {
        // Um StoredProcedures auszuführen brauchen wir CallableStatement.
        try (Connection connection = MySQL.getConnection();
                CallableStatement statement = connection.prepareCall("{CALL updateArtikelPreis(?,?)}"))
        {
            statement.setInt(1, nummer);
            statement.setBigDecimal(2, preis);

            return statement.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

}
