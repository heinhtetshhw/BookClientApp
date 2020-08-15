package com.shhw.audiobook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter  extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {

    Context context;
    FragmentManager fm;
    ArrayList<ChatMsgModel> chatMsgModels;

    public ChatAdapter(Context context, FragmentManager fm, ArrayList<ChatMsgModel> chatMsgModels) {
        this.context = context;
        this.fm = fm;
        this.chatMsgModels = chatMsgModels;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatitem,parent,false);
        return new ChatHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {

        holder.chatMsg.setText(chatMsgModels.get(position).msg);
    }

    @Override
    public int getItemCount() {
        return chatMsgModels.size();
    }

    public class ChatHolder extends RecyclerView.ViewHolder{
        TextView chatMsg;
        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            chatMsg = itemView.findViewById(R.id.chatmsg);
        }
    }

}
