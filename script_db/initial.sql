\c postgres
DROP DATABASE operateur;
CREATE DATABASE operateur;
\c operateur;


CREATE EXTENSION pgcrypto;
CREATE OR REPLACE FUNCTION SHA1(bytea) returns text AS $$
      SELECT encode(digest($1, 'sha1'), 'hex')
$$ LANGUAGE SQL STRICT IMMUTABLE;

CREATE SEQUENCE ClientSeq START WITH 1;
CREATE SEQUENCE MvolaSeq START WITH 1;
CREATE SEQUENCE CreditSeq START WITH 1;

CREATE SEQUENCE TypeOffreSeq START WITH 1;
CREATE SEQUENCE OffreSeq START WITH 1;
CREATE SEQUENCE DetailOffreSeq START WITH 1;
CREATE SEQUENCE OffreClientSeq START WITH 1;
CREATE SEQUENCE DetailOffreClientSeq START WITH 1;

CREATE SEQUENCE TokenSeq START WITH 1;

CREATE TABLE Client (
	idclient VARCHAR (10) PRIMARY KEY NOT NULL DEFAULT CONCAT('C', NEXTVAL('ClientSeq')),
	numero VARCHAR (10) UNIQUE, 
	nom VARCHAR (50) NOT NULL,
	prenom VARCHAR (50),
	password VARCHAR (100) NOT NULL,
	date_naissance TIMESTAMP NOT NULL,
	code_type INT DEFAULT 0 -- 1 admin, 0 client
);

CREATE TABLE Mvola (
	idmvola VARCHAR (10) PRIMARY KEY NOT NULL DEFAULT CONCAT('M', NEXTVAL('MvolaSeq')),
	idclient VARCHAR (10) NOT NULL,
	montant NUMERIC (20, 3) NOT NULL,
	sens INT NOT NULL, -- -1 ou 1
	etat INT DEFAULT 0, -- 1 valide, 0 non valide
	date_transac TIMESTAMP NOT NULL,
	FOREIGN KEY (idclient) REFERENCES Client (idclient)
);

CREATE TABLE Credit (
	idcredit VARCHAR (10) PRIMARY KEY NOT NULL DEFAULT CONCAT('CD', NEXTVAL('CreditSeq')),
	idclient VARCHAR (10) NOT NULL,
	montant NUMERIC (20, 3) NOT NULL,
	sens INT NOT NULL, -- -1 ou 1
	date_transac TIMESTAMP NOT NULL,
	FOREIGN KEY (idclient) REFERENCES Client (idclient)
);

-- Categorie offre
-- MORA, Internet Full, SMS Full, ...  
CREATE TABLE TypeOffre (
	idtypeoffre VARCHAR (10) PRIMARY KEY NOT NULL DEFAULT CONCAT ('TYPEOF', NEXTVAL('TypeOffreSeq')), 
	nom VARCHAR (20)  NOT NULL
);

-- Les Offres regis par leurs prix et duree
CREATE TABLE Offre (
	idoffre VARCHAR (10) PRIMARY KEY NOT NULL DEFAULT CONCAT ('OF', NEXTVAL('OffreSeq')),
	idtypeoffre VARCHAR (10) NOT NULL, -- A quel groupe cet offre appartient t'il?
	nom VARCHAR (30) NOT NULL, -- MORA 500, Internet One Day, ...
	duree NUMERIC (10, 3) NOT NULL, -- en heure
	prix NUMERIC (20, 3) NOT NULL, -- prix soutiree du Mvola (envisager Credit pour plus tard)
	priorite INT  NOT NULL, -- Exemple MORA before Internet
	FOREIGN KEY (idtypeoffre) REFERENCES TypeOffre (idtypeoffre)
);

