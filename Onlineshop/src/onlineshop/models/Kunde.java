package onlineshop.models;

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
        this.name = name;
    }

    @Override
    public String toString() {
        return "Kunde{" +
                "nummer=" + nummer +
                ", name='" + name + '\'' +
                '}';
    }

}

