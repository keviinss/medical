package com.example.medical.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medical.R;
import com.example.medical.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.medical.activity.SplashScreenActivity.setWindowFlag;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    private ArrayList<User> users;
    private TextView btnLogin;
    private Button btnRegister;
    private EditText password, namaLengkap, imageURL, email;
    private Spinner levelUser;
    ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        //register();
        fullscreen();
    }

    private void clearEdittext() {
        namaLengkap.requestFocus();
        namaLengkap.setText("");
        //username.setText("");
        email.setText("");
        password.setText("");
        levelUser.setSelection(0);
    }

    private void fullscreen() {
        //FullScreen
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        //Method toolbar transaparant
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void register() {
        //username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        namaLengkap = findViewById(R.id.namaLengkap);
        imageURL = findViewById(R.id.imageURL);
        levelUser = findViewById(R.id.levelUser);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id, xNamaLengkap, xEmail, xImageURL, xLeveruser, xPassword;

                id = databaseReference.push().getKey();
                xNamaLengkap = namaLengkap.getText().toString();
                xEmail = email.getText().toString();
                xImageURL = imageURL.getText().toString();
                xLeveruser = levelUser.getSelectedItem().toString();
                xPassword = password.getText().toString();

                if (xNamaLengkap.isEmpty()) {
                    namaLengkap.setError("Nama tidak boleh kosong");
                } else if (xEmail.isEmpty()) {
                    email.setError("Username tidak boleh kosong");
                } else if (xPassword.isEmpty()) {
                    password.setError("Password tidak boleh kosong");
                } else if (xLeveruser.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Wajib Di Pilih !!!", Toast.LENGTH_SHORT).show();
                } else {
                    User users = new User(id, xNamaLengkap, xEmail, xImageURL, xLeveruser, xPassword);
                    databaseReference.child(id).setValue(users);
                    Toast.makeText(RegisterActivity.this, "Berhasil melakukan pendaftaran", Toast.LENGTH_SHORT).show();
                    //Reset EditText
                    namaLengkap.requestFocus();
                    namaLengkap.setText("");
                    //username.setText("");
                    password.setText("");
                    levelUser.setSelection(0);
                    onBackPressed();
                }
            }
        });
    }


    private void init() {
        auth = FirebaseAuth.getInstance();
        //username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        namaLengkap = findViewById(R.id.namaLengkap);
        imageURL = findViewById(R.id.imageURL);
        levelUser = findViewById(R.id.levelUser);
        email = findViewById(R.id.email);
        namaLengkap.requestFocus();

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String txt_username = username.getText().toString();
                String txt_namaLengkap = namaLengkap.getText().toString();
                String txt_email = email.getText().toString();
                String txt_status = levelUser.getSelectedItem().toString();
                String txt_password = password.getText().toString();

                if  ((TextUtils.isEmpty(txt_namaLengkap)) | TextUtils.isEmpty(txt_email) | (TextUtils.isEmpty(txt_status)) |TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(getApplicationContext(), "Pastikan Data Terisi Semua", Toast.LENGTH_SHORT).show();
                } else {
                    register(txt_namaLengkap, txt_email, txt_status, txt_password);
                }
            }
        });

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void register(final String namaLengkap, String email, String status, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            //hashMap.put("username", username);
                            hashMap.put("namaLengkap", namaLengkap);
                            hashMap.put("email", email);
                            hashMap.put("status", status);
                            hashMap.put("imageURL", "default");
                            hashMap.put("password", password);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
//                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                                        startActivity(intent);
                                        onBackPressed();
                                        Toast.makeText(RegisterActivity.this, "Berhasil melakukan pendaftaran", Toast.LENGTH_SHORT).show();
                                        clearEdittext();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "Tidak bisa mendaftar dengan email atau password ini", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

}
