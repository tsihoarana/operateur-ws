-- permet de gerer la facon dont on consomme les offres
-- si la valeur par defaut n'est pas utilisee
ALTER TABLE DetailOffre 
    ADD COLUMN prix_unitaire NUMERIC (20, 3) NOT NULL DEFAULT -1;
-- Note : derniere colonne = priorite
INSERT INTO Offre VALUES (DEFAULT, 'TYPEOF1', 'MORA 500', 24.0, 500, 10); -- 1j doit se terminer 23h59 du jour
INSERT INTO DetailOffre VALUES (DEFAULT, 'OF1', 'INTERNET', 20, -1); -- (-1 == utiliser pu valeur par defaut)
INSERT INTO DetailOffre VALUES (DEFAULT, 'OF1', 'APPEL_INTERNE', 5, 100); -- 5min appel pour 100Ar/min
INSERT INTO DetailOffre VALUES (DEFAULT, 'OF1', 'APPEL_EXTERNE', 5, 150); -- 5min appel pour 150Ar/min
INSERT INTO DetailOffre VALUES (DEFAULT, 'OF1', 'SMS', 100, -1); -- 100 SMS

INSERT INTO Offre VALUES (DEFAULT, 'TYPEOF2', 'Faceboobaka', 7 * 24.0, 1000, 50); -- 7j
INSERT INTO DetailOffre VALUES (DEFAULT, 'OF2', 'FACEBOOK', 1000, -1); -- 1 Go

INSERT INTO Offre VALUES (DEFAULT, 'TYPEOF3', 'NET ONE WEEK', 7*24.0, 10000, 10); -- 7j
INSERT INTO DetailOffre VALUES (DEFAULT, 'OF3', 'INTERNET', 1000, -1); -- 1 Go

INSERT INTO Offre VALUES (DEFAULT, 'TYPEOF4', 'FIRST', 30 * 24.0, 5000, 10); -- 30j
INSERT INTO DetailOffre VALUES (DEFAULT, 'OF4', 'INTERNET', 500, -1); -- 500 Mo
INSERT INTO DetailOffre VALUES (DEFAULT, 'OF4', 'APPEL_INTERNE', 50, -1); -- 50min appel
INSERT INTO DetailOffre VALUES (DEFAULT, 'OF4', 'APPEL_EXTERNE', 50, -1); -- 50min appel
INSERT INTO DetailOffre VALUES (DEFAULT, 'OF4', 'SMS', 500, -1); -- 500 SMS



CREATE TABLE CustomConfigData (
    key VARCHAR (30) PRIMARY KEY NOT NULL,
    value NUMERIC (20, 3) NOT NULL
);

-- prix d'une minute (Ar / Minute)
-- ces prix seront utilises pour les valeurs par defaut du formulaire d'insertion
INSERT INTO CustomConfigData VALUES ('PRIX_APPEL_INTERNE', 100);
INSERT INTO CustomConfigData VALUES ('PRIX_APPEL_EXTERNE', 150);

-- prix d'une minute d'appel (si sans offre)
INSERT INTO CustomConfigData VALUES ('PRIX_APPEL_INTERNE_CREDIT', 100);
INSERT INTO CustomConfigData VALUES ('PRIX_APPEL_EXTERNE_CREDIT', 150);

-- Prix d'un sms brute (si sans offre)
INSERT INTO CustomConfigData VALUES ('PRIX_SMS_CREDIT', 100);

-- Prix d'un mega internet brute (si sans offre)
INSERT INTO CustomConfigData VALUES ('PRIX_INTERNET_CREDIT', 100);


-- Ces donnees servent juste pour l'affichage
CREATE SEQUENCE SpecialMobileSeq START WITH 1;
CREATE TABLE SpecialMobile (
    idspecialmobile VARCHAR (30) PRIMARY KEY NOT NULL DEFAULT CONCAT ('SPEMOB', NEXTVAL('SpecialMobileSeq')),
    nom VARCHAR (20) -- FACEBOOK, WHATSAPP, ...
);

INSERT INTO SpecialMobile VALUES (DEFAULT, 'FACEBOOK');
INSERT INTO SpecialMobile VALUES (DEFAULT, 'WHATSAPP');
INSERT INTO SpecialMobile VALUES (DEFAULT, 'INSTAGRAM');