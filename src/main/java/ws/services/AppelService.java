package ws.services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.mongodb.DBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;

import org.bson.Document;
import org.bson.conversions.Bson;

import ws.model.Appel;
import ws.model.Client;
import ws.model.CustomConfig;
import ws.model.OffreClient;
import ws.model.OffreClientDetails;
import ws.model.StatAppel;
import ws.model.StatusClient;
import ws.template_json.AppelJSON;
import ws.template_json.CreditJSON;
import ws.tools.Tools;

public class AppelService {

    public static void insert (MongoClient client, Appel appel) throws Exception {
        MongoDatabase database = client.getDatabase("operateur");
        MongoCollection<Document> collection = database.getCollection("appel");

        Document document = new Document();
        document.append("numeroAppelant", appel.getNumeroAppelant());
        document.append("numeroCible", appel.getNumeroCible());
        document.append("duree", appel.getDuree());
        document.append("date", appel.getDate());
        
        collection.insertOne(document);
    }

    public static ArrayList<StatAppel> getDuree (MongoClient client, String numero) throws Exception {
        MongoDatabase database = client.getDatabase("operateur");
        // Aggregation pipeline stages
        String match = "{ $match: { numeroAppelant : { $eq: \""+numero+"\" } } }";
        String group = "{ $group: {    _id : { numeroCible:  \"$numeroCible\" },   total_duree: { $sum: \"$duree\" }  }}";

        // Build pipeline as a Bson
        String pipe = match + ", " + group;
        String strcCmd = "{ 'aggregate': 'appel', 'pipeline': [" + pipe + "], 'cursor': { } }";
        Document bsonCmd = Document.parse(strcCmd);

        // Execute the native query
        // runCommand​
        Document result = database.runCommand(bsonCmd);
        ArrayList<StatAppel> res = StatAppel.format(result);
        
        return res;
    }

    public static ArrayList<Document> getHistoryAppel (MongoClient client, String numero) throws Exception {
        MongoDatabase database = client.getDatabase("operateur");
        MongoCollection<Document> collection = database.getCollection("appel");
        
        MongoCursor<Document> cursor = collection.find(new Document().append("numeroAppelant", numero))
                                                    .sort(Sorts.descending("date"))
                                                    .iterator();
        try {
            ArrayList<Document> docs = new ArrayList<>();
            while (cursor.hasNext())
                docs.add(cursor.next());
            return docs;
        } finally {
            cursor.close();
        }
    }

}