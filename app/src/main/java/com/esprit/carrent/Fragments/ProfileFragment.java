package com.esprit.carrent.Fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.esprit.carrent.Activities.LoginActivity;
import com.esprit.carrent.R;

import java.io.InputStream;


public class ProfileFragment extends Fragment {


    TextView nomPrenom,username,address,email;
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
        email = (TextView)v.findViewById(R.id.txtEmailProfil);
        profilpic = (ImageView)v.findViewById(R.id.ImgProfileDet);

        if(LoginActivity.Verif ==1){
           String name = getArguments().getString("name");
            String surname =  getArguments().getString("surname");
            String img = getArguments().getString("imgProfile");

            new ProfileFragment.DownloadImage(profilpic).execute(img);
            nomPrenom.setText(" "+name+" "+surname);
            username.setText(name);
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
