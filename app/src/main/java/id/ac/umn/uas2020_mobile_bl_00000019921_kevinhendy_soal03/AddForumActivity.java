package id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.models.Forum;

public class AddForumActivity extends AppCompatActivity {
    private EditText etForumTitle, etForumBody;
    private TextInputLayout tilForumTitle, tilForumBody;
    private Button btnCreate;
    private ImageButton ibtnImage;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;
    private StorageReference forumImageStorageReference;

    public static final int RC_PHOTO_PICKER = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_forum);

        etForumTitle = findViewById(R.id.etForumTitle);
        etForumBody = findViewById(R.id.etForumBody);
        btnCreate = findViewById(R.id.btnCreate);
        ibtnImage = findViewById(R.id.ibtnImage);
        tilForumTitle = findViewById(R.id.tilForumTitle);
        tilForumBody = findViewById(R.id.tilForumBody);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        databaseReference = firebaseDatabase.getReference().child("forum");
        forumImageStorageReference = firebaseStorage.getReference().child("forumImage");

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String forumUserUId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final String forumTitle = etForumTitle.getText().toString();
                final String forumBody = etForumBody.getText().toString();
                final int forumCount = 0;
                final String forumPublisher = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                final String[] forumUrl = new String[1];

                if (forumTitle.isEmpty()) tilForumTitle.setError("Please insert forum title");
                else tilForumTitle.setErrorEnabled(false);

                if (forumBody.isEmpty()) tilForumBody.setError("Please insert forum body");
                else tilForumBody.setErrorEnabled(false);

                if(imageUri != null) {
                    final StorageReference photoRef = forumImageStorageReference.child(imageUri.getLastPathSegment()); // Making a child named after last part of URI

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
                                forumUrl[0] = task.getResult().toString();
                                String key = databaseReference.push().getKey();
                                Forum newForum = new Forum(key, forumUserUId, forumTitle, forumBody, forumCount, forumPublisher, forumUrl[0]);

                                databaseReference.child(key).setValue(newForum);

                                finish();
                            } else {
                                Toast.makeText(AddForumActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else Toast.makeText(AddForumActivity.this, "Picture cannot be empty", Toast.LENGTH_LONG).show();
            }
        });

        ibtnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Called before onResume()
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            imageUri = data.getData();
            Glide.with(AddForumActivity.this)
                    .load(imageUri)
                    .into(ibtnImage);
        }
    }
}
