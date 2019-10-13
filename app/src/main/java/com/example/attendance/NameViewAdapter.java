package com.example.attendance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NameViewAdapter extends ArrayAdapter {


    private ArrayList<String> naName = new ArrayList<>();
    private Context context;
    private String  pos_value, sp_name, sp_roll,na;
    private String AP_url = "http://192.168.43.11/attend/daily.php";
    private String result = "Present";
    private String result1 = "Absent";
    private RadioGroup radioGroup;
    private RadioButton rb1, rb2;
    private TextView textView1;

    public NameViewAdapter(ArrayList<String> naName, Context context) {
        super(context, R.layout.name_list, naName);
        this.context = context;
        this.naName = naName;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View row = layoutInflater.inflate(R.layout.name_list, parent, false);
        final TextView textView = row.findViewById(R.id.naList);
        textView.setText(naName.get(position));
        assert convertView != null;
        radioGroup = row.findViewById(R.id.radioGroup);
        rb1 = row.findViewById(R.id.rbt1);
        rb2 = row.findViewById(R.id.rbt2);
        textView1=((Activity) context).findViewById(R.id.className);
        na=textView1.getText().toString();
        Log.d("k",na);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("kee", "OnChecked Working");
                pos_value = naName.get(position);
                Log.d("kee", "pos_value" + pos_value);
                String[] sp_values = pos_value.split("-");
                sp_name = sp_values[0];
                sp_roll = sp_values[1];
                Log.d("kee1", sp_name);
                Log.d("kee1", sp_roll);
                switch (checkedId) {
                    case R.id.rbt1:
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, AP_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("sha", response);
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String code = jsonObject.getString("code");
                                    String message = jsonObject.getString("message");
                                    Log.d("sha", message);
                                    Log.d("sha", code);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("sha", error.toString());
                                error.printStackTrace();
                            }

                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                Log.d("sha", "Its working");
                                params.put("name", sp_name);
                                params.put("roll", sp_roll);
                                params.put("class", na);
                                params.put("status", result);
                                return params;
                            }
                        };
                        MySingleton.getInstance(context).addToRequest(stringRequest);

                        break;
                    case R.id.rbt2:

                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, AP_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("sha", response);
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String code = jsonObject.getString("code");
                                    String message = jsonObject.getString("message");
                                    Log.d("sha", code);
                                    Log.d("sha", message);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("sha", error.toString());
                                error.printStackTrace();
                            }

                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("name", sp_name);
                                params.put("roll", sp_roll);
                                params.put("class", na);
                                params.put("status", result1);
                                return params;
                            }
                        };
                        MySingleton.getInstance(context).addToRequest(stringRequest1);
                }
            }
        });

        return row;
    }



}
