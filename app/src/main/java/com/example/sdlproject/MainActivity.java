package com.example.sdlproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText user_edit, pass_edit;
    private Button login, forgotpass;
    private TextView register;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_edit = (EditText)findViewById(R.id.username);
        pass_edit = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.loginButton);
        register = (TextView)findViewById(R.id.register);

        //forgotpass = (Button)findViewById(R.id.forgot);
        firebaseAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, SignupActivity.class));

            }

        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = validation();

                if(flag==true) {
                    firebaseAuth.signInWithEmailAndPassword(user_edit.getText().toString(),
                            pass_edit.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Logged in Succsfully",
                                                Toast.LENGTH_LONG).show();
                                        currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                        String rid = currentUser.getUid();
                                        myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(rid);

                                        myRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                String type = dataSnapshot.child("type").getValue().toString();
                                                if(type.equals("Buyer")) {
                                                    Toast.makeText(MainActivity.this, "I am a buyer", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                                }
                                                else
                                                    Toast.makeText(MainActivity.this, "I am a Seller", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

            //                          startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                                    } else {
                                        Toast.makeText(MainActivity.this, task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

//        forgotpass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, Forgot.class));
//            }
//        });


    }



    private boolean validation()
    {
        boolean isValid = true;
        if(user_edit.getText().toString().isEmpty())
        {
            user_edit.setError("Username Cannot be Empty!");
            isValid = false;
        }
        if(pass_edit.getText().toString().isEmpty())
        {
            pass_edit.setError("Password cannot be Empty!");
            isValid = false;
        }
        return isValid;
    }
}