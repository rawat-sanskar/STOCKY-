package com.example.stocky;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import java.util.HashSet;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    FirebaseFirestore db ;
    DocumentReference reference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home,container,false);
       // String strtext = getArguments().getString("msg");
       // Log.d("ok////////////",strtext);
        final Button add1=view.findViewById(R.id.add1);
        final EditText name=view.findViewById(R.id.itemName1);
        db=FirebaseFirestore.getInstance();
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        reference=db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid()).document("list1");

        //
        final ListView listView=view.findViewById(R.id.itemList1);
        final ArrayList<String> arrayList=new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
        arrayList.add("Example note");
        //
        final ProgressBar progressBar=view.findViewById(R.id.progressBar3);
        final ImageView refresh =view.findViewById(R.id.refresh);
         /////////////////////////////////////////////
        progressBar.setVisibility(View.VISIBLE);
        reference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                        {
                            int i=0;
                            ArrayList<String> notes=new ArrayList<>();
                            arrayList.clear();
                            while(documentSnapshot.get(Integer.toString(i))!=null)
                            {
                                Object msg= documentSnapshot.get(Integer.toString(i));
                                arrayList.add(msg.toString());
                                i++;
                            }
                            arrayAdapter.notifyDataSetChanged();

                        }else{
                            Toast.makeText(getActivity(), "not exists!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        progressBar.setVisibility(View.INVISIBLE);




        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                refresh.setEnabled(false);
                reference.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists())
                                {
                                    int i=0;
                                    ArrayList<String> notes=new ArrayList<>();
                                    arrayList.clear();
                                    while(documentSnapshot.get(Integer.toString(i))!=null)
                                    {
                                        Object msg= documentSnapshot.get(Integer.toString(i));
                                        arrayList.add(msg.toString());
                                        i++;
                                    }
                                    arrayAdapter.notifyDataSetChanged();

                                }else{
                                    Toast.makeText(getActivity(), "not exists!!", Toast.LENGTH_SHORT).show();
                                }
                                refresh.setEnabled(true);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        refresh.setEnabled(true);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
              //  Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=name.getText().toString();
                if(msg.isEmpty())
                {
                    name.setError("Can not be empty!");
                }
                else if(arrayList.contains(msg))
                {
                    Toast.makeText(getActivity(), "Already contains "+msg, Toast.LENGTH_SHORT).show();
                }
                else{
                    add1.setEnabled(false);
                    refresh.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                arrayList.add(name.getText().toString());
                arrayAdapter.notifyDataSetChanged();
                Map<String,Object> map=new HashMap<>();
                for(int i=0;i<arrayList.size();i++) {
                    map.put(Integer.toString(i), arrayList.get(i));
                    db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid()).document("list1").set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                    add1.setEnabled(true);
                    refresh.setEnabled(true);
                    progressBar.setVisibility(View.INVISIBLE);
                }


            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              // Toast.makeText(getActivity(), arrayList.get(position), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(),CompanyActivity.class);
                intent.putExtra("itemName",arrayList.get(position));
                intent.putExtra("Position",String.valueOf(position));
                startActivity(intent);
              // startActivity(new Intent(getActivity(),CompanyActivity.class));
            }
        });
        ///////////////////////////////////////////////////////////////
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int itemToDelete=i;
                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("are you sure?")
                        .setMessage("Do you want to delete the "+arrayList.get(itemToDelete).toUpperCase()+"?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final String delete=arrayList.get(itemToDelete);
                                arrayList.remove(itemToDelete);
                                arrayAdapter.notifyDataSetChanged();
                                ///////////update firestore
                                Map<String,Object> map=new HashMap<>();
                                for(i=0;i<arrayList.size();i++) {
                                    map.put(Integer.toString(i), arrayList.get(i));
                                }
                                    db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid()).document("list1").set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                               // Toast.makeText(getActivity(), "successfuly deleted!", Toast.LENGTH_SHORT).show();
                                                Snackbar.make(getView(), delete+" deleted", Snackbar.LENGTH_LONG)
                                                        .setAction("Action", null).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
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


        return  view;
    }

}
