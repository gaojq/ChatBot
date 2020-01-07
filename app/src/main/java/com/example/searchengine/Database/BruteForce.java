package com.example.searchengine.Database;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BruteForce {

    private  Context context;
    public BruteForce(Context context) {
        this.context = context;
    }


    /**
     * search a string [sensor; field; value]
     * @param searchStr
     * @return
     */
    public String searchInFile(String searchStr) {
        String[] strings = searchStr.split(";");
        String filename = strings[0];
        String field = strings[1];
        String value = strings[2];
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        int count = 0;
        try {
            filename += ".txt";
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename), "UTF-8"));
            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                if(mLine.contains(field) && mLine.contains(value)){
                    count ++;
                    sb.append(count + ": " + mLine + "\n");
                }
            }
        } catch (IOException e) {
            Log.i("bruteforce", e.toString());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.i("reader close fail", e.toString());
                }
            }
        }
        return sb.toString();
    }
}

