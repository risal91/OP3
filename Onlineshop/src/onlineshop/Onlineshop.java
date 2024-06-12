package onlineshop;


import onlineshop.models.*;
import onlineshop.models.services.*;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

public class Onlineshop {

    public static void ausgabe(String tabelle){
        try (Connection connection = MySQL.getConnection();
             Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tabelle);

            while (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    System.out.print(resultSet.getString(i) + "\t");
                }
                System.out.println("");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        HerstellerService.selectHersteller();
        ArtikelService.selectArtikel();
        KundeService.selectKunde();
        AdresseService.selectAdresse();
        BestellungService.selectBestellung();
        BerstellpositionService.selectBestellposition();

       //KundeService.updateKunde(Kunde.kundeMap.get(123123124),"name","Sanchez");
      //ArtikelService.updateArtikel(Artikel.artikelMap.get(1),"preis","249.99");

        ausgabe("artikel");

    }
}
