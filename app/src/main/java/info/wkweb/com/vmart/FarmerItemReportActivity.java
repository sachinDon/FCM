package info.wkweb.com.vmart;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class FarmerItemReportActivity extends AppCompatActivity {

    DatePickerDialog picker;
    JSONArray Array_farmeritem;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    ListView listview_farmerlist;

    ProgressDialog progressDialog;
    AlertDialog alert11;
    TextView text_result_farmer,text_back_farmer,text_dateset_farmeritem;
    ListviewAdaptercart listview_farmerlist_adapter;
    ProgressBar progress_farmer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_item_report);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        text_back_farmer=(TextView) findViewById(R.id.text_back_farmeritem);
        progress_farmer=(ProgressBar) findViewById(R.id.progress_farmeritem);
        listview_farmerlist=(ListView)findViewById(R.id.listview_farmeritem);
        text_result_farmer=(TextView) findViewById(R.id.text_result_farmeritem);
        text_dateset_farmeritem=(TextView) findViewById(R.id.text_dateset_farmeritem);
        text_result_farmer.setVisibility(View.INVISIBLE);
        progress_farmer.setVisibility(View.VISIBLE);


        String current_date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        text_dateset_farmeritem.setText(current_date);
        text_back_farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        text_dateset_farmeritem.setOnClickListener(new View.OnClickListener() {
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
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                try {
                    cal.setTime(sdf.parse(String.valueOf(text_dateset_farmeritem.getText())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                picker = new DatePickerDialog(FarmerItemReportActivity.this,
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
                                text_dateset_farmeritem.setText(dates_days + "-" + dates_month + "-" + year);
                                progress_farmer.setVisibility(View.VISIBLE);
                                text_result_farmer.setVisibility(View.INVISIBLE);
                                new Communication_farmeriems().execute();
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        Array_farmeritem = new JSONArray();
        new Communication_farmeriems().execute();
    }

    public class Communication_farmeriems extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.farmeritemreport);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("email", pref.getString("userid",""));
                postDataParams.put("date", text_dateset_farmeritem.getText());


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

                    text_result_farmer.setVisibility(View.VISIBLE);
                    Array_farmeritem = new JSONArray();
                        listview_farmerlist_adapter = new ListviewAdaptercart();
                        listview_farmerlist.setAdapter(listview_farmerlist_adapter);

                }
                else if (result.equalsIgnoreCase("[]"))
                {
                    Array_farmeritem = new JSONArray();
                    listview_farmerlist_adapter = new ListviewAdaptercart();
                    listview_farmerlist.setAdapter(listview_farmerlist_adapter);
                    text_result_farmer.setVisibility(View.VISIBLE);

                }
                else
                {
                    if (!result.equalsIgnoreCase("") && result.length()>=50) {
                        try {
                            Array_farmeritem = new JSONArray(result);
                            if (Array_farmeritem !=null) {

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
            return Array_farmeritem.length();
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

            view = inflater.inflate(R.layout.list_farmerbillrow, null,false);


            final TextView text_name=(TextView)view.findViewById(R.id.text_items_name1);
            final TextView text_farmer_rate=(TextView)view.findViewById(R.id.text_rate1);
            final TextView text_farmer_oty=(TextView)view.findViewById(R.id.text_qty1);
            final TextView text_farmer_totals=(TextView)view.findViewById(R.id.text_totalfarmer1);




            try {
                final JSONObject object_values = new JSONObject(String.valueOf(Array_farmeritem.getJSONObject(i)));
                text_name.setText(object_values.getString("pname"));
                text_farmer_rate.setText(object_values.getString("faramt"));
                text_farmer_oty.setText(object_values.getString("qty"));
                text_farmer_totals.setText(object_values.getString("avgamt"));


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return view;
        }
    }
}