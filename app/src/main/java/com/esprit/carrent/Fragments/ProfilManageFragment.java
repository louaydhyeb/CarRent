package com.esprit.carrent.Fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ProfilManageFragment extends Fragment {

    EditText txtLast,txtFirst,txtAge;
    ImageView imgUp;
    Button btnUpdate ;
    private ProgressDialog progDiag;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = null ;
        progDiag = new ProgressDialog(getActivity());

        v = inflater.inflate(R.layout.fragment_profil, container, false);

        getActivity().setTitle("Manage Profil");
        txtLast = (EditText)v.findViewById(R.id.txtLastName);
        txtFirst = (EditText)v.findViewById(R.id.txtFirstName);
        txtAge = (EditText)v.findViewById(R.id.txtage);
        imgUp = (ImageView)v.findViewById(R.id.tvimgUpdate);
        btnUpdate = (Button)v.findViewById(R.id.updatebtn);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               UpdateUser();
            }
        });
        return v;

    }
    private void UpdateUser() {
        final String Last = txtLast.getText().toString().trim();
        final String First = txtFirst.getText().toString().trim();
        final String Age = txtAge.getText().toString().trim();
        final Bundle bt = getArguments();
       Log.e("ID : ",bt.getString("id")+"");


        progDiag.setMessage("Updating User ...");
        progDiag.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progDiag.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progDiag.hide();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("id",bt.getInt("id")+"");
                params.put("lastname", Last);
                params.put("firstname", First);
                params.put("age", Age);
                return params;
            }
        };

        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}
