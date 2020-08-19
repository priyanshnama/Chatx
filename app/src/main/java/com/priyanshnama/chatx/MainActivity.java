package com.priyanshnama.chatx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
    private LinearLayout phone, verify, account;
    private EditText phoneNumber;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callBacks;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        SharedPreferences sharedPreferences = getSharedPreferences("account_status", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

        String account_status = sharedPreferences.getString("account_status", "yes");
        assert account_status != null;
        if(!account_status.equals("no")) userIsLoggedIn();

        phone = findViewById(R.id.phone);
        verify = findViewById(R.id.verify);
        account = findViewById(R.id.account);
        account.setVisibility(View.INVISIBLE);
        phoneNumber = findViewById(R.id.phoneNumber);

        findViewById(R.id.sendCode).setOnClickListener(v -> startPhoneAuth());
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
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                revertBack();
            }
        };
    }

    private void revertBack() {
        phone.setVisibility(View.VISIBLE);
        verify.setVisibility(View.INVISIBLE);
        Toast.makeText(this,"Verification Failed Please Try Again",Toast.LENGTH_LONG).show();
    }

    private void SignInWithPhoneCredentials(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()) {
                userIsLoggedIn();
            }
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
        phone.setVisibility(View.INVISIBLE);
        verify.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber.getText().toString(),
                60,
                TimeUnit.SECONDS,
                this,
                callBacks);
    }

    private boolean accounyCreated(){
        editor.putString("account_status","yes");
        editor.commit();
        return true;
    }
}