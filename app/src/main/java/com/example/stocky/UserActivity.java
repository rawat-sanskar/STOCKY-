package com.example.stocky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity {
    Button add;
    EditText userName1;
    FirebaseFirestore db ;
    DocumentReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        add=findViewById(R.id.add);
        db=FirebaseFirestore.getInstance();
        UserActivity.this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        reference=db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid()).document("userList");
        userName1=findViewById(R.id.userName);
        final ListView listView=findViewById(R.id.listView);
        final ArrayList<String> userList=new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(UserActivity.this, android.R.layout.simple_list_item_1, userList);
        listView.setAdapter(arrayAdapter);
        //userList.add("Example note");
        ImageView refresh=findViewById(R.id.userRefresh);
        {
            reference.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists())
                            {
                                int i=0;
                                //ArrayList<String> notes=new ArrayList<>();
                                userList.clear();
                                while(documentSnapshot.get(Integer.toString(i))!=null)
                                {
                                    Object msg= documentSnapshot.get(Integer.toString(i));
                                    userList.add(msg.toString());
                                    i++;
                                }
                                arrayAdapter.notifyDataSetChanged();
                                //Toast.makeText(UserActivity.this, "List updated!", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(UserActivity.this, "List not exists!", Toast.LENGTH_SHORT).show();
                            }
                            //refresh.setEnabled(true);
                            //progressBar.setVisibility(View.INVISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    //refresh.setEnabled(true);
                    //progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists())
                                {
                                    int i=0;
                                    //ArrayList<String> notes=new ArrayList<>();
                                    userList.clear();
                                    while(documentSnapshot.get(Integer.toString(i))!=null)
                                    {
                                        Object msg= documentSnapshot.get(Integer.toString(i));
                                        userList.add(msg.toString());
                                        i++;
                                    }
                                    arrayAdapter.notifyDataSetChanged();
                                    Toast.makeText(UserActivity.this, "List updated!", Toast.LENGTH_SHORT).show();

                                }else{
                                    Toast.makeText(UserActivity.this, "List not exists!", Toast.LENGTH_SHORT).show();
                                }
                                //refresh.setEnabled(true);
                                //progressBar.setVisibility(View.INVISIBLE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        //refresh.setEnabled(true);
                        //progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserActivity.this.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                String msg=userName1.getText().toString();
                if(userName1.getText().toString().isEmpty())
                {
                    userName1.setError("Can not be empty!");
                }
                else if(userList.contains(userName1.getText().toString()))
                {
                    Toast.makeText(UserActivity.this, "Already contains "+msg, Toast.LENGTH_SHORT).show();
                }
                else{
                    userList.add(msg);
                    arrayAdapter.notifyDataSetChanged();
                    Map<String,Object> map=new HashMap<>();
                    for(int i=0;i<userList.size();i++) {
                       map.put(Integer.toString(i), userList.get(i));
                    }
                        db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid()).document("userList").set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    // Toast.makeText(getActivity(), "successfuly added!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getActivity(), arrayList.get(position), Toast.LENGTH_SHORT).show();
               // Intent intent=new Intent(UserActivity.this,HomeActivity2.class);
               // intent.putExtra("intentUser",userList.get(position));
               // intent.putExtra("Position",String.valueOf(position));
               // startActivity(intent);
                // startActivity(new Intent(getActivity(),CompanyActivity.class));
            }
        });
        ///////////////////////////////////////////////////////////////
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, int i, long l) {
                final int itemToDelete=i;
                new AlertDialog.Builder(UserActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("are you sure?")
                        .setMessage("Do you want to delete the "+userList.get(itemToDelete).toUpperCase()+"?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final String delete=userList.get(itemToDelete);
                                userList.remove(itemToDelete);
                                arrayAdapter.notifyDataSetChanged();
                                ///////////update firestore
                                Map<String,Object> map=new HashMap<>();
                                for(i=0;i<userList.size();i++) {
                                    map.put(Integer.toString(i), userList.get(i));
                                }
                                reference.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            // Toast.makeText(getActivity(), "successfuly deleted!", Toast.LENGTH_SHORT).show();
                                            Snackbar.make(view, delete+" deleted", Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                //////////////update
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;
            }
        });


    }
}