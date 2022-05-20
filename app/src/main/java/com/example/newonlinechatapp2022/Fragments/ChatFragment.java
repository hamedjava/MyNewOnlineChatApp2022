package com.example.newonlinechatapp2022.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newonlinechatapp2022.FirebaseModel.FirebaseModel;
import com.example.newonlinechatapp2022.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class ChatFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    LinearLayoutManager linearLayoutManager;
    private FirebaseAuth firebaseAuth;
    ImageView img_cardViewOfUser;
    FirestoreRecyclerAdapter<FirebaseModel,NoteViewHolder> chatAdapter;
    RecyclerView rv_chat_list;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment,container,false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        rv_chat_list = view.findViewById(R.id.rv_chat_list);

        Query query = firebaseFirestore.collection("Users");
        FirestoreRecyclerOptions<FirebaseModel> allUsername = new FirestoreRecyclerOptions
                .Builder<FirebaseModel>()
                .setQuery(query,FirebaseModel.class)
                .build();


        chatAdapter = new FirestoreRecyclerAdapter<FirebaseModel, NoteViewHolder>(allUsername) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull NoteViewHolder holder, int position, @NonNull @NotNull FirebaseModel model) {
                holder.particularUsername.setText(model.getName());
                holder.userStatus.setText(model.getStatus());
                String uri = model.getImage();

                Picasso.get().load(uri).into(img_cardViewOfUser);

                if(model.getStatus().equals("Online")){
                    holder.userStatus.setText(model.getStatus());
                    holder.userStatus.setTextColor(Color.GREEN);
                }else{
                    holder.userStatus.setText(model.getStatus());
                    holder.userStatus.setTextColor(Color.RED);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "Item Is Clicked..", Toast.LENGTH_SHORT).show();
                    }
                });


                
            }

            @NonNull
            @NotNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_fragment,parent,false);
                return new NoteViewHolder(view);
            }
        };


        rv_chat_list.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rv_chat_list.setLayoutManager(linearLayoutManager);

        rv_chat_list.setAdapter(chatAdapter);

        return view;


    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    public class NoteViewHolder extends RecyclerView.ViewHolder {

        private TextView particularUsername;
        private TextView userStatus;
        private CardView cardView;

        public NoteViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            particularUsername = itemView.findViewById(R.id.tv_card_username);
            userStatus = itemView.findViewById(R.id.tv_card_status);
            img_cardViewOfUser = itemView.findViewById(R.id.user_logo_ProfileActivity);
            cardView = itemView.findViewById(R.id.cv_users_list_logo);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        chatAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(chatAdapter!=null){
            chatAdapter.stopListening();
        }
    }
}
