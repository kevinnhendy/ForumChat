package id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;

import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.adapters.ForumAdapter;
import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.fragments.HomeFragment;
import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.models.Forum;
import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.models.User;

public class MainActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth.AuthStateListener mAuthStateListener; // Check user's state login or logout
    FirebaseUser user;
    public static final int RC_SIGN_IN = 1;
    private ImageView ivUserImage;
    private TextView tvUserName, tvUserEmail;
    private User userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addForum = new Intent(MainActivity.this, AddForumActivity.class);
                startActivity(addForum);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_signout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerview = navigationView.getHeaderView(0);
        ivUserImage = headerview.findViewById(R.id.ivUserImage);
        tvUserName = headerview.findViewById(R.id.tvUsername);
        tvUserEmail = headerview.findViewById(R.id.tvUserEmail);
        Log.d("tvUsername", String.valueOf(tvUserName.getText()));
        Log.d("tvUserEmail", String.valueOf(tvUserEmail.getText()));
        //nav_view
        updateCurrentUser();

        headerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentProfile = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intentProfile);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Called before onResume()
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            if(resultCode == RESULT_OK && user != null) {
                Toast.makeText(this, "Signed in! "+user.getDisplayName(), Toast.LENGTH_SHORT).show();
            } else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = new Bundle();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        switch(item.getItemId()) {
            case R.id.sort_alphabet_menu:
                bundle.putString("SORTBY", "alphabet");
                homeFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.nav_host_fragment, homeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                Toast.makeText(this, "SORTED ALPHABET", Toast.LENGTH_LONG).show();
                return true;
            case R.id.sort_comments_menu:
                bundle.putString("SORTBY", "comments");
                homeFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.nav_host_fragment, homeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                Toast.makeText(this, "SORTED COMMENT", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCurrentUser();
    }

    public void updateCurrentUser(){
//        progress_bar.setVisibility(View.VISIBLE);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final String userUId = firebaseAuth.getCurrentUser().getUid();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("users");
        userData = new User();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User oneUser = snapshot.getValue(User.class);

                    if(userUId != null){
                        Log.d("oneUser.getUsersUId()", oneUser.getUsersUId());
                        Log.d("userUId", userUId);
                        if(oneUser.getUsersUId().equals(userUId)){
                            userData = oneUser;

                            if(userData.getUsersUrl().equals("no")){
                                ivUserImage.setBackgroundResource(R.drawable.ic_default_account);
                            }
                            else{
                                Glide.with(MainActivity.this)
                                        .load(userData.getUsersUrl())
                                        .into(ivUserImage);
                            }
                            tvUserName.setText(userData.getUsersName());
                            tvUserEmail.setText(userData.getUsersEmail());
                        }
                    }
                }
//                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
