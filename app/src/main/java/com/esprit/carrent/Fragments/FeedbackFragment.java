package com.esprit.carrent.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.esprit.carrent.Entities.Feedback;
import com.esprit.carrent.R;
import com.esprit.carrent.Utils.Constants;
import com.esprit.carrent.Utils.FeedbackAdapter;
import com.esprit.carrent.Utils.RequestHandler;
import com.esprit.carrent.Utils.SharedPrefManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;


public class FeedbackFragment extends Fragment {

    RecyclerView recyclerView;
    List<Feedback> feedbackList;
    FeedbackAdapter adapter;
    FloatingActionButton fab_plus, fab_addFeed, fab_addRate;
    Animation FabOpen, FabClose, fabClockWise, fabAntiClockWise;
    boolean isOpen = false;
    private ProgressDialog progDiag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = null;
        v = inflater.inflate(R.layout.fragment_feedback, container, false);
        getActivity().setTitle("FeedBacks");
        progDiag = new ProgressDialog(getActivity());
        fab_plus = (FloatingActionButton) v.findViewById(R.id.fab_plus);
        fab_addFeed = (FloatingActionButton) v.findViewById(R.id.fab_Add_Feedback);
        fab_addRate = (FloatingActionButton) v.findViewById(R.id.fab_Add_rate);

        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabClockWise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        fabAntiClockWise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

        fab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {
                    fab_addFeed.startAnimation(FabClose);
                    fab_addRate.startAnimation(FabClose);
                    fab_plus.startAnimation(fabAntiClockWise);
                    fab_addFeed.setClickable(false);
                    fab_addRate.setClickable(false);
                    isOpen = false;
                } else {
                    fab_addFeed.startAnimation(FabOpen);
                    fab_addRate.startAnimation(FabOpen);
                    fab_plus.startAnimation(fabClockWise);
                    fab_addFeed.setClickable(true);

                    fab_addFeed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("FeedBack");
                            final EditText input = new EditText(getActivity());
                            input.setInputType(InputType.TYPE_CLASS_TEXT);
                            builder.setView(input);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progDiag.setMessage("Adding the Feedback");
                                    progDiag.show();
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_ADD_FEEDBACK,
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
                                            params.put("idUser", String.valueOf(SharedPrefManager.getInstance(getActivity()).getUserId()));
                                            params.put("UserFeed", SharedPrefManager.getInstance(getActivity()).getUsername());
                                            params.put("feedback", input.getText().toString().trim());
                                            return params;
                                        }
                                    };

                                    RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
                                    loadFeedbacks();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            builder.show();
                        }
                    });

                    fab_addRate.setClickable(true);
                    isOpen = true;
                }
            }
        });

        feedbackList = new ArrayList<>();

        recyclerView = (RecyclerView) v.findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadFeedbacks();
        adapter = new FeedbackAdapter(getActivity(), feedbackList);
        recyclerView.setAdapter(adapter);


        return v;
    }

    private void loadFeedbacks() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_FEEDBACKS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {
                                //getting product object from json array
                                JSONObject feedbacks = array.getJSONObject(i);
                                String UserF = feedbacks.getString("UserFeed");
                                String feed = feedbacks.getString("feedback");
                                String date = feedbacks.getString("dateFeed");
                                Feedback f = new Feedback(UserF, feed, date);
                                feedbackList.add(f);
                            }
                            adapter = new FeedbackAdapter(getActivity(), feedbackList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idUser", String.valueOf(SharedPrefManager.getInstance(getActivity()).getUserId()));
                return params;
            }
        };
        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}
