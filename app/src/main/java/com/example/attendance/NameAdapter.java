package com.example.attendance;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NameAdapter extends BaseAdapter {

    private ArrayList<name> arrayList = new ArrayList<>();
    private List<name> naName;
    private String sp_name, sp_roll, na;
    private String AP_url = "http://192.168.43.11/attend/daily.php";
    private String result = "Present";
    private String result1 = "Absent";
    private Context Mcontext;
    private LayoutInflater inflater;
    private String name, rollno, cou, mobile_no, hour;
    private RadioGroup radioGroup;
    private RadioButton rb1, rb2;

    public NameAdapter(Context context, List<name> naName) {
        Mcontext = context;
        this.naName = naName;
        this.arrayList.addAll(naName);
        inflater = LayoutInflater.from(Mcontext);
    }

    @Override
    public int getCount() {
        return naName.size();
    }

    @Override
    public Object getItem(int position) {
        return naName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ProductHolder {
        TextView textView, textView1;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ProductHolder productHolder;
        if (convertView == null) {
            row = inflater.inflate(R.layout.name_list, null);
            productHolder = new ProductHolder();
            productHolder.textView = row.findViewById(R.id.naList);

            productHolder.textView1 = ((Activity) Mcontext).findViewById(R.id.className);
            na = productHolder.textView1.getText().toString();

            row.setTag(productHolder);
        } else {
            productHolder = (ProductHolder) row.getTag();
        }

        final name det = (name) getItem(position);
        cou = det.getCount();
        name = det.getName();
        rollno = det.getRollNo();
        productHolder.textView.setText(cou + "." + name + "-" + rollno);

        radioGroup = row.findViewById(R.id.radioGroup);
        rb1 = row.findViewById(R.id.rbt1);
        rb2 = row.findViewById(R.id.rbt2);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                sp_name = det.getCount() + "." + det.getName();
                sp_roll = det.getRollNo();
                mobile_no = det.getMobile();
                hour = det.getHour();
                Log.e("kee_check",hour);
                Log.d("sha", "working 2");
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
                                    if (message.equals("Thanks For Using....!")) {
                                        sendSms(result);
                                    }
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
                                Log.d("sha", "Values Sent");
                                params.put("name", sp_name);
                                params.put("roll", sp_roll);
                                params.put("class", na);
                                params.put("status", result);
                                return params;
                            }
                        };
                        MySingleton.getInstance(Mcontext).addToRequest(stringRequest);

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
                                    if (message.equals("Thanks For Using....!")) {
                                        sendSms(result1);

                                    }
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
                                Log.d("sha", "Values Sent");
                                params.put("name", sp_name);
                                params.put("roll", sp_roll);
                                params.put("class", na);
                                params.put("status", result1);
                                return params;
                            }
                        };
                        MySingleton.getInstance(Mcontext).addToRequest(stringRequest1);
                }
            }
        });

        //OnClickListener for Listview row Click
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        // End OnClickListener for Listview row Click

        return row;
    }

    public void sendSms(String res) {
        //Code for SMS
        Date d = new Date();
        CharSequence s = DateFormat.format("MMMM d, yyyy ", d.getTime());
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(mobile_no, null, "ROLL NO : " + sp_roll + " \nGreetings from College,\n " + sp_name + " is " + res + " on " + hour + " at " + s + "\n Thank You...!", null, null);
        //Code for SMS
    }


}
