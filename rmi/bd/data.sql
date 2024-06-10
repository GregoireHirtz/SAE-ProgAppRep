DROP TABLE IF EXISTS commande;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS tabl;
DROP TABLE IF EXISTS affecter;
DROP TABLE IF EXISTS plat;
DROP TABLE IF EXISTS serveur;


-- Table tabl
CREATE TABLE tabl (
    numtab INT(4) AUTO_INCREMENT,
    nbplace INT(2),
    PRIMARY KEY(numtab)
);

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

-- Table plat
CREATE TABLE plat (
    numplat INT(4) AUTO_INCREMENT,
    libelle VARCHAR(40),
    type VARCHAR(15),
    prixunit DECIMAL(6,2),
    qteservie INT(2),
    PRIMARY KEY (numplat)
);

-- Tuples de Plat
INSERT INTO plat (libelle, type, prixunit, qteservie)
VALUES
    ('assiette de crudités', 'Entrée', 90, 25),
    ('tarte de saison', 'Dessert', 90, 25),
    ('sorbet mirabelle', 'Dessert', 90, 35),
    ('filet de boeuf', 'Viande', 90, 62),
    ('salade verte', 'Entrée', 90, 15),
    ('chevre chaud', 'Entrée', 90, 21),
    ('pate lorrain', 'Entrée', 90, 25),
    ('saumon fumé', 'Entrée', 90, 30),
    ('entrecote printaniere', 'Viande', 90, 58),
    ('gratin dauphinois', 'Plat', 90, 42),
    ('brochet à l\'oseille', 'Poisson', 90, 68),
    ('gigot d\'agneau', 'Viande', 90, 56),
    ('crème caramel', 'Dessert', 90, 15),
    ('munster au cumin', 'Fromage', 90, 18),
    ('filet de sole au beurre', 'Poisson', 90, 70),
    ('fois gras de lorraine', 'Entrée', 90, 61);

-- Table serveur
CREATE TABLE serveur (
    numserv INT(2) AUTO_INCREMENT,
    email VARCHAR(255),
    passwd VARCHAR(255),
    nomserv VARCHAR(25),
    grade VARCHAR(20),
    PRIMARY KEY(numserv)
);

-- Tuples de Serveur
INSERT INTO serveur (email, passwd, nomserv, grade)
VALUES
    ('user1@mail.com', 'password', 'Tutus Peter', 'gestionnaire'),
    ('user2@mail.com', 'password', 'Lilo Vito', 'serveur'),
    ('user3@mail.com', 'password', 'Don Carl', 'serveur'),
    ('user4@mail.com', 'password', 'Leo Jon', 'serveur'),
    ('user5@mail.com', 'password', 'Dean Geak', 'gestionnaire');

-- Table reservation
CREATE TABLE reservation (
    numres INT(4) AUTO_INCREMENT,
    numtab INT(4),
    datres DATETIME,
    nbpers INT(2),
    datpaie DATETIME,
    modpaie VARCHAR(15),
    montcom DECIMAL(8,2),
    PRIMARY KEY (numres)
);

-- Tuples de reservation
ALTER TABLE reservation AUTO_INCREMENT = 100;
INSERT INTO reservation (numtab, datres, nbpers, datpaie, modpaie)
VALUES
    (10 , '2021-09-10 19:00', 2, '2021-09-10 20:50', 'Carte'),
    (11, '2021-09-10 20:00', 4, '2021-09-10 21:20', 'Chèque'),
    (17, '2021-09-10 18:00', 2, '2021-09-10 20:55', 'Carte'),
    (12, '2021-09-10 19:00', 2, '2021-09-10 21:10', 'Espèces'),
    (18, '2021-09-10 19:00', 1, '2021-09-10 21:00', 'Chèque'),
    (10, '2021-09-10 19:00', 2, '2021-09-10 20:45', 'Carte'),
    (14, '2021-10-11 19:00', 2, '2021-10-11 22:45', 'Carte'),
    (16, '2025-10-11 19:00', 3, null, null);

-- Table commande
CREATE TABLE commande (
    numres INT(4),
    numplat INT(4),
    quantite INT(2),
    PRIMARY KEY(numres, numplat)
);

-- Tuples de commande
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
    (103, 3, 1),
    (104, 7, 1),
    (104, 11, 1),
    (104, 14, 1),
    (104, 3, 1),
    (105, 3, 2),
    (106, 3, 2),
    (107, 3, 2);


-- Table affecter
CREATE TABLE affecter (
    numtab INT(4),
    dataff DATE,
    numserv INT(2),
    PRIMARY KEY(numtab, dataff)
);

-- Tuples de Affecter
INSERT INTO affecter (numtab, dataff, numserv)
VALUES
    (10, '2021-09-10', 1),
    (11, '2021-09-10', 1),
    (12, '2021-09-10', 1),
    (17, '2021-09-10', 2),
    (18, '2021-09-10', 2),
    (15, '2021-09-10', 3),
    (16, '2021-09-10', 3),
    (10, '2021-09-11', 1);





-- Ajout de la clé étrangère pour la table reservation
ALTER TABLE reservation
    ADD CONSTRAINT fk_reservation_tabl
        FOREIGN KEY (numtab)
            REFERENCES tabl(numtab);

-- Ajout de la clé étrangère pour la table affecter
ALTER TABLE affecter
    ADD CONSTRAINT fk_affecter_serveur
        FOREIGN KEY (numserv)
            REFERENCES serveur(numserv);

-- Ajout de la clé étrangère pour la table commande (numres)
ALTER TABLE commande
    ADD CONSTRAINT fk_commande_reservation
        FOREIGN KEY (numres)
            REFERENCES reservation(numres);

-- Ajout de la clé étrangère pour la table commande (numplat)
ALTER TABLE commande
    ADD CONSTRAINT fk_commande_plat
        FOREIGN KEY (numplat)
            REFERENCES plat(numplat);