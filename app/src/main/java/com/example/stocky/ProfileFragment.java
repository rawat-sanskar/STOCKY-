package com.example.stocky;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profile,container,false);
        final TextView name=view.findViewById(R.id.profile_name);
        final TextView email=view.findViewById(R.id.profileEmail);
        final TextView mobile=view.findViewById(R.id.profileMobile);
        final TextView id=view.findViewById(R.id.profileId);
        id.setText("Unique id->  "+FirebaseAuth.getInstance().getCurrentUser().getUid());
        final TextView password=view.findViewById(R.id.profilePassword);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                ArrayList<String> pro =new ArrayList<>();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    // Log.d("msg","////@@@@@@@@@@@@@@@@@@@@@@@//////////////"+dataSnapshot.getChildren().toString()+" ////  "+dataSnapshot.getKey().toString());

                    pro.add(dataSnapshot.getValue().toString());
                }
                name.setText(pro.get(3));
                password.setText("Password->  "+pro.get(4));
                mobile.setText("Mobile No.->  "+pro.get(2));
                email.setText("Email->  "+pro.get(0));
                // Log.d("arraylist","/////!!!!!!!!!!!11"+pro.get(0)+pro.get(1)+"***************");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
