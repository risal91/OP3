package onlineshop.models;

import java.util.ArrayList;
import java.util.List;

public class Bestellposition {
    //wenn ich nicht einen Eindeutigen einzigartigen PK habe nutzen wir List anstelle von MAP
    public static final List<Bestellposition> bestellpositionListe = new ArrayList<>();

    private final Bestellung bestllung;
    private final Artikel artikel;
    private int anzahl;

    public Bestellposition(Bestellung bestllung, Artikel artikel, int anzahl) {
        this.bestllung = bestllung;
        this.artikel = artikel;
        this.anzahl = anzahl;

        bestellpositionListe.add(this);
    }

    public Bestellung getBestllung() {
        return bestllung;
    }

    public Artikel getArtikel() {
        return artikel;
    }

    public int getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }

    @Override
    public String toString() {
        return "Bestellposition{" +
                "bestllung=" + bestllung +
                ", artikel=" + artikel +
                ", anzahl=" + anzahl +
                '}';
    }
}
