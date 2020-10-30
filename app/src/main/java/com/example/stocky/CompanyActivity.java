package com.example.stocky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CompanyActivity extends AppCompatActivity {
    FirebaseFirestore db;
    DocumentReference reference,reference1;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<String> companyList;
    String intentMsg;
    String position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
       // CompanyActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        /////////////////////////////\
        Intent intent=getIntent();
        //intentUser=intent.getStringExtra("intentUser");
        intentMsg=intent.getStringExtra("itemName");
        position=intent.getStringExtra("position");
        getSupportActionBar().setTitle(intentMsg+" ->Company List");
        /////////////
        TextView update=findViewById(R.id.companyUpdate);
        db=FirebaseFirestore.getInstance();
        reference=db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid()).document("list2-"+intentMsg);
        reference1=db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid()).document("list1");
        final TextView name=findViewById(R.id.companyName1);
        final Button add1=findViewById(R.id.add1);
        companyList=new ArrayList<>();
        progressBar=findViewById(R.id.companyProgressBar);

        ///////////////////////////////////////////update time
        /*
         Calendar calendar= Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MMM-yyyy  hh:mm:ss a");
        String dateTime="Last Updated ->"+simpleDateFormat.format(calendar.getTime());
        update.setText(dateTime);

         */
        ////////////////////////////////////////////recycler View
        recyclerView=findViewById(R.id.companyRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       // final String[] company = {"birla birsanskar", "wallmax wallmax", "kajariassssssssssss", "ssss", "ddddd", "dssss", "ghgj", "jhhjjh", "hjhggh"};
        //String[] size={"111111111111jkjhhjkjkhkjk","2","3","4","4","dssss","ghgj","8","9"};
        //recyclerView.setAdapter(new RecyclerViewAdapter1(company,this));


        /////////////////////////////////////////add button
        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String msg1=name.getText().toString();
                if(msg1.isEmpty())
                {
                    name.setError("Can not be empty!");
                }
                else if(companyList.contains(msg1))
                {
                    Toast.makeText(CompanyActivity.this, "Already contains "+msg1, Toast.LENGTH_SHORT).show();
                }
                else{
                     // add1.setEnabled(false);
                     // progressBar.setVisibility(View.VISIBLE);
                    companyList.add(name.getText().toString());
                    //String[] temp=new String[companyList.size()];
                   // for(int i=0;i<companyList.size();i++) {
                   //     temp[i]=companyList.get(i);
                  //  }
                    recyclerView.setAdapter(new RecyclerViewAdapter1(companyList,intentMsg,CompanyActivity.this));

                    //arrayAdapter.notifyDataSetChanged();
                    Map<String,String> map=new HashMap<>();
                    for(int i=0;i<companyList.size();i++) {
                        map.put(Integer.toString(i), companyList.get(i));
                    }
                        db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid()).document("list2-"+intentMsg).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Snackbar.make(v, name.getText().toString()+" added!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    // Toast.makeText(getActivity(), "successfuly added!", Toast.LENGTH_SHORT).show();
                                   // add1.setEnabled(true);
                                   // progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                               // add1.setEnabled(true);
                               // progressBar.setVisibility(View.INVISIBLE);
                                //Toast.makeText(CompanyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                   // add1.setEnabled(true);
                    //refresh.setEnabled(true);
                    //progressBar.setVisibility(View.INVISIBLE);
                }

            }
        });
        ////////////////////////////refresh

         progressBar.setVisibility(View.VISIBLE);
        reference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                        {
                            int i=0;
                            //ArrayList<String> notes=new ArrayList<>();
                            companyList.clear();
                            while(documentSnapshot.get(Integer.toString(i))!=null)
                            {
                                Object msg= documentSnapshot.get(Integer.toString(i));
                                companyList.add(msg.toString());
                                i++;
                            }
                           // String[] temp=new String[companyList.size()];
                          //  for(i=0;i<companyList.size();i++) {
                             //   temp[i]=companyList.get(i);
                           // }
                            recyclerView.setAdapter(new RecyclerViewAdapter1(companyList,intentMsg,CompanyActivity.this));

                            // arrayAdapter.notifyDataSetChanged();

                        }else{
                            Toast.makeText(CompanyActivity.this, "List not exists!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CompanyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        progressBar.setVisibility(View.INVISIBLE);


        //////////////////////////////////////on create ended

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.company_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.itemEdit) {
            Toast.makeText(this, "edit selected!", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId()==R.id.itemDelete) {
            Toast.makeText(this, "delete selected", Toast.LENGTH_SHORT).show();

            ///////////////////


           /* new AlertDialog.Builder(CompanyActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("are you sure?")
                    .setMessage("Do you want to delete the note?")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           // Map<String,Object> updates = new HashMap<>();
                            //updates.put(position, FieldValue.delete());

                            //reference1.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                              //  @Override
                                //public void onComplete(@NonNull Task<Void> task) {
                                  //  Toast.makeText(CompanyActivity.this, "deleted", Toast.LENGTH_SHORT).show();
                                //}
                            //});
                            Log.d("1ok",position+"///////////////////////////////////////////////"+"//////////////"+String.valueOf(position)+"////////////");

                            reference1.get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Log.d("2ok",position+"///////////////////////////////////////////////"+"//////////////"+String.valueOf(position)+"////////////");

                                            if(documentSnapshot.exists())
                                            {
                                                Log.d("3ok",position+"///////////////////////////////////////////////"+"//////////////"+String.valueOf(position)+"////////////");
                                              int itemToDel =0;
                                                ArrayList<String> notes;
                                                int i=0;
                                                notes=new ArrayList<>();

                                                while(documentSnapshot.get(Integer.toString(i))!=null)
                                                {
                                                    Object msg= documentSnapshot.get(Integer.toString(i));
                                                    notes.add(msg.toString());
                                                    Log.d(notes.get(i),intentMsg+"///////////////////////////////////////////////");
                                                    Log.d(String.valueOf(i),itemToDel+"///////////////@@@@@@@@@@@@@@@@@");
                                                   if(notes.get(i).toString()==intentMsg.toString()) {
                                                       itemToDel = i;
                                                        Log.d(String.valueOf(i),itemToDel+"///////////////sanskarrawaratataa@@@@@@@@@@@@@@@@@");

                                                        break;
                                                    }
                                                    i++;
                                                }
                                                /////////////////
                                               notes.remove(itemToDel);
                                                // arrayAdapter.notifyDataSetChanged();
                                                ///////////update firestore
                                                Map<String,Object> map=new HashMap<>();
                                                for(i=0;i<notes.size();i++) {
                                                    map.put(Integer.toString(i), notes.get(i));
                                                    db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid()).document("list1").set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful())
                                                            {
                                                                Toast.makeText(CompanyActivity.this, "successfuly deleted!", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(CompanyActivity.this,HomeActivity2.class));

                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(CompanyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                                /////////////////
                                               // Log.d("1-----!!!!!!!!!!!",Integer.toString(itemToDel));
                                            }else{
                                                Toast.makeText(CompanyActivity.this, "not exists!!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CompanyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });



                            //////////////update
                        }
                    })
                    .setNegativeButton("No",null)
                    .show();
            return true;

            */
        }







    //////////////////////
        else if(item.getItemId()==R.id.companyRefresh) {
            Toast.makeText(this, "Data refreshed!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.VISIBLE);
            reference.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists())
                            {
                                int i=0;
                                //ArrayList<String> notes=new ArrayList<>();
                                companyList.clear();
                                while(documentSnapshot.get(Integer.toString(i))!=null)
                                {
                                    Object msg= documentSnapshot.get(Integer.toString(i));
                                    companyList.add(msg.toString());
                                    i++;
                                }
                              //  String[] temp=new String[companyList.size()];
                                //for(i=0;i<companyList.size();i++) {
                                //    temp[i]=companyList.get(i);
                               // }
                                recyclerView.setAdapter(new RecyclerViewAdapter1(companyList,intentMsg,CompanyActivity.this));

                                // arrayAdapter.notifyDataSetChanged();

                            }else{
                                Toast.makeText(CompanyActivity.this, "not exists!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CompanyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            progressBar.setVisibility(View.INVISIBLE);

        }

        return true;
    }
}