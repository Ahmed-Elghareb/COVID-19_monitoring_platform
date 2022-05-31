package com.example.corona;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView first = (ImageView) findViewById(R.id.main) ;

        Button button = findViewById(R.id.button);
        Button button1 = findViewById(R.id.button2);
        EditText Texbox = findViewById(R.id.Textbox);
        TextView txtview = findViewById(R.id.textView2);


        button.setOnClickListener(v -> {
            String countryname= Texbox.getText().toString();
            if (countryname.equals("")){ txtview.setText("Please Enter A Country Name !");} else {
        Intent intent = new Intent(this, Main2Activity.class);
                txtview.setText("");


            intent.putExtra("Country", countryname);
        startActivity((intent));
            }

        });

        button1.setOnClickListener(v -> {
            String countryname= Texbox.getText().toString();

            if (countryname.equals("")){ txtview.setText("Please Enter A Country Name !");} else {
                Intent intent = new Intent(this, Main3Activity.class);


                intent.putExtra("Country", countryname);
                startActivity((intent));

            }
        });


    }
}
