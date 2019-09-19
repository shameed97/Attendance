package com.example.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WelcomeActivity extends AppCompatActivity {
    private TextView textStaff, textMobile, textEmail, textQuali,textDate;
    private String username;
    private String info_Url = "http://192.168.43.11/attend/staffInfo.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        Log.d("kee", username);

        textStaff = findViewById(R.id.staff);
        textMobile = findViewById(R.id.mobile);
        textEmail = findViewById(R.id.email);
        textQuali = findViewById(R.id.qualification);
        textDate=findViewById(R.id.date);
        getResult();
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

// textView is the TextView view that should display it
        textDate.setText(currentDateTimeString);
    }

    public void getResult() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, info_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("kee", jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("array");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject Object = jsonArray.getJSONObject(i);

                        String name = Object.getString("name");
                        String mobile = Object.getString("mobile_no");
                        String email = Object.getString("email");
                        String quali = Object.getString("qualification");
                        textStaff.setText(name);
                        textMobile.setText(mobile);
                        textEmail.setText(email);
                        textQuali.setText(quali);
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
                return params;
            }
            //End Code For Send Data's to PHP file
        };
        MySingleton.getInstance(WelcomeActivity.this).addToRequest(stringRequest);

    }

    public void classList(View view) {

        Intent intent=new Intent(WelcomeActivity.this,ClassActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }
}
