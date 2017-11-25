package com.esprit.carrent.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.esprit.carrent.R;
import com.esprit.carrent.Utils.Constants;
import com.esprit.carrent.Utils.RequestHandler;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends Activity implements View.OnClickListener {

    EditText txtMail, txtPassReg, txtvalid, txtUserName;
    Button btnRegister;


    private ProgressDialog progDiag;
    //private FirebaseAuth firebaseaut ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        progDiag = new ProgressDialog(this);
        //firebaseaut = FirebaseAuth.getInstance();
        txtMail = (EditText) findViewById(R.id.emailTxtreg);
        txtPassReg = (EditText) findViewById(R.id.passwordtxtreg);
        txtvalid = (EditText) findViewById(R.id.passwordvalidreg);
        txtUserName = (EditText) findViewById(R.id.Username);
        btnRegister = (Button) findViewById(R.id.signUnButton);
        btnRegister.setOnClickListener(this);

    }

    private void RegistreUser() {
        final String user = txtUserName.getText().toString().trim();
        final String email = txtMail.getText().toString().trim();
        final String password1 = txtPassReg.getText().toString().trim();

        progDiag.setMessage("Registering User");
        progDiag.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progDiag.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progDiag.hide();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", user);
                params.put("email", email);
                params.put("password", password1);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-za-z0-9-]+)*@[A-za-z0+9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onClick(View view) {
        if (view == btnRegister) {
            String password1 = txtPassReg.getText().toString().trim();
            String password2 = txtvalid.getText().toString().trim();
            if (TextUtils.isEmpty(txtMail.getText().toString().trim()) || TextUtils.isEmpty(txtPassReg.getText().toString().trim())) {
                txtMail.setError("Fields can't be empty");
                txtPassReg.setError("Fields can't be empty");
            } else if (!emailValidator(txtMail.getText().toString().trim())) {
                txtMail.setError("Please enter Valid Email Adress");
            } else if (!password1.equals(password2)) {
                txtvalid.setError("Same Password Please");
            } else if (!password2.equals(password1)) {
                txtvalid.setError("Same Password Please");
            } else {
                RegistreUser();
                Intent LoginIntent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(LoginIntent);
            }
        }
    }
}