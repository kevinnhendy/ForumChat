package id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.AddForumActivity;
import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.ForumChatActivity;
import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.R;
import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.models.Chat;
import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.models.Forum;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ForumViewHolder> implements Filterable {
    private Context context;
    private List<Forum> forumList;
    private List<Forum> forumListFull;
    private String mode;

    public ForumAdapter(Context context, List<Forum> forumList, String mode){
        this.context = context;
        this.forumList = forumList;
        forumListFull = new ArrayList<>(forumList);
        this.mode = mode;
    }

    @NonNull
    @Override
    public ForumAdapter.ForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.forum_item, parent, false);
        return new ForumAdapter.ForumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumAdapter.ForumViewHolder holder, int position) {
        final Forum forum = forumList.get(position);
        holder.tvForumTitle.setText(forum.getForumTitle());
        holder.tvForumPublisher.setText("Posted by "+forum.getForumPublisher());
        holder.tvForumCount.setText(String.valueOf(forum.getForumCount()));

        if(forum.getForumUrl() != null){
            Glide.with(context)
                    .load(forum.getForumUrl())
                    .into(holder.ivForumImage);
        }

        if(mode.equals("home")){
            holder.cvForumList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Pindah ke Forum: "+forum.getForumTitle(), Toast.LENGTH_LONG).show();
                    Intent newForumChat = new Intent(context, ForumChatActivity.class);
                    newForumChat.putExtra("forumId", forum.getForumId());
                    newForumChat.putExtra("forumPublisher", forum.getForumPublisher());
                    newForumChat.putExtra("forumTitle", forum.getForumTitle());
                    newForumChat.putExtra("forumBody", forum.getForumBody());
                    newForumChat.putExtra("forumUrl", forum.getForumUrl());
                    context.startActivity(newForumChat);
                }
            });
        }
        else if(mode.equals("myforum")){
            holder.tvForumCount.setVisibility(View.GONE);
            holder.ibLogo.setBackgroundResource(R.drawable.ic_delete_black_24dp);
            holder.ibLogo.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    DatabaseReference forumReference = FirebaseDatabase.getInstance().getReference("forum");
                    forumReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Forum forum1 = snapshot.getValue(Forum.class);
                                if(forum1.getForumId().equals(forum.getForumId())){
                                    snapshot.getRef().removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference("chats");
                    chatReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Chat chat = snapshot.getValue(Chat.class);
                                if(chat.getChatsForumId().equals(forum.getForumId())){
                                    snapshot.getRef().removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return forumList.size();
    }

    public Filter getFilter(){
        return filterForum;
    }

    private Filter filterForum = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Forum> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(forumListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Forum item : forumListFull) {
                    if (item.getForumTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            forumList.clear();
            forumList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

//    public class sortedData(List<Forum> forumList){
//        this.forumList = forumList;
//        forumListFull = new ArrayList<>(forumList);
//    }

    public class ForumViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivForumImage;
        public TextView tvForumTitle, tvForumPublisher, tvForumCount;
        public CardView cvForumList;
        public ImageButton ibLogo;

        public ForumViewHolder(@NonNull View itemView) {
            super(itemView);
            ivForumImage = itemView.findViewById(R.id.ivForumImage);
            tvForumTitle = itemView.findViewById(R.id.tvForumTitle);
            tvForumPublisher = itemView.findViewById(R.id.tvForumPublisher);
            tvForumCount = itemView.findViewById(R.id.tvForumCount);
            ibLogo = itemView.findViewById(R.id.ibLogo);
            cvForumList = itemView.findViewById(R.id.cvForumList);
        }
    }
}
