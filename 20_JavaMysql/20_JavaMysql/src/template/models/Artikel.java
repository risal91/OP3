package template.models;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Model-Klasse für Artikel.
 */
public class Artikel
{
    // Wir nutzen hier eine HashMap, um einfacher an die Artikel zu kommen, wenn wir diese für Bestellungen speichern möchten.
    public static final Map<Integer, Artikel> artikel = new HashMap<>();

    private final int nummer;
    private String bezeichnung;
    // https://www.roseindia.net/jdbc/jdbc-mysql/mapping-mysql-data-types-in-java.shtml
    private BigDecimal preis; // Der Preis ist in SQL als Decimal gespeichert. Dies entspricht BigDecimal in Java.

    // Anstelle der Nummer speichern wir hier die Referenz auf das Hersteller-Objekt.
    // Vorteil: Wir können direkt auf die Hersteller zugreifen.
    // Nachteil: Wir müssen darauf achten, die Hersteller zuerst in unser Programm zu laden, bevor wir die Artikel laden können.
    private final Hersteller hersteller;

    public Artikel(int nummer, String bezeichnung, BigDecimal preis, Hersteller hersteller)
    {
        this.nummer = nummer;
        this.bezeichnung = bezeichnung;
        this.preis = preis;
        this.hersteller = hersteller;

        artikel.put(nummer, this);
    }

    // Keinen Setter zu Nummer, da Nummer ein Primärschlüssel ist und sich nicht ändern darf.
    public int getNummer()
    {
        return nummer;
    }

    public String getBezeichnung()
    {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung)
    {
        this.bezeichnung = bezeichnung;
    }

    public BigDecimal getPreis()
    {
        return preis;
    }

    public void setPreis(BigDecimal preis)
    {
        this.preis = preis;
    }

    // Keinen setter zu Hersteller, da sich die Objekt-Referenz des Herstellers in unserem Programm-Design nicht ändern wird.
    public Hersteller getHersteller()
    {
        return hersteller;
    }

    // toString() überschreiben, für eine einfache Ausgabe aller Attribute.
    @Override
    public String toString()
    {
        return "Artikel{" +
            "nummer=" + nummer +
            ", bezeichnung='" + bezeichnung + '\'' +
            ", preis=" + preis +
            ", hersteller=" + hersteller +
            '}';
    }
}
