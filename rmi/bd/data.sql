DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS restaurant;

-- Table reservation
CREATE TABLE reservation (
    numres INT(4) AUTO_INCREMENT,
    nom VARCHAR(16),
    PRENOM VARCHAR(16),
    nbpers INT(2),
    telephone VARCHAR(10),
    numrestau INT(4),
    PRIMARY KEY (numres)
);

-- Table restaurent
CREATE TABLE restaurant
(
    numrestau INT(4) AUTO_INCREMENT,
    nom VARCHAR(32),
    prenom VARCHAR2(32),
    latitude DOUBLE,
    longitude DOUBLE,
    PRIMARY KEY (numrestau)
);







ALTER TABLE reservation
    ADD CONSTRAINT fk_reservation_restaurant
        FOREIGN KEY (numrestau)
            REFERENCES restaurant(numrestau);









-- Tuples de tabl
ALTER TABLE tabl AUTO_INCREMENT = 10;
INSERT INTO tabl (nbplace)
VALUES
    (4),
    (6),
    (8),
    (4),
    (6),
    (4),
    (4),
    (6),
    (2),
    (4);


-- Tuples de reservation
#ALTER TABLE reservation AUTO_INCREMENT = 100;
#INSERT INTO reservation (datres, nbpers, datpaie, modpaie)
#VALUES
#    ('2021-09-10 19:00', 2, '2021),
