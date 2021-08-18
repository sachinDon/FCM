package info.wkweb.com.vmart;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class ReviewOrderActivity extends AppCompatActivity {


    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    public static String str_name="",str_mobile="",str_address="",str_email="";
    LinearLayout ll;
    RelativeLayout relative_scroll_row_first1,scroll_row_sec;
    LayoutInflater inflater1;
    String str_payment,Str_address,str_totals,str_namefull;
    TextView text_back_revieworder,scroll_change_addd, scroll_address_type,scroll_check_cod,text_check_online,myorderchange,text_review_total
    ,text_revieworder_buy,scroll_change_addd1, scroll_address_type1;

    JSONArray Array_cartArray;
    JSONObject jsonObj_address;
    ProgressDialog progressDialog;
    AlertDialog alert11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_order);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();


        inflater1 =ReviewOrderActivity.this.getLayoutInflater();
        ll = (LinearLayout)findViewById(R.id.linear_dyanamicrow_revieworder);
        relative_scroll_row_first1 = (RelativeLayout)findViewById(R.id.scroll_row_first1);
        text_back_revieworder= (TextView) findViewById(R.id.text_back_revieworder);
        scroll_change_addd= (TextView) findViewById(R.id.scroll_change_addd);
        scroll_address_type= (TextView) findViewById(R.id.scroll_address_type);
        scroll_change_addd1= (TextView) findViewById(R.id.scroll_change_addd1);
        scroll_address_type1= (TextView) findViewById(R.id.scroll_address_type1);
        scroll_row_sec = (RelativeLayout)findViewById(R.id.scroll_row_sec);

        scroll_check_cod= (TextView) findViewById(R.id.scroll_check_cod);
        text_check_online= (TextView) findViewById(R.id.text_check_online);

        myorderchange= (TextView) findViewById(R.id.myorderchange);

        text_review_total= (TextView) findViewById(R.id.text_review_total);
        text_revieworder_buy= (TextView) findViewById(R.id.text_revieworder_buy);
        str_name="";
        str_address="";
        str_mobile="";
        str_email="";

        LocalBroadcastManager.getInstance(ReviewOrderActivity.this).registerReceiver(Update_Address,
                new IntentFilter("updateaddressreview"));
        LocalBroadcastManager.getInstance(ReviewOrderActivity.this).registerReceiver(Update_Address1,
                new IntentFilter("updateaddressreview1"));
        relative_scroll_row_first1.setVisibility(View.GONE);

        if (pref.getString("type","").equalsIgnoreCase("cust"))
        {
            scroll_row_sec.setVisibility(View.VISIBLE);
        }
        else
            {
                scroll_row_sec.setVisibility(View.GONE);
        }

        if (pref.getString("mode","").equalsIgnoreCase("online"))
        {
            str_payment = "online";
            scroll_check_cod.setBackgroundResource(R.drawable.unchecked);
            text_check_online.setBackgroundResource(R.drawable.check);
        }
        else
        {
            str_payment = "cod";
            scroll_check_cod.setBackgroundResource(R.drawable.check);
            text_check_online.setBackgroundResource(R.drawable.unchecked);
        }

                scroll_check_cod.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        str_payment = "cod";
                        scroll_check_cod.setBackgroundResource(R.drawable.check);
                        text_check_online.setBackgroundResource(R.drawable.unchecked);
                        editor.putString("mode","cod");
                        editor.commit();
                    }
                });

        text_check_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_payment = "online";
                scroll_check_cod.setBackgroundResource(R.drawable.unchecked);
                text_check_online.setBackgroundResource(R.drawable.check);
                editor.putString("mode","online");
                editor.commit();

            }
        });


        text_back_revieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();
            }
        });

        scroll_change_addd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                MyAddressActivity.str_selectactivity = "noo";
                Intent intens = new Intent(ReviewOrderActivity.this,MyAddressActivity.class);
                startActivity(intens);
            }
        });

        myorderchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        text_revieworder_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pref.getString("type","").equalsIgnoreCase("vendor"))
                {
                    editor.putString("mode", "cod");
                    editor.commit();
                    progressDialog = new ProgressDialog(ReviewOrderActivity.this);
                    progressDialog.setMessage("Loading..."); // Setting Message
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                    progressDialog.show(); // Display Progress Dialog
                    progressDialog.setCancelable(false);
                    new Communication_Order().execute();
                }
                else

                {

                    if (str_address.length() == 0 && str_name.length() == 0) {
                        LocationsActivity.str_viewact = "order";
                        Intent intens = new Intent(ReviewOrderActivity.this, LocationsActivity.class);
                        startActivity(intens);
                    } else {
                        editor.putString("mode", "cod");
                        editor.commit();
                        progressDialog = new ProgressDialog(ReviewOrderActivity.this);
                        progressDialog.setMessage("Loading..."); // Setting Message
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                        progressDialog.show(); // Display Progress Dialog
                        progressDialog.setCancelable(false);
                        new Communication_Order().execute();
                    }
                }


            }
        });

        scroll_change_addd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    LocationsActivity.str_viewact = "order";
                    Intent intens = new Intent(ReviewOrderActivity.this,LocationsActivity.class);
                    startActivity(intens);

            }
        });

        if (pref.getString("addressselect","").equalsIgnoreCase(""))
        {

        }
        else
        {
            try {
                jsonObj_address = new JSONObject(pref.getString("addressselect",""));

                String str_add = "Name: "+jsonObj_address.getString("fullname")+"\n"+jsonObj_address.getString("home")+" ,"+jsonObj_address.getString("address")+" ," +jsonObj_address.getString("landmark")+" \nPin-"+jsonObj_address.getString("pincode")+"\n"+"Phone: "+ jsonObj_address.getString("mobile");
                scroll_address_type.setText(str_add);
                Str_address = str_add;
                str_namefull = jsonObj_address.getString("fullname");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Array_cartArray = SingletonObject.Instance().getArray_Addcart();

        addRow();
    }

    public  void  addRow()
    {

        Integer totals = 0;
        ll.removeAllViews();
        for (int i = 0; i < Array_cartArray.length(); i++)
        {
            final View view1 = inflater1.inflate(R.layout.list_revieworderlist, null);
            final ImageView image_vend = (ImageView) view1.findViewById(R.id.image_rvorder);
           final TextView text_title_cart = (TextView) view1.findViewById(R.id.text_title_rvor);
            final TextView text_title_vend_price = (TextView) view1.findViewById(R.id.text_title_rvor_price);
         final TextView text_title_rvor_kgs = (TextView) view1.findViewById(R.id.text_title_rvor_kgs1);


            try {
                JSONObject obj_val = new JSONObject(String.valueOf(Array_cartArray.getJSONObject(i)));

                if(pref.getString("type","").equalsIgnoreCase("vendor")) {

                    text_title_vend_price.setText("₹ " + obj_val.getString("amount")+ "/"+obj_val.getString("weightv"));
                    text_title_rvor_kgs.setText(obj_val.getString("purch"));

                }
                else
                {
                    text_title_vend_price.setText("₹ " + obj_val.getString("amount")+"/"+ obj_val.getString("weights"));
                    text_title_rvor_kgs.setText(obj_val.getString("purch"));
                }
                final String str_imageurl = obj_val.getString("image");
                totals +=Integer.parseInt(obj_val.getString("subtotal"));
                str_totals = String.valueOf(totals);
                text_review_total.setText("₹ "+String.valueOf(totals));

                Picasso.with(ReviewOrderActivity.this)
                        .load(str_imageurl)
                        .placeholder(R.drawable.default1)
                        .into( image_vend, new Callback() {
                            @Override
                            public void onSuccess() {


                            }

                            @Override
                            public void onError() {

                            }
                        });




            } catch (JSONException e)
            {
                e.printStackTrace();
            }


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 25);
            view1.setLayoutParams(params);


            ll.addView(view1);
        }
    }

    private BroadcastReceiver Update_Address = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent)
        {


            if(pref.getString("address","").equalsIgnoreCase("yes"))
            {

                try {
                    if (jsonObj_address !=null) {
                    jsonObj_address = new JSONObject(pref.getString("addressselect",""));
                    String str_add = "Name: " + jsonObj_address.getString("fullname") + "\n" + jsonObj_address.getString("home") + " ," + jsonObj_address.getString("address") + " ," + jsonObj_address.getString("landmark") + " \nPin-" + jsonObj_address.getString("pincode") + "\n" + "Phone: " + jsonObj_address.getString("mobile");
                    scroll_address_type.setText(str_add);
                        Str_address = str_add;
                        str_namefull = jsonObj_address.getString("fullname");
                    }
                    else
                    {
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else
            {
                finish();
            }
        }
    };

    private BroadcastReceiver Update_Address1 = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (str_address.length() == 0 && str_name.length() ==0)
            {
               relative_scroll_row_first1.setVisibility(View.GONE);
            }
            else
            {
                relative_scroll_row_first1.setVisibility(View.VISIBLE);
                String str_add = "Name: " + ReviewOrderActivity.str_name + "\n" + str_address + "\n" +"Mobile NO.:" + str_mobile ;
                scroll_address_type1.setText(str_add);
            }

        }
    };

    public class Communication_Order extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.venderorder);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("email", pref.getString("userid",""));
                postDataParams.put("name", str_namefull);
                postDataParams.put("address",Str_address);
                postDataParams.put("paymentmode", str_payment);
                postDataParams.put("total", str_totals);
                postDataParams.put("type", pref.getString("type",""));

                postDataParams.put("vemail", str_email);

                    postDataParams.put("vname",str_name);


                postDataParams.put("vmobile", str_mobile);
                postDataParams.put("vaddress",str_address);


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
        protected void onPostExecute(String result) {

progressDialog.dismiss();
            if (result != null) {

                if (result.equalsIgnoreCase("nullerror"))
                {

                    AlertDialog.Builder builder2 = new AlertDialog.Builder(ReviewOrderActivity.this);
                    builder2.setTitle("Oops");
                    builder2.setMessage("Some field missing,Please try again.");
                    builder2.setCancelable(false);
                    builder2.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alert11 = builder2.create();
                    alert11.show();
                }
                else if (result.equalsIgnoreCase("error"))
                {

                    AlertDialog.Builder builder2 = new AlertDialog.Builder(ReviewOrderActivity.this);
                    builder2.setTitle("Oops");
                    builder2.setMessage("Server could not found,Please try again.");
                    builder2.setCancelable(false);
                    builder2.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alert11 = builder2.create();
                    alert11.show();
                }
                else if (result.equalsIgnoreCase("added"))
                {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(ReviewOrderActivity.this);
                    builder2.setTitle("Successfull order!");
                    builder2.setMessage("Your order has been submitted");
                    builder2.setCancelable(false);
                    builder2.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                    Intent intent1 = new Intent("cartupdate");
                                    LocalBroadcastManager.getInstance(ReviewOrderActivity.this).sendBroadcast(intent1);
                                    Intent intent2 = new Intent("cartcustomerupdate");
                                    LocalBroadcastManager.getInstance(ReviewOrderActivity.this).sendBroadcast(intent2);

                                    if (MyAddressActivity.myadd_activity !=null)
                                    {
                                        MyAddressActivity.myadd_activity.finish();
                                    }
                                    finish();
                                }
                            });
                    alert11 = builder2.create();
                    alert11.show();

                }
                else
                {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(ReviewOrderActivity.this);
                    builder2.setTitle("Oops");
                    builder2.setMessage("Server could not found");
                    builder2.setCancelable(false);
                    builder2.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alert11 = builder2.create();
                    alert11.show();
                }


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
