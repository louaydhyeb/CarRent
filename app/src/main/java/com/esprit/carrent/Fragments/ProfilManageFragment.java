package com.esprit.carrent.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.esprit.carrent.Activities.LoginActivity;
import com.esprit.carrent.Manifest;
import com.esprit.carrent.R;
import com.esprit.carrent.Utils.Constants;
import com.esprit.carrent.Utils.RequestHandler;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class ProfilManageFragment extends Fragment {

    EditText txtLast,txtFirst,txtEmail,address,phone ;
    ImageView imgUp;
    Button btnUpdate,upload ;
    private ProgressDialog progDiag;
    private final int IMG_REQUEST = 1 ;
    public Bitmap bitmap ;
    Activity mActivity;;
    private Uri path ;
    private static final int STORAGE_PERMISSION_CODE = 2342;
    int PLACE_PICKER_REQUEST = 1 ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = null ;
        progDiag = new ProgressDialog(getActivity());

        v = inflater.inflate(R.layout.fragment_profil, container, false);

        getActivity().setTitle("Manage Profil");
        requestStoragePermission();

        txtLast = (EditText)v.findViewById(R.id.txtLastName);
        txtFirst = (EditText)v.findViewById(R.id.txtFirstName);
        txtEmail = (EditText)v.findViewById(R.id.txtmail);
        address = (EditText)v.findViewById(R.id.txtAdress);
        phone = (EditText)v.findViewById(R.id.txtPhone);
        imgUp = (ImageView)v.findViewById(R.id.tvimgUpdate);
        btnUpdate = (Button)v.findViewById(R.id.updatebtn);

        if (LoginActivity.Verif == 1 )
        {
            String name = getArguments().getString("name");
            String surname =  getArguments().getString("surname");
            String img = getArguments().getString("imgProfile");

            new ProfilManageFragment.DownloadImage(imgUp).execute(img);
            txtFirst.setText(" "+name);
            txtLast.setText(" "+surname);

        }
       /* address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goPlacePicker(view);
            }
        });*/

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LoginActivity.Verif == 0){
                    UpdateUser();
                    uploadImage();
                }
                else if (LoginActivity.Verif == 1){

                    InsertFacebook();
                }


            }
        });
        imgUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        return v;

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    private String getPath(Uri uri){
        Cursor cursor = getActivity().getContentResolver().query(uri,null,null,null,null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);

        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null,
                MediaStore.Images.Media._ID + " = ?",new String[]{document_id},null
        );
        cursor.moveToFirst();

        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        return path ;
    }
    private void InsertFacebook()
    {
        String pathIm = getPath(path);
        final String Last = txtLast.getText().toString().trim();
        final String First = txtFirst.getText().toString().trim();
        final String Email = txtEmail.getText().toString().trim();
        final String Ph = phone.getText().toString().trim();
        final String Ad = address.getText().toString().trim();
        try {
            String uploadid = UUID.randomUUID().toString();
            new MultipartUploadRequest(getActivity(),uploadid,Constants.URL_INSERT_FACEBOOK)
                    .addFileToUpload(pathIm,"image")
                    .addArrayParameter("name")
                    .addArrayParameter("username",First)
                    .addArrayParameter("firstname",First)
                    .addArrayParameter("lastname",Last)
                    .addArrayParameter("email",Email)
                    .addArrayParameter("phone",Ph)
                    .addArrayParameter("address",Ad)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(7).startUpload()
            ;
            ;
        }catch (Exception e){

        }
    }
  private void uploadImage(){
        final String id = getArguments().getString("idA");
        String pathIm = getPath(path);
        try {
            String uploadid = UUID.randomUUID().toString();
            new MultipartUploadRequest(getActivity(),uploadid,Constants.URL_UPDATEImage)
                .addFileToUpload(pathIm,"image")
                    .addArrayParameter("name")
                .addArrayParameter("id",id)
                .setNotificationConfig(new UploadNotificationConfig())
                .setMaxRetries(2).startUpload()
            ;
               ;
        }catch (Exception e){

        }
    }
    private void requestStoragePermission(){

        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        return;

        ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE){

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                Toast.makeText(getActivity(),"Permission granted",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(),"Permission denied",Toast.LENGTH_SHORT).show();

            }
        }
    }
    private void UpdateUser() {
        final String Last = txtLast.getText().toString().trim();
        final String First = txtFirst.getText().toString().trim();
        final String Email = txtEmail.getText().toString().trim();
        final String Ph = phone.getText().toString().trim();
        final String Ad = address.getText().toString().trim();
        final String id = getArguments().getString("idA");


        progDiag.setMessage("Updating User ...");
        progDiag.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_UPDATE ,
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
                params.put("id",id);
                params.put("lastname", Last);
                params.put("firstname", First);
                params.put("email", Email);
                params.put("phone", Ph);
                params.put("address", Ad);
                return params;
            }
        };

        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);


    }
    private void selectImage(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMG_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK &&data!=null ){

           path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(),path);
                imgUp.setImageURI(path);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       /* if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(mActivity.getApplicationContext(),data);

                if (place != null){
                    address.setText(place.getAddress().toString());
                }

            }
        }*/
    }
   /* public void goPlacePicker(View view){

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(getActivity()),PLACE_PICKER_REQUEST);
        }catch (GooglePlayServicesRepairableException e){
            e.printStackTrace();
        }catch (GooglePlayServicesNotAvailableException e){
            e.printStackTrace();
        }
    }*/
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
