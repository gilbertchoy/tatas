package com.bort.tatari;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private String tag = "results problem 1";
    private String tag1 = "results problem 2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Map<String, String>> spotList = new ArrayList<Map<String, String>>();
        Set<String> creativeSet = new HashSet<String>();
        Set<String> dateSet = new HashSet<String>();
        try {
            InputStreamReader is = new InputStreamReader(getAssets()
                    .open("spots.csv"));
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] chunks = line.split(",");
                HashMap<String, String> map = new HashMap<>();
                map.put("Date", chunks[0]);
                map.put("Time",chunks[1].replaceAll("^\"|\"$", ""));
                map.put("Creative",chunks[2]);
                map.put("Spend",chunks[3]);
                map.put("Views",chunks[4]);
                spotList.add(map);
                creativeSet.add(chunks[2]);
                dateSet.add(chunks[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String temp : creativeSet) {
            int views=0;
            double spend = 0;
            for(int i=0;i<spotList.size();i++){
                HashMap<String,String> row = (HashMap<String, String>) spotList.get(i);
                if(row.get("Creative").equals(temp)){
                    spend = spend + Double.parseDouble(row.get("Spend"));
                    views = views + Integer.parseInt(row.get("Views"));
                }

            }
            Log.d(tag,temp + " cpv: " + spend/views);
        }

        //problem 2
        List<Map<String, String>> rotationList = new ArrayList<Map<String, String>>();
        try {
            InputStreamReader is = new InputStreamReader(getAssets()
                    .open("rotations.csv"));
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] chunks = line.split(",");
                HashMap<String, String> map = new HashMap<>();
                map.put("Start", chunks[0].replaceAll("^\"|\"$", ""));
                map.put("End",chunks[1].replaceAll("^\"|\"$", ""));
                map.put("Name",chunks[2]);
                rotationList.add(map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String temp : dateSet) {
            double morningSpend = 0;
            int morningViews = 0;
            double afternoonSpend = 0;
            int afternoonViews = 0;
            double primeSpend = 0;
            int primeViews = 0;

            for(int i=0;i<spotList.size();i++){
                HashMap<String,String> row = (HashMap<String, String>) spotList.get(i);

                if(row.get("Date").equals(temp)){
                    SimpleDateFormat parser = new SimpleDateFormat("HH:mm a");
                    try {
                        Date time = parser.parse(row.get("Time"));
                        Date morningStart = parser.parse(rotationList.get(0).get("Start"));
                        Date morningEnd = parser.parse(rotationList.get(0).get("End"));
                        Date afternoonStart = parser.parse(rotationList.get(1).get("Start"));
                        Date afternoonEnd = parser.parse(rotationList.get(1).get("End"));
                        Date primeStart = parser.parse(rotationList.get(2).get("Start"));
                        Date primeEnd = parser.parse(rotationList.get(2).get("End"));
                        //if slot time falls in specified rotation ranges
                        if(time.compareTo(morningStart) > 0 && morningEnd.compareTo(time) > 0) {
                            morningSpend = morningSpend + Double.parseDouble(row.get("Spend"));
                            morningViews = morningViews + Integer.parseInt(row.get("Views"));
                        }
                        if(time.compareTo(afternoonStart) > 0 && afternoonEnd.compareTo(time) > 0) {
                            afternoonSpend = afternoonSpend + Double.parseDouble(row.get("Spend"));
                            afternoonViews = afternoonViews + Integer.parseInt(row.get("Views"));
                        }
                        if(time.compareTo(primeStart) > 0 && primeEnd.compareTo(time) > 0) {
                            primeSpend = primeSpend + Double.parseDouble(row.get("Spend"));
                            primeViews = primeViews + Integer.parseInt(row.get("Views"));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.d(tag1, temp + " morning cpv: " + morningSpend/morningViews);
            Log.d(tag1, temp + " afternoon cpv: " + afternoonSpend/afternoonViews);
            Log.d(tag1, temp + " prime cpv: " + primeSpend/primeViews);
        }
    }
}