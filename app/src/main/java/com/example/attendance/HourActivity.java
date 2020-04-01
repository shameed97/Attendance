package com.example.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HourActivity extends AppCompatActivity {

    private String username;
    private ListView listView;
    private String[] hourList = {"1-hour", "2-hour", "3-hour", "4-hour", "5-hour", "6-hour", "7-hour", "8-hour"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hour);

        username = getIntent().getStringExtra("username");
        listView = findViewById(R.id.listHour);
        getHourList();
    }

    private void getHourList() {
        final ArrayAdapter<String> listHourAdapter = new ArrayAdapter<>(this, R.layout.class_list, R.id.clList, hourList);
        listView.setAdapter(listHourAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String result = listHourAdapter.getItem(position);
                Log.d("kee_hourAdapter", result);
                Intent intent = new Intent(getApplicationContext(), ClassActivity.class);
                String[] values = {username, result};
                intent.putExtra("values", values);
                startActivity(intent);
            }
        });

    }
}
