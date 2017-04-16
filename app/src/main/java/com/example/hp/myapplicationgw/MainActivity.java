package com.example.hp.myapplicationgw;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ubidots.ApiClient;
import com.ubidots.Value;
import com.ubidots.Variable;

import java.net.Inet4Address;

public class MainActivity extends AppCompatActivity {

    private double temp_data = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button swichOn = (Button) findViewById(R.id.on);
        Button swichOff = (Button) findViewById(R.id.off);
        if (temp_data > 25) {
            Notification.Builder builder = new Notification.Builder(this).setContentTitle("High Temparature").setContentText(Double.toString(temp_data)).setAutoCancel(true).setSmallIcon(R.drawable.alert);
            Intent intent = new Intent(String.valueOf(MainActivity.this));
            intent.putExtra("msg", Double.toString(temp_data) + "showed");
            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 3, intent, 0);
            builder.setContentIntent(pendingIntent);
            Notification notification = builder.build();
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(1, notification);
        }
        swichOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SwictchOn().execute();
            }
        });
        swichOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SwictchOff().execute();
            }
        });
    }

    public class SwictchOn extends AsyncTask<Integer, Void, Void> {
        private final String API_KEY = "50d443f06ac3bbf32ff5677a336a701176db2929";
        private final String VARIABLE_TEMP_ID1 = "587f6d0676254202b4b6dc69";
        private final String VARIABLE_RELAY_ID1 = "587f6d8a7625424ff40f1dd1";

        ApiClient coolUrCar;
        Variable relay;
        TextView setTemp;
        Variable temparature;

        @Override
        protected void onPreExecute() {
        setTemp=(TextView) findViewById(R.id.textView);
        setTemp.setText(Double.toString(temp_data));
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... params) {

            coolUrCar = new ApiClient(API_KEY);
            temparature = coolUrCar.getVariable(VARIABLE_TEMP_ID1);
            relay = coolUrCar.getVariable(VARIABLE_RELAY_ID1);
            Value[] temp = temparature.getValues();
            temp_data = temp[0].getValue();
            if (temp_data > 25) {
                relay.saveValue(1);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setTemp = (TextView) findViewById(R.id.textView);
            setTemp.setText(Double.toString(temp_data));
            super.onPostExecute(aVoid);
        }
    }

    public class SwictchOff extends AsyncTask<Integer, Void, Void> {
        private final String API_KEY = "50d443f06ac3bbf32ff5677a336a701176db2929";
        private final String VARIABLE_TEMP_ID1 = "587f6d0676254202b4b6dc69";
        private final String VARIABLE_RELAY_ID1 = "587f6d8a7625424ff40f1dd1";
        private double temp_data = 0;
        ApiClient coolUrCar;
        Variable relay;
        TextView setTemp;
        Variable temparature;

        @Override
        protected Void doInBackground(Integer... params) {

            coolUrCar = new ApiClient(API_KEY);
            temparature = coolUrCar.getVariable(VARIABLE_TEMP_ID1);
            relay = coolUrCar.getVariable(VARIABLE_RELAY_ID1);
            Value[] temp = temparature.getValues();
            temp_data = temp[0].getValue();
            if (temp_data <= 25) {
                relay.saveValue(0);

            }
            return null;
        }
    }

}

