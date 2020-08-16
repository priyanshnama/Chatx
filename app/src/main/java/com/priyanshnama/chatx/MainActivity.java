package com.priyanshnama.chatx;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private LinearLayout phone, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phone = findViewById(R.id.phone);
        code = findViewById(R.id.code);
    }


    public void sendCode(View view) {
        phone.setVisibility(View.INVISIBLE);
        code.setVisibility(View.VISIBLE);
    }

    public void verifyCode(View view) {
        phone.setVisibility(View.VISIBLE);
        code.setVisibility(View.INVISIBLE);
    }
}