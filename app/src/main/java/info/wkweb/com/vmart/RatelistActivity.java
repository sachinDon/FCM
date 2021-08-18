package info.wkweb.com.vmart;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class RatelistActivity extends AppCompatActivity {

    DatePickerDialog picker;
    JSONArray Array_custrate;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    ListView listview_custrate;

    ProgressDialog progressDialog;
    AlertDialog alert11;
    TextView text_result_custrate,text_back_custrate,text_dateset_custrate;
    ListviewAdaptercart listview_custrate_adapter;
    ProgressBar progress_farmer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratelist);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        text_back_custrate=(TextView) findViewById(R.id.text_back_custrate);
        progress_farmer=(ProgressBar) findViewById(R.id.progress_custrate);
        listview_custrate=(ListView)findViewById(R.id.listview_custrate);
        text_result_custrate=(TextView) findViewById(R.id.text_result_custrate);
        text_dateset_custrate=(TextView) findViewById(R.id.text_dateset_custrate);
        text_result_custrate.setVisibility(View.INVISIBLE);
        progress_farmer.setVisibility(View.VISIBLE);


        String current_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        text_dateset_custrate.setText(current_date);
        text_back_custrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        text_dateset_custrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                final Calendar cldr = Calendar.getInstance();
//                int day = cldr.get(Calendar.DAY_OF_MONTH);
//                int month = cldr.get(Calendar.MONTH);
//                int year = cldr.get(Calendar.YEAR);

                // date picker dialog

//                Calendar c = Calendar.getInstance();
//                int year = c.get(Calendar.YEAR);
//                int month = c.get(Calendar.MONTH);
//                int day = c.get(Calendar.DAY_OF_MONTH);

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                try {
                    cal.setTime(sdf.parse(String.valueOf(text_dateset_custrate.getText())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                picker = new DatePickerDialog(RatelistActivity.this,
                        new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                            {
                                String dates_days,dates_month;
                                if (dayOfMonth<=9)
                                {
                                    dates_days = "0"+String.valueOf(dayOfMonth);
                                }
                                else
                                {
                                    dates_days= String.valueOf(dayOfMonth);
                                }
                                if ((monthOfYear + 1)<=9)
                                {
                                    dates_month = "0"+String.valueOf((monthOfYear + 1));
                                }
                                else
                                {
                                    dates_month= String.valueOf((monthOfYear + 1));
                                }
                                text_dateset_custrate.setText(year + "-" + dates_month + "-" + dates_days);
                                progress_farmer.setVisibility(View.VISIBLE);
                                text_result_custrate.setVisibility(View.INVISIBLE);
                                new Communication_custrateiems().execute();
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        Array_custrate = new JSONArray();
        new Communication_custrateiems().execute();

    }

    public class Communication_custrateiems extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.customerrate);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("email", pref.getString("userid",""));
                postDataParams.put("date", text_dateset_custrate.getText());


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
            text_result_custrate.setVisibility(View.INVISIBLE);
            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {



                }
                else if (result.equalsIgnoreCase("error"))
                {


                }
                else if (result.equalsIgnoreCase("nodata"))
                {

                    text_result_custrate.setVisibility(View.VISIBLE);
                    Array_custrate = new JSONArray();
                    listview_custrate_adapter = new ListviewAdaptercart();
                    listview_custrate.setAdapter(listview_custrate_adapter);

                }
                else if (result.equalsIgnoreCase("[]"))
                {
                    Array_custrate = new JSONArray();
                    listview_custrate_adapter = new ListviewAdaptercart();
                    listview_custrate.setAdapter(listview_custrate_adapter);
                    text_result_custrate.setVisibility(View.VISIBLE);

                }
                else
                {
                    if (!result.equalsIgnoreCase("") && result.length()>=50) {
                        try {
                            Array_custrate = new JSONArray(result);
                            if (Array_custrate !=null) {

                                listview_custrate_adapter = new ListviewAdaptercart();
                                listview_custrate.setAdapter(listview_custrate_adapter);

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
            return Array_custrate.length();
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

            view = inflater.inflate(R.layout.list_cust_rate, null,false);


            final TextView text_name=(TextView)view.findViewById(R.id.text_items_name1_custrate);
            final TextView text_cust_rate=(TextView)view.findViewById(R.id.text_rate1_custrate);
            final TextView text_cust_vendor=(TextView)view.findViewById(R.id.text_vendor_custrate);




            try {
                final JSONObject object_values = new JSONObject(String.valueOf(Array_custrate.getJSONObject(i)));
                text_name.setText(object_values.getString("itemname"));
                text_cust_rate.setText("₹"+object_values.getString("custrate"));
                text_cust_vendor.setText("₹"+object_values.getString("vrate"));


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return view;
        }
    }
}