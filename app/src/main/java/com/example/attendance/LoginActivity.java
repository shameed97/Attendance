package com.example.attendance;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static String retrive_Url = "http://192.168.43.11/attend/login_det.php";
    private EditText editUser, editPass;
    private AlertDialog.Builder builder;
    private String username, password;
    private static final int REQUST_INTERNET = 123;
    private static final int REQUST_SMS = 456;
    private static final int REQUEST_GROUP = 990;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        permission();
        editUser = findViewById(R.id.user_name);
        editPass = findViewById(R.id.pass_word);
        builder = new AlertDialog.Builder(this);
    }


        public void signIn (View view)
        {
            username = editUser.getText().toString();
            password = editPass.getText().toString();

            //Code For Checking Username and Password Field Empty
            if (username.equals("") || password.equals("")) {
                builder.setTitle("Something Went Wrong :");
                builder.setMessage("Please Fill All The Fields...");
                displayAlerts("input_error");
                //End Code For Checking Username and Password Field Empty
            } else {
                //Code For Getting Data From Mysql
                StringRequest stringRequest = new StringRequest(Request.Method.POST, retrive_Url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            builder.setTitle("Login Information :");
                            builder.setMessage(message);
                            displayAlerts(code);
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("user", username);
                        params.put("pass", password);
                        return params;
                    }
                    //End `Code For Send Data's to PHP file
                };
                MySingleton.getInstance(LoginActivity.this).addToRequest(stringRequest);
            }
            //End Code For Getting Data From Mysql
        }


        public void createNew (View view){

            startActivity(new Intent(this, SignupActivity.class));
        }

        public void displayAlerts ( final String message){
            //Code For Alert Dialog
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (message.equals("input_error")) {
                        editUser.setText("");
                        editPass.setText("");
                    }
                    if (message.equals("Login Success...")) {
                        Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                    if (message.equals("Login Failed...")) {
                        editUser.setText("");
                        editPass.setText("");
                    }
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            //End Code For Alert Dialog
        }

    private void permission() {
        ArrayList<String> permissionNeeded = new ArrayList<>();
        ArrayList<String> permissionAvailable = new ArrayList<>();
        permissionAvailable.add(Manifest.permission.INTERNET);
        permissionAvailable.add(Manifest.permission.SEND_SMS);

        for (String permission : permissionAvailable) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                permissionNeeded.add(permission);
            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        REQUST_SMS);
            }
        }
        requestGroup(permissionNeeded);

    }

    private void requestGroup(ArrayList<String> permissions) {

        String[] permissionList = new String[permissions.size()];
        permissions.toArray(permissionList);
        ActivityCompat.requestPermissions(this, permissionList, REQUEST_GROUP);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUST_INTERNET:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    break;
            case REQUST_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    break;
        }
    }
    }

