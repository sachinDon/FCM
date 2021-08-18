package info.wkweb.com.vmart;

import android.annotation.SuppressLint;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static android.content.Context.MODE_PRIVATE;


public class OrderFragment extends Fragment {

    SwipeRefreshLayout swipeToRefresh_vendor;
    JSONArray array_vegitables;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    ListView listview_customer_veg;
    String str_orderid;
    ProgressDialog progressDialog;
    AlertDialog alert11;
    Integer int_cancelor;
    TextView text_result_cust_order;
    ListviewAdaptercart listview_customadpte;
    ProgressBar progress_order;
    public OrderFragment() {
        // Required empty public constructor



    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order, container, false);
        pref = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        progress_order=(ProgressBar) view.findViewById(R.id.progress_order);
        listview_customer_veg=(ListView)view.findViewById(R.id.listview_orderlist);
        text_result_cust_order=(TextView) view.findViewById(R.id.text_result_cust_order);
        text_result_cust_order.setVisibility(View.INVISIBLE);
        swipeToRefresh_vendor=(SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh_vendor);
        swipeToRefresh_vendor.setColorSchemeResources(R.color.color_green1);
        str_orderid="";
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(ordercancel,
                new IntentFilter("ordercancel"));

        swipeToRefresh_vendor.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                swipeToRefresh_vendor.setRefreshing(true);
                new Communication_Order().execute();



            }
        });


        listview_customer_veg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView

                try {
                    JSONObject objectval = new JSONObject(String.valueOf(array_vegitables.getJSONObject(position)));
                    JSONArray jsonArr = new JSONArray(objectval.getString("orderdata"));
                      OrderlistActivity.Array_orderlist = jsonArr;
                      int_cancelor = position;

                    Intent intent = new Intent(getActivity(),OrderlistActivity.class);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        array_vegitables = new JSONArray();
        new Communication_Order().execute();

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    private class ListviewAdaptercart extends BaseAdapter {
        private LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);

        @Override
        public int getCount() {
            return array_vegitables.length();
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

            view = inflater.inflate(R.layout.list_order_list, null,false);

           final TextView text_orderid=(TextView)view.findViewById(R.id.text_orderid);
            final TextView text_order_date=(TextView)view.findViewById(R.id.text_order_date);
           final TextView text_order_total=(TextView)view.findViewById(R.id.text_order_total);
           final TextView text_order_status=(TextView)view.findViewById(R.id.text_order_status);
           final TextView text_order_paymentmode=(TextView)view.findViewById(R.id.text_order_paymentmode);
            final TextView text_oeder_details=(TextView)view.findViewById(R.id.text_oeder_details);
            final TextView text_order_cancel=(TextView)view.findViewById(R.id.text_order_cancel);



            text_order_cancel.setTextColor(getResources().getColor(R.color.white));
            GradientDrawable bgShape1 = (GradientDrawable)text_order_cancel.getBackground();
            bgShape1.mutate();
            bgShape1.setColor(getResources().getColor(R.color.red));
            bgShape1.setStroke(text_order_cancel.getWidth(),getResources().getColor(R.color.red));

            text_oeder_details.setTag(i);
            text_order_cancel.setTag(i);
            if (pref.getString("type","").equalsIgnoreCase("cust"))
            {
                text_order_paymentmode.setVisibility(View.VISIBLE);
            }

            try {
                final JSONObject object_values = new JSONObject(String.valueOf(array_vegitables.getJSONObject(i)));
                text_orderid.setText("Order_id: "+object_values.getString("orderid"));
                text_order_date.setText("Date: "+object_values.getString("date"));
              //  text_order_status.setText("Status: "+object_values.getString("status"));
                if (object_values.getString("paymentmode").equalsIgnoreCase("cod"))
                {
                   // text_order_paymentmode.setText("Payment: Cash On Dilevery");
                    text_order_paymentmode.setText(Html.fromHtml( " <font color=#414141>  "+"Payment: "+ " </font> <font color=#00b33c> <b> "+"Cash On Dilevery"+ " </b> </font>"));

                }
                else
                {
                   // text_order_paymentmode.setText("Payment: Online");
                    text_order_paymentmode.setText(Html.fromHtml( " <font color=#414141>  "+"Payment: "+ " </font> <font color=#00b33c> <b> "+"Online"+ " </b> </font>"));

                }

                if (object_values.getString("status").equalsIgnoreCase("Pending"))
                {
                    text_order_status.setText(Html.fromHtml( " <font color=#414141>  "+"Status: "+ " </font> <font color=#ff0000> <b> "+object_values.getString("status")+ " </b> </font>"));

                }
                else
                {
                    text_order_status.setText(Html.fromHtml( " <font color=#414141>  "+"Status: "+ " </font> <font color=#00b33c> <b> "+object_values.getString("status")+ " </b> </font>"));

                }
                text_order_total.setText(Html.fromHtml( " <font color=#414141>  "+"Total amount: "+ " </font> <font color=#00b33c> <b> "+"₹"+object_values.getString("total")+ " </b> </font>"));

                text_oeder_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            JSONObject objectval = new JSONObject(String.valueOf(array_vegitables.getJSONObject((Integer)v.getTag())));
                            JSONArray jsonArr = new JSONArray(objectval.getString("orderdata"));
                            OrderlistActivity.Array_orderlist = jsonArr;
                            int_cancelor = (Integer)v.getTag();

                            Intent intent = new Intent(getActivity(),OrderlistActivity.class);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });


                text_order_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            JSONObject objectval = new JSONObject(String.valueOf(array_vegitables.getJSONObject((Integer)v.getTag())));

                            str_orderid = objectval.getString("orderid");
                            int_cancelor = (Integer)v.getTag();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setMessage("Cancel..."); // Setting Message
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                        progressDialog.show(); // Display Progress Dialog
                        progressDialog.setCancelable(false);
                        new Communication_ordercancel().execute();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return view;
        }
    }

    private BroadcastReceiver ordercancel = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent)
        {

            if (array_vegitables.length() !=0)
            {
                array_vegitables.remove(int_cancelor);
                if(array_vegitables.length() ==0)
                {
                    text_result_cust_order.setVisibility(View.VISIBLE);
                }
            }
            else
            {
                text_result_cust_order.setVisibility(View.VISIBLE);
            }
            listview_customadpte.notifyDataSetChanged();

        }
    };

    public class Communication_ordercancel extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.ordercancel);

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


                    if (array_vegitables.length() !=0)
                    {
                        array_vegitables.remove(int_cancelor);
                        listview_customadpte.notifyDataSetChanged();
                        if (array_vegitables.length() ==0)
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

                URL url = new URL(Urlclass.venderorderlist);

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
            swipeToRefresh_vendor.setRefreshing(false);
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

                }
                else
                {
                    if (!result.equalsIgnoreCase("") && result.length()>=50) {
                        try {
                            array_vegitables = new JSONArray(result);
                            if (array_vegitables !=null) {

                                listview_customadpte = new ListviewAdaptercart();
                                listview_customer_veg.setAdapter(listview_customadpte);

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
//    public void onBackPressed() {
//
//    }
}
