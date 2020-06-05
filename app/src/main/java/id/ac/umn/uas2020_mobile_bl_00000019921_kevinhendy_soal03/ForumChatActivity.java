package id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.adapters.ChatAdapter;
import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.models.Chat;
import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.models.Forum;

public class ForumChatActivity extends AppCompatActivity {
    private String forumId;
    private String forumPublisher;
    private String forumTitle;
    private String forumBody;
    private String forumUrl;


    private ImageView ivForumImage;
    private TextView tvForumTitle, tvForumPublisher, tvForumBody;
    private ImageButton btn_send;
    private EditText text_send;

    private RecyclerView rvChatList;
    private ChatAdapter chatAdapter;
    private List<Chat> chatList;
    private ProgressBar progressBar;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_chat);

        Intent i = getIntent();
        forumId = i.getStringExtra("forumId");
        forumPublisher = i.getStringExtra("forumPublisher");
        forumTitle = i.getStringExtra("forumTitle");
        forumBody = i.getStringExtra("forumBody");
        forumUrl = i.getStringExtra("forumUrl");

        tvForumTitle = findViewById(R.id.tvForumTitle);
        tvForumPublisher = findViewById(R.id.tvForumPublisher);
        tvForumBody = findViewById(R.id.tvForumBody);
        ivForumImage = findViewById(R.id.ivForumImage);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        progressBar = findViewById(R.id.progressBar);

        tvForumTitle.setText(forumTitle);
        tvForumPublisher.setText(forumPublisher);
        tvForumBody.setText(forumBody);
        Glide.with(this)
                .load(forumUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        //Log.d("USER PHOTO", UserData.user.getRole());
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(ivForumImage);

        rvChatList = findViewById(R.id.rvChatList);
        rvChatList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        rvChatList.setLayoutManager(linearLayoutManager);

        firebaseDatabase = FirebaseDatabase.getInstance();
        chatList  = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = text_send.getText().toString();
                if(!message.equals("")) {
                    DatabaseReference chatsReference = FirebaseDatabase.getInstance().getReference("chats");
                    final DatabaseReference countReference = FirebaseDatabase.getInstance().getReference("forum").child(forumId);
                    countReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Forum forum = dataSnapshot.getValue(Forum.class);
                            int count = forum.getForumCount()+1;
                            dataSnapshot.child("forumCount").getRef().setValue(count);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    Chat newChat = new Chat(forumId, message, user.getDisplayName(), user.getUid());
                    chatsReference.push().setValue(newChat);
                } else {
                    Toast.makeText(ForumChatActivity.this, "Message is empty", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getChatsForumId().equals(forumId)){
                        chatList.add(chat);
                    }
                }

                chatAdapter = new ChatAdapter(ForumChatActivity.this, chatList);
                rvChatList.setAdapter(chatAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
