package modules;

import java.util.HashMap;
import java.util.Map;

public class Benutzer {
    public static final Map<Integer, Benutzer> benutzerMap = new HashMap<>();

    private final int id;
    private String benutzer;

    public Benutzer(int id, String benutzer) {
        this.id = id;
        this.benutzer = benutzer;

        benutzerMap.put(id,this);

    }

    public int getId() {
        return id;
    }

    public String getBenutzer() {
        return benutzer;
    }
}
