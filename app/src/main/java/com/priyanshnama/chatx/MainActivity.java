package com.priyanshnama.chatx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private LinearLayout phone, code;
    private Button sendCode, verify;
    private EditText phoneNumber, verifyCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        userIsLoggedIn();

        phone = findViewById(R.id.phone);
        code = findViewById(R.id.code);
        sendCode = findViewById(R.id.sendCode);
        verify = findViewById(R.id.verify);
        phoneNumber = findViewById(R.id.phoneNumber);
        verifyCode = findViewById(R.id.verifyCode);

        sendCode.setOnClickListener(v -> startPhoneAuth());

        callBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                SignInWithPhoneCredentials(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                revertBack();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                phone.setVisibility(View.INVISIBLE);
                code.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                revertBack();
            }
        };
    }

    private void revertBack() {
        phone.setVisibility(View.VISIBLE);
        code.setVisibility(View.INVISIBLE);
        Toast.makeText(this,"Verification Failed Please Try Again",Toast.LENGTH_LONG).show();
    }

    private void SignInWithPhoneCredentials(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, task -> {
            if(task.isSuccessful())
                userIsLoggedIn();
        });
    }

    private void userIsLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            startActivity(new Intent(this,MainPageActivity.class));
            finish();
        }
    }

    private void startPhoneAuth() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber.getText().toString(),
                60,
                TimeUnit.SECONDS,
                this,
                callBacks);
    }
}