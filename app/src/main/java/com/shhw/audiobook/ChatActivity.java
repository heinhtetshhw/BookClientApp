package com.shhw.audiobook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore db;
    CollectionReference chat;
    CollectionReference privatechat;
    RecyclerView chatList;
    EditText msg;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        chat = db.collection("chat");;
        msg = findViewById(R.id.msg);
        chatList = findViewById(R.id.chatList);
       user = auth.getCurrentUser();
        if(user!=null)
        {
            privatechat = chat.document(user.getEmail()).collection("privatechat");
            privatechat.orderBy("currentTimeMillis")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            ArrayList<ChatMsgModel> msgModels= new ArrayList<>();
                            for(DocumentSnapshot snapshot : value)
                            {
                                msgModels.add(snapshot.toObject(ChatMsgModel.class));
                            }
                            ChatAdapter adapter  = new ChatAdapter(getApplicationContext(),getSupportFragmentManager(),msgModels);
                            chatList.setAdapter(adapter);
                            chatList.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
                        }
                    });
        }


    }

    public void sendMsg(View view) {
        if(!msg.getText().toString().trim().equals("")){
            user=auth.getCurrentUser();
            if(user==null)
            {
                Intent intent = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(intent);
            }
            else {

                privatechat = chat.document(user.getEmail()).collection("privatechat");
                ChatMsgModel model = new ChatMsgModel();
                model.userName = user.getDisplayName();
                model.msg =msg.getText().toString().trim();
                model.currentTimeMillis =System.currentTimeMillis();
                privatechat.add(model);
                msg.setText("");
            }
        }
    }
}