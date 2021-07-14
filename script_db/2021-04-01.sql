DROP VIEW v_OffreActifs;

CREATE OR REPLACE VIEW v_OffreActifs AS (
	SELECT Offre.nom, Offre.priorite, Client.numero, OffreClient.* FROM 
		Offre 
		JOIN OffreClient ON Offre.idoffre = OffreClient.idoffre
		JOIN Client on Client.idclient = OffreClient.idclient
	WHERE date_expiration >= NOW()
	ORDER BY date_expiration ASC
);

-- juste pour uniformiser les choses
-- comme l'idee du prix_unit est rejetee
UPDATE DetailOffre SET prix_unitaire = 0;