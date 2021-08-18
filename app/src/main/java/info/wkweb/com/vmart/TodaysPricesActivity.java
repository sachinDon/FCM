package info.wkweb.com.vmart;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TodaysPricesActivity extends AppCompatActivity {
    SearchView searchbar;
    JSONArray array_vegitables,array_vegitables1;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    RecyclerView recyclerView;
    TodayPriceAdapter adapter;
    ProgressBar progress_todayprice;
    TextView textview_back,text_result_vendor_todayprice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_prices);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();


        recyclerView = (RecyclerView)findViewById(R.id.recyclerView_today);

        textview_back = (TextView) findViewById(R.id.text_back_tdp);
        text_result_vendor_todayprice= (TextView) findViewById(R.id.text_result_vendor_todayprice);
        text_result_vendor_todayprice.setVisibility(View.INVISIBLE);
        progress_todayprice = (ProgressBar) findViewById(R.id.progress_todayprice);
        searchbar = (SearchView) findViewById(R.id.today_searchView);
        array_vegitables1 = new JSONArray();
        array_vegitables = new JSONArray();

        int id = searchbar.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText textView = (EditText) searchbar.findViewById(id);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(Color.WHITE);

        textview_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            public boolean onQueryTextSubmit(String query) {
                Log.d("seach_query", query);
                // do something on text submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                array_vegitables = new JSONArray();
                if (array_vegitables1 != null)

                {
                    if (TextUtils.isEmpty(newText.toString())) {

                        //str_search_txt = "";
                        array_vegitables = array_vegitables1;

                    } else {

                        for (int i = 0; i < array_vegitables1.length(); i++) {

                            try {
                                String string = array_vegitables1.getJSONObject(i).getString("name");
                                // str_search_txt = newText;
                                if ((string.toLowerCase()).contains(newText.toLowerCase())) {

                                    Log.e("constraint_str11", string);
                                    array_vegitables.put(array_vegitables1.getJSONObject(i));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }


                    }
                    if (array_vegitables != null) {

                        adapter = new TodayPriceAdapter(array_vegitables,getApplicationContext());
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(adapter);
                    }


                }
                else
                {
                    adapter = new TodayPriceAdapter(array_vegitables,getApplicationContext());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(adapter);
                }
                return false;
            }
        });
        new Communication_todayprice().execute();
    }

    public class Communication_todayprice extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.todayprice);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("email", pref.getString("userid",""));
                postDataParams.put("type","vendor");

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
            progress_todayprice.setVisibility(View.INVISIBLE);
            text_result_vendor_todayprice.setVisibility(View.INVISIBLE);
            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {



                } else if (result.equalsIgnoreCase("error"))
                {


                }
                else if (result.equalsIgnoreCase("nodata"))
                {

                    text_result_vendor_todayprice.setVisibility(View.VISIBLE);
                }
                else
                {
                    if (!result.equalsIgnoreCase("") && result.length()>=50) {
                        try {
                            array_vegitables1  = new JSONArray(result);
                            array_vegitables = new JSONArray(result);
                            if (array_vegitables !=null) {

                                text_result_vendor_todayprice.setVisibility(View.INVISIBLE);
                                adapter = new TodayPriceAdapter(array_vegitables,getApplicationContext());
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(adapter);

                            }
                            else
                            {
                                text_result_vendor_todayprice.setVisibility(View.VISIBLE);
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
}