-- Une offre peut avoir plusieurs details (sms? + appel? + internet?)
CREATE TABLE DetailOffre (
	iddetailoffre VARCHAR (10) PRIMARY KEY NOT NULL DEFAULT CONCAT ('DETOFFR', NEXTVAL('DetailOffreSeq')),
	idoffre VARCHAR (10) NOT NULL, -- Offre parent
	type_detail VARCHAR (20) NOT NULL, -- INTERNET, SMS, APPEL, FB ...
	valeur NUMERIC (20, 3) NOT NULL, -- valeur ar appel ? megas ? SMS count ?
	FOREIGN KEY (idoffre) REFERENCES Offre (idoffre)
);

-- Permet de savoir l'offre courante du client avec sa validite
CREATE TABLE OffreClient (
	idoffreclient VARCHAR (10) PRIMARY KEY NOT NULL DEFAULT CONCAT ('OFFRCLI', NEXTVAL('OffreClientSeq')),
	idoffre VARCHAR (10)  NOT NULL, --  
	idclient VARCHAR (10)  NOT NULL,
	date_achat TIMESTAMP  NOT NULL,
	date_expiration TIMESTAMP  NOT NULL,
	FOREIGN KEY (idoffre) REFERENCES Offre (idoffre),
	FOREIGN KEY (idclient) REFERENCES Client (idclient)
);
-- Permet de determiner les valeurs restants de chaque offre actif du client
CREATE TABLE DetailOffreClient (
	iddetailoffreclient VARCHAR (10) PRIMARY KEY NOT NULL DEFAULT CONCAT ('DETOFFCL', NEXTVAL('DetailOffreClientSeq')),
	idoffreclient VARCHAR (10) NOT NULL, -- MORA 500, MORA 300, Net One Day ?, Net One Week ? ...
	iddetailoffre VARCHAR (10) NOT NULL, -- SMS ? INTERNET ? APPEL ?
	valeur_actuel NUMERIC (20, 3) NOT NULL, -- valeur ar appel ? megas ? SMS count ?
	FOREIGN KEY (idoffreclient) REFERENCES OffreClient (idoffreclient),
	FOREIGN KEY (iddetailoffre) REFERENCES DetailOffre (iddetailoffre)
);


CREATE TABLE Token (
	idtoken VARCHAR (10) PRIMARY KEY NOT NULL DEFAULT CONCAT('TK', NEXTVAL('TokenSeq')),
	idclient VARCHAR(10) NOT NULL, -- proprio
	code_type INT NOT NULL, -- 1 ou 0 (token admin or not)
	token_value VARCHAR(120) NOT NULL,
	expiration TIMESTAMP  NOT NULL
);


-- VIEWS
CREATE OR REPLACE VIEW TokenValidView AS (
	SELECT * FROM Token
	WHERE expiration >= NOW()
);

-- MVOLA
CREATE OR REPLACE VIEW MvolaWithNumero AS (
	SELECT numero, Mvola.* FROM Mvola JOIN Client ON Mvola.idclient = Client.idclient
	ORDER BY date_transac DESC
);


CREATE OR REPLACE VIEW MvolaValide AS 
	(SELECT * FROM Mvola WHERE etat > 0);

CREATE OR REPLACE VIEW MvolaValideInitView AS (
	SELECT 
		Client.numero,
		Client.idclient,
		COALESCE(sens, CAST(1 as INT)) sens, 
		COALESCE(montant, CAST(0.0 as NUMERIC(20, 3))) montant
	FROM 
		Client LEFT JOIN MvolaValide ON Client.idclient = MvolaValide.idclient
);

CREATE OR REPLACE VIEW MvolaTotal AS (
	SELECT 
		numero, idclient, SUM (sens * montant) AS total
	FROM MvolaValideInitView
	GROUP BY numero, idclient
);

-- CREDIT
CREATE OR REPLACE VIEW CreditWithNumero AS (
	SELECT numero, Credit.* FROM Credit JOIN Client ON Credit.idclient = Client.idclient
);

