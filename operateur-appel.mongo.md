# Mongodb

```
use operateur;
db.createCollection("appel");
db.appel.insertMany([
    {"numeroAppelant" : "0340000001", "numeroCible" : "0340000002", "duree" : 3.6, "date" : "2020-01-01"},
    {"numeroAppelant" : "0340000001", "numeroCible" : "0340000002", "duree" : 1.6, "date" : "2020-01-01"},
    {"numeroAppelant" : "0340000001", "numeroCible" : "0340000002", "duree" : 1.5, "date" : "2020-01-01"},
    {"numeroAppelant" : "0340000001", "numeroCible" : "0340000002", "duree" : 10.8, "date" : "2020-01-01"},
    {"numeroAppelant" : "0340000001", "numeroCible" : "0340000003", "duree" : 120.6, "date" : "2020-01-01"},
    {"numeroAppelant" : "0340000001", "numeroCible" : "0340000003", "duree" : 3.6, "date" : "2020-01-01"},
    {"numeroAppelant" : "0340000001", "numeroCible" : "0340000004", "duree" : 4.8, "date" : "2020-01-01"},
    {"numeroAppelant" : "0340000001", "numeroCible" : "0340000004", "duree" : 4.8, "date" : "2020-01-01"},

    {"numeroAppelant" : "0340000002", "numeroCible" : "0340000001", "duree" : 3, "date" : "2020-01-01"},
    {"numeroAppelant" : "0340000002", "numeroCible" : "0340000001", "duree" : 4, "date" : "2020-01-01"},
    {"numeroAppelant" : "0340000002", "numeroCible" : "0340000001", "duree" : 4, "date" : "2020-01-01"},
    {"numeroAppelant" : "0340000002", "numeroCible" : "0340000001", "duree" : 3, "date" : "2020-01-01"}
]);

db.createCollection("test_collection");
db.test_collection.insertMany([
    {"nom" : "Rabe", "age" : 18},
    {"nom" : "Ranaivo", "age" : 40}
]);
```

# Requetes utiles
```javascript
use operateur; // cree une BDD
db.createCollection(<name>); // cree une collection
db.appel.find({numero : });
db.appel.remove({...});
db.appel.insert({...});
db.appel.insertMany([{...}, ... ]);
```

# Connection avec Atlas en CLI
```
mongo "mongodb+srv://operateur-cluster.xuzpo.mongodb.net/myFirstDatabase" --username operateur
password : 1234
```

# Connection avec Atlas en java
Le nom de la bdd etant "operateur"
```
mongodb+srv://operateur:1234@operateur-cluster.xuzpo.mongodb.net/operateur?retryWrites=true&w=majority
```