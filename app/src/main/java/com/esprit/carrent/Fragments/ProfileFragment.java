package com.esprit.carrent.Fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.carrent.Activities.LoginActivity;
import com.esprit.carrent.Activities.MainScreenDrawer;
import com.esprit.carrent.R;
import com.esprit.carrent.Utils.Constants;
import com.esprit.carrent.Utils.RequestHandler;
import com.esprit.carrent.Utils.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ProfileFragment extends Fragment {


    TextView nomPrenom,username,address,email,phone,feed,rate;
    ImageView profilpic ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = null ;
        v = inflater.inflate(R.layout.fragment_profile_affichage, container, false);
        getActivity().setTitle("Profil");

        nomPrenom = (TextView)v.findViewById(R.id.txtNomPrenom);
        username = (TextView)v.findViewById(R.id.txtUsernameProfil);
        address = (TextView)v.findViewById(R.id.txtAddress);
        feed = (TextView)v.findViewById(R.id.feedbackbtn);
        email = (TextView)v.findViewById(R.id.txtEmailProfil);
        phone = (TextView)v.findViewById(R.id.txtphoneNumber);
        profilpic = (ImageView)v.findViewById(R.id.ImgProfileDet);
        rate = (TextView)v.findViewById(R.id.tvRating);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent email1 = new Intent(Intent.ACTION_SEND);
                email1.putExtra(Intent.EXTRA_EMAIL, new String[]{email.getText().toString().substring(7)});
                email1.putExtra(Intent.EXTRA_SUBJECT, "Rent your car");
                email1.putExtra(Intent.EXTRA_TEXT, "message");
                email1.setType("message/rfc822");
                startActivity(Intent.createChooser(email1, "Choose an Email client :"));
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +phone.getText()));
                startActivity(intent);
            }
        });
        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment feedFrag = new FeedbackFragment();
                getFragmentManager().beginTransaction().replace(R.id.containerfr,feedFrag).commit();
            }
        });
        if(LoginActivity.Verif ==1){
           String name = getArguments().getString("name");
            String surname =  getArguments().getString("surname");
            String img = getArguments().getString("imgProfile");

            new ProfileFragment.DownloadImage(profilpic).execute(img);
            nomPrenom.setText(" "+name+" "+surname);
            username.setText(name);
        }
        if (LoginActivity.Verif == 0)
        {
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Constants.URL_SHOW_USER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj = new JSONObject(response);

                                if (!obj.getBoolean("error")) {

                                    username.setText("Username : " +obj.getString("username"));
                                    email.setText("Email : " +obj.getString("email"));
                                    nomPrenom.setText(""+obj.getString("firstname") +" "+obj.getString("lastname"));
                                    phone.setText("Phone : "+ obj.getString("phone"));
                                    address.setText("Address : " +obj.getString("address"));

                                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

                                    // Initialize a new ImageRequest
                                    ImageRequest imageRequest = new ImageRequest(
                                            obj.getString("url"), // Image URL
                                            new Response.Listener<Bitmap>() { // Bitmap listener
                                                @Override
                                                public void onResponse(Bitmap response) {
                                                    // Do something with response
                                                    profilpic.setImageBitmap(response);

                                                }
                                            },
                                            0, // Image width
                                            0, // Image height
                                            ImageView.ScaleType.CENTER_CROP, // Image scale type
                                            Bitmap.Config.RGB_565, //Image decode configuration
                                            new Response.ErrorListener() { // Error listener
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    // Do something with error response
                                                    error.printStackTrace();
                                                }
                                            }
                                    );

                                    // Add ImageRequest to the RequestQueue
                                    requestQueue.add(imageRequest);
                                    //new ProfileFragment.DownloadImage(profilpic).execute(obj.getString("url"));

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
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id",String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUserId()) );
                    return params;
                }
            };
            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);


            StringRequest stringRequest2 = new StringRequest(
                    Request.Method.POST,
                    Constants.URL_SHOW_RATING,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj = new JSONObject(response);
                                    rate.setText(obj.getString("rating"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id_user2",String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUserId()) );
                    return params;
                }
            };
            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest2);
        }

        return v ;
}
    public class DownloadImage extends AsyncTask<String,Void,Bitmap> {
        ImageView bmImmage;
        public DownloadImage(ImageView bmImmage){
            this.bmImmage = bmImmage;
        }
        protected  Bitmap doInBackground(String... urls){
            String urldisplay = urls[0];
            Bitmap mIconll = null;
            try{
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIconll = BitmapFactory.decodeStream(in);
            }catch(Exception e){
                Log.e("error",e.getMessage());
                e.printStackTrace();
            }
            return mIconll;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            bmImmage.setImageBitmap(result);

        }
}
}
