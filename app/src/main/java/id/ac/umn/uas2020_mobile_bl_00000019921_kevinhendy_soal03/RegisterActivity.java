package id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.models.User;

public class RegisterActivity extends AppCompatActivity {

    private TextView tv_login;
    private EditText etUserName, etUserEmail, etUserNIM, etUserPassword, etConfirmPassword;
    private Button btn_register;
    private User user;
    private String userName, userEmail, userPassword, userNIM, userUId;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUserName = findViewById(R.id.etUserName);
        etUserEmail = findViewById(R.id.etUserEmail);
        etUserNIM = findViewById(R.id.etUserNIM);
        etUserPassword = findViewById(R.id.etUserPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btn_register = findViewById(R.id.btnRegister);
        tv_login = findViewById(R.id.tvLogin);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = etUserName.getText().toString();
                userEmail = etUserEmail.getText().toString();
                userPassword = etUserPassword.getText().toString();
                userNIM = etUserNIM.getText().toString();

                firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        userUId = firebaseAuth.getCurrentUser().getUid();

                        if(userUId != null){
                            user = new User(userEmail, userNIM, userName, userUId, "no");
                            DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(userUId);
                            databaseReference.setValue(user);

                            Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        }
                    }
                });
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }
}