CREATE OR REPLACE VIEW CrediteInitView AS (
	SELECT 
		Client.numero,
		Client.idclient,
		COALESCE(sens, CAST(1 as INT)) sens, 
		COALESCE(montant, CAST(0.0 as NUMERIC(20, 3))) montant
	FROM 
		Client LEFT JOIN Credit ON Client.idclient = Credit.idclient
);

CREATE OR REPLACE VIEW CreditTotal AS (
	SELECT 
		numero, idclient, SUM (sens * montant) AS total
	FROM CrediteInitView
	GROUP BY numero, idclient
);

CREATE OR REPLACE VIEW AgeClientsView AS (
	SELECT 
		(DATE_PART('year', now()) - DATE_PART('year', date_naissance)) AS age
	FROM Client
);

CREATE OR REPLACE VIEW AverageAgeClientsView AS (
	SELECT COALESCE (SUM(age) / COUNT(*), 0) age FROM AgeClientsView 
);

-- affichage mvola par date (en enlevant la partie temps)
CREATE OR REPLACE VIEW MvolaUsingDatePartView AS (
	SELECT (sens * montant) montant, DATE(date_transac) FROM Mvola
	WHERE etat = 1
	ORDER BY date
);
CREATE OR REPLACE VIEW MvolaSumGroupByDayView AS (
	SELECT 
		SUM (montant) montant, 
		COUNT(*) nb_transac, 
		date 
	FROM MvolaUsingDatePartView GROUP BY DATE 
);

CREATE OR REPLACE VIEW CreditUsingDatePartView AS (
	SELECT (sens * montant) montant, DATE(date_transac) FROM Credit
	ORDER BY date
);
CREATE OR REPLACE VIEW CreditSumGroupByDayView AS (
	SELECT 
		SUM (montant) montant, 
		COUNT(*) nb_transac, 
		date 
	FROM CreditUsingDatePartView GROUP BY DATE 
);

-- test datas
-- Offres
INSERT INTO TypeOffre VALUES (DEFAULT, 'MORA 24h'); -- TYPEOF1 (la date courante jusque 23h59)
INSERT INTO TypeOffre VALUES (DEFAULT, 'Special_mobile'); -- TYPEOF2 (duree configurable)
INSERT INTO TypeOffre VALUES (DEFAULT, 'Internet'); -- TYPEOF3 (hebdomadaire et mensuel)
INSERT INTO TypeOffre VALUES (DEFAULT, 'First'); -- TYPEOF4 (duree 30j) comme MORA mais en 30j


INSERT INTO Client VALUES (DEFAULT, '0340000001', 'Jean', 'Rakoto', SHA1('1234'), '1980-04-03 00:00:00', 1);
INSERT INTO Client VALUES (DEFAULT, '0340000002', 'Rabe', 'Zafy', SHA1('1234'), '1999-03-01 00:00:00', 0);
INSERT INTO Client VALUES (DEFAULT, '0340000003', 'Louis', 'Matter', SHA1('1234'), '1980-01-01 00:00:00', 0);
INSERT INTO Client VALUES (DEFAULT, '0340000004', 'Loyd', 'Carlos', SHA1('1234'), '1999-01-01 00:00:00', 1);
INSERT INTO Client VALUES (DEFAULT, '0340000005', 'Carl', 'Tom', SHA1('1234'), '2000-01-01 00:00:00', 0);
INSERT INTO Client VALUES (DEFAULT, '0340000006', 'Bob', 'Tommy', SHA1('1234'), '2010-01-01 00:00:00', 0);
INSERT INTO Client VALUES (DEFAULT, '0340000007', 'Alice', 'Walter', SHA1('1234'), '1989-01-01 00:00:00', 0);
INSERT INTO Client VALUES (DEFAULT, '0340000008', 'Alex', 'Walker', SHA1('1234'), '1999-01-01 00:00:00', 0);
INSERT INTO Client VALUES (DEFAULT, '0340000009', 'Guts', 'Griffith', SHA1('1234'), '2000-01-01 00:00:00', 0);
INSERT INTO Client VALUES (DEFAULT, '0340000010', 'George', 'Son', SHA1('1234'), '2000-01-01 00:00:00', 1);
INSERT INTO Client VALUES (DEFAULT, '0340000011', 'Zuuk', 'Be', SHA1('1234'), '2000-05-01 00:00:00', 0);
INSERT INTO Client VALUES (DEFAULT, '0340000012', 'Marc', 'Cod', SHA1('1234'), '2000-06-03 00:00:00', 0);
INSERT INTO Client VALUES (DEFAULT, '0340000013', 'Ane', 'Walter', SHA1('1234'), '2000-03-01 00:00:00', 1);
INSERT INTO Client VALUES (DEFAULT, '0340000014', 'Coyle', 'Prim', SHA1('1234'), '2000-04-01 00:00:00', 0);
INSERT INTO Client VALUES (DEFAULT, '0340000015', 'Stark', 'Coyote', SHA1('1234'), '2000-03-04 00:00:00', 1);

