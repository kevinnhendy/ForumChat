package id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private TextView tvRegister, tvErrorMessage;
    private TextInputEditText edtUserEmail, edtUserPassword;
    private TextInputLayout tilUserEmail, tilUserPassword;
    private Button btnLogin;
    private ProgressBar progressBar;

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUserEmail = findViewById(R.id.etUserEmail);
        edtUserPassword = findViewById(R.id.etUserPassword);
        tilUserEmail = findViewById(R.id.tilUserEmail);
        tilUserPassword = findViewById(R.id.tilUserPassword);

        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);

        progressBar = findViewById(R.id.progressBar);

        mFirebaseAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvErrorMessage.setVisibility(View.GONE);
                //Checking empty and invalid input field
                String email = edtUserEmail.getText().toString();
                Pattern pattern = Patterns.EMAIL_ADDRESS;
                boolean emailFlag = pattern.matcher(email).matches();
                if (email.isEmpty()) tilUserEmail.setError("Please insert your email.");
                else if (!emailFlag) tilUserEmail.setError("Invalid email address.");
                else tilUserEmail.setErrorEnabled(false);

                String password = edtUserPassword.getText().toString();
                if (password.isEmpty()) tilUserPassword.setError("Please insert your password");
                else tilUserPassword.setErrorEnabled(false);

                if (emailFlag && !password.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            tvErrorMessage.setVisibility(View.INVISIBLE);

                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                //Error message
                                String errorMessage = "";
                                if (task.getException() instanceof FirebaseAuthException) {
                                    errorMessage = getString(R.string.login_error);
                                } else {
                                    errorMessage = getString(R.string.network_error);
                                }
                                tvErrorMessage.setVisibility(View.VISIBLE);
                                tvErrorMessage.setText(errorMessage);
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
