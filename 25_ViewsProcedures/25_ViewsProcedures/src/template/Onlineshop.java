package template;

import java.math.BigDecimal;

public class Onlineshop {

    public static void main(String[] args)
    {

        int nummer = Service.insertArtikel("Hundefutter", BigDecimal.valueOf(1.99), 2);
        System.out.println(nummer);

        int anzahl = Service.updateArtikelPreis(nummer, BigDecimal.valueOf(1.49));
        System.out.println(anzahl);

        String alles = Service.selectAlles();
        System.out.println(alles);

    }

}
