package com.example.chatapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatapp.MessageActivity;
import com.example.chatapp.Model.Chat;
import com.example.chatapp.Model.User;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mcontext;
    private List<User> mUsers;
    private boolean ischat;

    String theLastMessage;

    public UserAdapter(Context mcontext, List<User> mUsers, boolean ischat){
        this.mcontext = mcontext;
        this.mUsers = mUsers;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.user_item, viewGroup, false);
        return new UserAdapter.ViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final User user = mUsers.get(i);
        viewHolder.username.setText(user.getUsername());
        if(user.getImageURL().equals("default")){
            viewHolder.profile_image.setImageResource(R.raw.default_image);
        }else {
            Glide.with(mcontext).load(user.getImageURL()).into(viewHolder.profile_image);
        }

        if (ischat){
            lastMessage(user.getId(), viewHolder.last_msg);
        }else{
            viewHolder.last_msg.setVisibility(View.GONE);
        }

        if(ischat){
            if(user.getStatus().equals("online")){
                viewHolder.img_on.setVisibility(View.VISIBLE);
                //viewHolder.img_off.setVisibility(View.GONE);
            }else{
                viewHolder.img_on.setVisibility(View.GONE);
                //viewHolder.img_off.setVisibility(View.VISIBLE);
            }
        }else{
            viewHolder.img_on.setVisibility(View.GONE);
            viewHolder.img_off.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;
        public TextView last_msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            last_msg = itemView.findViewById(R.id.last_msg);

        }
    }

    private void lastMessage(final String userid, final TextView last_msg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    assert firebaseUser!=null;
                    if(chat.getReceiver().equals(userid)&&chat.getSender().equals(firebaseUser.getUid()) ||
                            chat.getReceiver().equals(firebaseUser.getUid())&&chat.getSender().equals(userid)){
                        theLastMessage= chat.getMessage();
                    }
                }

                switch (theLastMessage){
                    case "default":
                        last_msg.setText("");
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
