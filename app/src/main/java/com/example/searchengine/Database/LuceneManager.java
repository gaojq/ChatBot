package com.example.searchengine.Database;
import android.content.Context;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

public class LuceneManager {


    String indexPath;
    String filePath = "file:///android_asset/";
    IndexWriter indexWriter = null;
    private Context context;


    public LuceneManager(Context context) {
        this.context = context;
        this.indexPath = context.getFilesDir().getPath()+"/index";
    }

    public void createIndex() throws IOException {
        openIndex();
        parseAllFiles();
        finish();
    }

    /**
     * open lucene index
     * @return
     */
    private boolean openIndex(){
        try {

            Directory dir = FSDirectory.open(Paths.get(indexPath));
            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

            //Always overwrite the directory
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            indexWriter = new IndexWriter(dir, iwc);

            return true;
        } catch (Exception e) {
            System.err.println("Error opening the index. " + e.getMessage());
        }
        return false;
    }


    /**
     * Add documents to the index
     * @param jsonObjects
     */
    private void addDocuments(JSONArray jsonObjects) {
        for (JSONObject object : (List<JSONObject>) jsonObjects) {
            Document doc = new Document();
            for (String field : (Set<String>) object.keySet()) {
                Class type = object.get(field).getClass();
                if (type.equals(String.class)) {
                    doc.add(new StringField(field, (String) object.get(field.toLowerCase()), Field.Store.YES));
                } else if (type.equals(JSONObject.class)) {
                    JSONObject ja = (JSONObject) object.get(field);
                    for (String f : (Set<String>) ja.keySet()) {
                        doc.add(new StringField(f, String.valueOf(ja.get(f)).toLowerCase(), Field.Store.YES));
                    }
                }
            }
            try {
                indexWriter.addDocument(doc);
            } catch (IOException ex) {
                System.err.println("Error adding documents to the index. " + ex.getMessage());

            }
        }
    }


    /**
     * Write the document to the index and close it
     */
    private void finish(){
        try {
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException ex) {
            System.err.println("We had a problem closing the index: " + ex.getMessage());
        }
    }


    /**
     * Parse all files
     * @throws IOException
     */
    private void parseAllFiles() throws IOException {
        String [] list;
        list = context.getAssets().list("");
        for (String file : list) {

            if(file.contains(".txt")){
                JSONArray jsonObjects = parseFile(file);
                addDocuments(jsonObjects);
            }
        }
    }



    /**
     * Parse one file.
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
     * search a string [sensor; field; value]
     * @param searchString
     * @return
     * @throws IOException
     */
    public String search(String searchString) throws IOException {

        String[] strings = searchString.split(";");

        Directory indexDirectory = FSDirectory.open(FileSystems.getDefault().getPath(indexPath));
        IndexReader indexReader = DirectoryReader.open(indexDirectory);
        final IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        TopDocs topDocs = null;
        ScoreDoc[] scoreDocs = null;

        Term t = new Term(strings[1], strings[2]);
        Query query = new TermQuery(t);

        topDocs = indexSearcher.search(query, 100);
        scoreDocs = topDocs.scoreDocs;

        int count = 0;
        StringBuilder sb = new StringBuilder();
        for (ScoreDoc scoreDoc : scoreDocs) {
            count++;

            Document document = indexSearcher.doc(scoreDoc.doc);
            List<IndexableField> fields = document.getFields();
            sb.append(count + ": ");

            for(int i = 0; i < fields.size(); i++){
                sb.append(fields.get(i).name() + ": ");
                sb.append(fields.get(i).stringValue() + ", ");
            }

            sb.append("\n");
        }

        return sb.toString();
    }


}
