package com.example.priya.firebaseauthdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    TextView textView;

    Button signout;
    Button signin;

    String phonenumber=null;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView    =   (TextView)findViewById(R.id.detail);
        signin      =   (Button)findViewById(R.id.signin);
        signout     =   (Button)findViewById(R.id.signout);

        SharedPreferences preferences  = getApplicationContext().getSharedPreferences("Phonenumber", MODE_PRIVATE);
        final SharedPreferences.Editor editor=preferences.edit();


            signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(phonenumber.equals("Not Registered")) {
                        Intent intent = new Intent(MainActivity.this, PhoneAuthActivity.class);
                        startActivity(intent);
                    }
                    if(!phonenumber.equals("Not Registered")){
                        Toast.makeText(MainActivity.this,"Already signed-in :"+phonenumber,Toast.LENGTH_LONG).show();
                    }
                }
            });
            signout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!phonenumber.equals("Not Registered")) {
                        textView.setText("Not Registered");
                        phonenumber="Not Registered";
                        editor.remove("number");
                        editor.apply();
                        Toast.makeText(MainActivity.this,"Signed-out",Toast.LENGTH_LONG).show();

                    }
                    else {
                        Toast.makeText(MainActivity.this,"Not signed-in",Toast.LENGTH_LONG).show();
                    }
                }
            });



        }



    @Override
    public void onResume(){
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("Phonenumber", MODE_PRIVATE);
        if (true) {
            phonenumber= prefs.getString("number", "Not Registered");//"No name defined" is the default value.
            textView.setText(phonenumber);
        }



    }

    }