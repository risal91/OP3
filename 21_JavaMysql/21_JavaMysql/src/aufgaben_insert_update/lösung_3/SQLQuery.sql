CREATE DATABASE IF NOT EXISTS Filmsammlung;

USE Filmsammlung;
DROP TABLE IF EXISTS Film;

CREATE TABLE Film (
	ID INT PRIMARY KEY AUTO_INCREMENT,
	Titel VARCHAR(100) NOT NULL,
	Lagerort VARCHAR(100) NOT NULL,
	Spieldauer INT,
	BonusFeatures VARCHAR(100),
	Genre VARCHAR(100)
);

INSERT INTO Film (Titel, Lagerort, Spieldauer, BonusFeatures, Genre) VALUES
('Jurassic Park', 'Regal Wohnzimmer', 100, 'Directors Cut', 'Dinosaurier');

SELECT * FROM Film;