package info.wkweb.com.vmart;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class LocationsActivity extends AppCompatActivity {

    private static final Location TODO = null;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    JSONArray array_locations;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    ListView listview_locations;
    ListviewAdaptercart listview_customadpte;
    public  static  String str_viewact="",str_email="",str_mobile="",str_address="",str_name;
    TextView text_back_location,text_result_location,text_addd_save22;
    ProgressBar progress_location;

    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    Location location;
    Double MyLat, MyLong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        pref = LocationsActivity.this.getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        progress_location=(ProgressBar)findViewById(R.id.progress_location);
        listview_locations=(ListView)findViewById(R.id.listview_locationslist);
        text_back_location =(TextView)findViewById(R.id.text_back_location);
        text_result_location =(TextView)findViewById(R.id.text_result_location);
        text_addd_save22=(TextView)findViewById(R.id.text_addd_save22);




        if (str_viewact.equalsIgnoreCase("order"))
        {
            str_mobile=ReviewOrderActivity.str_mobile ;
            str_address=ReviewOrderActivity.str_address ;
            str_name=ReviewOrderActivity.str_name ;
            str_email=ReviewOrderActivity.str_email;
            text_addd_save22.setVisibility(View.VISIBLE);
        }
        else
        {
            text_addd_save22.setVisibility(View.GONE);
            str_mobile="" ;
            str_address="";
            str_name="";
            str_email="";

        }

        text_result_location.setVisibility(View.INVISIBLE);
        location_permission_checked();

        text_back_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listview_locations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView

//                try {
//                    JSONObject objectval = new JSONObject(String.valueOf(array_locations.getJSONObject(position)));
//                    JSONArray jsonArr = new JSONArray(objectval.getString("orderdata"));
//                    OrderlistActivity.Array_orderlist = jsonArr;
//

//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }
        });

        text_addd_save22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ReviewOrderActivity.str_mobile = str_mobile;
                ReviewOrderActivity.str_address = str_address;
                ReviewOrderActivity.str_name = str_name;
                ReviewOrderActivity.str_email = str_email;

                Intent intent1 = new Intent("updateaddressreview1");
                LocalBroadcastManager.getInstance(LocationsActivity.this).sendBroadcast(intent1);
                finish();

            }
        });

        array_locations = new JSONArray();
        new Communication_Locations().execute();
    }


    private class ListviewAdaptercart extends BaseAdapter {
        private LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(getApplication().LAYOUT_INFLATER_SERVICE);

        @Override
        public int getCount() {
            return array_locations.length();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = getLayoutInflater().inflate(R.layout.location_list, null,false);


          final TextView text_location_name=(TextView)view.findViewById(R.id.text_location_name);
            final TextView text_location_address=(TextView)view.findViewById(R.id.text_location_address);
            final TextView text_location_Distance=(TextView)view.findViewById(R.id.text_location_Distance);
            final TextView text_location_info=(TextView)view.findViewById(R.id.text_location_info);
            final TextView text_location_stock =(TextView)view.findViewById(R.id.text_location_stock);
            final TextView text_location_stock1 =(TextView)view.findViewById(R.id.text_location_stock1);
            final TextView text_location_selects =(TextView)view.findViewById(R.id.text_location_selects);





            text_location_info.setTag(i);
            text_location_stock.setTag(i);
            text_location_selects.setTag(i);




            text_location_selects.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    try {
                        JSONObject object_values = new JSONObject(String.valueOf(array_locations.getJSONObject((Integer) v.getTag())));

                        str_email=object_values.getString("email");
                        str_address=object_values.getString("address");
                        str_mobile=object_values.getString("mobile");
                        str_name=object_values.getString("name");
                        listview_customadpte.notifyDataSetChanged();;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });

            text_location_stock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
//                    try {
//                        JSONObject object_values = new JSONObject(String.valueOf(array_locations.getJSONObject((Integer) v.getTag())));
//
//                        LocationInfoActivity.d_latitude = Double.parseDouble(object_values.getString("lat"));
//                        LocationInfoActivity.d_longitude = Double.parseDouble(object_values.getString("long"));
//                        LocationInfoActivity.str_name = object_values.getString("name")+"\n"+object_values.getString("address");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    Intent intent = new Intent(LocationsActivity.this,StockAvailabelActivity.class);
                    startActivity(intent);

                }
            });

            text_location_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    try {
                        JSONObject object_values = new JSONObject(String.valueOf(array_locations.getJSONObject((Integer) v.getTag())));

                    LocationInfoActivity.d_latitude = Double.parseDouble(object_values.getString("lat"));
                    LocationInfoActivity.d_longitude = Double.parseDouble(object_values.getString("long"));
                        LocationInfoActivity.str_name = object_values.getString("name")+"\n"+object_values.getString("address");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(LocationsActivity.this,LocationInfoActivity.class);
   startActivity(intent);

                }
            });

            try {
                JSONObject object_values = new JSONObject(String.valueOf(array_locations.getJSONObject(i)));
                text_location_name.setText("Name: "+object_values.getString("name"));
                text_location_address.setText(object_values.getString("address")+"\n"+"Phone: "+object_values.getString("mobile"));
//                text_location_Distance.setText("Status: "+object_values.getString("status"));
//

                if (str_viewact.equalsIgnoreCase("order"))
                {
                    text_location_selects.setVisibility(View.VISIBLE);
                    text_location_info.setVisibility(View.INVISIBLE);
                    text_location_stock.setVisibility(View.INVISIBLE);
                    text_location_stock1.setVisibility(View.INVISIBLE);

                    if ( str_email.equalsIgnoreCase(object_values.getString("email")))
                    {
                        text_location_selects.setBackgroundResource(R.drawable.check);
                    }
                    else
                    {
                        text_location_selects.setBackgroundResource(R.drawable.unchecked);
                    }
                }
                else
                {
                    text_location_selects.setVisibility(View.INVISIBLE);
                    text_location_info.setVisibility(View.VISIBLE);
                    text_location_stock.setVisibility(View.VISIBLE);
                    text_location_stock1.setVisibility(View.VISIBLE);

                }


                if(MyLat !=null ) {
                    Location startPoint = new Location("locationA");
                    startPoint.setLatitude(MyLat);
                    startPoint.setLongitude(MyLong);

                    Location endPoint = new Location("locationA");
                    endPoint.setLatitude(Double.parseDouble(object_values.getString("lat")));
                    endPoint.setLongitude(Double.parseDouble(object_values.getString("long")));

                    double distance = startPoint.distanceTo(endPoint);
                    int value = (int)distance;
                    if (value <=999)
                    {

                        text_location_Distance.setText("Distance: "+String.valueOf(value)+"m");
                    }
                    else

                    {
                        text_location_Distance.setText("Distance: "+String.valueOf(value/1000)+"Km");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return view;
        }
    }
    public class Communication_Locations extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {




            try {

                URL url = new URL(Urlclass.getlocationlist);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("email", pref.getString("userid",""));
                postDataParams.put("lat", "18.991044");
                postDataParams.put("long","73.116646");

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
            progress_location.setVisibility(View.INVISIBLE);

            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {



                }
                else if (result.equalsIgnoreCase("error"))

                {

                    text_result_location.setVisibility(View.INVISIBLE);
                }
                else if (result.equalsIgnoreCase("nodata"))

                {

                    text_result_location.setVisibility(View.VISIBLE);
                }
                else
                {
                    if (!result.equalsIgnoreCase("") && result.length()>=50) {
                        try {
                            array_locations = new JSONArray(result);
                            if (array_locations !=null) {

                                listview_customadpte = new ListviewAdaptercart();
                                listview_locations.setAdapter(listview_customadpte);

                            }
                            else
                            {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {


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

    void location_permission_checked()
    {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)  {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Location Permission");
                builder.setMessage("The app needs location permissions. Please grant this permission to continue using the features of the app.");
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (ActivityCompat.checkSelfPermission(LocationsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)  {



                            ActivityCompat.requestPermissions(LocationsActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                        }

                    }
                });
                //requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_REQUEST_COARSE_LOCATION);}});
                builder.setNegativeButton(android.R.string.no, null);
                builder.show();
            } else {


                getMyCurrentLocation();
            }
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean isGpsProviderEnabled, isNetworkProviderEnabled;
            isGpsProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkProviderEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGpsProviderEnabled && !isNetworkProviderEnabled) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Location sachin Permission");
                builder.setMessage("The app needs location permissions. Please grant this permission to continue using the features of the app.");
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton(android.R.string.no, null);
                builder.show();
            } else {

                getMyCurrentLocation();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  getMyCurrentLocation();


                }
            }
        }


        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {


            }
        }


    }

    /** Check the type of GPS Provider available at that instance and  collect the location informations
     @Output Latitude and Longitude
      * */
    void getMyCurrentLocation() {

        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locListener = new MyLocationListener();
        try {
            gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        //don't start listeners if no provider is enabled

        //if(!gps_enabled && !network_enabled)

        //return false;

        if (gps_enabled) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);

        }


        if (gps_enabled) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }

        if (network_enabled && location == null) {

            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);

        }
        if (network_enabled && location == null) {
            location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        }

        if (location != null) {

            MyLat = location.getLatitude();
            MyLong = location.getLongitude();
            new Communication_Locations().execute();
//            ListviewAdaptercart listview_customadpte = new ListviewAdaptercart();
//            listview_locations.setAdapter(listview_customadpte);


        } else {
            Location loc = getLastKnownLocation(this);
            if (loc != null) {
                MyLat = loc.getLatitude();
                MyLong = loc.getLongitude();

                ListviewAdaptercart listview_customadpte = new ListviewAdaptercart();
                listview_locations.setAdapter(listview_customadpte);
                new Communication_Locations().execute();
            }
        }

        locManager.removeUpdates(locListener); // removes the periodic updates from location listener to avoid battery drainage. If you want to get location at the periodic intervals call this method using pending intent.

        try {
// Getting address from found locations.
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
         //   addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
//            StateName = addresses.get(0).getAdminArea();
//            CityName = addresses.get(0).getLocality();
//            CountryName = addresses.get(0).getCountryName();
            // you can get more details other than this . like country code, state code, etc.

//            if (CityName.length() != 0 && CountryName.length() != 0) {
//                editor.putString("city1", CityName);
//                editor.putString("country1", CountryName);
//                editor.commit();
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            if (location != null)
            {

                MyLat = location.getLatitude();
                MyLong = location.getLongitude();



//                ListviewAdaptercart listview_customadpte = new ListviewAdaptercart();
//                listview_locations.setAdapter(listview_customadpte);
            }
        }

        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }


