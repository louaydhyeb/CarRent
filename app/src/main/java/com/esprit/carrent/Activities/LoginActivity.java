package com.esprit.carrent.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.esprit.carrent.R;
import com.esprit.carrent.Utils.Constants;
import com.esprit.carrent.Utils.RequestHandler;
import com.esprit.carrent.Utils.SharedPrefManager;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends Activity {

    public static int Verif  ;

    Button btnLogin;
    EditText user, passLogin;
    TextView registrebtn;
    private ProgressDialog progressDialog;
    private CallbackManager callBackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken currentToken) {
                accessTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken currentToken) {

                    }
                };
                profileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

                    }
                };
                accessTokenTracker.startTracking();
                profileTracker.startTracking();

                loginButton = (LoginButton) findViewById(R.id.login_button);
            }
        };

        accessTokenTracker.startTracking();


        loginButton = (LoginButton) findViewById(R.id.login_button);
        btnLogin = (Button) findViewById(R.id.signinButton);
        user = (EditText) findViewById(R.id.usernameTxt);
        passLogin = (EditText) findViewById(R.id.passwordtxt);
        registrebtn = (TextView) findViewById(R.id.txtRegister);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");

        callBackManager = CallbackManager.Factory.create();

        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            System.out.println(currentProfile.getFirstName());
                            Intent main = new Intent(LoginActivity.this, MainScreenDrawer.class);
                            main.putExtra("name", currentProfile.getFirstName());
                            main.putExtra("surname", currentProfile.getLastName());
                            main.putExtra("email",currentProfile.getLinkUri().toString());
                            main.putExtra("imageUrl", currentProfile.getProfilePictureUri(100,100).toString());
                            Verif = 1;
                            startActivity(main);
                            mProfileTracker.stopTracking();
                        }
                    };
                    // no need to call startTracking() on mProfileTracker
                    // because it is called by its constructor, internally.
                }
                else {
                    Profile profile = Profile.getCurrentProfile();
                    Intent main = new Intent(LoginActivity.this, MainScreenDrawer.class);
                    main.putExtra("name", profile.getFirstName());
                    main.putExtra("surname", profile.getLastName());
                    main.putExtra("imageUrl", profile.getProfilePictureUri(100, 100).toString());
                    Verif =1;
                    startActivity(main);
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        };
        loginButton.setReadPermissions("public_profile");
        loginButton.registerCallback(callBackManager,callback);

        registrebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SignUp = new Intent(getBaseContext(), SignUp.class);
                startActivity(SignUp);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    protected void OnStop(){
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    private void login() {

        final String user1 = user.getText().toString().trim();
        final String pass = passLogin.getText().toString().trim();
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")) {
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(obj.getInt("id"),
                                        obj.getString("username"),
                                        obj.getString("email"));

                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), MainScreenDrawer.class);
                                i.putExtra("username",obj.getString("username"));
                                i.putExtra("id",obj.getString("id"));

                                Verif = 0 ;
                                startActivity(i);

                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", user1);
                params.put("password", pass);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callBackManager.onActivityResult(requestCode, resultCode, data);


    }



}