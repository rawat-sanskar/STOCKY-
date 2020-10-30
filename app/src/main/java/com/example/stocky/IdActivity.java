package com.example.stocky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
//import android.widget.SearchView;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IdActivity extends AppCompatActivity {
     RecyclerView recyclerView;
     TextView idNameText;
     TextView sizeText;
    DocumentReference reference;
     ArrayList<String> idList;
    FirebaseFirestore db;
    String intentMsg;
    ArrayList<String> sizeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Button add=findViewById(R.id.idAdd);
        idNameText=findViewById(R.id.idName);
        ImageView refresh=findViewById(R.id.idRefresh);
        sizeText=findViewById(R.id.idSize);
        idList=new ArrayList<>();
        sizeList=new ArrayList<>();

        ///////////intrnt
        Intent intent=getIntent();
        intentMsg=intent.getStringExtra("itemName");
        getSupportActionBar().setTitle(intentMsg+" ->Id List");
        db=FirebaseFirestore.getInstance();
        final ProgressBar progressBar=findViewById(R.id.progressBar);
        reference=db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid()).document("list3-"+intentMsg);
        /////////////////recycler
        recyclerView=findViewById(R.id.idRecyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //final String[] id = {"birla birsanskar", "wallmax wallmax", "kajariassssssssssss", "ssss", "ddddd", "dssss", "ghgj", "jhhjjh", "hjhggh"};
       // final String[] size={"111111111111jkjhhjkjkhkjk","2","3","4","4","dssss","ghgj","8","9"};
       // recyclerView.setAdapter(new RecyclerViewAdapter2(id,size,this));
        ///////////refresh

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                reference.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists())
                                {
                                    int i=0;
                                    ArrayList<String> notes=new ArrayList<>();
                                    idList.clear();
                                    sizeList.clear();
                                    while(documentSnapshot.get(Integer.toString(i))!=null)
                                    {
                                        Object msg= documentSnapshot.get(Integer.toString(i));
                                        idList.add(msg.toString());
                                        i++;
                                    }
                                    i=0;
                                    while(documentSnapshot.get(Integer.toString(i)+"s")!=null)
                                    {
                                        Object msg= documentSnapshot.get(Integer.toString(i)+"s");
                                        sizeList.add(msg.toString());
                                        i++;
                                    }
                                    Toast.makeText(IdActivity.this, "Refreshed!", Toast.LENGTH_SHORT).show();
                                    recyclerView.setAdapter(new RecyclerViewAdapter2(intentMsg,idList,sizeList,IdActivity.this));

                                    // arrayAdapter.notifyDataSetChanged();

                                }else{
                                    Toast.makeText(IdActivity.this, "List not exists!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(IdActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        reference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                        {
                            int i=0;
                            ArrayList<String> notes=new ArrayList<>();
                            idList.clear();
                            sizeList.clear();
                            while(documentSnapshot.get(Integer.toString(i))!=null)
                            {
                                Object msg= documentSnapshot.get(Integer.toString(i));
                                idList.add(msg.toString());
                                i++;
                            }
                            i=0;
                            while(documentSnapshot.get(Integer.toString(i)+"s")!=null)
                            {
                                Object msg= documentSnapshot.get(Integer.toString(i)+"s");
                                sizeList.add(msg.toString());
                                i++;
                            }

                            recyclerView.setAdapter(new RecyclerViewAdapter2(intentMsg,idList,sizeList,IdActivity.this));

                            // arrayAdapter.notifyDataSetChanged();

                        }else{
                            Toast.makeText(IdActivity.this, "List not exists!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(IdActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        progressBar.setVisibility(View.INVISIBLE);

        /////////////add
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String idNameString=idNameText.getText().toString();
                final String sizeString= sizeText.getText().toString();
                if(idNameString.isEmpty())
                {
                    idNameText.setError("Can not be empty!");
                }else if(sizeString.isEmpty())
                {
                    sizeText.setError("Can not be empty!");
                }
                else if(idList.contains(idNameString)&&sizeList.contains(sizeString))
                {
                    Toast.makeText(IdActivity.this, "Already contains "+idNameString+" with "+sizeString, Toast.LENGTH_SHORT).show();
                }
                else{
                    sizeList.add(sizeString);
                    idList.add(idNameString);
                    String[] temp1=new String[sizeList.size()];

                    recyclerView.setAdapter(new RecyclerViewAdapter2(intentMsg,idList,sizeList,IdActivity.this));
                    Map<String,String> map=new HashMap<>();
                    for(int i=0;i<idList.size();i++) {
                        map.put(Integer.toString(i), idList.get(i));
                    }
                    for(int i=0;i<sizeList.size();i++) {
                        map.put(Integer.toString(i)+"s", sizeList.get(i));
                    }

                        db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid()).document("list3-"+intentMsg).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Snackbar.make(v, idNameString+"/"+sizeString+" added!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    // Toast.makeText(getActivity(), "successfuly added!", Toast.LENGTH_SHORT).show();
                                    // add1.setEnabled(true);
                                    // progressBar.setVisibility(View.INVISIBLE);

                                    getWindow().setSoftInputMode(
                                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
        ////////////////
        //////////////////on create end
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem item=menu.findItem(R.id.search);
        SearchView searchView=(SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               // int idSize=idList.size();
                //int sizeSize=sizeList.size();
                ArrayList<String> newId =new ArrayList<>();
                ArrayList<String> newSize =new ArrayList<>();
                //int[] size=new int[newId.length];
                for(int j=0;j<idList.size();j++)
                {
                    if(idList.get(j).contains(newText)==true)
                    {
                        newId.add(idList.get(j));
                        newSize.add(sizeList.get(j));
                    }
                }
                //String[] temp1=new String[newSize.size()];

                recyclerView.setAdapter(new RecyclerViewAdapter2(intentMsg,newId,newSize,IdActivity.this));
                return true;
            }
        });
           return  super.onCreateOptionsMenu(menu);
    }
}