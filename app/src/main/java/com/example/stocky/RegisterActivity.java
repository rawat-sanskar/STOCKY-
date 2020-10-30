package com.example.stocky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button register;
    private EditText email,password,mobile,name;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    private ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("STOCKY-LET'S MANAGE STOCK");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        register=findViewById(R.id.register);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        mobile=findViewById(R.id.mobile);
        name=findViewById(R.id.name);
        progressBar2=findViewById(R.id.progressBar2);
        auth=FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register.setEnabled(false);
            String txtEmail=email.getText().toString();
            String txtPassword=password.getText().toString();
            String txtName=name.getText().toString();
            String txtMobile=mobile.getText().toString();
            if(TextUtils.isEmpty(txtEmail)||TextUtils.isEmpty(txtName)||TextUtils.isEmpty(txtPassword)){
                Toast.makeText(RegisterActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                register.setEnabled(true);
            }
            else if(txtPassword.length()<6){
                Toast.makeText(RegisterActivity.this, "Password Strength too short!", Toast.LENGTH_SHORT).show();
                register.setEnabled(true);
            }
            else{
                progressBar2.setVisibility(View.VISIBLE);
               // database.getReference().child("information").child(txtName).setValue(txtPassword);
                registerUser(txtName,txtEmail,txtPassword,txtMobile);
            }
            }
        });

    }

    private void registerUser(final String txtName, final String txtEmail, final String txtPassword, final String txtMobile) {

     auth.createUserWithEmailAndPassword(txtEmail,txtPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
         @Override
         public void onSuccess(AuthResult authResult) {
             HashMap<String, Object> map = new HashMap<>();
             map.put("name", txtName);
             map.put("email", txtEmail);
             map.put("password", txtPassword);
             map.put("mobile", txtMobile);
             map.put("id", auth.getCurrentUser().getUid());
             databaseReference.child("users").child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {
                     if (task.isSuccessful()) {
                         Toast.makeText(RegisterActivity.this, "Welcome " + txtName + ",your Registration is successful!", Toast.LENGTH_SHORT).show();
                         auth.signInWithEmailAndPassword(txtEmail, txtPassword);
                         progressBar2.setVisibility(View.INVISIBLE);
                         startActivity(new Intent(RegisterActivity.this, HomeActivity2.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                         finish();
                     }
                     register.setEnabled(false);
                 }
             });
         }
     }).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {
             Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
             progressBar2.setVisibility(View.INVISIBLE);
             register.setEnabled(true);
         }
     });
    }
    }
