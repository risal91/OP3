/* ACHTUNG: DIESE LÖSUNG IST NOCH FÜR MS-SQL! Bei MySQL könnte die Syntax anders sein */

CREATE TABLE Zimmer (
	ZimmerNummer INT PRIMARY KEY,
	Telefonnummer INT UNIQUE
)

CREATE TABLE Kurs (
	KursKürzel CHAR(6) PRIMARY KEY,
	[Name] VARCHAR(255) NOT NULL -- [] bei Spalten, deren Bezeichnung identisch zu SQL Schlüsselwörtern ist (z.B. FROM, SELECT, etc), oder bei Leerzeichen im Spaltennamen
)

CREATE TABLE Student (
	StudentNummer INT PRIMARY KEY,
	Nachname VARCHAR(255) NOT NULL,
	Vorname VARCHAR(255) NOT NULL,
	Geburtsdatum DATE NOT NULL,
	ZimmerNummer INT FOREIGN KEY REFERENCES Zimmer (ZimmerNummer),
	CHECK (StudentNummer <= 99999 AND StudentNummer > 0)
)

CREATE TABLE Kursbelegung (
	StudentNummer INT FOREIGN KEY REFERENCES Student (StudentNummer),
	KursKürzel CHAR(6) FOREIGN KEY REFERENCES Kurs (KursKürzel),
	Semester CHAR(3) NOT NULL,
	Note DECIMAL(2,1),
	PRIMARY KEY (StudentNummer, KursKürzel) -- Primary Key Spalten sind automatisch NOT NULL und bei zusammengesetzten Schlüsseln in der Kombination UNIQUE
)

INSERT INTO Zimmer (ZimmerNummer, Telefonnummer) VALUES
(120, 2136),
(237, 3127)
SELECT * FROM Zimmer

INSERT INTO Kurs (KursKürzel, [Name]) VALUES
('Mat122', 'Zählen bis 10'),
('Phy120', 'Grundlagen der Schwerkraft'),
('Wiw330', 'Geldausgeben ganz leicht'),
('Mat130', 'Rechnen mit Fingern'),
('Phy230', 'Schweben für Anfänger'),
('Mat120', 'Zählen bis 3')
SELECT * FROM Kurs

INSERT INTO Student (StudentNummer, Nachname, Vorname, Geburtsdatum, ZimmerNummer) VALUES
(3215, 'Jonas', 'Mike', '1991-02-23', 120),
(3456, 'Klaus', 'Schmidt', '1990-03-05', 237),
(4576, 'Paul', 'Neider', '1989-07-17', 120),
(1111, 'Lisa', 'Müller', '1990-04-15', 237)


SELECT * FROM Student

INSERT INTO Kursbelegung (StudentNummer, KursKürzel, Semester, Note) VALUES
(3215,'Mat122','W88',1.4),
(3215,'Phy120','S88',2.5),
(3215,'Wiw330','W89',3.1),
(3456,'Mat122','W87',3.2),
(3456,'Mat130','S87',2.9),
(4576,'Phy230','W88',2.8),
(4576,'Mat120','S88',2.1)
SELECT * FROM Kursbelegung

-- Durch die Check-Einschränkung bei Student können keine Studenten mit Nummer > 99.999 und <= 0 angelegt werden
INSERT INTO Student (StudentNummer, Vorname, Nachname, Geburtsdatum) VALUES
(100000, 'Vorname', 'Nachname', '1990-01-01')





-- NICHT TEIL DER LÖSUNG

go
CREATE VIEW KursbelegungView AS
	SELECT Kurs.KursKürzel, [Name], Student.StudentNummer, Vorname, Nachname, Semester, Note
	FROM Kursbelegung
	JOIN Kurs ON Kursbelegung.KursKürzel = Kurs.KursKürzel
	JOIN Student ON Kursbelegung.StudentNummer = Student.StudentNummer
go

SELECT * FROM KursbelegungView ORDER BY KursKürzel, StudentNummer
--DROP VIEW KursbelegungView



go
CREATE PROCEDURE KursbelegungNeu
@KursKürzel CHAR(6), @StudentNummer INT, @Semester CHAR(3) -- Funktionsparameter
AS
BEGIN -- entspricht { in C#
	INSERT INTO Kursbelegung (KursKürzel, StudentNummer, Semester) -- Spaltenliste
	VALUES (@KursKürzel, @StudentNummer, @Semester) -- Parameter als Values
END -- entspricht } in C#
go

EXEC KursbelegungNeu 'Mat120', 1111, 'S21' -- Ausführen der Prozedur über EXEC und Auflistung der Argumente durch Komma getrennt

go
CREATE PROCEDURE KursbelegungNote
@KursKürzel CHAR(6), @StudentNummer INT, @Note DECIMAL(2,1)
AS
BEGIN
	UPDATE Kursbelegung SET Note = @Note WHERE KursKürzel = @KursKürzel AND StudentNummer = @StudentNummer
END
go

EXEC KursbelegungNote 'Mat120', 1111, 2.2




-- Beispiel für Prozeduren
go
CREATE PROCEDURE ZimmerTauschen
@StudentNummer1 INT, @StudentNummer2 INT, @Ergebnis INT OUTPUT, @Ergebnis2 INT OUTPUT -- Werte aus Funktion zurückgeben mit OUTPUT-Parametern
AS
BEGIN
	DECLARE @ZimmerNummer1 INT = (SELECT ZimmerNummer FROM Student WHERE StudentNummer = @StudentNummer1)
	DECLARE @ZimmerNummer2 INT = (SELECT ZimmerNummer FROM Student WHERE StudentNummer = @StudentNummer2)
	SET @Ergebnis = 42 -- Zuweisung an Variablen mit SET
	SET @Ergebnis2 = 1111111
	BEGIN TRANSACTION
	BEGIN TRY
		UPDATE Student SET ZimmerNummer = @ZimmerNummer2 WHERE StudentNummer = @StudentNummer1;
		UPDATE Student SET ZimmerNummer = @ZimmerNummer1 WHERE StudentNummer = @StudentNummer2;
		COMMIT
	END TRY
	BEGIN CATCH
		ROLLBACK
		RETURN
	END CATCH
END
go


DECLARE @ErgebnisA INT
DECLARE @ErgebnisB INT
EXEC ZimmerTauschen 3215, 3456, @ErgebnisA OUTPUT, @ErgebnisB OUTPUT

SELECT @ErgebnisA, @ErgebnisB -- Variablen ausgeben

-- Variablen in SQL
DECLARE @A CHAR(1)
SET @A = 'A'
DECLARE @B CHAR(1) = 'B'
DECLARE @C CHAR(2) = @A + @B
SELECT @C