-- A valider
INSERT INTO Mvola VALUES (DEFAULT, 'C1', 25300, -1, 0, '2021-03-01 01:00:00');
INSERT INTO Mvola VALUES (DEFAULT, 'C1',102000, 1, 0, '2021-03-01 15:50:00');
INSERT INTO Mvola VALUES (DEFAULT, 'C2', 21000, -1, 0, '2021-03-02 00:00:00');

-- Lignes mvola par date pour les stats

INSERT INTO Mvola VALUES (DEFAULT, 'C1', 10000, 1, 1, '2021-03-01 01:00:00');
INSERT INTO Mvola VALUES (DEFAULT, 'C1',  2000,-1, 1, '2021-03-01 15:50:00');

INSERT INTO Mvola VALUES (DEFAULT, 'C1', 32000, 1, 1, '2021-03-02 00:00:00');
INSERT INTO Mvola VALUES (DEFAULT, 'C1', 43000, 1, 1, '2021-03-02 17:00:00');
INSERT INTO Mvola VALUES (DEFAULT, 'C1', 44000, 1, 1, '2021-03-02 08:30:00');

INSERT INTO Mvola VALUES (DEFAULT, 'C1', 10000, 1, 1, '2021-03-01 01:00:00');
INSERT INTO Mvola VALUES (DEFAULT, 'C1', 32000,-1, 1, '2021-03-01 15:50:00');

INSERT INTO Mvola VALUES (DEFAULT, 'C1', 12000, 1, 1, '2021-03-02 00:00:00');
INSERT INTO Mvola VALUES (DEFAULT, 'C1', 33000, 1, 1, '2021-03-02 17:00:00');
INSERT INTO Mvola VALUES (DEFAULT, 'C1', 44000, 1, 1, '2021-03-02 08:30:00');

INSERT INTO Mvola VALUES (DEFAULT, 'C1', 35000,-1, 1, '2021-03-03 10:00:00');
INSERT INTO Mvola VALUES (DEFAULT, 'C1', 12000, 1, 1, '2021-03-03 11:00:00');
INSERT INTO Mvola VALUES (DEFAULT, 'C1',  7200, 1, 1, '2021-03-03 14:00:00');

INSERT INTO Mvola VALUES (DEFAULT, 'C1',   110, 1, 1, '2021-03-04 15:00:00');

