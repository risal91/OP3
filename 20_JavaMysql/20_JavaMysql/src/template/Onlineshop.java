package template;

import template.models.services.ArtikelService;
import template.models.services.HerstellerService;
import template.models.Artikel;
import template.models.Hersteller;

// Dieses Beispiel basiert auf 10_dbsql_wiederholung

// Importieren von Bibliotheken, z.B. den MySQL Connector:
// https://www.jetbrains.com/help/idea/library.html

public class Onlineshop
{
    public static void main(String[] args)
    {
        // Reihenfolge beachten: Wir müssen zuerst Hersteller laden,
        // damit wir für die Artikel auch die Hersteller-Referenzen speichern können.
        HerstellerService.selectHersteller();
        ArtikelService.selectArtikel();

        for (Hersteller h : Hersteller.hersteller.values())
            System.out.println(h); // toString() wird automatisch durch println aufgerufen.
        for (Artikel a : Artikel.artikel.values())
            System.out.println(a);

    }
}
