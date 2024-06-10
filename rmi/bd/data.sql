SET AUTOCOMMIT =1;

DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS restaurant;

-- Table reservation
CREATE TABLE reservation (
    numres INT(4) AUTO_INCREMENT,
    nom VARCHAR(16),
    prenom VARCHAR(16),
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
    latitude DOUBLE,
    longitude DOUBLE,
    PRIMARY KEY (numrestau)
);


ALTER TABLE reservation
    ADD CONSTRAINT fk_reservation_restaurant
        FOREIGN KEY (numrestau)
            REFERENCES restaurant(numrestau);




-- Insère quelques restaurants
INSERT INTO restaurant (nom, latitude, longitude) VALUES
    ('Le Petit Parisien', 48.8566, 2.3522), -- Paris
    ('The London Pub', 51.5074, -0.1278), -- Londres
    ('La Bella Italia', 41.9028, 12.4964), -- Rome
    ('Tokyo Sushi House', 35.6895, 139.6917); -- Tokyo

-- Insère quelques réservations
INSERT INTO reservation (nom, prenom, nbpers, telephone, numrestau) VALUES
    ('Doe', 'John', 2, '1234567890', 1), -- Réservation au Le Petit Parisien
    ('Smith', 'Jane', 4, '9876543210', 2), -- Réservation au The London Pub
    ('Garcia', 'Maria', 3, '4561237890', 3), -- Réservation à La Bella Italia
    ('Sato', 'Takashi', 2, '7894561230', 4); -- Réservation au Tokyo Sushi House