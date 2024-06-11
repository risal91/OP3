package onlineshop;


import onlineshop.models.*;
import onlineshop.models.services.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

public class Onlineshop {

    public static void main(String[] args) {

        HerstellerService.selectHersteller();
        ArtikelService.selectArtikel();
        KundeService.selectKunde();
        AdresseService.selectAdresse();
        BestellungService.selectBestellung();
        BerstellpositionService.selectBestellposition();

//        HerstellerService.createHersteller("Bfone");
//        KundeService.creatKunde("Marco Polo");
//        Adresse adresse = AdresseService.creatadresse("Hauptring 1","55663","Haupthausen",Kunde.kundeMap.get(123123127));
//        Artikel artikel = ArtikelService.creatArtikel("Handy", BigDecimal.valueOf(1899.99), Hersteller.herstellerMap.get(5));
//        Bestellung bestellung = BestellungService.createBestellung(Date.valueOf(LocalDate.now()),Kunde.kundeMap.get(123123127),Adresse.adresseMap.get(5),Adresse.adresseMap.get(4));
//        Bestellposition bestellposition = BerstellpositionService.createbestellposition(Bestellung.bestellungMap.get(5),Artikel.artikelMap.get(6),1);



//        System.out.println("Kunden:\n");
//
//        for (Kunde k : Kunde.kundeMap.values()){
//            System.out.println(k);
//        }
//
//        System.out.println("");
//        System.out.println("");
//
//        System.out.println("Artikel:\n");
//
//        for (Artikel a : Artikel.artikelMap.values()){
//            System.out.println(a);
//        }
//
//        System.out.println("");
//        System.out.println("");
//
//        System.out.println("Bestellung:\n");
//        for (Bestellung b : Bestellung.bestellungMap.values()){
//            System.out.println(b);
//        }
//
//        System.out.println("");
//        System.out.println("");
//        System.out.println("Hersteller:\n");
//        for (Hersteller h : Hersteller.herstellerMap.values()){
//            System.out.println(h);
//        }
//
//        System.out.println("");
//        System.out.println("");
//
        System.out.println("Bestellpositionen:\n");
        for (Bestellposition bb : Bestellposition.bestellpositionListe){
            System.out.println(bb);
            System.out.println("");
        }

    }
}
