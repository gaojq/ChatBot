package com.example.searchengine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.searchengine.Model.Chat;
import com.example.searchengine.Model.Message;

import java.util.List;

import androidx.appcompat.app.AlertDialog;

public class HistoryActivity extends BaseActivity {

    private HistoryMessagesListAdapter historyAdapter = new HistoryMessagesListAdapter(this, chatList);
    private ListView listViewMessages;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listViewMessages = (ListView) findViewById(R.id.history_list_view);
        inflater = getLayoutInflater();
        listViewMessages.setAdapter(historyAdapter);


        listViewMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                customDialog(pos);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.back_to_chat_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Toast.makeText(getApplicationContext(), "Back to chat", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, ChatActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }




    public class HistoryMessagesListAdapter extends BaseAdapter {

        private HistoryActivity context;
        private List<Chat> chatItems;

        public HistoryMessagesListAdapter(HistoryActivity context, List<Chat> navDrawerItems) {
            this.context = context;
            this.chatItems = navDrawerItems;
        }

        @Override
        public int getCount() {
            return chatItems.size();
        }

        @Override
        public Object getItem(int position) {
            return chatItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Chat chat = chatItems.get(position);
            System.out.println(chat.getMsgFromChatbot().getMessage());
            Message msgFromUser = chatItems.get(position).getMsgFromUser();
            Message msgFromChatbot = chatItems.get(position).getMsgFromChatbot();


            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.list_item_history, null);

            TextView txtMsg = (TextView) convertView.findViewById(R.id.history_msg_from_user);
            TextView txtMode = (TextView) convertView.findViewById(R.id.history_msg_mode);

            txtMsg.setText(msgFromUser.getMessage() + " ");
            txtMode.setText(msgFromChatbot.getMode() + " ");

            return convertView;
        }

    }


    /**
     * pop up a customdialog that contains detail info of a history record
     * @param pos
     */
    private void customDialog(int pos) {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(HistoryActivity.this);
        LayoutInflater inflater = LayoutInflater.from(HistoryActivity.this);
        View myView = inflater.inflate(R.layout.chat_detail, null);
        final AlertDialog dialog = myDialog.create();
        dialog.setView(myView);


        Chat chat = chatList.get(pos);
        System.out.println(chat.getMsgFromChatbot().getMessage());


        TextView msg = myView.findViewById(R.id.detail_msg);
        TextView mode = myView.findViewById(R.id.detail_mode);
        TextView extTime = myView.findViewById(R.id.detail_exe_time);
        TextView replay = myView.findViewById(R.id.detail_reply);


        msg.setText(chat.getMsgFromUser().getMessage());
        mode.setText(chat.getMsgFromChatbot().getMode().toString());
        extTime.setText(String.valueOf(chat.getMsgFromChatbot().getExecutionTime()) + " ms");
        replay.setText(chat.getMsgFromChatbot().getMessage());

        dialog.show();
    }



}
