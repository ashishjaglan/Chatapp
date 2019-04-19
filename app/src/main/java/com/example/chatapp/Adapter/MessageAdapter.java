package com.example.chatapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatapp.MessageActivity;
import com.example.chatapp.Model.Chat;
import com.example.chatapp.Model.User;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mcontext;
    private List<Chat> mChat;
    private String imageurl;

    FirebaseUser fuser;

    public MessageAdapter(Context mcontext, List<Chat> mChat, String imageurl){
        this.mcontext = mcontext;
        this.mChat = mChat;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i==MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_right, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        }else {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_left, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int i) {

        Chat chat = mChat.get(i);

        viewHolder.show_message.setText(chat.getMessage());

        if(imageurl.equals("default")){
            viewHolder.profile_image.setImageResource(R.raw.default_image);
        }else {
            Glide.with(mcontext).load(imageurl).into(viewHolder.profile_image);
        }

        if (i==mChat.size()-1){
            if(chat.isIsseen()){
                viewHolder.txt_seen.setText("seen");
            }else {
                viewHolder.txt_seen.setText("deleivered");
            }
        }else{
            viewHolder.txt_seen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;

        public TextView txt_seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);

        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}