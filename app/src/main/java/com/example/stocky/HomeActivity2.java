package com.example.stocky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity2 extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    //MainFragment mainFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
   // String intentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
       // Intent intent=getIntent();
       // intentUser=intent.getStringExtra("intentUser");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Dashboard");
        drawer = findViewById(R.id.drawer);
       // NavigationView navView=findViewById(R.id.drawer_h)
        navigationView = findViewById(R.id.nested);
        toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        View headerView=navigationView.getHeaderView(0);
        final TextView headerName=headerView.findViewById(R.id.header_name);
        final TextView headerEmail=headerView.findViewById(R.id.textView5);
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
               headerName.setText(pro.get(3));
               headerEmail.setText(pro.get(0));
              // Log.d("arraylist","/////!!!!!!!!!!!11"+pro.get(0)+pro.get(1)+"***************");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

       // headerName.setText("your name");

        // mainFragment = new MainFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment,new HomeFragment());
        fragmentTransaction.commit();// add the fragment


        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        int id=item.getItemId();
        switch (id)
        {
            case R.id.home:
                //Bundle bundle = new Bundle();
                //bundle.putString("msg", intentUser);
                   // set Fragmentclass Arguments
               // HomeFragment fragobj = new HomeFragment();
               // fragobj.setArguments(bundle);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment,new HomeFragment());
                fragmentTransaction.commit();
                break;
            case  R.id.profile: fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment,new ProfileFragment());
                fragmentTransaction.commit();
                break;
            case  R.id.about:fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment,new AboutFragment());
                fragmentTransaction.commit();
                break;
            case  R.id.rating:fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment,new RatingFragment());
                fragmentTransaction.commit();
                break;
            case  R.id.helpdesk:
                Toast.makeText(this, "Contact id-> rawat.sanskar00@gmail.com", Toast.LENGTH_LONG).show();
                break;
            case  R.id.share:
                Toast.makeText(this, "No Need To Share!", Toast.LENGTH_LONG).show();
                break;
            case  R.id.logout: FirebaseAuth.getInstance().signOut();
                               Toast.makeText(this, "Logout successful!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(HomeActivity2.this,MainActivity.class));
                                finish();
                                 break;
        }
        return true;
    }
}