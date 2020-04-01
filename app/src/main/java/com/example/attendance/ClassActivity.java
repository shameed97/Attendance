package com.example.attendance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassActivity extends AppCompatActivity {
    private String username, hour;
    private ListView listView;
    private ListviewAdapter listviewAdapter;
    private List<String> clName = new ArrayList<>();
    private String className;
    private String class_Url = "http://192.168.43.11/attend/classInfo.php";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        Intent intent = getIntent();
        String[] values = intent.getStringArrayExtra("values");
        username = values[0];
        hour = values[1];
        Log.d("Checking", username + hour);
        listView = findViewById(R.id.listClass);
        TextView empty=findViewById(R.id.emptyText);
        listView.setEmptyView(empty);
        getClassName();

    }

    public void getClassName() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, class_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("array");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject Object = jsonArray.getJSONObject(i);

                        className = Object.getString("class");
                        clName.add(className);
                        listviewAdapter = new ListviewAdapter((ArrayList<String>) clName, ClassActivity.this);
                        listView.setAdapter(listviewAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String value = listviewAdapter.getItem(position);
                                Intent intent = new Intent(ClassActivity.this, NameActivity.class);
                                intent.putExtra("value", value);
                                startActivity(intent);
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("kee", e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("kee", error.toString());
                error.printStackTrace();

            }
        }) {
            //Code For Send Data's to PHP file
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("hour", hour);
                return params;
            }
            //End Code For Send Data's to PHP file
        };
        MySingleton.getInstance(ClassActivity.this).addToRequest(stringRequest);

    }
}
