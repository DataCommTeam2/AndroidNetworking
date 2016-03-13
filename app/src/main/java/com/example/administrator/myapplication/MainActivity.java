package com.example.administrator.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void done(View v){
        Intent intent = new Intent(this, MapActivity.class);

        EditText editText = (EditText) findViewById(R.id.name);
        String message = editText.getText().toString();
        intent.putExtra("name", message);

        editText = (EditText) findViewById(R.id.ip);
        message = editText.getText().toString();
        intent.putExtra("ip", message);

        editText = (EditText) findViewById(R.id.port);
        message = 0 + editText.getText().toString();
        intent.putExtra("port", message);

        startActivity(intent);
    }
}
