package onlineshop.models;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class Bestellung {

    public static final Map<Integer, Bestellung> bestellungMap = new HashMap<>();

    private final int Nummer;
    private Date datum;
    private Kunde kunde;
    private Adresse rechnungsadresse;
    private Adresse lieferadresse;

    public Bestellung(int nummer, Date datum, Kunde kunde, Adresse rechnungsadresse, Adresse lieferadresse) {
        Nummer = nummer;
        this.datum = datum;
        this.kunde = kunde;
        this.rechnungsadresse = rechnungsadresse;
        this.lieferadresse = lieferadresse;

        bestellungMap.put(nummer,this);
    }

    public int getNummer() {
        return Nummer;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public Kunde getKunde() {
        return kunde;
    }

    public void setKunde(Kunde kunde) {
        this.kunde = kunde;
    }

    public Adresse getRechnungsadresse() {
        return rechnungsadresse;
    }

    public void setRechnungsadresse(Adresse rechnungsadresse) {
        this.rechnungsadresse = rechnungsadresse;
    }

    public Adresse getLieferadresse() {
        return lieferadresse;
    }

    public void setLieferadresse(Adresse lieferadresse) {
        this.lieferadresse = lieferadresse;
    }

    @Override
    public String toString() {
        return "Bestellung{" +
                "Nummer=" + Nummer +
                ", datum=" + datum +
                ", kunde=" + kunde +
                ", rechnungsadresse=" + rechnungsadresse +
                ", lieferadresse=" + lieferadresse +
                '}';
    }
}
