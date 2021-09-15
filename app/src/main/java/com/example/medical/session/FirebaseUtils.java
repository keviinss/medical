package com.example.medical.session;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtils {
    public static final String ACCOUNTS_PATH = "Users";

    private static final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public static DatabaseReference getReference(String path){
        return firebaseDatabase.getReference(path);
    }
}
