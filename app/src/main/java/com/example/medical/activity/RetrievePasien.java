package com.example.medical.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.medical.R;
import com.example.medical.adapter.UserAdapterPasien;
import com.example.medical.model.User;
import com.example.medical.session.MyPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RetrievePasien extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapterPasien userAdapter;
    private List<User> userList;
    private ImageView img_back, btnLogout;
    private TextView userLogin, lvlUser;
    private User user;
    private ProgressBar mProgressCircle;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_pasien);

        userList = new ArrayList<User>();
        mProgressCircle = findViewById(R.id.progress_circle);
        searchView = findViewById(R.id.searchView);
        lvlUser = findViewById(R.id.lvlUser);
        img_back = findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lvlUser.setText(MyPreferences.getSharedPreferences()
                .getString(MyPreferences.STATUS, "status"));

        init();
    }

    protected void onStart(){
        super.onStart();
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }
    }

    private void init() {
        userLogin = findViewById(R.id.username);
        /*
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), btnLogout);
                popupMenu.inflate(R.menu.menu_item);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.action_logout:
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();
                                return true;
                        }

                        return false;
                    }
                });
            }
        });
        */
        recyclerView = findViewById(R.id.rvDokter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        userList = new ArrayList<>();
        readUsers();
    }

    private void search(String str)
    {
        ArrayList<User> myList = new ArrayList<>();
        for (User object : userList)
        {
            if(object.getNamaLengkap().toLowerCase().contains(str.toLowerCase()))
            {
                myList.add(object);
            }
        }
        userAdapter = new UserAdapterPasien(RetrievePasien.this, myList);
        recyclerView.setAdapter(userAdapter);
    }

    private void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        String level = lvlUser.getText().toString();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (!user.getId().equals(firebaseUser.getUid())) {
                        userList.add(user);
                    }

                    else if (!user.getId().equals(level)){
                        userList.add(user);
                    }
                }

                userAdapter = new UserAdapterPasien(getApplicationContext(), userList);
                recyclerView.setAdapter(userAdapter);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }
}