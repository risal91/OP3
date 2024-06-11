package aufgaben_insert_update.lösung_3;

import java.util.HashMap;
import java.util.Map;

public class Film
{
    private static final Map<Integer, Film> filme = new HashMap<>();

    public static Map<Integer, Film> getFilme() {
        return new HashMap<>(filme);
    }

    private final int id;
    private String titel;
    private String lagerort;
    private int spieldauer;
    private String bonusFeatures;
    private String genre;


    public Film(int id, String titel, String lagerort, int spieldauer, String bonusFeatures, String genre)
    {
        this.id = id;
        this.titel = titel;
        this.lagerort = lagerort;
        this.spieldauer = spieldauer;
        this.bonusFeatures = bonusFeatures;
        this.genre = genre;

        filme.put(id,  this);
    }

    public void setLagerort(String lagerort)
    {
        if (Service.updateFilm(this, "lagerort", lagerort))
            this.lagerort = lagerort;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString()
    {
        return "Film{" +
                "id=" + id +
                ", titel='" + titel + '\'' +
                ", lagerort='" + lagerort + '\'' +
                ", spieldauer=" + spieldauer +
                ", bonusFeatures='" + bonusFeatures + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
}