// below method to get the last remembered location. because we don't get locations all the times .At some instances we are unable to get the location from GPS. so at that moment it will show us the last stored location.

    public Location getLastKnownLocation(Context context)
    {
        Location location = null;
        @SuppressLint("WrongConstant") LocationManager locationmanager = (LocationManager) context.getSystemService("location");
        List list = locationmanager.getAllProviders();
        boolean i = false;
        Iterator iterator = list.iterator();
        do {
            //System.out.println("---------------------------------------------------------------------");
            if (!iterator.hasNext())
                break;
            String s = (String) iterator.next();
            //if(i != 0 && !locationmanager.isProviderEnabled(s))
            if (i != false && !locationmanager.isProviderEnabled(s))
                continue;
            // System.out.println("provider ===> "+s);
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return TODO;
            }
            Location location1 = locationmanager.getLastKnownLocation(s);
            if(location1 == null)
                continue;
            if(location != null)
            {
                //System.out.println("location ===> "+location);
                //System.out.println("location1 ===> "+location);
                float f = location.getAccuracy();
                float f1 = location1.getAccuracy();
                if(f >= f1)
                {
                    long l = location1.getTime();
                    long l1 = location.getTime();
                    if(l - l1 <= 600000L)
                        continue;
                }
            }
            location = location1;
            // System.out.println("location  out ===> "+location);
            //System.out.println("location1 out===> "+location);
            i = locationmanager.isProviderEnabled(s);
            // System.out.println("---------------------------------------------------------------------");
        } while(true);
        return location;

    }

//    public void onBackPressed() {
//
//    }
}
