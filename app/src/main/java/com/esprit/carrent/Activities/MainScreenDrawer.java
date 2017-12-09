package com.esprit.carrent.Activities;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.esprit.carrent.Fragments.ProfilManageFragment;
import com.esprit.carrent.Fragments.ProfileFragment;
import com.esprit.carrent.R;
import com.esprit.carrent.Utils.SharedPrefManager;
import com.facebook.FacebookSdk;

import java.io.InputStream;

public class MainScreenDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main_screen_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle inBundle = getIntent().getExtras();
        String name = inBundle.getString("name");
        String surnamename = inBundle.getString("surname");
        String img = inBundle.getString("imageUrl");
        String username = inBundle.getString("username");






        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        if(LoginActivity.Verif == 1){
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            TextView nomPrenomTxt = (TextView)headerView.findViewById(R.id.nomPrenom);
            ImageView a = (ImageView)headerView.findViewById(R.id.ImageProfile);
            nomPrenomTxt.setText(""+ name + " " +surnamename);
            new MainScreenDrawer.DownloadImage(a).execute(img);
            navigationView.setNavigationItemSelectedListener(this);
        }
        else if (LoginActivity.Verif == 0){
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            TextView nomPrenomTxt = (TextView)headerView.findViewById(R.id.nomPrenom);
            ImageView a = (ImageView)headerView.findViewById(R.id.ImageProfile);
            a.setVisibility(View.INVISIBLE);
            nomPrenomTxt.setText(SharedPrefManager.getInstance(this).getUsername());
            navigationView.setNavigationItemSelectedListener(this);

        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen_drawer, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profilebtn) {
            // Handle the camera action

            Bundle inBundle = getIntent().getExtras();
            String name = inBundle.getString("name");
            String surnamename = inBundle.getString("surname");
            String img = inBundle.getString("imageUrl");

            Bundle pFr = new Bundle();
            pFr.putString("name",name);
            pFr.putString("surname",surnamename);
            pFr.putString("imgProfile",img);


            Fragment frP = new ProfileFragment();
            frP.setArguments(pFr);
            getFragmentManager().beginTransaction().replace(R.id.containerfr, frP).commit();
        } else if (id == R.id.reservationsbtn) {

        }
        else if (id == R.id.gerer) {

            Bundle bndle ;
            bndle = getIntent().getExtras();
            String idU = bndle.getString("id");
            Bundle inBundle = getIntent().getExtras();
            String name = inBundle.getString("name");
            String surnamename = inBundle.getString("surname");
            String img = inBundle.getString("imageUrl");
            Bundle bFr = new Bundle();
            bFr.putString("idA",idU);
            bFr.putString("name",name);
            bFr.putString("surname",surnamename);
            bFr.putString("imgProfile",img);
            Fragment fr = new ProfilManageFragment();
            fr.setArguments(bFr);
            getFragmentManager().beginTransaction().replace(R.id.containerfr, fr).commit();

        }
        else if (id == R.id.logoutdraw) {
            SharedPrefManager.getInstance(this).logout();
            finish();
            startActivity(new Intent(this ,LoginActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
   public class DownloadImage extends AsyncTask<String,Void,Bitmap>{
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
