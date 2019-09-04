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

public class LoginActivity extends AppCompatActivity {

    private static String retrive_Url = "http://192.168.43.11/attend/login_det.php";
    private EditText editUser, editPass;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUser = findViewById(R.id.user_name);
        editPass = findViewById(R.id.pass_word);
        builder = new AlertDialog.Builder(this);
    }

    public void signIn(View view) {
        final String username = editUser.getText().toString();
        final String password = editPass.getText().toString();

        if (username.equals("") || password.equals("")) {
            builder.setTitle("Something Went Wrong :");
            builder.setMessage("Please Fill All The Fields...");
            displayAlerts("input_error");
        } else {
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
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user", username);
                    params.put("pass", password);
                    return params;
                }
            };
            MySingleton.getInstance(LoginActivity.this).addToRequest(stringRequest);
        }

    }


    public void createNew(View view) {

        startActivity(new Intent(this, SignupActivity.class));
    }

    public void displayAlerts(final String message) {
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (message.equals("input_error")) {
                    editUser.setText("");
                    editPass.setText("");
                }
                if (message.equals("Login Success...")) {
                    startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
                }
                if (message.equals("Login Failed...")) {
                    editUser.setText("");
                    editPass.setText("");
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
