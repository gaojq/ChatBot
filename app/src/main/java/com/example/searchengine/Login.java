package com.example.searchengine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class Login extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    TextView changeSignup;
    Boolean signupActive = true;
    EditText password;


    public void showUserList(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()== R.id.changeSignup){
            Button signupButton = (Button) findViewById(R.id.signupButton);
            if(signupActive){

                signupActive = false;
                signupButton.setText("Login");
                changeSignup.setText("Or, Signup");

            }else{
                signupActive = true;
                signupButton.setText("Signup");
                changeSignup.setText("Or, Login");

            }
            Log.i("AppInfo","Change into Sign up");
        }else if(view.getId() == R.id.background || view.getId() == R.id.imageView){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void signUp(View view){

        EditText username = (EditText) findViewById(R.id.usernameText);
        password = (EditText) findViewById(R.id.passwordText);

        if(username.getText().toString().matches("") || password.getText().toString().matches("")){
            Toast.makeText(this, "A username and Pssword is required", Toast.LENGTH_SHORT).show();
        }else{

            if(signupActive) {
                ParseUser user = new ParseUser();
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("SignUp", "Success");
                            showUserList();
                        } else {
                            Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if(parseUser != null) {
                            Log.i("Sign up","Login successful");
                            showUserList();
                        }

                        else Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        changeSignup = (TextView) findViewById(R.id.changeSignup);

        changeSignup.setOnClickListener(this);

        password = (EditText) findViewById(R.id.passwordText);

        password.setOnKeyListener(this);

        ConstraintLayout background = (ConstraintLayout) findViewById(R.id.background);

        ImageView image = (ImageView) findViewById(R.id.imageView);

        background.setOnClickListener(this);

        image.setOnClickListener(this);

        if(ParseUser.getCurrentUser()!= null){
            showUserList();
        }



    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == event.getAction()){

            signUp(v);

        }

        return false;
    }

}


