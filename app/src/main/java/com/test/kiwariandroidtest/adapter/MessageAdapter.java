package com.test.kiwariandroidtest.adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.test.kiwariandroidtest.R;
import com.test.kiwariandroidtest.model.Chats;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public String TAG = MessageAdapter.class.getSimpleName();

    public static final int MSG_TIPE_LEFT = 0;
    public static final int MSG_TIPE_RIGHT = 1;



    private Context context;
    private List<Chats> chatsList;
    private String ImageUrl;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context context, List<Chats> chatsList, String imageUrl) {
        Log.d(TAG, "UserAdapter: " + chatsList);
        this.context = context;
        this.chatsList = chatsList;
        this.ImageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TIPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chats chats = chatsList.get(position);

        holder.show_message.setText(chats.getMessage());

        if (ImageUrl.equals("default")) {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);

        } else {
            Glide.with(context).load(ImageUrl).into(holder.profile_image);
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + chatsList.size());
        return chatsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView show_message;
        ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatsList.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TIPE_RIGHT;
        } else {
            return MSG_TIPE_LEFT;
        }
    }
}
