/*
Erstellen Sie eine Anwendung (gerne mit GUI, Konsole ist aber auch okay), um Ihre Filmsammlung verwalten zu können.
Entnehmen Sie aus folgendem SQL Code die benötigten Informationen

CREATE TABLE Film (
	ID INT PRIMARY KEY AUTO_INCREMENT,
	Titel VARCHAR(100) NOT NULL,
	Lagerort VARCHAR(100) NOT NULL,
	Spieldauer INT,
	BonusFeatures VARCHAR(100),
	Genre VARCHAR(100)
)

INSERT INTO Film (Titel, Lagerort, Spieldauer, BonusFeatures, Genre) VALUES
('Jurassic Park', 'Regal Wohnzimmer', 100, 'Directors Cut', 'Dinosaurier')

Erstellen Sie die benötigten Models und Controller.
Erstellen Sie ein Menü, mit dem ein Benutzer neue Filme der Sammlung hinzufügen kann.
Bei der Auswahl des Lagerortes sollen alle bereits eingetragenen Lagerorte vorgeschlagen werden (in einer GUI z.B. als ComboBox).
Der Benutzer kann direkt beim Anlegen neuer Filme neue Lagerorte hinzufügen.

*/

package aufgaben_insert_update.lösung_3;

import java.util.Scanner;

public class Lösung_3
{
    public static void main(String[] args)
    {
        Service.selectFilme();
        for (Film f : Film.getFilme().values())
            System.out.println(f);

        //neuerFilm();
        lagerortÄndern();

        for (Film f : Film.getFilme().values())
            System.out.println(f);
    }

    public static void neuerFilm()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Titel: ");
        String titel = scanner.nextLine();

        System.out.println("Bisherige Lagerorte: ");
        System.out.println(Service.selectLagerorte());
        System.out.print("Lagerort: ");
        String lagerort = scanner.nextLine();

        System.out.print("Spieldauer in vollen Minuten: ");
        int spieldauer = Integer.parseInt(scanner.nextLine());

        System.out.print("Bonus Features: ");
        String bonusFeatures = scanner.nextLine();

        System.out.print("Genre: ");
        String genre = scanner.nextLine();

        Film f = Service.createFilm(titel, lagerort, spieldauer, bonusFeatures, genre);
        System.out.println(f);
    }

    public static void lagerortÄndern()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Film-ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.println("Bisherige Lagerorte: ");
        System.out.println(Service.selectLagerorte());
        System.out.print("Lagerort: ");
        String lagerort = scanner.nextLine();

        Film.getFilme().get(id).setLagerort(lagerort);

    }
}
