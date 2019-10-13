package com.example.attendance;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class NameActivity extends AppCompatActivity {

    private ListView listView;
    private TextView textView;
    private String value, name, rollno,cou,pre,abs;
    private NameViewAdapter nameviewAdapter;
    private List<String> naName = new ArrayList<>();
    private String name_Url = "http://192.168.43.11/attend/name.php";
    private String St_url = "http://192.168.43.11/attend/status.php";
    private Dialog dialog;
    int count = 0;
    private TextView close, present, absent, total;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        Intent intent = getIntent();
        value = intent.getStringExtra("value");

        listView = findViewById(R.id.listName);
        textView = findViewById(R.id.className);
        textView.setText(value);
        dialog=new Dialog(this);
        getClassName();

    }

    public void getClassName() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, name_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.d("kee", response);
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("kee", jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("array");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject Object = jsonArray.getJSONObject(i);
                        count++;
                        name = Object.getString("name");
                        rollno = Object.getString("rollno");
                        Log.d("kee", name);
                        Log.d("kee", rollno);
                        naName.add(count + "." + name + "-" + rollno);
                    }
                    cou=""+count;

                    nameviewAdapter = new NameViewAdapter((ArrayList<String>) naName, NameActivity.this);
                    listView.setAdapter(nameviewAdapter);

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
                params.put("value", value);
                Log.d("kee", params.toString());
                return params;
            }
            //End Code For Send Data's to PHP file
        };
        MySingleton.getInstance(NameActivity.this).addToRequest(stringRequest);
    }

    public void getStatus(View view) {

        dialog.setContentView(R.layout.status_pop);
        dialog.setCancelable(false);
        close = dialog.findViewById(R.id.txtClose);
        button=dialog.findViewById(R.id.butRound2);
        present = dialog.findViewById(R.id.present);
        absent = dialog.findViewById(R.id.absent);
        total = dialog.findViewById(R.id.total);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, St_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.d("sha", response);
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("sha", jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("array");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject Object = jsonArray.getJSONObject(i);

                        pre= Object.getString("pre");
                        abs= Object.getString("abs");

                        present.setText(pre);
                        absent.setText(abs);
                        total.setText(cou);


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
                params.put("classname", value);
                Log.d("kee", params.toString());
                return params;
            }
            //End Code For Send Data's to PHP file
        };
        MySingleton.getInstance(NameActivity.this).addToRequest(stringRequest);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}