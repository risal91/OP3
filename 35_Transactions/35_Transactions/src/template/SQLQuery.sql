# Transaktionen in MySQL
# Mit Transaktionen können wir im Falle eines Fehlers Änderungen rückgängig machen.
# Beispiel: Wir buchen Geld von einem Konto auf ein anderes. Zuerst ziehen wir von A das Geld ab und buchen es auf B.
# Wenn nun aber beim Buchen auf B ein Fehler auftritt, muss das Abbuchen von A rückgängig gemacht werden.

USE bank;

# Hinweis: Transact-SQL (Das von Microsoft) kennt Try-Catch. MySQL hat leider kein TryCatch.
# Das Error/Exception Handling in MySQL ist kompliziert, darum verzichte ich hier darauf.
# https://www.mysqltutorial.org/mysql-error-handling-in-stored-procedures/

DROP PROCEDURE IF EXISTS überweisung;

DELIMITER $$

CREATE PROCEDURE überweisung
(kontoVon CHAR(10), kontoNach CHAR(10), betrag DOUBLE)
BEGIN
	START TRANSACTION;

    UPDATE konto SET kontostand = kontostand - betrag WHERE nummer = kontoVon;
    SET @count1 = (SELECT ROW_COUNT()); # ROW_COUNT() gibt die Anzahl der betroffenen Zeilen des letzten Befehls zurück
    UPDATE konto SET kontostand = kontostand + betrag WHERE nummer = kontoNach;
    SET @count2 = (SELECT ROW_COUNT());

    IF @count1 = 1 AND @count2 = 1 THEN
    	COMMIT; # Wenn bei beiden Befehlen ein Datensatz betroffen wurde, bestätigen wir die Transaktion.
    ELSE
    	ROLLBACK; # Funktioniert eine der Befehle nicht richtig, machen wir alles rückgängig.
        SELECT CONCAT('Fehler bei der Überweisung von ', kontoVon, ' auf ', kontoNach) AS Meldung;
    END IF;
END$$
DELIMITER ;

CALL überweisung('0000987654', '0000123456', 1000.0);
SELECT * FROM konto;
CALL überweisung('0000987654', '0000', 1000.0);
SELECT * FROM konto;