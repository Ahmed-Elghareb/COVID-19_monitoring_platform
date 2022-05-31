package com.example.corona;


import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.log;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        TextView txt_R0;
        Button cumu ;
        Button daily ;
        Button log ;
        txt_R0 = findViewById(R.id.txt_R);
        cumu = findViewById(R.id.cumu);
        daily = findViewById(R.id.daily);
        log = findViewById(R.id.log);

        String countryname = getIntent().getStringExtra("Country").trim();
        if (countryname.contains(" ")){

            countryname = countryname.replace(" ", "-");

        }


        final OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url("https://api.covid19api.com/total/dayone/country/"+countryname+"/status/confirmed")
                .method("GET", null)
                .build();




        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                txt_R0.setText("Failure!");
            }

            @Override
            public void onResponse(final Response response) throws IOException {

                String myresponse = response.body().string();

                String substring = myresponse.replace("\"", "").trim().substring(1);
                String newline = System.getProperty("line.separator");
                boolean hasNewline = substring.contains(newline);
                String substring1;
                if (hasNewline){
                    substring = substring.replace("\n ", "").trim();
                    substring = substring.replace(" ", "").trim();
                }
                     substring1 = substring.substring(0, substring.length() - 1);
                    //int indxcase = substring.indexOf("Cases"); // + 5
                    //int indxstatus = substring.indexOf(",Status");


               // Map<String, Integer> myMap = new HashMap<String, Integer>();
                String[] pairs = substring1.split(",");

                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();


                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");

                graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {


                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {
                            return sdf.format(new Date((long) value));
                        }
                        else {
                            return super.formatLabel(value, isValueX);
                    }
                }
                });


                if (substring1.equals("message:Not Found")){
                    txt_R0.setText("Country Not Found !");
                }

                else {
                    Integer val = 0;
                    String datekey = "";
                    // int numberdays = 0;
                    int firstcase = 0;
                    for (int i = 0; i < pairs.length; i++) {
                        String pair = pairs[i];

                        String[] keyValue = pair.split(":");
                        String key = keyValue[0];

                        if (key.equals("Cases")) {
                            val = Integer.valueOf(keyValue[1]);
                            if (i <= 10) {
                                firstcase = val;
                            }
                        }
                        if (key.equals("Date")) {
                            //if (i <= 10 ){firstdate = val ;}
                            //numberdays =1 + numberdays ;
                            datekey = (keyValue[1].substring(5, keyValue[1].length() - 3));


                            try {
                                series.appendData(new DataPoint(sdf.parse(datekey), val), true, pairs.length);


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    } /*
                    double taw = 6;
                    //int R0 = 0;
                    double t = pairs.length / 10;
                    int nt = val;
                    int n0 = firstcase;
                    if (n0 == 0) {
                        n0 = 1;
                    }

                    double lognt = (Math.log(nt));
                    double logn0 = (Math.log(n0));
                    double consta = taw / t;

                    double R0 = (lognt - logn0) * consta;

                    double R = Math.exp(R0);
                    if (t < 10) {
                        txt_R0.setText("Not Enough Data! ");
                    } else {
                        txt_R0.setText("R0 : " + R);
                    } */

                    //Math.log(R0) = ((Math.log(nt) - Math.log(n0)) * τ) / t;


                    graph.getGridLabelRenderer().setNumHorizontalLabels(10);

                    graph.getGridLabelRenderer().setHorizontalLabelsAngle(135);


                    //graph.getViewport().setScrollable(true);
                    //graph.getViewport().setScrollableY(true);
                    //graph.getViewport().setScalable(true);
                    //graph.getViewport().setScalableY(true);
                    Integer finalVal = val;
                    String finalDatekey = datekey;
                    cumu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            graph.getGridLabelRenderer().setVerticalAxisTitle("Cases");
                            graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
                            graph.removeAllSeries();
                            graph.getViewport().setYAxisBoundsManual(true);
                            graph.getViewport().setXAxisBoundsManual(true);
                            try {
                                graph.getViewport().setMaxX(sdf.parse(finalDatekey).getTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            graph.getViewport().setMaxY(finalVal);

                            series.setColor(Color.RED);
                            series.setTitle("Cumulative");
                            //graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                            graph.setHorizontalFadingEdgeEnabled(true);
                            graph.addSeries(series);
                        }
                    });

                }

            }
        });



        final OkHttpClient client1 = new OkHttpClient();

        Request request1 = new Request.Builder()
                .url("https://coronavirus-monitor-v2.p.rapidapi.com/coronavirus/cases_by_days_by_country.php?country="+countryname)
                .get()
                .addHeader("x-rapidapi-host", "coronavirus-monitor-v2.p.rapidapi.com")
                //.addHeader("x-rapidapi-key", "60b688dd13msh0d0c7a34fa1ffeap1d0e02jsn2904bb2675d9")
                .addHeader("x-rapidapi-key", "c191ff48b3msh15eb22f54031d70p1a6b92jsnb7315b15271d")
                .build();




        client1.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Request request1, IOException e) {
                txt_R0.setText("Failure!");
            }

            @Override
            public void onResponse(final Response response1) throws IOException {

                String myresponse1 = response1.body().string();
                //String substring1 = "";
                String substring1 = myresponse1.replace("\"", "").trim().substring(1);
                String newline = System.getProperty("line.separator");
                boolean hasNewline = substring1.contains(newline);

                if (hasNewline){
                    substring1 = substring1.replace("\n ", "").trim();
                    substring1 = substring1.replace(" ", "").trim();
                }
                substring1 = substring1.substring(0, substring1.length() - 1);
                //int indxcase = substring.indexOf("Cases"); // + 5
                //int indxstatus = substring.indexOf(",Status");


                // Map<String, Integer> myMap = new HashMap<String, Integer>();

                //client1.dispatcher().executorService().shutdown();

                String[] pairs1 = substring1.split(",");

                LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>();
                PointsGraphSeries<DataPoint> series2 = new PointsGraphSeries<DataPoint>();
                LineGraphSeries<DataPoint> series3 = new LineGraphSeries<DataPoint>();


                        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");

                graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {


                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {
                            return sdf.format(new Date((long) value));
                        }
                        else {
                            return super.formatLabel(value, isValueX);
                        }
                    }
                });


                if (substring1.equals("message:Not Found")){
                    txt_R0.setText("Country Not Found !");
                }
                //2020-03-17:{country_name:Egypt,total_cases:196,new_cases:30,active_cases:164,total_deaths:6,new_deaths:2,total_recovered:26,serious_critical:,total_cases_per1m:1.9,record_date:2020-03-17 23:50:02.071},

                else {
                    Integer val = 0;

                    // int numberdays = 0;
                    int firstcase = 0;
                    String firstcasedate = "";
                    String key  = "";
                    int  n = 0 ;
                    Map<Integer, Double> myMap = new HashMap<>();
                    String x_axis = "" ;
                    for (int i = 0; i < pairs1.length-20; i++) {
                        String pair = pairs1[i];

                        String[] keyValue = pair.split(":");

                        if (keyValue.length ==  3) {
                             key = keyValue[0];
                            //if (i <= 10 ){firstdate = val ;}
                            //numberdays =1 + numberdays ;
                        }



                        if (keyValue[0].equals("new_cases")) {
                            String[] keyValue1 = pairs1[i+1].split(":");
                            if (keyValue1[0].matches(".*\\d.*")){
                                val = Integer.valueOf(keyValue[1]+pairs1[i+1]);
                                if (i <= 10) { firstcase = val;
                                    firstcasedate = key;}
                                if (val==0){Double put = myMap.put(n, (double) 0);}
                                else{
                                    Double put = myMap.put(n,  Math.log(val));}
                                n++ ;

                                         }
                        else{

                            val = Integer.valueOf(keyValue[1]);
                            if (i <= 10) { firstcase = val;
                            firstcasedate = key;}
                            if (val==0){Double put = myMap.put(n, (double) 0);}
                            else{
                            Double put = myMap.put(n,  Math.log(val));}
                            n++ ;
                            }

                            String datekey1 = (key.substring(5, key.length()));
                            try {
                                series1.appendData(new DataPoint(sdf.parse(datekey1), val), true, pairs1.length);
                                //graph.getViewport().setMaxX(sdf.parse(datekey1).getTime());
                                series2.appendData(new DataPoint(sdf.parse(datekey1), Math.log(val)), true, pairs1.length);
                                x_axis = x_axis + datekey1 + ",";

                            } catch (ParseException e) {
                                e.printStackTrace(); }
                        }


                    }

                    double data[][]  = new double[n+1][2];
                    for (int i=0; i < n ; i++)
                    {
                        data[i][0] = (int) i;
                        data[i][1] = (double) myMap.get(i);

                    }
                    SimpleRegression regression = new SimpleRegression();
                    regression.addData(data);
                    System.out.println(regression.getSlope());
                    System.out.println(regression.getIntercept());
                    double slope = regression.getSlope();

                    String[] xAxis = x_axis.split(",");
                    for (int i = 0; i < xAxis.length; i++) {
                        String date = xAxis[i];
                        double y = regression.getSlope()*i + regression.getIntercept();
                        try {
                            series3.appendData(new DataPoint(sdf.parse(date),y ), true, xAxis.length);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    //log n(t) = t/τ log R0 + log n(0)
                    //y(t) = ax(t) + b

                    double taw = 6;
                    slope = slope*taw ;
                    //int R0 = 0;
                    double t = n+1;
                  //  int nt = val;
                  //  int n0 = firstcase;
                //    if (n0 == 0) {
                   //     n0 = 1;
                //    }

                    //double lognt = (log(nt));
                    //double logn0 = (log(n0));
                    //double consta = taw / t;

                    //double R0 = (lognt - logn0) * consta;

                    double R = Math.exp(slope);
                    if (t < 10) {
                        txt_R0.setText("Not Enough Data! ");
                    } else {
                        txt_R0.setText("R0 : " + R);
                    }

                    //Math.log(R0) = ((Math.log(nt) - Math.log(n0)) * τ) / t;


                    //graph.getGridLabelRenderer().setNumHorizontalLabels(5);
                    graph.getGridLabelRenderer().setVerticalAxisTitle("Cases");
                    graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");

                    //graph.getViewport().setScrollable(true);
                    //graph.getViewport().setScrollableY(true);
                    //graph.getViewport().setScalable(true);
                    String finalFirstcasedate = firstcasedate;
                    Integer finalVal = val;
                    daily.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            graph.getGridLabelRenderer().setVerticalAxisTitle("Cases");
                            graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
                            graph.removeAllSeries();
                            graph.getViewport().setScalableY(true);

                            graph.getViewport().setXAxisBoundsManual(true);
                            graph.getViewport().setYAxisBoundsManual(true);
                            try {
                                graph.getViewport().setMinX(sdf.parse(finalFirstcasedate.substring(5, finalFirstcasedate.length())).getTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            graph.getViewport().setMinY(0);
                            graph.getViewport().setMaxY(finalVal);

                            series1.setColor(Color.BLUE);
                            graph.addSeries(series1);

                            series1.setTitle("Daily");
                            //graph.getLegendRenderer().setVisible(true);
                        }
                    });


                    log.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            graph.removeAllSeries();
                            graph.getGridLabelRenderer().setVerticalAxisTitle("Log(Cases)");
                            graph.getViewport().setScalableY(true);

                            graph.getViewport().setXAxisBoundsManual(true);
                            graph.getViewport().setYAxisBoundsManual(true);
                            try {
                                graph.getViewport().setMinX(sdf.parse(finalFirstcasedate.substring(5, finalFirstcasedate.length())).getTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            graph.getViewport().setMinY(0);
                            graph.getViewport().setMaxY(log(finalVal));

                            series2.setColor(Color.GREEN);
                            series2.setShape(PointsGraphSeries.Shape.POINT);
                            series2.setSize(5);
                            graph.addSeries(series2);

                            series2.setTitle("Log(Daily)");
                            //graph.getLegendRenderer().setVisible(true);

                            graph.addSeries(series3);
                            series3.setColor(Color.YELLOW);
                        }
                    });

                    //series1.setColor(Color.GREEN);



                }

            }
        });


    }

}