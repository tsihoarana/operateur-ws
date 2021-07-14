# TODO
## Backend
* base de donne [Michael]
	- preparation terrain `[ok]`
	- preparation deploiement `[ok]`
	- test postgres `[ok]`
	- mise en place de afQuery `[ok]`
	- concevoir BDD `[ok]`
	- Resolution CORS (test avec jquery) `[ok]`
	- test mongo `[ok]`

* Client [Michael]
	- connexion `/client/connexion` `[ok]`
	- get all users `client` `[ok]`
	- inscription `client/inscription` `[ok]`
	- status client `client/status/{idclient}` `[ok]`
		- nom, prenom, numero, mvola, credit

* Mvola [Michael]
	- Total mvola `[ok]`
		- `GET /mvola/total/{numero}` (pour un seul Client)
		- `GET /mvola/total` (pour tous)
	- Les transac mvola pour un client `[ok]`
		- `GET /mvola/{all, valide ou invalide}/{numero}`
	- client insert mvola `[ok]`
		- `POST /mvola/action/depot avec body {idclient: ?, montant : ?} + header TOKEN` `[ok]`
	- client deduire mvola  `[ok]`
		- `POST /mvola/action/retrait avec body {idclient: ?, montant : ?} + header TOKEN`
	- validation mvola `PUT /mvola/valider/{idmvola}` `[ok]`

* Credit [Michael]
	- Total credit `[ok]`
		- `GET /credit/total/{numero}` (pour un seul Client)
		- `GET /credit/total` (pour tous)
	- Les transac credit pour un client `[ok]`
		- `GET /credit/all/{numero}`
	- client insert credit `[ok]`
		- `POST /credit/action/depot avec body {idclient: ?, montant : ?} + header TOKEN`
	- client deduire credit `[ok]`
		- `POST /credit/action/retrait avec body {idclient: ?, montant : ?} + header TOKEN`

* Offre [Tsihoarana]
	- L'ajout d'offre doit etre protege par un token maitre different du token Client (a ignorer pour le moment)
	- ajouter offre `POST /offre avec body de struct Offre.java sans id (concevoir OffreJSON.java implements InputValidateInterf)`
	- Supprimer offre `DELETE /offre/{idoffre}`
	- Acheter offre pour client transactionnelle (deduire credit (Utiliser `CreditService.operer(...)`), puis ajouter offre) `[ok]`
		- `POST /offre/client/achat avec body meme struct OffreClient.java mais sans id + TOKEN Client`
	- select offre actif pour un client (avec une date sinon prendre current date) `[ok]`
		- `GET /offre/client/{numero}?date=.... + TOKEN Client`
		- `GET /offre/client/{numero} + TOKEN Client`
	- deduire valeur offre actif apres un appel par exemple
		- `PUT /offre/client/deduire avec body {idoffreclient : ?, valeur_a_deduire : ?} + TOKEN Client`

* FIX !!!!!!!!
 - achat offre avadika mvola no mihena

* Appel : MONGODB [Tsihoarana]
	- Utiliser les test mongo pour reference (ajout ligne, recherche)
	- liste appel avec filtre `GET /appel/{numero} + TOKEN`
	- appel simuler lasa mihena ny credit raha tsy misy offre actif
	 	- `/appel/simuler` => miandry objet de type appel (voir mongodb md amle structure)
		- retirer credit en fonction de la duree
		- puis...
		- ajouter appel vers mongodb
	- statistique appel `GET /appel/stats/{numero}`
		- total minute appel pour le numero
			- L'ideal serait de convertir les secondes total en "HH:MM:SS"
		- duree appel la plus longue pour le numero

## Angular
* Creation projet Bac a sable

* Mise en place CSS / design
	- header
	- voir les codes couleurs facon google
	- boutons
	- grid flex et machin chose

* Fabrication des components
	- creation component tableau
	- creation component recherche

## FrontOffice Angular
- login
- inscription
- utiliser localStorage.setItem pour les sessions
- Voir liste appel (utiliser component tableau)

## BackOffice angular
- Ajouter offre
- Supprimer offre
- Valider mvola
- creation component Statistiqu
	- Component Courbe
	- Component mvola/credit stat
		- mvola entrant du jour
		- credit achete du jour
	- offre achete du jour

## Mobile
* METTRE LE DESIGN UNIFORME
- Voir TODO.md dans le projet mobile

## CREATION Mini doc
