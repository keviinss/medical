package com.example.medical.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medical.R;
import com.example.medical.model.User;
import com.example.medical.session.FirebaseUtils;
import com.example.medical.session.MyPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private String TAG = LoginActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private String email;
    private String password;
    private TextView btnDaftar;
    private Button btnLogin;
    private EditText IDusername, IDpassword;
    private FirebaseAuth auth;
    private List<User> userList;
    //String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //init();
        auth = FirebaseAuth.getInstance();
        login();
        fullscreen();
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

    private void login() {
        IDusername = findViewById(R.id.IDusername);
        IDpassword = findViewById(R.id.IDpassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnDaftar = findViewById(R.id.btnDaftar);
        progressDialog = new ProgressDialog(this);

        //got to Login
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //login to dashboard user
                email = IDusername.getText().toString().trim();
                password = IDpassword.getText().toString().trim();
                auth = FirebaseAuth.getInstance();

                Query query = FirebaseUtils.getReference(FirebaseUtils.ACCOUNTS_PATH).orderByKey();

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // jika Nama Pengguna kosong
                        if (email.isEmpty()) {
                            IDusername.setError("Username wajib diisi");
                            return;
                        }
                        // jika password kosong
                        else if (password.isEmpty()) {
                            IDpassword.setError("Password wajib diisi");
                            return;
                        }
                        //jika password kurang dari 6 karakter
                        else if (password.length() < 6) {
                            IDpassword.setError("Password minimal terdiri dari 6 karakter");
                        } else {

                        }
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User userData = snapshot.getValue(User.class);
                                if (userData.getEmail().equals(email) &&
                                        userData.getPassword().equals(password) && userData.getStatus().equals("Pasien")) {
                                    MyPreferences.getEditorPreferences()
                                            .putBoolean(MyPreferences.IS_LOGINPASIEN, true);

                                    //GET DATA ID USER
                                    MyPreferences.getEditorPreferences()
                                            .putString(MyPreferences.IDUSER, userData.getId());

                                    //GET DATA NAMA LENGKAP
                                    MyPreferences.getEditorPreferences()
                                            .putString(MyPreferences.NAMALENGKAP, userData.getNamaLengkap());

                                    //GET DATA EMAIL
                                    MyPreferences.getEditorPreferences()
                                            .putString(MyPreferences.EMAIL, userData.getEmail());

                                    //GET DATA PASSWORD
                                    MyPreferences.getEditorPreferences()
                                            .putString(MyPreferences.PASSWORD, userData.getPassword());

                                    //GET DATA LEVER
                                    MyPreferences.getEditorPreferences()
                                            .putString(MyPreferences.STATUS, userData.getStatus());

                                    auth.signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        //Intent intent = new Intent(getApplicationContext(), DashboardPasien.class);
                                                        //startActivity(intent);
                                                        //finish();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });

                                    progressDialog.setMessage("Mohon Tunggu");
                                    progressDialog.show();
                                    progressDialog.setCanceledOnTouchOutside(false);

                                    MyPreferences.getEditorPreferences().commit();
                                    Intent intent = new Intent(LoginActivity.this, DashboardPasien.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(LoginActivity.this, "Selamat Datang " + userData.getNamaLengkap(), Toast.LENGTH_SHORT).show();

                                } else if (userData.getEmail().equals(email) &&
                                        userData.getPassword().equals(password) && userData.getStatus().equals("Dokter")) {
                                    MyPreferences.getEditorPreferences()
                                            .putBoolean(MyPreferences.IS_LOGINDOKTER, true);

                                    //GET DATA ID USER
                                    MyPreferences.getEditorPreferences()
                                            .putString(MyPreferences.IDUSER, userData.getId());

                                    //GET DATA NAMA LENGKAP
                                    MyPreferences.getEditorPreferences()
                                            .putString(MyPreferences.NAMALENGKAP, userData.getNamaLengkap());

                                    //GET DATA EMAIL
                                    MyPreferences.getEditorPreferences()
                                            .putString(MyPreferences.EMAIL, userData.getEmail());

                                    //GET DATA PASSWORD
                                    MyPreferences.getEditorPreferences()
                                            .putString(MyPreferences.PASSWORD, userData.getPassword());

                                    //GET DATA LEVER
                                    MyPreferences.getEditorPreferences()
                                            .putString(MyPreferences.STATUS, userData.getStatus());

                                    auth.signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        //Intent intent = new Intent(getApplicationContext(), DashboardDokter.class);
                                                        //startActivity(intent);
                                                        //finish();
                                                    } else {
                                                        //Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });

                                    progressDialog.setMessage("Mohon Tunggu");
                                    progressDialog.show();
                                    progressDialog.setCanceledOnTouchOutside(false);

                                    MyPreferences.getEditorPreferences().commit();
                                    Intent intent = new Intent(LoginActivity.this, DashboardDokter.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(LoginActivity.this, "Selamat Datang " + userData.getNamaLengkap(), Toast.LENGTH_SHORT).show();

                                } else {
                                    //Toast.makeText(LoginActivity.this, "NIK dan Password belum terdaftar", Toast.LENGTH_SHORT).show();
                                    //progressDialog.dismiss();
                                }
                            }
                        } else {
                            //Toast.makeText(login.this, "Nama Pengguna dan Password belum terdaftar", Toast.LENGTH_SHORT).show();
                            //progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, databaseError.getDetails() + " | " + databaseError.getMessage());
                    }
                });
            }
        });


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


    private void init() {
        auth = FirebaseAuth.getInstance();
        IDusername = findViewById(R.id.IDusername);
        IDpassword = findViewById(R.id.IDpassword);
        btnDaftar = findViewById(R.id.btnDaftar);
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = IDusername.getText().toString();
                String txt_password = IDpassword.getText().toString();


                if (TextUtils.isEmpty(txt_email) | TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(getApplicationContext(), "Pastikan Email dan Password terisi", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(getApplicationContext(), DashboardDokter.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });

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


}