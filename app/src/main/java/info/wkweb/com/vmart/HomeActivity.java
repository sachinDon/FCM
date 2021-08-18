package info.wkweb.com.vmart;


import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public  static int image_grid_width;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    AlertDialog alert11;
    TextView textView_name,texts_logo,text_home_cartval;
    public  static  String str_count_cartval ="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        text_home_cartval =(TextView)findViewById(R.id.text_home_cartval);

        text_home_cartval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.getString("type","").equalsIgnoreCase("vendor"))
                {

                Intent intens = new Intent(HomeActivity.this,CartitemActivity.class);
                startActivity(intens);
            }
            else
                {
                Intent intens = new Intent(HomeActivity.this,CartCustomerActivity.class);
                startActivity(intens);
            }
            }
        });
        Display display = this.getWindowManager().getDefaultDisplay();
        image_grid_width = (display.getWidth()/2);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        if (pref.getString("type","").equalsIgnoreCase("vendor"))
        {
            HomeFragment  fragment = new HomeFragment();
            FragmentManager hfragmentManager = getFragmentManager();
            android.app.FragmentTransaction hfragmentTransaction = hfragmentManager.beginTransaction();
            hfragmentTransaction.replace(R.id.content_frame, fragment, MainActivity.class.getSimpleName());
            hfragmentTransaction.commit();


            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_billbook).setVisible(true);
        }
        else
        {


            CustomerFragment  custfragment = new CustomerFragment();
            FragmentManager custfragmentManager = getFragmentManager();
            android.app.FragmentTransaction custfragmentTransaction = custfragmentManager.beginTransaction();
            custfragmentTransaction.replace(R.id.content_frame, custfragment, MainActivity.class.getSimpleName());
            custfragmentTransaction.commit();
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_billbook).setVisible(false);
        }
        LocalBroadcastManager.getInstance(HomeActivity.this).registerReceiver(cartupdate,
                new IntentFilter("cartcustomerupdate"));
        LocalBroadcastManager.getInstance(HomeActivity.this).registerReceiver(cartupdate,
                new IntentFilter("cartcustomerupdate1"));
        LocalBroadcastManager.getInstance(HomeActivity.this).registerReceiver(cartupdate1,
                new IntentFilter("cartupdate"));


        new Communication_countaddcart().execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
            {
          //  super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//         getMenuInflater().inflate(R.menu.home, menu);
        textView_name=(TextView)findViewById(R.id.text_username);
        texts_logo=(TextView)findViewById(R.id.texts_logo);
        textView_name.setText(pref.getString("name",""));
        texts_logo.setText(pref.getString("name","").substring(0,1));

          return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings)
