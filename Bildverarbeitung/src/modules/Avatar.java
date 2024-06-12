package modules;

import java.sql.Blob;
import java.util.HashMap;
import java.util.Map;

public class Avatar {
    public static final Map<Benutzer, Avatar> avatarMap = new HashMap<>();

    private final Benutzer benutzer;
    private Blob bild;

    public Avatar(Benutzer benutzer, Blob bild) {
        this.benutzer = benutzer;
        this.bild = bild;

        avatarMap.put(benutzer,this);
    }

    public Benutzer getBenutzer() {
        return benutzer;
    }

    public Blob getBild() {
        return bild;
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "benutzer=" + benutzer +
                ", bild=" + bild +
                '}';
    }
}
