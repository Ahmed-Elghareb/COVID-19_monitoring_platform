package com.example.corona;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


import java.io.IOException;


public class Main2Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TextView txt_recovered;
        TextView txt_reported;
        TextView txt_deaths;
        TextView txt_confirmed;
        TextView txt_loc;

        Button button;


            txt_recovered = findViewById(R.id.txt_reocvered);
            txt_reported = findViewById(R.id.txt_reported);
            txt_deaths = findViewById(R.id.txt_deaths);
            txt_confirmed = findViewById(R.id.Confirmed);

            txt_loc = findViewById(R.id.txt_loc);
            button = findViewById(R.id.button);



        String countryname = getIntent().getStringExtra("Country");
        String country = "";
        if (countryname.contains(" ")) {
            String[] splitStr = countryname.trim().split("\\s+");
            String firsthalf = splitStr[0].substring(0, 1).toUpperCase() + splitStr[0].substring(1).toLowerCase();
            String secondhalf = splitStr[1].substring(0, 1).toUpperCase() + splitStr[1].substring(1).toLowerCase();
            country = firsthalf + "%20" + secondhalf;
        }
        if (countryname.length()== 2){
            country = countryname.substring(0, 1).toUpperCase() + countryname.substring(1).toUpperCase();
        }
        else {
            country = countryname.substring(0, 1).toUpperCase() + countryname.substring(1).toLowerCase();
        }

            final OkHttpClient client = new OkHttpClient();

        String finalCountry = country;
        button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

/*
                public static void main(String args[]) throws IOException {
                    System.out.println("Hello Java");
*/

                    final OkHttpClient client = new OkHttpClient();


                    Request request = new Request.Builder()
                            .url("https://covid-19-coronavirus-statistics.p.rapidapi.com/v1/total?country="+finalCountry.trim())
                            .get()
                            .addHeader("x-rapidapi-host", "covid-19-coronavirus-statistics.p.rapidapi.com")
                            .addHeader("x-rapidapi-key", "60b688dd13msh0d0c7a34fa1ffeap1d0e02jsn2904bb2675d9")
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {

                            txt_loc.setText("Failure !");
                        }

                        @Override
                        public void onResponse(final Response response) throws IOException {

                            String myresponse = response.body().string();

                            int len = myresponse.length();
                            String data = myresponse.substring(54, len).replace("\"", "");
                            //{"recovered":1075,"deaths":294,"confirmed":4092,"lastChecked":"2020-04-26T00:22:10+00:00","lastReported":"2020-04-25T06:30:33+00:00","location":"Egypt"}}

                            int dataLen = data.length();


                            String recovered = data.substring(11, data.indexOf(",deaths"));

                            String deaths = data.substring(data.indexOf(",deaths:") + 8, data.indexOf(",confirmed:"));

                            String confirmed = data.substring(data.indexOf(",confirmed:") + 11, data.indexOf(",lastChecked:"));
                            String lastchecked = data.substring(data.indexOf(",lastChecked:") + 13, data.indexOf(",lastReported:"));
                            String lastreported = data.substring(data.indexOf(",lastReported:") + 14, data.indexOf(",location:"));
                            String location = data.substring(data.indexOf(",location:") + 10, data.indexOf("}"));

                            txt_recovered.setText("Recovered : " + recovered);
                            txt_reported.setText("Last Reported : " + lastreported);
                            txt_deaths.setText("Deaths : " + deaths);
                            txt_confirmed.setText("Confirmed : " + confirmed);
                            txt_loc.setText("Location : " + location);


                        }

                    });
                }


            });

        }



    }




