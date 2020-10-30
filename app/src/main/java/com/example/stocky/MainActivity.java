package com.example.stocky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button register,login;
    private EditText email,password;
    FirebaseAuth auth;
    TextView forgot;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("STOCKY-LET'S MANAGE STOCK");
        register=findViewById(R.id.register);
        login=findViewById(R.id.login);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        forgot=findViewById(R.id.forgot);
        auth=FirebaseAuth.getInstance();
         progressBar=findViewById(R.id.progressBar1);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                finish();
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "new id bna lo aur ab se yaad rakhna!!", Toast.LENGTH_SHORT).show();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txtEmail=email.getText().toString();
                String txtPassword=password.getText().toString();
                if(txtEmail.isEmpty()){
                     email.setError("Email can not be empty!");
                }else if(txtPassword.isEmpty())
                {
                    password.setError("Password can not be Empty");
                }
                else {
                    login.setEnabled(false);
                    register.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(txtEmail, txtPassword);
                }
            }
        });

    }

    private void loginUser(String txtEmail, String txtPassword) {
        auth.signInWithEmailAndPassword(txtEmail,txtPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(MainActivity.this, "login successful!!!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                login.setEnabled(true);
                register.setEnabled(true);
                startActivity(new Intent(MainActivity.this,HomeActivity2.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                login.setEnabled(true);
                register.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

   @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            startActivity(new Intent(MainActivity.this,HomeActivity2.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }

    }
    }