package com.example.stocky;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter1 extends RecyclerView.Adapter<RecyclerViewAdapter1.ViewHolder> {
    private ArrayList<String> companyStr;
    private  String msg;
   // private  String[] serialStr;
    private Context context;
    public RecyclerViewAdapter1(ArrayList<String> companyStr,String msg, Context context) {
        this.companyStr = companyStr;
        this.msg=msg;
        //this.serialStr = serialStr;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.company_recycler_view,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String company=companyStr.get(position);
        //String serial=serialStr[position];
        holder.company.setText(company);
        //holder.serial.setText(serial);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,IdActivity.class);
                intent.putExtra("itemName",msg+"-"+companyStr.get(position));
                context.startActivity(intent);
                //Toast.makeText(context, "oko", Toast.LENGTH_SHORT).show();
                //context.startActivity(new Intent(context,MainActivity5.class));
            }
        });
/////////////////long
        /*
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DocumentReference reference = FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid()).document("list2-" + msg);
                Toast.makeText(context, companyStr.get(position), Toast.LENGTH_SHORT).show();
                //final ArrayList<String> arrayList = (ArrayList<String>) Arrays.asList(companyStr);
                  companyStr.remove(position);
                  notifyItemRemoved(position);


                return  true;
            }
        });

         */

///////////////////lomg

    }

    @Override
    public int getItemCount() {
        return companyStr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView company;
        //TextView serial;
        View view;
        LinearLayout linearLayout;
        public ViewHolder(View itemView){
            super(itemView);
            company=itemView.findViewById(R.id.companyName);
            //serial=itemView.findViewById(R.id.companySerial);
            linearLayout=itemView.findViewById(R.id.companyLayout);
            view=itemView.findViewById(R.id.companyLayout);
            view.setOnLongClickListener(this);
        }


        @Override
        public boolean onLongClick(final View v) {
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
                            final String delete=companyStr.get(position);
                            companyStr.remove(position);
                            notifyItemRemoved(position);
                            ///////////update firestore

                            Map<String,Object> map=new HashMap<>();
                            for(i=0;i<companyStr.size();i++) {
                                map.put(Integer.toString(i), companyStr.get(i));
                            }
                            DocumentReference reference=FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid()).document("list2-"+msg);

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
