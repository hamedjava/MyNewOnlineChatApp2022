package com.example.newonlinechatapp2022;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter{

    Context context;
    ArrayList<MessagesModel> messagesModelArrayList;

    private static final int ITEM_SEND = 1;
    private static final int ITEM_RECEIVE = 2;

    public MessagesAdapter(Context context,ArrayList<MessagesModel>messagesModelArrayList){
        this.context = context;
        this.messagesModelArrayList = messagesModelArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SEND){
            View view = LayoutInflater.from(this.context).inflate(R.layout.sender_chat_layout,parent,false);
            return new SenderViewHolder(view);
        }else{
            View view = LayoutInflater.from(this.context).inflate(R.layout.receiver_chat_layout,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return messagesModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessagesModel messagesModel = this.messagesModelArrayList.get(position);

        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messagesModel.getSenderID())){
            return ITEM_SEND;
        }else{
            return ITEM_RECEIVE;
        }

    }

    class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView textViewMessage;
        TextView timeOfMessage;

        public SenderViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textViewMessage = (TextView)itemView.findViewById(R.id.tv_sender_message);
            timeOfMessage = (TextView)itemView.findViewById(R.id.tv_sender_timeMessage);

        }
    }


    class ReceiverViewHolder extends RecyclerView.ViewHolder{

        TextView textViewMessage;
        TextView timeOfMessage;

        public ReceiverViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textViewMessage = (TextView)itemView.findViewById(R.id.tv_receiver_message);
            timeOfMessage = (TextView)itemView.findViewById(R.id.tv_receiver_timeMessage);

        }
    }

}
