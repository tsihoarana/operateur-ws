DROP VIEW v_OffreActifs;

CREATE OR REPLACE VIEW v_OffreActifs AS (
	SELECT Offre.nom, Offre.priorite, Client.numero, OffreClient.*, DetailOffre.prix_unitaire
	FROM 
		Offre 
		JOIN OffreClient ON Offre.idoffre = OffreClient.idoffre
		JOIN Client ON Client.idclient = OffreClient.idclient
		JOIN DetailOffre ON DetailOffre.idoffre = Offre.idoffre
	WHERE date_expiration >= NOW()
	ORDER BY date_expiration ASC
);




-- Cette table n'est rien d'autre qu'une abstraction d'un Map<String, List<String>>
-- On aurait pu mettre ceci dans CustomConfig.java mais il serait plus "safe" de le faire direct dans SQL
CREATE SEQUENCE CustomMapListDataSeq START WITH 1;
CREATE TABLE CustomMapListData (
    idcustommaplistdata VARCHAR(20) PRIMARY KEY NOT NULL DEFAULT CONCAT('CMapListData', NEXTVAL('CustomMapListDataSeq')),
    key VARCHAR (50), -- une cle peut avoir plusieurs values (=> il peut y avoir n-lignes du meme cle)
    value VARCHAR (50)
);

-- MORA  = INTERNET + SMS + APPEL
INSERT INTO CustomMapListData VALUES (DEFAULT, 'MORA', 'INTERNET');
INSERT INTO CustomMapListData VALUES (DEFAULT, 'MORA', 'SMS');
INSERT INTO CustomMapListData VALUES (DEFAULT, 'MORA', 'APPEL_INTERNE');
INSERT INTO CustomMapListData VALUES (DEFAULT, 'MORA', 'APPEL_EXTERNE');

-- INTERENT = INTERNET
INSERT INTO CustomMapListData VALUES (DEFAULT, 'INTERNET', 'INTERNET');

-- FIRST = INTERNET + SMS + APPEL
INSERT INTO CustomMapListData VALUES (DEFAULT, 'FIRST', 'INTERNET');
INSERT INTO CustomMapListData VALUES (DEFAULT, 'FIRST', 'SMS');
INSERT INTO CustomMapListData VALUES (DEFAULT, 'FIRST', 'APPEL_INTERNE');
INSERT INTO CustomMapListData VALUES (DEFAULT, 'FIRST', 'APPEL_EXTERNE');