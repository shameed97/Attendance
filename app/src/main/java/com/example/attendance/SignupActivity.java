package com.example.attendance;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private static String insert_Url = "http://192.168.43.11/attend/retrive.php";
    private EditText editUser, editPass, editCon;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editUser = findViewById(R.id.user_name);
        editPass = findViewById(R.id.password);
        editCon = findViewById(R.id.con_Password);
        builder = new AlertDialog.Builder(this);
    }

    public void signUp(View view) {
        final String username = editUser.getText().toString();
        final String password = editPass.getText().toString();
        final String con_Pass = editCon.getText().toString();

        //Code For Username,Password is Empty
        if (username.equals("") || password.equals("") || con_Pass.equals("")) {
            builder.setTitle("Something Went Wrong :");
            builder.setMessage("Please Fill All the Fields...");
            displayAlerts("input_error");
        //End Code For Username,Password is Empty
        }
        //Code For Password Mismatch
        else if (!password.equals(con_Pass)) {
            builder.setTitle("Something Went Wrong :");
            builder.setMessage("Password Mismatch...");
            displayAlerts("Mismatch");
        //End Code For Password Mismatch
        } else {
            //Code For Getting Data From Mysql
            StringRequest stringRequest = new StringRequest(Request.Method.POST, insert_Url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        builder.setTitle("SignUp Information...");
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
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user", username);
                    params.put("pass", password);
                    params.put("con_pass", con_Pass);
                    return params;
                }
            };

            MySingleton.getInstance(SignupActivity.this).addToRequest(stringRequest);
            //Code For Getting Data From Mysql
        }
    }

    public void displayAlerts(final String message) {
        //Code For Alert Dialog
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (message.equals("input_error")) {
                    editUser.setText("");
                    editPass.setText("");
                    editCon.setText("");
                }
                if (message.equals("Register Successfull")) {
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                }
                if (message.equals("Register failed")) {
                    editUser.setText("");
                    editPass.setText("");
                    editCon.setText("");
                }
                if (message.equals("Mismatch")) {
                    editPass.setText("");
                    editCon.setText("");
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
     //End Code For Alert Dialog
    }
}
