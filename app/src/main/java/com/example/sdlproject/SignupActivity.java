package com.example.sdlproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText user_edit, pass_edit, repass_edit;
    private Button signup;
    FirebaseAuth firebaseAuth;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        user_edit = (EditText) findViewById(R.id.s_username);
        pass_edit = (EditText) findViewById(R.id.s_password);
        repass_edit = (EditText) findViewById(R.id.s_repassword);
        signup = (Button) findViewById(R.id.signup);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        firebaseAuth = FirebaseAuth.getInstance();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = user_edit.getText().toString();
                String type = null;

                if(radioButton.getText().equals("Buyer"))
                    type = "Buyer";
                else
                    type = "Seller";

                boolean flag = validation();
                if(flag==true){
                    final String finalType = type;
                    firebaseAuth.createUserWithEmailAndPassword(user_edit.getText().toString(),
                            pass_edit.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this, "Registered Succsfully",
                                                Toast.LENGTH_LONG).show();
                                        users ob = new users(email, finalType);
                                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(ob).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                    Toast.makeText(SignupActivity.this, "Signed up as " + finalType, Toast.LENGTH_SHORT).show();
                                                else
                                                    Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                            }
                                        });
                                    } else {
                                        Toast.makeText(SignupActivity.this, task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                }
            }
        });
    }

    public void checkbutton(View view){

        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

        Toast.makeText(SignupActivity.this, "Selected user is" + radioButton.getText(), Toast. LENGTH_SHORT).show();

    }

    private boolean validation()
    {
        boolean isValid = true;
        if(user_edit.getText().toString().isEmpty())
        {
            user_edit.setError("Username Cannot be Empty!");
            isValid = false;
        }

        if(pass_edit.getText().toString().isEmpty()) {
            pass_edit.setError("Password cannot be Empty!");
            isValid = false;
        }

        if(!pass_edit.getText().toString().equals(repass_edit.getText().toString())) {
            isValid = false;
            repass_edit.setError("Password not matched");
        }

        return isValid;
    }


}
