package com.test.kiwariandroidtest.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.test.kiwariandroidtest.R;
import com.test.kiwariandroidtest.adapter.UserAdapter;
import com.test.kiwariandroidtest.model.Chats;
import com.test.kiwariandroidtest.model.User;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private RecyclerView chatRecycler;

    private UserAdapter adapter;
    private List<User> userList;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    private List<String> mUsers;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        chatRecycler = view.findViewById(R.id.chatsRecycler);
        chatRecycler.setHasFixedSize(true);
        chatRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mUsers = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chats chats = snapshot.getValue(Chats.class);

                    if (chats.getSender().equals(firebaseUser.getUid())) {
                        mUsers.add(chats.getReceiver());
                    }

                    if (chats.getReceiver().equals(firebaseUser.getUid())) {
                        mUsers.add(chats.getSender());
                    }
                }
                
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void readChats() {
         userList = new ArrayList<>();

         databaseReference = FirebaseDatabase.getInstance().getReference("Users");

         databaseReference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 userList.clear();

                 for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                     User user = snapshot.getValue(User.class);

                     for (String id :mUsers) {
                         if (user.getId().equals(id)) {
                             if (userList.size() != 0) {
                                 for (User users : userList) {
                                     if (!user.getId().equals(users.getId())) {
                                         userList.add(user);
                                     }
                                 }
                             } else {
                                 userList.add(user);
                             }
                         }
                     }
                 }

                 adapter = new UserAdapter(getContext(), userList);
                 chatRecycler.setAdapter(adapter);
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });
    }
}
