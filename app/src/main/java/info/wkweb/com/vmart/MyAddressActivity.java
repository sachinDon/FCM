package info.wkweb.com.vmart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MyAddressActivity extends AppCompatActivity {

    TextView text_addlist_myaddress,text_back_myaddress ,text_buy_myaddress;
    public  static Activity myadd_activity;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    AlertDialog alert11;
    LinearLayout  ll;
    JSONArray array_addAddress;
    LayoutInflater inflater1;
String str_select_row,str_select_rowid,str_typeadd,str_id;
    ArrayList<String> Array_orderid = new ArrayList<>();
    ArrayList<String> Array_updateorderid = new ArrayList<>();
    ProgressBar progress_myaddress;

    public  static  String str_selectactivity="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        myadd_activity = this;
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        inflater1 =MyAddressActivity.this.getLayoutInflater();

        progress_myaddress= (ProgressBar) findViewById(R.id.progress_myaddress);
        ll = (LinearLayout) findViewById(R.id.linear_myaddress);
        text_addlist_myaddress = (TextView)findViewById(R.id.text_addlist_myaddress);
                text_back_myaddress = (TextView)findViewById(R.id.text_back_myaddress);
        text_buy_myaddress = (TextView)findViewById(R.id.text_buy_myaddress);


        text_buy_myaddress.setTextColor(getResources().getColor(R.color.white));
        GradientDrawable bgShape1 = (GradientDrawable)text_buy_myaddress.getBackground();
        bgShape1.mutate();
        bgShape1.setColor(getResources().getColor(R.color.color_green));
        bgShape1.setStroke(text_buy_myaddress.getWidth(),getResources().getColor(R.color.color_green));

        str_select_row = "";
        str_typeadd = "getadd";
        str_id = "";
        LocalBroadcastManager.getInstance(MyAddressActivity.this).registerReceiver(Update_Address,
                new IntentFilter("updateaddress"));

        text_buy_myaddress.setVisibility(View.VISIBLE);


        if (pref.getString("address","").equalsIgnoreCase("yes"))
        {
            progress_myaddress.setVisibility(View.INVISIBLE);
            Array_orderid.add(pref.getString("addressid",""));
            try {
                array_addAddress = new JSONArray(pref.getString("array_address",""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SingletonObject.Instance().setArray_MyAddress(array_addAddress);
            text_buy_myaddress.setVisibility(View.VISIBLE);
        }
        else
        {
            progress_myaddress.setVisibility(View.VISIBLE);
            text_buy_myaddress.setVisibility(View.INVISIBLE);
        }

        if (str_selectactivity.equalsIgnoreCase("yes"))
        {
            text_buy_myaddress.setVisibility(View.INVISIBLE);
        }

        text_back_myaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent1 = new Intent("updateaddressreview");
                LocalBroadcastManager.getInstance(MyAddressActivity.this).sendBroadcast(intent1);


            finish();
            }
        });
        text_addlist_myaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AddmyAddressActivity.str_selecttype = "new";
                Intent intens = new Intent(MyAddressActivity.this,AddmyAddressActivity.class);
                startActivity(intens);

            }
        });


        text_buy_myaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                if (str_selectactivity.equalsIgnoreCase("noo"))
                {
                    Intent intent1 = new Intent("updateaddressreview");
                    LocalBroadcastManager.getInstance(MyAddressActivity.this).sendBroadcast(intent1);
                    finish();
                }
                else {
                    Intent intens = new Intent(MyAddressActivity.this, ReviewOrderActivity.class);
                    startActivity(intens);
                }
            }
        });



        if (SingletonObject.Instance().getArray_MyAddress() !=null)
        {
            if (SingletonObject.Instance().getArray_MyAddress().length() != 0)
            {
                progress_myaddress.setVisibility(View.INVISIBLE);
                array_addAddress = new JSONArray();
                array_addAddress = SingletonObject.Instance().getArray_MyAddress();
                addRow();
            }
            else
            {
                progress_myaddress.setVisibility(View.VISIBLE);
                array_addAddress = new JSONArray();
                SingletonObject.Instance().setArray_MyAddress(array_addAddress);
                new Communication_addresslist().execute();
            }
        }
        else {
            progress_myaddress.setVisibility(View.INVISIBLE);
            array_addAddress = new JSONArray();
            SingletonObject.Instance().setArray_MyAddress(array_addAddress);
            new Communication_addresslist().execute();
        }



    }

    public  void  addRow()
    {

        ll.removeAllViews();
        Array_updateorderid.clear();
        for (int i = 0; i < array_addAddress.length(); i++)
        {

            final View view1 = inflater1.inflate(R.layout.list_address_list, null);
            final TextView text_select_addresslist = (TextView) view1.findViewById(R.id.text_select_addresslist);
           final TextView text_name_addresslist = (TextView) view1.findViewById(R.id.text_name_addresslist);
            final TextView text_gen_addresslist = (TextView) view1.findViewById(R.id.text_gen_addresslist);
           final TextView text_mobile_addresslist = (TextView) view1.findViewById(R.id.text_mobile_addresslist);
            final RelativeLayout rel_remove_address1 = (RelativeLayout) view1.findViewById(R.id.rel_remove_address1);
            final RelativeLayout rel_edit_address1 = (RelativeLayout) view1.findViewById(R.id.rel_edit_address1);

            text_select_addresslist.setTag(i);
            text_name_addresslist.setTag(i);
            text_gen_addresslist.setTag(i);
            text_mobile_addresslist.setTag(i);
            rel_remove_address1.setTag(i);
            rel_edit_address1.setTag(i);
            try {
                JSONObject obj_val = new JSONObject(String.valueOf(array_addAddress.getJSONObject(i)));

                text_name_addresslist.setText(obj_val.getString("fullname"));
                text_gen_addresslist.setText(obj_val.getString("home")+" ,"+obj_val.getString("address")+" ," +obj_val.getString("landmark")+" \nPin-"+obj_val.getString("pincode"));
                //text_mobile_addresslist.setText("Phone: "+obj_val.getString("mobile"));
                String str_msg1 = "Phone: ",str_msg2 = obj_val.getString("mobile");
                text_mobile_addresslist.setText(Html.fromHtml( " <font color=#414141>  "+str_msg1+ " </font> <font color=#008ae6> <b> "+str_msg2+ " </b> </font>"));



                if (Array_orderid.size() !=0) {
                    if (Array_orderid.contains(obj_val.getString("id")))
                    {
                        text_select_addresslist.setBackgroundResource(R.drawable.check);
                        editor.putString("addressselect",obj_val.toString());
                        editor.commit();
                    }
                    else
                    {
                        text_select_addresslist.setBackgroundResource(R.drawable.unchecked);
                        Array_updateorderid.add(obj_val.getString("id"));

                    }
                }
                else
                {
                    text_select_addresslist.setBackgroundResource(R.drawable.unchecked);
                    Array_updateorderid.add(obj_val.getString("id"));

                }

            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            Log.d("ConvertArray==", String.valueOf(Array_updateorderid));
            text_select_addresslist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        JSONObject obj_val = new JSONObject(String.valueOf(array_addAddress.getJSONObject((Integer)v.getTag())));
                       TextView text_check =(TextView)v.findViewWithTag(v.getTag());
                        str_typeadd = "select";
                       str_id = obj_val.getString("id");


                        if (Array_orderid.size() !=0)
                        {
                            if (Array_orderid.contains(str_id))
                            {
                                Array_orderid.clear();
                                editor.putString("address","no");
                                editor.putString("addressid","");

                                text_buy_myaddress.setVisibility(View.INVISIBLE);

                                editor.putString("addressselect","");
                                editor.commit();
                            }
                            else
                                {

                                    Array_orderid.clear();
                                Array_orderid.add(str_id);
                                    editor.putString("address","yes");
                                    editor.putString("addressid",str_id);
                                    editor.putString("addressselect",obj_val.toString());


                                    if (str_selectactivity.equalsIgnoreCase("yes"))
                                    {
                                        text_buy_myaddress.setVisibility(View.INVISIBLE);
                                    }
                                    else
                                    {
                                        text_buy_myaddress.setVisibility(View.VISIBLE);
                                    }


                            }
                        }
                        else
                        {
                            Array_orderid.add(str_id);
                            editor.putString("address","yes");
                            editor.putString("addressid",str_id);
                            editor.putString("addressselect",obj_val.toString());
                            if (str_selectactivity.equalsIgnoreCase("yes"))
                            {
                                text_buy_myaddress.setVisibility(View.INVISIBLE);
                            }
                            else
                            {
                                text_buy_myaddress.setVisibility(View.VISIBLE);
                            }
                        }
editor.commit();
                      //  new Communication_addresslist().execute();

//                        if ( obj_val.getString("order").equalsIgnoreCase("yes"))
//                        {
//                           text_check.setBackgroundResource(R.drawable.unchecked);
//                            obj_val.put("order","no");
//                        }
//                        else
//                        {
//                            text_check.setBackgroundResource(R.drawable.check);
//                            obj_val.put("order","yes");
//                        }
//
//                        array_addAddress.put((Integer) v.getTag(),obj_val);
//                        SingletonObject.Instance().setArray_MyAddress(array_addAddress);

                        addRow();



                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
//                    progressDialog = new ProgressDialog(MyAddressActivity.this);
//                    progressDialog.setMessage("Loading..."); // Setting Message
//                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);                                             progressDialog.show(); // Display Progress Dialog
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();



                }
            });

            rel_edit_address1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AddmyAddressActivity.str_selecttype = "update";
                    AddmyAddressActivity.indexArray = (Integer) v.getTag();
                    Intent intens = new Intent(MyAddressActivity.this,AddmyAddressActivity.class);
                    startActivity(intens);

                }
            });


            rel_remove_address1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    str_typeadd = "remove";
                    progressDialog = new ProgressDialog(MyAddressActivity.this);
                    progressDialog.setMessage("Delete address..."); // Setting Message
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);                                             progressDialog.show(); // Display Progress Dialog
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    try {
                        JSONObject obj_val = new JSONObject(String.valueOf(array_addAddress.getJSONObject((Integer) v.getTag())));

                        str_id = obj_val.getString("id");
                        new Communication_addresslist().execute();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                 //   new Communication_deletecart().execute();
                }
            });


            ll.addView(view1);
        }
    }


    public class Communication_addresslist extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.getaddress);

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("email", pref.getString("userid",""));
                postDataParams.put("typeadd", str_typeadd);
                if (str_typeadd.equalsIgnoreCase("select"))
                {
                    if (Array_orderid.size() !=0)
                    {
                        postDataParams.put("id", str_id);
                    }
                    else
                    {
                        postDataParams.put("id", "0");
                    }
                }
                else
                {
                    postDataParams.put("id", str_id);
                }



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
            progress_myaddress.setVisibility(View.INVISIBLE);
            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {



                } else if (result.equalsIgnoreCase("error"))

                {


                }
                else if (result.equalsIgnoreCase("nodata"))

                {

                    array_addAddress = new JSONArray();
                    SingletonObject.Instance().setArray_MyAddress(array_addAddress);
                    ll.removeAllViews();
                    editor.putString("address","no");
                    editor.commit();
                }
                else
                {
                    if (!result.equalsIgnoreCase("") && result.length()>=50) {
                        try {
                            array_addAddress = new JSONArray(result);
                            if (array_addAddress !=null) {

                                SingletonObject.Instance().setArray_MyAddress(array_addAddress);
                                editor.putString("array_address",array_addAddress.toString());
                                editor.commit();
                                addRow();


                            }
                            else
                            {
                                editor.putString("array_address",array_addAddress.toString());
                                editor.commit();
                            }

                            if (array_addAddress.length() == 0)
                            {
                                ll.removeAllViews();
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

    private BroadcastReceiver Update_Address = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent)
        {

            addRow();
        }
    };

//    public void onBackPressed() {
//
//    }
}
