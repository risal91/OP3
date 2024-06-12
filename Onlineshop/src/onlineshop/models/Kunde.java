package onlineshop.models;

import onlineshop.models.services.KundeService;

import java.util.HashMap;
import java.util.Map;

public class Kunde {

    public static final Map<Integer, Kunde> kundeMap = new HashMap<>();

    private final int nummer;
    private String name;

    public Kunde(int nummer, String name) {
        this.nummer = nummer;
        this.name = name;

        kundeMap.put(nummer,this);
    }


    public int getNummer() {
        return nummer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (KundeService.updateKunde(this,"name",name))
        {
            this.name =name;
        }

        this.name =name;

    }

    @Override
    public String toString() {
        return "Kunde{" +
                "nummer=" + nummer +
                ", name='" + name + '\'' +
                '}';
    }

}

