DROP DATABASE IF EXISTS ticketautomat;
CREATE DATABASE ticketautomat;
USE ticketautomat;

CREATE TABLE ticket (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nummer INT NOT NULL
);