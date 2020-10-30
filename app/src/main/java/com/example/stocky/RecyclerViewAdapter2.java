package com.example.stocky;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.ViewHolder> {
    private ArrayList<String> companyStr;
    private  ArrayList<String> serialStr;
    private Context context;
    private String intentMsg;
    private String intentMsgId;
    private String intentMsgSize;

    public RecyclerViewAdapter2(String intentMsg, ArrayList<String> companyStr, ArrayList<String> serialStr, Context context) {
        this.companyStr = companyStr;
        this.serialStr = serialStr;
        this.context = context;
        this.intentMsg=intentMsg;

    }

    @NonNull
    @Override
    public RecyclerViewAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.id_recycler_view,parent,false);

        return new RecyclerViewAdapter2.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter2.ViewHolder holder, final int position) {
        String company=companyStr.get(position);
        String serial=serialStr.get(position);
        holder.company.setText(company);
        holder.serial.setText(serial);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,DescriptionActivity.class);
                intent.putExtra("intentMsg",intentMsg);
                intent.putExtra("intentMsgId",companyStr.get(position));
                intent.putExtra("intentMsgSize",serialStr.get(position));
                context.startActivity(intent);
                //Toast.makeText(context, "okokok", Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, "oko", Toast.LENGTH_SHORT).show();
                //context.startActivity(new Intent(context,MainActivity5.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return companyStr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView company;
        TextView serial;
        View view;
        LinearLayout linearLayout;
        public ViewHolder(View itemView){
            super(itemView);
            company=itemView.findViewById(R.id.idNameRecycler);
            serial=itemView.findViewById(R.id.idSizeRecycler);
            linearLayout=itemView.findViewById(R.id.idLayout);
            view=itemView.findViewById(R.id.idLayout);
            view.setOnLongClickListener( this);

        }

        @Override
        public boolean onLongClick(View v) {
            final int position=getAdapterPosition();
           // Toast.makeText(context, companyStr.get(position), Toast.LENGTH_SHORT).show();
            //////////////dialog
            new AlertDialog.Builder(context)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("are you sure?")
                    .setMessage("Do you want to delete the "+companyStr.get(position).toUpperCase()+"?")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final String deleteId=companyStr.get(position);
                            final String deleteSize=serialStr.get(position);

                            companyStr.remove(position);
                            serialStr.remove(position);
                            notifyItemRemoved(position);
                            ///////////update firestore

                            Map<String,Object> map=new HashMap<>();
                            for(i=0;i<companyStr.size();i++) {
                                map.put(Integer.toString(i), companyStr.get(i));
                            }
                            for(i=0;i<serialStr.size();i++) {
                                map.put(Integer.toString(i)+"s", serialStr.get(i));
                            }
                            DocumentReference reference= FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid()).document("list3-"+intentMsg);

                            reference.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        // Toast.makeText(getActivity(), "successfuly deleted!", Toast.LENGTH_SHORT).show();
                                        //  Snackbar.make(v, delete+" deleted", Snackbar.LENGTH_LONG)
                                        //  .setAction("Action", null).show();
                                        Toast.makeText(context, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });





                            //////////////update
                        }
                    })
                    .setNegativeButton("No",null)
                    .show();
            return true;
            //////////////dialog ends
            //// companyStr.remove(position);
            // notifyItemRemoved(position);
        }
    }
}
