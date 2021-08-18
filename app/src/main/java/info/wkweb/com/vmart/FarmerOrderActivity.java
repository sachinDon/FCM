package info.wkweb.com.vmart;

import android.annotation.SuppressLint;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class FarmerOrderActivity extends AppCompatActivity {


    SwipeRefreshLayout swipeToRefresh_farmerbill;
    JSONArray Array_farmer;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    ListView listview_farmer;
    String str_orderid;
    ProgressDialog progressDialog;
    AlertDialog alert11;
    Integer int_cancelor;
    TextView text_result_cust_order,text_back;
    ListviewAdapter listview_customadpte;
    ProgressBar progress_order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_order);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        text_back= (TextView)findViewById(R.id.text_back_farmer);
        progress_order = (ProgressBar)findViewById(R.id.progress_farmer_order);
        text_result_cust_order= (TextView)findViewById(R.id.text_result_farmer_order);
        listview_farmer = (ListView)findViewById(R.id.listview_farmerorderlist);
        swipeToRefresh_farmerbill=(SwipeRefreshLayout) findViewById(R.id.swipeToRefresh_farmerorder);
        swipeToRefresh_farmerbill.setColorSchemeResources(R.color.color_green1);

        progress_order.setVisibility(View.INVISIBLE);
        text_result_cust_order.setVisibility(View.INVISIBLE);

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

                    new Communication_Order().execute();
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


        Array_farmer = new JSONArray();
        new Communication_Order().execute();

        text_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listview_farmer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView

                try {
                    JSONObject objectval = new JSONObject(String.valueOf(Array_farmer.getJSONObject(position)));
                    JSONArray jsonArr = new JSONArray(objectval.getString("orderdata"));
                    OrderlistActivity.Array_orderlist = jsonArr;
                    OrderlistActivity.str_viewa="farmer";
                    Intent intent = new Intent(FarmerOrderActivity.this,OrderlistActivity.class);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private class ListviewAdapter extends BaseAdapter {
        private LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);

        @Override
        public int getCount() {
            return Array_farmer.length();
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

            view = inflater.inflate(R.layout.list_farmersorder, null,false);

            final TextView text_order_name_d=(TextView)view.findViewById(R.id.text_order_name_f1);
            final TextView text_order_date=(TextView)view.findViewById(R.id.text_order_name_datef1);
            final TextView text_orderid_d=(TextView)view.findViewById(R.id.text_orderid_f1);
            final TextView text_order_total_d=(TextView)view.findViewById(R.id.text_order_total_f1);

//            final TextView text_oeder_accept=(TextView)view.findViewById(R.id.text_oeder_accept);
//            final TextView text_order_cancel_d=(TextView)view.findViewById(R.id.text_order_cancel_d);
//            final LinearLayout linear_divs=(LinearLayout)view.findViewById(R.id.linear_divs);
//            text_oeder_accept.setTag(i);

//            if (pref.getString("type","").equalsIgnoreCase("farmer"))
//            {
//                linear_divs.setVisibility(View.GONE);
//            }
//            else {
//                linear_divs.setVisibility(View.VISIBLE);
//            }

//            text_order_cancel_d.setTag(i);
//            text_order_cancel_d.setVisibility(View.INVISIBLE);
            try {
                final JSONObject object_values = new JSONObject(String.valueOf(Array_farmer.getJSONObject(i)));
                text_orderid_d.setText("Order_id: "+object_values.getString("id"));
                text_order_name_d.setText("Name: "+object_values.getString("vendorname"));
                text_order_date.setText("Date: "+object_values.getString("date"));
                text_order_total_d.setText(Html.fromHtml( " <font color=#414141>  "+"Total amount: "+ " </font> <font color=#00b33c> <b> "+"₹"+object_values.getString("netamt")+ " </b> </font>"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
//

            return view;
        }
    }

    public class Communication_ordercancel extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.farmerordercancel);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("email", pref.getString("userid",""));
                postDataParams.put("type", pref.getString("type",""));
                postDataParams.put("orderid", str_orderid);

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

            if (progressDialog !=null)
            {
                progressDialog.dismiss();
            }
            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {



                }
                else if (result.equalsIgnoreCase("deleteerror"))
                {


                }

                else if (result.equalsIgnoreCase("deleted"))
                {


                    if (Array_farmer.length() !=0)
                    {
                        Array_farmer.remove(int_cancelor);
                        listview_customadpte.notifyDataSetChanged();
                        if (Array_farmer.length() ==0)
                        {
                            text_result_cust_order.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        text_result_cust_order.setVisibility(View.VISIBLE);
                        listview_customadpte.notifyDataSetChanged();
                    }


                }
                else
                {


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


    public class Communication_Order extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.farmerorderbill);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("email", pref.getString("userid",""));
                postDataParams.put("type", pref.getString("type",""));

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
            progress_order.setVisibility(View.INVISIBLE);
            text_result_cust_order.setVisibility(View.INVISIBLE);
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
                    text_result_cust_order.setVisibility(View.VISIBLE);
                    Array_farmer = new JSONArray();

                }
                else if (result.equalsIgnoreCase("nodata"))
                {

                    Array_farmer = new JSONArray();
                    listview_customadpte.notifyDataSetChanged();
                }
                else
                {
                    if (!result.equalsIgnoreCase("") && result.length()>=50) {
                        try {
                            Array_farmer = new JSONArray(result);
                            if (Array_farmer !=null) {

                                listview_customadpte = new ListviewAdapter();
                                listview_farmer.setAdapter(listview_customadpte);

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
    public  void Connections()
    {
        android.app.AlertDialog.Builder builder4 = new android.app.AlertDialog.Builder(FarmerOrderActivity.this);
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
//    public void onBackPressed() {
//
//    }
}
