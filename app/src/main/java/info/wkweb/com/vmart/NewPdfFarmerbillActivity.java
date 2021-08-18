package info.wkweb.com.vmart;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class NewPdfFarmerbillActivity extends AppCompatActivity {


    JSONArray Array_farmeritem;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    ListView listview_farmerlist;

    ProgressDialog progressDialog;
    AlertDialog alert11;
    TextView text_result_farmer,text_back_farmer;
    ListviewAdaptercart listview_farmerlist_adapter;
    ProgressBar progress_farmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pdf_farmerbill);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        text_back_farmer=(TextView) findViewById(R.id.text_back_farmeritem_pd);
        progress_farmer=(ProgressBar) findViewById(R.id.progress_farmeritem_pd);
        listview_farmerlist=(ListView)findViewById(R.id.listview_farmeritem_pd);
        text_result_farmer=(TextView) findViewById(R.id.text_result_farmeritem_pd);

        text_result_farmer.setVisibility(View.INVISIBLE);
        progress_farmer.setVisibility(View.VISIBLE);



        text_back_farmer.setOnClickListener(new View.OnClickListener() {
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
                    JSONObject objectval = new JSONObject(String.valueOf(Array_farmeritem.getJSONObject(position)));
                    String str_pdfpath = objectval.getString("path");
                    FarmerBillShowPdfActivity.str_pdfpath = str_pdfpath;
//                    Intent intent = new Intent(NewPdfFarmerbillActivity.this,FarmerBillShowPdfActivity.class);
//                    startActivity(intent);

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(str_pdfpath));
                    startActivity(browserIntent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

                URL url = new URL(Urlclass.farmerpdfbill);

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
                            if (Array_farmeritem !=null)
                            {

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

            view = inflater.inflate(R.layout.list_pdfbill, null,false);


            final TextView text_pdfbill_invoice=(TextView)view.findViewById(R.id.text_pdfbill_invoice);
            final TextView text_pdfbill_date=(TextView)view.findViewById(R.id.text_pdfbill_date);
            final TextView text_pdfbill_total=(TextView)view.findViewById(R.id.text_pdfbill_total);




            try {
                final JSONObject object_values = new JSONObject(String.valueOf(Array_farmeritem.getJSONObject(i)));
                text_pdfbill_invoice.setText(object_values.getString("invoice"));
                text_pdfbill_total.setText(object_values.getString("total"));
                text_pdfbill_date.setText(object_values.getString("billdate"));


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return view;
        }
    }
}