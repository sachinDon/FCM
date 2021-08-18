package info.wkweb.com.vmart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;

public class LocationInfoActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    TextView text_back_locinfo;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    GoogleMap mMap1;
    Bitmap smallMarker1;
    public  static  double d_latitude,d_longitude;
    public  static  String str_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_info);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        text_back_locinfo= (TextView)findViewById(R.id.text_back_locinfo);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapCord);
        mapFragment.getMapAsync(this);

        text_back_locinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }

    @Override
    public void onConnected( Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed( ConnectionResult connectionResult) {

    }

    @Override
    public void onPolygonClick(Polygon polygon) {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap1 = googleMap;
        mMap1.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        CameraUpdate center =
                CameraUpdateFactory.newLatLng(new LatLng(d_latitude,
                        d_longitude));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);

        mMap1.moveCamera(center);
        mMap1.animateCamera(zoom);
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.markerloc);
        Bitmap b = bitmapdraw.getBitmap();
        smallMarker1 = Bitmap.createScaledBitmap(b, 96, 96, false);
        LatLng latlong = new LatLng(d_latitude, d_longitude);
        Marker melbourne = mMap1.addMarker(new MarkerOptions()
                .position(latlong)
                .title("")
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker1)));



        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

//                Map_Latitude = cameraPosition.target.latitude;
//                Map_Lonigude = cameraPosition.target.longitude;

                Log.d("centerLat", cameraPosition.target.latitude + "");

                Log.d("centerLong", cameraPosition.target.longitude + "");


            }
        });


        mMap1.getUiSettings().setMapToolbarEnabled(false);

        mMap1.setInfoWindowAdapter(setMarkerWindow());
    }

    private GoogleMap.InfoWindowAdapter setMarkerWindow() {
        return new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker)

            {




                    final View myContentView = getLayoutInflater().inflate(
                            R.layout.map_marker_info, null);
                    myContentView.setBackgroundColor(getResources().getColor(R.color.transperent));

                    TextView tvTitle = (TextView) myContentView.findViewById(R.id.txt_markertitle);
                    tvTitle.setText(str_name);
                    final String title = marker.getTitle();
//#FFE61D

                    return myContentView;

            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        };
    }


//    @Override
//    public void onBackPressed() {
//
//    }
}