//        {
//            if (pref.getString("type","").equalsIgnoreCase("vendor")) {
//
//                Intent intens = new Intent(HomeActivity.this,CartitemActivity.class);
//                startActivity(intens);
//            }
//            else {
//                Intent intens = new Intent(HomeActivity.this,CartCustomerActivity.class);
//                startActivity(intens);
//            }
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:

                if (pref.getString("type","").equalsIgnoreCase("vendor"))
                {
                    HomeFragment  fragment = new HomeFragment();
                    FragmentManager hfragmentManager = getFragmentManager();
                    android.app.FragmentTransaction hfragmentTransaction = hfragmentManager.beginTransaction();
                    hfragmentTransaction.replace(R.id.content_frame, fragment, MainActivity.class.getSimpleName());
                    hfragmentTransaction.commit();
                }
                else
                {
                    CustomerFragment  custfragment = new CustomerFragment();
                    FragmentManager custfragmentManager = getFragmentManager();
                    android.app.FragmentTransaction custfragmentTransaction = custfragmentManager.beginTransaction();
                    custfragmentTransaction.replace(R.id.content_frame, custfragment, MainActivity.class.getSimpleName());
                    custfragmentTransaction.commit();
                }


                break;

            case R.id.nav_order:

                OrderFragment  orderfrag = new OrderFragment();
                FragmentManager orderManager = getFragmentManager();
                android.app.FragmentTransaction orderTransaction = orderManager.beginTransaction();
                orderTransaction.replace(R.id.content_frame, orderfrag, MainActivity.class.getSimpleName());
                orderTransaction.commit();
                break;

            case R.id.nav_addcart:

                if (pref.getString("type","").equalsIgnoreCase("vendor"))
                {
//                                    CartFragment  cartfarg = new CartFragment();
//                FragmentManager cartfarger = getFragmentManager();
//                android.app.FragmentTransaction cartTransaction = cartfarger.beginTransaction();
//                cartTransaction.replace(R.id.content_frame, cartfarg, MainActivity.class.getSimpleName());
//                cartTransaction.commit();

                    Intent intens = new Intent(HomeActivity.this,CartitemActivity.class);
                    startActivity(intens);
                }
                else
                {
                    Intent intens = new Intent(HomeActivity.this,CartCustomerActivity.class);
                    startActivity(intens);
                }





                break;

            case R.id.nav_delete:

                Intent intent = new Intent(HomeActivity.this,ProfileActivity.class);
                startActivity(intent);


                break;

            case R.id.nav_logout:

                editor.putString("login","no");
                editor.putString("address","no");
                JSONArray array_addAddress = new JSONArray();
                SingletonObject.Instance().setArray_MyAddress(array_addAddress);
                editor.putString("array_address",array_addAddress.toString());
                editor.commit();
                Intent intents = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(intents);
                break;

            case R.id.nav_billbook:

                Intent intensb = new Intent(HomeActivity.this,FarmerBillListActivity.class);
                startActivity(intensb);

                break;


            case R.id.nav_aboutUs:

                Intent intens = new Intent(HomeActivity.this,AboutUsActivity.class);
                startActivity(intens);

                break;
            case R.id.nav_Patners:

                Intent intens1 = new Intent(HomeActivity.this,PatnerActivity.class);
                startActivity(intens1);
                break;


        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    private BroadcastReceiver cartupdate = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent)
        {

            new Communication_countaddcart().execute();

        }
    };
    private BroadcastReceiver cartupdate1 = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent)
        {

            new Communication_countaddcart().execute();

        }
    };
    public class Communication_deleteacc extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.accountdelete);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("email", pref.getString("userid",""));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result)
        {

            progressDialog.dismiss();
            if (result !=null)
            {

                if (result.equalsIgnoreCase("deleted"))
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(HomeActivity.this);
                    builder1.setTitle("Deleted!");
                    builder1.setMessage("You account has been deleted sucessfully");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    editor.putString("login","no");
                                    editor.commit();
                                    Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                }
                            });
                    alert11 = builder1.create();
                    alert11.show();



                }  else if (result.equalsIgnoreCase("deleteerror"))

                {
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(HomeActivity.this);
                    builder3.setMessage("You Account could mot delete please contact to admin.");
                    builder3.setCancelable(false);
                    builder3.setTitle("Oops");
                    builder3.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alert11 = builder3.create();
                    alert11.show();
                }
                else if (result.equalsIgnoreCase("nullerror"))

                {
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(HomeActivity.this);
                    builder4.setTitle("Oops");
                    builder4.setMessage("Could not retrieve one of the Account Ids. Please login and try again.");
                    builder4.setCancelable(false);
                    builder4.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alert11 = builder4.create();
                    alert11.show();


                }

                    else
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(HomeActivity.this);
                        builder2.setTitle("Oops");
                        builder2.setMessage("Server could not found.");
                        builder2.setCancelable(false);
                        builder2.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        // finish();
                                    }
                                });
                        alert11 = builder2.create();
                        alert11.show();

                    }
                }
            else
            {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(HomeActivity.this);
                builder2.setTitle("Oops");
                builder2.setMessage("Server could not found.");
                builder2.setCancelable(false);
                builder2.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                // finish();
                            }
                        });
                alert11 = builder2.create();
                alert11.show();

            }

        }

        @Override
        public void onCancel(DialogInterface dialogInterface) {
            this.cancel(true);

        }
    }

    public class Communication_countaddcart extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.countcartval);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("email", pref.getString("userid",""));
                postDataParams.put("type",pref.getString("type",""));


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result)
        {


            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {



                } else if (result.equalsIgnoreCase("error"))
                {


                }
                else
                {



                    try {

                        JSONArray jsonarray = new JSONArray(result);
                        if (jsonarray.length() !=0)
                        {
                            JSONObject obj_val = new JSONObject(String.valueOf(jsonarray.getJSONObject(0)));
                            str_count_cartval=obj_val.getString("count");
                            text_home_cartval.setText(obj_val.getString("count"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            else
            {


            }

        }

        @Override
        public void onCancel(DialogInterface dialogInterface) {
            this.cancel(true);

        }
    }


    public static String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

}

