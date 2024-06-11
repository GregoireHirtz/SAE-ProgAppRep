DROP TABLE IF EXISTS commande;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS restaurant;
DROP TABLE IF EXISTS plat;

-- Table reservation
CREATE TABLE reservation (
    numres INT(4) AUTO_INCREMENT,
    nom VARCHAR(16),
    prenom VARCHAR(16),
    nbpers INT(2),
    telephone VARCHAR(10),
    numrestau INT(4),
    date DATE,
    PRIMARY KEY (numres)
);

-- Table restaurent
CREATE TABLE restaurant (
    numrestau INT(4) AUTO_INCREMENT,
    nom VARCHAR(32),
    nbPlace INT(3),
    latitude DOUBLE,
    longitude DOUBLE,
    PRIMARY KEY (numrestau)
);

-- Table plat
CREATE TABLE plat (
    numplat INT(4) AUTO_INCREMENT,
    libelle VARCHAR(40),
    type VARCHAR(15),
    prixunit DECIMAL(6,2),
    PRIMARY KEY (numplat)
);

CREATE TABLE commande (
    numres INT(4),
    numplat INT(4),
    quantite INT(2),
    PRIMARY KEY(numres, numplat)
);



ALTER TABLE reservation
    ADD CONSTRAINT fk_reservation_restaurant
        FOREIGN KEY (numrestau)
            REFERENCES restaurant(numrestau);


ALTER TABLE commande
    ADD CONSTRAINT fk_commande_reservation
        FOREIGN KEY (numres)
            REFERENCES reservation(numres);
ALTER TABLE commande
    ADD CONSTRAINT fk_commande_plat
        FOREIGN KEY (numplat)
            REFERENCES plat(numplat);





-- Insère quelques restaurants
INSERT INTO restaurant (nom, nbPlace, latitude, longitude) VALUES
    ('Le Petit Parisien', 10, 48.8566, 2.3522), -- Paris
    ('The London Pub', 12, 51.5074, -0.1278), -- Londres
    ('La Bella Italia', 2, 41.9028, 12.4964), -- Rome
    ('Tokyo Sushi House', 8, 35.6895, 139.6917); -- Tokyo


-- Insère quelques restaurants
ALTER TABLE reservation AUTO_INCREMENT = 100;
INSERT INTO reservation (nom, prenom, nbpers, telephone, date, numrestau) VALUES
    ('Doe', 'John', 2, '1234567890', '2024-06-15', 1), -- Réservation au Le Petit Parisien
    ('Smith', 'Jane', 4, '9876543210', '2024-06-16', 2), -- Réservation au The London Pub
    ('Garcia', 'Maria', 3, '4561237890', '2024-06-17', 3), -- Réservation à La Bella Italia
    ('Sato', 'Takashi', 2, '7894561230', '2024-06-18', 4); -- Réservation au Tokyo Sushi House


-- Tuples de Plat
INSERT INTO plat (libelle, type, prixunit)
VALUES
    ('assiette de crudités', 'Entrée', 90),
    ('tarte de saison', 'Dessert', 90),
    ('sorbet mirabelle', 'Dessert', 90),
    ('filet de boeuf', 'Viande', 90),
    ('salade verte', 'Entrée', 90),
    ('chevre chaud', 'Entrée', 90),
    ('pate lorrain', 'Entrée', 90),
    ('saumon fumé', 'Entrée', 90),
    ('entrecote printaniere', 'Viande', 90),
    ('gratin dauphinois', 'Plat', 90),
    ('brochet à l\'oseille', 'Poisson', 90),
    ('gigot d\'agneau', 'Viande', 90),
    ('crème caramel', 'Dessert', 90),
    ('munster au cumin', 'Fromage', 90),
    ('filet de sole au beurre', 'Poisson', 90),
    ('fois gras de lorraine', 'Entrée', 90);


INSERT INTO commande (numres, numplat, quantite)
VALUES
    (100, 4, 2),
    (100, 5, 2),
    (100, 13, 1),
    (100, 3, 1),
    (101, 7, 2),
    (101, 16, 2),
    (101, 12, 2),
    (101, 15, 2),
    (101, 2, 2),
    (101, 3, 2),
    (102, 1, 2),
    (102, 10, 2),
    (102, 14, 2),
    (102, 2, 1),
    (102, 3, 1),
    (103, 9, 2),
    (103, 14, 2),
    (103, 2, 1),
    (103, 3, 1)
