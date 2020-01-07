package com.example.searchengine;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import com.example.searchengine.Database.BruteForce;
import com.example.searchengine.Database.LuceneManager;
import com.example.searchengine.Database.MongoManager;
import com.example.searchengine.Database.MysqlManager;
import com.example.searchengine.Model.Chat;
import com.example.searchengine.Model.Message;
import java.util.ArrayList;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    public static ArrayList<Message> messageList = new ArrayList<>(); //store message history
    public static ArrayList<Chat> chatList = new ArrayList<>(); //store message history
    public static MongoManager mongoManager;
    public static BruteForce bruteForce;
    public static LuceneManager luceneManager;
    public static MysqlManager mysqlManager;


}




