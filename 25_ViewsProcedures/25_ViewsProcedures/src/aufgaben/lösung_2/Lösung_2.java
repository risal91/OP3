package aufgaben.lösung_2;

import aufgaben.lösung_2.kurs.Kurs;
import aufgaben.lösung_2.kurs.KursController;
import aufgaben.lösung_2.kursbelegung.Kursbelegung;
import aufgaben.lösung_2.kursbelegung.KursbelegungController;
import aufgaben.lösung_2.student.Student;
import aufgaben.lösung_2.student.StudentController;
import aufgaben.lösung_2.zimmer.ZimmerController;
import template.MySQL;

public class Lösung_2
{
    public static void main(String[] args)
    {
        MySQL.setConnectionString("jdbc:mysql://127.0.0.1:3306/universität");

        ZimmerController.selectZimmer();
        StudentController.selectStudenten();
        KursController.selectKurs();
        KursbelegungController.selectKursbelegung();

        /*for (Zimmer z : Zimmer.zimmer.values())
            System.out.println(z);*/

        Kursbelegung kursbelegung = KursbelegungController.createKursbelegung(Kurs.kurse.get("Mat130"), Student.studenten.get(4576), "S22", null);
        if (kursbelegung != null)
            kursbelegung.setNote(1.1);

        for (Kursbelegung k : Kursbelegung.kursbelegungen)
            System.out.println(k);
    }
}
