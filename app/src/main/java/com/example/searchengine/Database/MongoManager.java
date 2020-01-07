package com.example.searchengine.Database;

import android.content.Context;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.local.LocalMongoDbService;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class MongoManager {

    private MongoDatabase dbObj;
    private Context context;

    public MongoManager(Context context) {
        this.context = context;
        if(databaseSetUp()){
            try {
                parseAllFiles();
            }catch (Exception e){

            }
        }
    }


    /**
     * parse all files and insert to database
     * @throws IOException
     */
    private void parseAllFiles() throws IOException {
        String [] list;
        list = context.getAssets().list("");
        for (String file : list) {

            if(file.contains(".txt")){
                MongoCollection<Document> col = dbObj.getCollection(file.split(".txt")[0]);

                JSONArray jsonObjects = parseFile(file);
                col.insertMany(addDocuments(jsonObjects));
            }
        }
    }



    /**
     * Parse a Json file.
     * @param file
     * @return
     * @throws IOException
     */
    private JSONArray parseFile(String file) throws IOException {

        InputStreamReader readerJson = new InputStreamReader(context.getAssets().open(file));
        Object fileObjects= JSONValue.parse(readerJson);
        JSONArray arrayObjects=(JSONArray)fileObjects;
        readerJson.close();

        return arrayObjects;
    }


    /**
     * Add documents to the index
     * @param jsonObjects
     * @return
     */
    public List<Document> addDocuments(JSONArray jsonObjects) {
        //{"sensor_name":"HeartRate","timestamp":"Sat Jun 10 16:33:20 EDT 2017","sensor_data":{"bpm":76}}
        //"sensor_name":"HeartRate","timestamp":"Sat Jun 10 16:33:20 EDT 2017","bpm":76

        List<Document> list = new ArrayList<>();
        for (JSONObject object : (List<JSONObject>) jsonObjects) {
            Document doc = new Document();
            for (String field : (Set<String>) object.keySet()) {
                Class type = object.get(field).getClass();
                if (type.equals(String.class)) {
                    doc.put(field, String.valueOf(object.get(field)).toLowerCase());
                } else if (type.equals(JSONObject.class)) {
                    JSONObject ja = (JSONObject) object.get(field);
                    for (String f : (Set<String>) ja.keySet()) {
                        doc.put(f, String.valueOf(ja.get(f)).toLowerCase());
                    }
                }
            }
            list.add(doc);
        }
        return list;
    }


    /**
     * search a string [sensor; field; value]
     * @param searchStr
     * @return
     */
    public String search(String searchStr){
        String[] strings = searchStr.split(";");
        MongoCollection<Document> col = dbObj.getCollection(strings[0]);

        List<Bson> filters = new ArrayList<>();
        filters.add(eq(strings[1], new BasicDBObject("$regex", strings[2])));
        List<Document> docs = col.find(and(filters)).into(new ArrayList<>());

        int count = 0;
        StringBuilder sb = new StringBuilder();
        for(Document doc: docs){
            count++;
            sb.append(count + ": ");
            for(String key: doc.keySet()){
                if(!key.equals("_id")){
                    sb.append(key + ": " + doc.get(key) + ", ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }


    /**
     * setup mongodb databse
     * mongo stitch
     *
     * @return
     * true: haven't setup, first time user
     * false: databse is already setup
     */
    public boolean databaseSetUp(){

        final StitchAppClient client = Stitch.initializeAppClient("stitch_client_app_id");
        final MongoClient mobileClient = client.getServiceClient(LocalMongoDbService.clientFactory);
        MongoDatabase dbObj = mobileClient.getDatabase("admin");
        this.dbObj = dbObj;

        int count = 0;
        for (String name : dbObj.listCollectionNames()) {
            System.out.println("Collections inside this db:" + name);

            count++;
        }

        return count == 1;
    }

}