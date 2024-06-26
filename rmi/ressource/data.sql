DROP TABLE IF EXISTS commande;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS restaurant;
DROP TABLE IF EXISTS plat;
DROP TABLE IF EXISTS tabl;

-- Table reservation
CREATE TABLE reservation (
    numres INT(4) AUTO_INCREMENT,
    nom VARCHAR(16),
    prenom VARCHAR(16),
    nbpers INT(2),
    telephone VARCHAR(10),
    numrestau INT(4),
    date DATE,
    dateajout DATE,
    numtab int(4),
    PRIMARY KEY (numres)
);

-- Table restaurent
CREATE TABLE restaurant (
    numrestau INT(4) AUTO_INCREMENT,
    nom VARCHAR(32),
    latitude DOUBLE,
    longitude DOUBLE,
    PRIMARY KEY (numrestau)
);

-- Table tabl
CREATE TABLE tabl (
    numtab INT(4) AUTO_INCREMENT,
    nbplace INT(2),
    numrestau INT(4),
    PRIMARY KEY(numtab)
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

CREATE TABLE menu (
    numrestau INT(4),
    numplat INT(4),
    PRIMARY KEY (numrestau, numplat)
);

ALTER TABLE reservation
    ADD CONSTRAINT fk_reservation_restaurant
        FOREIGN KEY (numrestau)
            REFERENCES restaurant(numrestau),
    ADD CONSTRAINT fk_reservation_table
        FOREIGN KEY (numtab)
            REFERENCES tabl(numtab);

ALTER TABLE commande
    ADD CONSTRAINT fk_commande_reservation
        FOREIGN KEY (numres)
            REFERENCES reservation(numres),
    ADD CONSTRAINT fk_commande_plat
        FOREIGN KEY (numplat)
            REFERENCES plat(numplat);

ALTER TABLE menu
    ADD CONSTRAINT fk_menu_restaurant
        FOREIGN KEY (numrestau)
            REFERENCES restaurant(numrestau),
    ADD CONSTRAINT fk_menu_plat
        FOREIGN KEY (numplat)
            REFERENCES plat(numplat);



-- Insère quelques restaurants
INSERT INTO restaurant (nom, latitude, longitude) VALUES
    ('Chez Marcel', 48.6908, 6.1772),
    ('Le Bistrot Lorrain', 48.6987, 6.1813),
    ('La Table Provençale', 48.6930, 6.1825),
    ('Sushi Fusion', 48.6905, 6.1860);

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


ALTER TABLE tabl AUTO_INCREMENT = 10;
INSERT INTO tabl (nbplace, numrestau)
VALUES
    (4, 1),
    (6, 1),
    (8, 2),
    (4, 2),
    (6, 3),
    (4, 3),
    (4, 3),
    (6, 4),
    (2, 4),
    (4, 4);


-- Insère des données de test pour la table menu
INSERT INTO menu (numrestau, numplat)
VALUES
    -- Menu pour 'Le Petit Parisien' (Paris)
    (1, 1),  -- assiette de crudités
    (1, 4),  -- filet de boeuf
    (1, 13), -- crème caramel
    (1, 11), -- brochet à l'oseille

    -- Menu pour 'The London Pub' (Londres)
    (2, 7),  -- pate lorrain
    (2, 9),  -- entrecote printaniere
    (2, 16), -- fois gras de lorraine
    (2, 3),  -- sorbet mirabelle

    -- Menu pour 'La Bella Italia' (Rome)
    (3, 6),  -- chevre chaud
    (3, 10), -- gratin dauphinois
    (3, 14), -- munster au cumin
    (3, 5),  -- salade verte

    -- Menu pour 'Tokyo Sushi House' (Tokyo)
    (4, 8),  -- saumon fumé
    (4, 15), -- filet de sole au beurre
    (4, 12), -- gigot d'agneau
    (4, 2);  -- tarte de saison

-- Insère quelques réservations
ALTER TABLE reservation AUTO_INCREMENT = 100;
INSERT INTO reservation (nom, prenom, nbpers, telephone, date, numrestau, numtab) VALUES
    ('Doe', 'John', 2, '1234567890', '2024-06-15', 1, 11), -- Réservation au Le Petit Parisien
    ('Smith', 'Jane', 4, '9876543210', '2024-06-16', 2, 13), -- Réservation au The London Pub
    ('Garcia', 'Maria', 3, '4561237890', '2024-06-17', 3, 15), -- Réservation à La Bella Italia
    ('Sato', 'Takashi', 2, '7894561230', '2024-06-18', 4, 18);

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
    (103, 3, 1);