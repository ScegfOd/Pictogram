package com.example.pictogram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private String name;
    private String password;
    private EditText password_box;
    private EditText name_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name_box = findViewById(R.id.name);
        password_box = findViewById(R.id.password);
        if(ParseUser.getCurrentUser() != null)
            goToMainActivity();
    }


    public void login(View view) {
        nvLogin();
    }

    private void nvLogin() {
        pullText();
        ParseUser.logInInBackground(name, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null){
                    // no error, everything is fine
                    goToMainActivity();
                }else{
                    Log.e(TAG, "failed to log in", e);
                    Toast.makeText(LoginActivity.this , "Check username and/or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void pullText() {
        password = password_box.getText().toString();
        name = name_box.getText().toString();
    }

    private void goToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void signup(View view) {
        ParseUser user = new ParseUser();
        pullText();
        user.setPassword(password);
        user.setUsername(name);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(LoginActivity.this, "Sign up failed!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "sign up failed!", e);
                }else{
                    Toast.makeText(LoginActivity.this, "You're all signed up!", Toast.LENGTH_LONG).show();
                    nvLogin();
                }
            }
        });
    }
}