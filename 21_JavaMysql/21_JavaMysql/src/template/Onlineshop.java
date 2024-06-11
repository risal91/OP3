package template;

import template.models.*;
import template.models.services.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Dieses Beispiel basiert auf 10_dbsql_wiederholung

// Importieren von Bibliotheken, z.B. den MySQL Connector:
// https://www.jetbrains.com/help/idea/library.html

public class Onlineshop
{
    public static void main(String[] args)
    {
        // Reihenfolge beachten: Wir müssen zuerst Hersteller laden,
        // damit wir für die Artikel auch die Hersteller-Referenzen speichern können
        HerstellerService.selectHersteller();
        ArtikelService.selectArtikel();

        /*for (Hersteller h : Hersteller.hersteller.values())
			System.out.println(h); // toString() wird automatisch durch println aufgerufen */
		/*for (Artikel a : Artikel.artikel.values())
			System.out.println(a);*/

        KundeService.selectKunde();
        AdresseService.selectAdresse();
        BestellungService.selectBestellung();
        BestellpositionService.selectBestellposition();

        /*for (Bestellposition b : Bestellposition.bestellpositionen)
            System.out.println(b);*/

        Artikel artikel = ArtikelService.createArtikel("BluRay Player Deluxe", BigDecimal.valueOf(129.90), Hersteller.hersteller.get(1));
        artikel.setPreis(BigDecimal.valueOf(29.99));
        Kunde k = KundeService.createKunde("Cirillo");
        Adresse l = AdresseService.createAdresse("Schöne Straße 1", "12345", "Hauptstadt", k);
        Bestellung b = BestellungService.createBestellung(LocalDateTime.now(), k, l, l);
        Bestellposition bestellposition = BestellpositionService.createBestellposition(b, artikel, 1);
        bestellposition.setAnzahl(2);

        for (Bestellposition bp : Bestellposition.bestellpositionen)
            System.out.println(bp);

        Hersteller neuerHersteller = HerstellerService.createHersteller("Ein super toller neuer Hersteller für nette Dinge");
        neuerHersteller.setName("Dieser Hersteller hat nun einen neuen Namen");

        for (Hersteller h : Hersteller.hersteller.values())
            System.out.println(h);
    }
}
