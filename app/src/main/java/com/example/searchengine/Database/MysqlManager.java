package com.example.searchengine.Database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.simple.JSONValue;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;


public class MysqlManager {

    private Context context;
    private MysqlDatabaseHelper mysqldbHelper;

    public MysqlManager(Context context) throws IOException {
        this.context = context;
        mysqldbHelper = new MysqlDatabaseHelper(context);
        if(mysqldbHelper.isEmpty()){
            parseAllFiles();
        }
    }



    /**
     * parse all files and add to mysql database
     * @throws IOException
     */
    public void parseAllFiles() throws IOException {
        String [] list;
        list = context.getAssets().list("");
        for (String file : list) {

            if(file.contains(".txt")){
                String filename = file.split("\\.")[0];
                JSONArray jsonObjects = parseFile(file);
                addToMySql(filename, jsonObjects);
            }
        }
    }


    /**
     * parse one file
     * @param file
     * @return
     * @throws IOException
     */
    public JSONArray parseFile(String file) throws IOException {

        InputStreamReader readerJson = new InputStreamReader(context.getAssets().open(file));
        //Parse the json file using simple-json library
        Object fileObjects= JSONValue.parse(readerJson);
        JSONArray arrayObjects=(JSONArray)fileObjects;
        readerJson.close();

        return arrayObjects;
    }


    /**
     * insert data
     * @param filename
     * @param jsonObjects
     */
    public void addToMySql(String filename, JSONArray jsonObjects) {
        for (JSONObject object : (List<JSONObject>) jsonObjects) {
            ContentValues contentValues = new ContentValues();
            for (String field : (Set<String>) object.keySet()) {
                Class type = object.get(field).getClass();
                if (type.equals(String.class)) {
                    contentValues.put(field, object.get(field).toString());
                } else if (type.equals(JSONObject.class)) {
                    JSONObject ja = (JSONObject) object.get(field);
                    for (String f : (Set<String>) ja.keySet()) {
                        contentValues.put(f, ja.get(f).toString());
                    }
                }
            }
            if(!mysqldbHelper.insertData(filename, contentValues)){
                System.out.println(filename + " fail to insert data");
            }
        }
    }


    /**
     * search a string [sensor; field; value]
     * @param searchStr
     * @return
     */
    public String search(String searchStr) {
        String[] strings = searchStr.split(";");
        String table = strings[0];
        String field = strings[1];
        String value = strings[2];

        Cursor res = mysqldbHelper.getData(table, field, value);

        int totalMatch = 0;
        StringBuilder sb = new StringBuilder();
        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                int count = res.getColumnCount();
                totalMatch++;
                sb.append(totalMatch + ": ");
                for (int i = 1; i < count; i++) {
                    sb.append(res.getColumnName(i) + ": " + res.getString(i) + "\n");
                }
            }
        }
        Log.i("totalMatch", String.valueOf(totalMatch));
        return sb.toString();
    }

    public void getTables(){
        mysqldbHelper.getAllTables();
    }


}
