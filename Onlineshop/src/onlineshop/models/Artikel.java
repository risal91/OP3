package onlineshop.models;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Artikel {

    public static  final Map<Integer, Artikel> artikelMap = new HashMap<>();

    private final int nummer;
    private String bezeichnung;
    private BigDecimal preis;
    private Hersteller hersteller;

    public Artikel(int nummer, String bezeichnung, BigDecimal preis, Hersteller hersteller) {
        this.nummer = nummer;
        this.bezeichnung = bezeichnung;
        this.preis = preis;
        this.hersteller = hersteller;

        artikelMap.put(nummer, this);
    }

    public int getNummer() {
        return nummer;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public BigDecimal getPreis() {
        return preis;
    }

    public void setPreis(BigDecimal preis) {
        this.preis = preis;
    }

    public Hersteller getHersteller() {
        return hersteller;
    }

    public void setHersteller(Hersteller hersteller) {
        this.hersteller = hersteller;
    }

    @Override
    public String toString() {
        return "Artikel{" +
                "nummer=" + nummer +
                ", bezeichnung='" + bezeichnung + '\'' +
                ", preis=" + preis +
                ", hersteller=" + hersteller +
                '}';
    }
}

