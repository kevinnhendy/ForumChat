package id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.R;
import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.adapters.ForumAdapter;
import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.models.Forum;

public class MyForumFragment extends Fragment implements SearchView.OnQueryTextListener{
    private RecyclerView rvForumList;
    private List<Forum> forumList;
    private ForumAdapter forumAdapter;
    private ProgressBar progressBar;
    private SearchView searchView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    FirebaseUser user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_forum, container, false);

        rvForumList = root.findViewById(R.id.rvForumList);
        progressBar = root.findViewById(R.id.progressBar);
        rvForumList.setLayoutManager(new LinearLayoutManager(getActivity()));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("forum");
        forumList  = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                forumList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Forum forum = snapshot.getValue(Forum.class);

                    if(user != null){
                        if(forum.getForumUserUId().equals(user.getUid())){
                        forumList.add(forum);
                        }
                    }
                }

                progressBar.setVisibility(View.GONE);
                forumAdapter = new ForumAdapter(getActivity(), forumList, "myforum");
                rvForumList.setAdapter(forumAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        searchView = root.findViewById(R.id.svForum);
        searchView.setOnQueryTextListener(this);
        return root;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        forumAdapter.getFilter().filter(newText);
        return false;
    }
}
