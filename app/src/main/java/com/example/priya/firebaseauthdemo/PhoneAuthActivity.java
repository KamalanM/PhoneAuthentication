package com.example.priya.firebaseauthdemo;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity {

    EditText phoneNumber;
    EditText verificationcode;

    TextView username;

    Button send_otp;
    Button back;

    String phonenumber=null;
    String mVerificationId;

    // [START declare_auth]
     private FirebaseAuth mAuth;
     // [END declare_auth]

    boolean mVerificationInProgress = false;


    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        phoneNumber      =     (EditText) findViewById(R.id.phone_number);
        verificationcode =     (EditText) findViewById(R.id.verification_code);

        username         =     (TextView) findViewById(R.id.username);

        send_otp         =     (Button) findViewById(R.id.send_otp);
        back             =     (Button) findViewById(R.id.back);

        SharedPreferences preferences  = getApplicationContext().getSharedPreferences("Phonenumber", MODE_PRIVATE);
        final SharedPreferences.Editor editor=preferences.edit();




        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.d("ATAG", "onVerificationCompleted:" + credential);
                verificationcode.setText(credential.getSmsCode());
                Toast.makeText(PhoneAuthActivity.this,"SignInWithCredential:SUCCESS",Toast.LENGTH_LONG).show();
                phonenumber=phoneNumber.getText().toString();
                back.setVisibility(View.VISIBLE);
                editor.putString("number",phonenumber);
                editor.apply();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("ATAG", "onVerificationFailed", e);
                Toast.makeText(PhoneAuthActivity.this,"onVerificationFailed",Toast.LENGTH_LONG).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                    Toast.makeText(PhoneAuthActivity.this,"Invalid Mobile Number",Toast.LENGTH_LONG).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    Toast.makeText(PhoneAuthActivity.this,"Quota over",Toast.LENGTH_LONG).show();
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("ATAG", "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };






        send_otp.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v) {

            String str=phoneNumber.getText().toString();
            if(str!=null) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + phoneNumber.getText().toString(),        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        PhoneAuthActivity.this,               // Activity (for callback binding)
                        mCallbacks);        // OnVerificationStateChangedCallbacks
            }
            else
            {
                Toast.makeText(PhoneAuthActivity.this,"Enter phonenumber",Toast.LENGTH_LONG).show();
            }

        }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(phonenumber!=null)
                {
                    Intent intent=new Intent(PhoneAuthActivity.this,MainActivity.class);
                    intent.putExtra("phone number",phonenumber);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(PhoneAuthActivity.this,"Phone Not Verified",Toast.LENGTH_SHORT).show();;
                }

            }
        });




    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d("ATAG", "signInWithCredential:success");
                           // Toast.makeText(PhoneAuthActivity.this,"SignInWithCredential:SUCCESS",Toast.LENGTH_LONG).show();

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("ATAG", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

}