INSERT INTO Mvola VALUES (DEFAULT, 'C1', 13200,-1, 1, '2021-03-06 18:00:00');
INSERT INTO Mvola VALUES (DEFAULT, 'C1',  6000, 1, 1, '2021-03-06 00:00:00');
INSERT INTO Mvola VALUES (DEFAULT, 'C2',  6000, 1, 1, '2021-03-06 19:00:00');
INSERT INTO Mvola VALUES (DEFAULT, 'C1',  3000, 1, 1, '2021-03-06 03:00:00');
INSERT INTO Mvola VALUES (DEFAULT, 'C2',  5000, 1, 1, '2021-03-06 04:00:00');
INSERT INTO Mvola VALUES (DEFAULT, 'C1',   100, 1, 1, '2021-03-06 11:00:00');
INSERT INTO Mvola VALUES (DEFAULT, 'C2',  1350, 1, 1, '2021-03-06 11:08:00');
INSERT INTO Mvola VALUES (DEFAULT, 'C2', 10600, 1, 1, '2021-03-09 11:08:00');
INSERT INTO Mvola VALUES (DEFAULT, 'C2', 30300, 1, 1, '2021-03-10 11:08:00');
INSERT INTO Mvola VALUES (DEFAULT, 'C2', 60350, 1, 1, '2021-03-10 11:08:00');

-- Credit
INSERT INTO Credit VALUES (DEFAULT, 'C1', 100, 1, '2021-03-01 19:04:00');
INSERT INTO Credit VALUES (DEFAULT, 'C1', 200, 1, '2021-03-02 12:03:00');
INSERT INTO Credit VALUES (DEFAULT, 'C2', 300, 1, '2021-03-03 12:03:00');
INSERT INTO Credit VALUES (DEFAULT, 'C1', -100, 1, '2021-03-04 12:03:00');
INSERT INTO Credit VALUES (DEFAULT, 'C2', 200, 1, '2021-03-01 12:03:00');
INSERT INTO Credit VALUES (DEFAULT, 'C1', 100, 1, '2021-03-05 13:03:00');
INSERT INTO Credit VALUES (DEFAULT, 'C1', -100, 1, '2021-03-06 15:03:00');
INSERT INTO Credit VALUES (DEFAULT, 'C2', 100, 1, '2021-03-07 12:03:00');
INSERT INTO Credit VALUES (DEFAULT, 'C1', 50, 1, '2021-03-07 10:04:00');
INSERT INTO Credit VALUES (DEFAULT, 'C2', 70, 1, '2021-03-10 12:18:00');
INSERT INTO Credit VALUES (DEFAULT, 'C2', 70, 1, '2021-03-10 12:18:00');

-- VIEW v_OffreActifs par rapport a une date (avec nom, duree, debut et expiration)
CREATE OR REPLACE VIEW v_OffreActifs AS (
	SELECT Offre.nom, Offre.priorite, Client.numero, OffreClient.* FROM 
		Offre 
		JOIN OffreClient ON Offre.idoffre = OffreClient.idoffre
		JOIN Client on Client.idclient = OffreClient.idclient
	WHERE date_expiration >= NOW()
	ORDER BY date_expiration ASC
);

CREATE OR REPLACE VIEW v_OffreEnDetail AS (
	SELECT 
		Offre.idoffre, idtypeoffre, nom, duree, prix, priorite,
		iddetailoffre, type_detail, valeur
	FROM
		Offre JOIN DetailOffre 
	ON Offre.idoffre = DetailOffre.idoffre
);

CREATE OR REPLACE VIEW v_DetailOffreClientPriority AS (
	SELECT
		DetailOffreClient.*,
		v_OffreEnDetail.priorite
	FROM
		v_OffreEnDetail 
	JOIN 
		DetailOffreClient ON v_OffreEnDetail.iddetailoffre = DetailOffreClient.iddetailoffre
	ORDER BY priorite DESC
);

CREATE OR REPLACE VIEW v_DetailOffreClientComplet AS (
	SELECT
		DetailOffreClient.*, DetailOffre.type_detail, Offre.nom
	FROM
		DetailOffreClient
	JOIN
		DetailOffre ON DetailOffre.iddetailoffre = DetailOffreClient.iddetailoffre
	JOIN
		Offre ON Offre.idoffre = DetailOffre.idoffre
);