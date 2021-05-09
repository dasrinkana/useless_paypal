package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {
    private Button Login;
    private EditText Email;
    private EditText Password;
    //private TextView Info;
    private int counter = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Email = (EditText)findViewById(R.id.email);
        Password =(EditText)findViewById(R.id.password);
        Login = (Button)findViewById(R.id.button);
        //Info = (TextView)findViewById(R.id.info);

        //Info.setText("attempts remaining: 10");

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Email.getText().toString(), Password.getText().toString());
            }
        });


    }

    private void validate(String userName, String userPassword){
        if ((userName.equals("Admin")) && (userPassword.equals("1234")||(userName.equals("milian.klein@gmail.com")&&(userPassword.equals("password"))))) {
            Intent intent = new Intent(MainActivity.this, PaymentMethods.class);
            startActivity(intent);
        }else{
            counter--;

            //Info.setText("attempts remaining: " + String.valueOf(counter));

            if (counter == 0){
                Login.setEnabled(false);
            }
        }
    }
}