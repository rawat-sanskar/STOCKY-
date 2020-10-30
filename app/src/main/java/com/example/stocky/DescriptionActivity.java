package com.example.stocky;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DescriptionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    TextView id,size,quantity,description,update,buy,sell,last;
    EditText descriptionEdit,buyEdit,sellEdit,sizeEdit,idEdit;
    Button save,edit,refresh;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    DocumentReference reference;
    ProgressBar progressBar;
    private Uri mImageUri;
    ImageView image;
    private  static final  int PICK_IMAGE_REQUEST=1;
    private  static final String KEY_ID="id";
    private  static  final String KEY_SIZE="size";
    private  static final String KEY_QUANTITY="quantity";

    private  static final String KEY_DESCRIPTION="description";
    private  static final String KEY_UPDATE="update";
    private  static final String KEY_FSELL="fBuy";
    private  static final String KEY_FBUY="fSell";
    ImageView fView,fAdd;
    String intentMsgId;
    String intentMsgSize;
    HashMap<String,Object> map;
    String dropText;
    TextView dropList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        getSupportActionBar().setTitle("STOCKY-Let's Manage Stock");
        db=FirebaseFirestore.getInstance();
        fView=findViewById(R.id.fView);
        dropList=findViewById(R.id.dropList);
        //image=findViewById(R.id.imageView5);
        fAdd=findViewById(R.id.fAdd);
        Intent intent=getIntent();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        String intentMsg=intent.getStringExtra("intentMsg");
        intentMsgId=intent.getStringExtra("intentMsgId");
        intentMsgSize=intent.getStringExtra("intentMsgSize");
        reference=db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid()).document("list4-"+intentMsg+"-"+intentMsgId+"-"+intentMsgSize);
        final Spinner spinner=findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.drop,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        {
            last=findViewById(R.id.fLast);
            buy=findViewById(R.id.finalBuy);
            sell=findViewById(R.id.finalSell);
            buyEdit=findViewById(R.id.fBuy);
            sellEdit=findViewById(R.id.fSell);
            progressBar=findViewById(R.id.fProgressBar);
            refresh=findViewById(R.id.fRefresh);
            id = findViewById(R.id.fId);
            size = findViewById(R.id.fSize);
           quantity = findViewById(R.id.fQuantity);
            description = findViewById(R.id.fDescription);
            idEdit = findViewById(R.id.fIdEdit);
            sizeEdit = findViewById(R.id.fSizeEdit);
           // quantityEdit = findViewById(R.id.fQuantityEdit);
            descriptionEdit = findViewById(R.id.fDescriptionEdit);
            update = findViewById(R.id.finalUpdate);
            save = findViewById(R.id.fSave);
            edit = findViewById(R.id.fEdit);
            id.setText(intentMsgId);
            size.setText(intentMsgSize);
            TextView fIntent=findViewById(R.id.fIntent);
            fIntent.setText(intentMsg);
             map= new HashMap<>();

        }
        {
            progressBar.setVisibility(View.VISIBLE);
            reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists())
                    {
                        quantity.setText(documentSnapshot.getString(KEY_QUANTITY));
                        update.setText(documentSnapshot.getString(KEY_UPDATE));
                        description.setText(documentSnapshot.getString(KEY_DESCRIPTION));
                        last.setText("Last Edit-> buy="+documentSnapshot.getString(KEY_FBUY)+",sell="+documentSnapshot.getString(KEY_FSELL));
                       // Toast.makeText(DescriptionActivity.this, "Refreshed!", Toast.LENGTH_SHORT).show();

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
            progressBar.setVisibility(View.INVISIBLE);
        }
        {
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buyEdit.setText("0");
                    sellEdit.setText("0");
                    dropList.setVisibility(View.INVISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                    fAdd.setVisibility(View.INVISIBLE);
                    fView.setVisibility(View.INVISIBLE);
                    buyEdit.setVisibility(View.VISIBLE);
                    sellEdit.setVisibility(View.VISIBLE);
                    buy.setVisibility(View.VISIBLE);
                    sell.setVisibility(View.VISIBLE);
                    // id.setVisibility(View.INVISIBLE);
                    //size.setVisibility(View.INVISIBLE);
                    refresh.setVisibility(View.INVISIBLE);
                    //quantity.setVisibility(View.INVISIBLE);
                    description.setVisibility(View.INVISIBLE);
                    // idEdit.setVisibility(View.VISIBLE);
                    // sizeEdit.setVisibility(View.VISIBLE);
                   // quantityEdit.setVisibility(View.VISIBLE);
                    descriptionEdit.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.INVISIBLE);
                    save.setVisibility(View.VISIBLE);
                    //quantityEdit.setText(quantity.getText().toString());
                    descriptionEdit.setText(description.getText().toString());
                }
            });
        }
        {
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (idEdit.getText().toString().isEmpty() || sizeEdit.getText().toString().isEmpty()  || descriptionEdit.getText().toString().isEmpty()) {
                        Toast.makeText(DescriptionActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                    } else {
                        String fBuy,fSell;
                        fBuy=buyEdit.getText().toString();
                        fSell=sellEdit.getText().toString();
                        if(fBuy=="")
                        {
                           fBuy="0";
                        }
                        if(fSell=="")
                        {
                            fSell="0";
                        }
                        Calendar calendar= Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MMM-yyyy  hh:mm:ss a");
                        String dateTime="Last Updated ->"+simpleDateFormat.format(calendar.getTime());
                        update.setText(dateTime);
                      //  spinner.setOnItemSelectedListener(this);
                        spinner.setVisibility(View.INVISIBLE);
                        dropList.setVisibility(View.VISIBLE);
                        buyEdit.setVisibility(View.INVISIBLE);
                        sellEdit.setVisibility(View.INVISIBLE);
                        buy.setVisibility(View.INVISIBLE);
                        fAdd.setVisibility(View.VISIBLE);
                        fView.setVisibility(View.VISIBLE);
                        sell.setVisibility(View.INVISIBLE);
                        quantity.setVisibility(View.VISIBLE);
                        refresh.setVisibility(View.VISIBLE);
                        description.setVisibility(View.VISIBLE);
                        // idEdit.setVisibility(View.INVISIBLE);
                        //sizeEdit.setVisibility(View.INVISIBLE);
                        //quantityEdit.setVisibility(View.INVISIBLE);
                        descriptionEdit.setVisibility(View.INVISIBLE);
                        edit.setVisibility(View.INVISIBLE);
                        save.setVisibility(View.VISIBLE);
                        int quantityInt=Integer.parseInt(quantity.getText().toString());
                        int buyInt=Integer.parseInt(fBuy);
                        int sellInt=Integer.parseInt(fSell);
                        last.setText("Last Edit-> buy="+buyInt+",sell="+sellInt);
                        quantity.setText(String.valueOf(quantityInt+buyInt-sellInt));
                        description.setText(descriptionEdit.getText().toString());
                        edit.setVisibility(View.VISIBLE);
                        save.setVisibility(View.INVISIBLE);

                        map.put(KEY_ID,id.getText().toString());
                        map.put(KEY_SIZE,size.getText().toString());
                        map.put(KEY_QUANTITY,quantity.getText().toString());
                        map.put(KEY_DESCRIPTION,description.getText().toString());
                        map.put(KEY_UPDATE,dateTime);
                        map.put(KEY_FBUY,fBuy);
                        map.put(KEY_FSELL,fSell);

                        reference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Snackbar.make(v, "saved!", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(v, e.getMessage().toString(), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });
                    }
                }
            });
        }
        {
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                quantity.setText(documentSnapshot.getString(KEY_QUANTITY));
                                update.setText(documentSnapshot.getString(KEY_UPDATE));
                                description.setText(documentSnapshot.getString(KEY_DESCRIPTION));
                                last.setText("Last Edit-> buy=" + documentSnapshot.getString(KEY_FBUY) + ",sell=" + documentSnapshot.getString(KEY_FSELL));
                                Toast.makeText(DescriptionActivity.this, "Refreshed!", Toast.LENGTH_SHORT).show();

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DescriptionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
        //Adding image
        {
            fAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   openFileChooser();
                 //  uploadImage();
                }
            });
            fView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(DescriptionActivity.this,ImageActivity.class);
                    intent.putExtra("imageName",intentMsgId+intentMsgSize);
                    startActivity(intent);
                }
            });
        }
    }

    private void openFileChooser() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
           mImageUri=data.getData();
          //  Bitmap bitmap= null;
            try {
              Bitmap  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),mImageUri);
                // fView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if(mImageUri!=null){
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("Adding Image...");
            progressDialog.show();
            StorageReference reference2=storageReference.child(intentMsgId+intentMsgSize);
            reference2.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(DescriptionActivity.this, "Added!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Added"+(int)progress+"%");
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        dropText=parent.getItemAtPosition(position).toString();
        dropList.setText(dropText);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    /*private  void uploadImage(){
        if(mImageUri!=null){
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference reference2=storageReference.child(intentMsgId+intentMsgSize);
            reference2.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(DescriptionActivity.this, "uploaded!!!!!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                     double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                     progressDialog.setMessage("uploaded"+(int)progress+"%");
                }
            });
        }
    }

     */
}