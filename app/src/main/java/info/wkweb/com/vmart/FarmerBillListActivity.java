package info.wkweb.com.vmart;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
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

import javax.net.ssl.HttpsURLConnection;

public class FarmerBillListActivity extends AppCompatActivity {


    JSONArray Array_farmerbillslist;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    ListView listview_farmerlist;
    SwipeRefreshLayout swipeToRefresh_farmerbill;

    ProgressDialog progressDialog;
    AlertDialog alert11;
    TextView text_result_farmer,text_back_farmersbills12;
    ListviewAdaptercart listview_farmerlist_adapter;
    ProgressBar progress_farmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_bill_list);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        text_back_farmersbills12=(TextView) findViewById(R.id.text_back_farmersbills12);
        progress_farmer=(ProgressBar) findViewById(R.id.progress_farmerlist1);
        listview_farmerlist=(ListView)findViewById(R.id.listview_farmerlist1);
        text_result_farmer=(TextView) findViewById(R.id.text_result_farmer1);
        swipeToRefresh_farmerbill=(SwipeRefreshLayout) findViewById(R.id.swipeToRefresh_farmerbill);
        swipeToRefresh_farmerbill.setColorSchemeResources(R.color.color_green1);
        text_result_farmer.setVisibility(View.INVISIBLE);

        text_back_farmersbills12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        listview_farmerlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView

                try {
                    JSONObject objectval = new JSONObject(String.valueOf(Array_farmerbillslist.getJSONObject(position)));
                    JSONArray jsonArr = new JSONArray(objectval.getString("orderdata"));
                    FarmerBillActivity2.Array_bill = jsonArr;


                    Intent intent = new Intent(FarmerBillListActivity.this,FarmerBillActivity2.class);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        swipeToRefresh_farmerbill.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {

                ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    swipeToRefresh_farmerbill.setRefreshing(true);
                    new Communication_farmerbilllist().execute();
                }
                else
                {
                    swipeToRefresh_farmerbill.setRefreshing(false);
                    Connections();
                }

                // Your implementation goes here
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//                }).start();


            }
        });

        Array_farmerbillslist = new JSONArray();
        new Communication_farmerbilllist().execute();
    }

    public class Communication_farmerbilllist extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.venderorderbill);

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
            progress_farmer.setVisibility(View.INVISIBLE);
            text_result_farmer.setVisibility(View.INVISIBLE);
            swipeToRefresh_farmerbill.setRefreshing(false);
            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {



                }
                else if (result.equalsIgnoreCase("error"))
                {


                }
                else if (result.equalsIgnoreCase("[]"))
                {
                    text_result_farmer.setVisibility(View.VISIBLE);

                }
                else
                {
                    if (!result.equalsIgnoreCase("") && result.length()>=50) {
                        try {
                            Array_farmerbillslist = new JSONArray(result);
                            if (Array_farmerbillslist !=null) {

                                listview_farmerlist_adapter = new ListviewAdaptercart();
                                listview_farmerlist.setAdapter(listview_farmerlist_adapter);

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

    private class ListviewAdaptercart extends BaseAdapter {
        private LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);

        @Override
        public int getCount() {
            return Array_farmerbillslist.length();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = inflater.inflate(R.layout.list_farmerbilllist_name, null,false);


            final TextView text_name=(TextView)view.findViewById(R.id.text_farmerbill_name11);
            final TextView text_farmer_date11=(TextView)view.findViewById(R.id.text_farmer_date11);
            final TextView text_farmer_totals11=(TextView)view.findViewById(R.id.text_farmer_totals11);


            text_name.setTag(i);
            text_farmer_totals11.setTag(i);

            try {
                final JSONObject object_values = new JSONObject(String.valueOf(Array_farmerbillslist.getJSONObject(i)));
                text_farmer_date11.setText(object_values.getString("date"));
                text_name.setText(object_values.getString("invoiceno"));
                text_farmer_totals11.setText(object_values.getString("netamt"));


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return view;
        }
    }
    public  void Connections()
    {
        android.app.AlertDialog.Builder builder4 = new android.app.AlertDialog.Builder(FarmerBillListActivity.this);
        builder4.setTitle("No Internet");
        builder4.setMessage("Your internet connection not available");
        builder4.setCancelable(false);
        builder4.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        alert11 = builder4.create();
        alert11.show();
    }
}