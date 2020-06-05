package id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.models.Forum;
import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.models.User;

public class ProfileActivity extends AppCompatActivity {

    private ImageView iv_picture;
    private EditText et_name, et_email, et_password, et_nim;
    private Button btn_update;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private static final int RC_PHOTO_PICKER =  1;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference userStorageReference;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        et_name = findViewById(R.id.et_name);
        et_nim = findViewById(R.id.et_nim);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        iv_picture = findViewById(R.id.iv_picture);
        btn_update = findViewById(R.id.btn_update);

        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();
        String email = currentUser.getEmail();
        et_email.setText(email);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        userStorageReference = mFirebaseStorage.getReference("users");
        mDatabaseReference = mFirebaseDatabase.getReference("users").child(currentUser.getUid());
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                et_name.setText(user.getUsersName());
                et_nim.setText(user.getUsersNIM());
//                if(user.getUsersUrl() != null)
//                    Glide.with(ProfileActivity.this).load(user.getUsersUrl()).into(iv_picture);

                if(user.getUsersUrl().equals("no")){
                    iv_picture.setBackgroundResource(R.drawable.ic_default_account);
                }
                else{
                    Glide.with(ProfileActivity.this)
                            .load(user.getUsersUrl())
                            .into(iv_picture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        iv_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.child("usersName").getRef().setValue(et_name.getText().toString());
                        dataSnapshot.child("usersNIM").getRef().setValue(et_nim.getText().toString());
                        dataSnapshot.child("usersEmail").getRef().setValue(et_email.getText().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                String newEmail = et_email.getText().toString();
                if(!newEmail.isEmpty()) {
                    currentUser.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ProfileActivity.this, "Email Updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                String newPassword = et_password.getText().toString();
                Log.d("pass", newPassword);
                if(!newPassword.isEmpty()) {
                    currentUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ProfileActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if(imageUri != null) {
                    final StorageReference photoRef = userStorageReference.child(imageUri.getLastPathSegment()); // Making a child named after last part of URI

                    UploadTask uploadTask = photoRef.putFile(imageUri);
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            return photoRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                final String downloadUri = task.getResult().toString();
                                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        dataSnapshot.child("usersUrl").getRef().setValue(downloadUri);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                finish();
                            } else {
                                Toast.makeText(ProfileActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Called before onResume()
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            imageUri = data.getData();
            Glide.with(ProfileActivity.this)
                    .load(imageUri)
                    .into(iv_picture);
        }
    }
}
