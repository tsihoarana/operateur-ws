package ws.services;

import java.util.ArrayList;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import ws.model.CustomConfig;

/**
 * Petit service pour recuperer une connexion BDD
 */
public class MongoService {
    static MongoClient client = null;
    /**
     * Ouvre une nouvelle connexion ou utilise la liaison existante
     */
    public static MongoClient getClient () {
        if (client == null)
            client = MongoClients.create(CustomConfig.mongo_url);
        return client;
    }

    /**
     * Fonction de test
     * @return
     * @throws Exception
     */
    public static ArrayList<Document> testData (MongoClient client) throws Exception {
        MongoDatabase database = client.getDatabase("operateur");
        MongoCollection<Document> collection = database.getCollection("test_collection"); 
        
        // Document document = new Document()
        //                     .append("nom", "Someone")
        //                     .append("age", (int) (Math.random() * 50) + 10)
        //                     .append("loisirs", Arrays.asList("foot", "basket"))
        //                     .append("date", Tools.str_to_Date("2021-01-02 00:00:00"))
        //                     ;

        // collection.insertOne(document);

        // Document rabe = collection.find(new Document().append("nom", "Rabe")).first();
        // System.out.println("Here " + rabe);

        MongoCursor<Document> cursor = collection.find().iterator();
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