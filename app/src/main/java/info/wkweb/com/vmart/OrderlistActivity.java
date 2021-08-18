package info.wkweb.com.vmart;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;

public class OrderlistActivity extends AppCompatActivity {


    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    String str_orderid;
    LinearLayout ll;
    RelativeLayout scroll_row_forth_dr1,scroll_row_first_dr;
    LayoutInflater inflater1;
    String str_payment,Str_address,str_totals;

    TextView  text_back_orderdetail,text_orderid_details,text_orderlist_status,text_orderlist_datetime,text_orderlist_payment,
            text_orderlist_totalamt,text_orderlist_cancel,text_orderlist_addrss_dr,text_orderlist_addrss_dr1;

   public static JSONArray Array_orderlist;
   public  static  String str_viewa="";

    ProgressDialog progressDialog;
    AlertDialog alert11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);


        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();


        inflater1 =OrderlistActivity.this.getLayoutInflater();
        ll = (LinearLayout)findViewById(R.id.linear_dyn_orderlist);
        scroll_row_forth_dr1= (RelativeLayout) findViewById(R.id.scroll_row_forth_dr1);
        scroll_row_first_dr= (RelativeLayout) findViewById(R.id.scroll_row_first_dr);
        text_back_orderdetail= (TextView) findViewById(R.id.text_back_orderdetail);
        text_orderid_details= (TextView) findViewById(R.id.text_orderid_details);
        text_orderlist_status= (TextView) findViewById(R.id.text_orderlist_status);
        text_orderlist_datetime= (TextView) findViewById(R.id.text_orderlist_datetime);
        text_orderlist_payment= (TextView) findViewById(R.id.text_orderlist_payment);
        text_orderlist_totalamt= (TextView) findViewById(R.id.text_orderlist_totalamt);
        text_orderlist_cancel= (TextView) findViewById(R.id.text_orderlist_cancel);
        text_orderlist_addrss_dr= (TextView) findViewById(R.id.text_orderlist_addrss_dr);
        text_orderlist_addrss_dr1= (TextView) findViewById(R.id.text_orderlist_addrss_dr1);

        text_orderlist_cancel.setTextColor(getResources().getColor(R.color.white));
        GradientDrawable bgShape1 = (GradientDrawable)text_orderlist_cancel.getBackground();
        bgShape1.mutate();
        bgShape1.setColor(getResources().getColor(R.color.red));
        bgShape1.setStroke(text_orderlist_cancel.getWidth(),getResources().getColor(R.color.red));

        text_orderlist_cancel.setVisibility(View.VISIBLE);
        scroll_row_first_dr.setVisibility(View.VISIBLE);
        if (str_viewa.equalsIgnoreCase("farmer"))
        {
            scroll_row_first_dr.setVisibility(View.GONE);
            text_orderlist_cancel.setVisibility(View.GONE);
        }
            if (str_viewa.equalsIgnoreCase("vendor") || str_viewa.equalsIgnoreCase("farmer") )
            {
                scroll_row_forth_dr1.setVisibility(View.GONE);
            }
            else
            {
                scroll_row_forth_dr1.setVisibility(View.VISIBLE);
            }

        text_orderlist_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(OrderlistActivity.this);
                progressDialog.setMessage("Cancel..."); // Setting Message
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);
        new Communication_ordercancel().execute();

            }
        });




        text_back_orderdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


//[{"orderid":"45c2e-d3f5120250257a8c-2ae96","status":"pending","total":"0","paymentmode":"cod","orderdata":[{"orderid":"45c2e-d3f5120250257a8c-2ae96","status":"pending","total":"0","paymentmode":"cod"},{"orderid":"45c2e-d3f5120250257a8c-2ae96","status":"pending","total":"0","paymentmode":"cod"}]},{"orderid":"299ea-e249c202502b1c6c-a6389","status":"pending","total":"0","paymentmode":"cod","orderdata":[{"orderid":"299ea-e249c202502b1c6c-a6389","status":"pending","total":"0","paymentmode":"cod"},{"orderid":"299ea-e249c202502b1c6c-a6389","status":"pending","total":"0","paymentmode":"cod"},{"orderid":"299ea-e249c202502b1c6c-a6389","status":"pending","total":"0","paymentmode":"cod"}]}]




        if (str_viewa.equalsIgnoreCase("farmer"))
        {
            try {
                JSONObject jsonObj_address = new JSONObject(String.valueOf(Array_orderlist.getJSONObject(0)));
                str_orderid =jsonObj_address.getString("id");
                text_orderlist_totalamt.setText(Html.fromHtml( " <font color=#414141>  "+"Total amount: "+ " </font> <font color=#00b33c> <b> "+"₹"+jsonObj_address.getString("netamt")+ " </b> </font>"));
                text_orderlist_addrss_dr.setText(jsonObj_address.getString("address"));
                text_orderlist_addrss_dr1.setText("Name: "+jsonObj_address.getString("vendorname")+"\n"+jsonObj_address.getString("address"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            addRowfarmer();
        }
        else {
            try {
                JSONObject jsonObj_address = new JSONObject(String.valueOf(Array_orderlist.getJSONObject(0)));
                str_orderid =jsonObj_address.getString("orderid");
                text_orderid_details.setText("Order_id: "+jsonObj_address.getString("orderid"));
                // text_orderlist_status.setText("Status: "+jsonObj_address.getString("status"));
                text_orderlist_datetime.setText("Date: "+jsonObj_address.getString("date"));
                //  text_orderlist_payment.setText("Payement: "+jsonObj_address.getString("paymentmode"));
                // text_orderlist_totalamt.setText("Total: "+jsonObj_address.getString("total"));


                if (jsonObj_address.getString("paymentmode").equalsIgnoreCase("cod"))
                {
                    // text_order_paymentmode.setText("Payment: Cash On Dilevery");
                    text_orderlist_payment.setText(Html.fromHtml( " <font color=#414141>  "+"Payment: "+ " </font> <font color=#00b33c> <b> "+"Cash On Dilevery"+ " </b> </font>"));

                }
                else
                {
                    // text_order_paymentmode.setText("Payment: Online");
                    text_orderlist_payment.setText(Html.fromHtml( " <font color=#414141>  "+"Payment: "+ " </font> <font color=#00b33c> <b> "+"Online"+ " </b> </font>"));

                }

                if (jsonObj_address.getString("status").equalsIgnoreCase("Pending"))
                {
                    text_orderlist_status.setText(Html.fromHtml( " <font color=#414141>  "+"Status: "+ " </font> <font color=#ff0000> <b> "+jsonObj_address.getString("status")+ " </b> </font>"));

                }
                else
                {
                    text_orderlist_status.setText(Html.fromHtml( " <font color=#414141>  "+"Status: "+ " </font> <font color=#00b33c> <b> "+jsonObj_address.getString("status")+ " </b> </font>"));

                }
                text_orderlist_totalamt.setText(Html.fromHtml( " <font color=#414141>  "+"Total amount: "+ " </font> <font color=#00b33c> <b> "+"₹"+jsonObj_address.getString("total")+ " </b> </font>"));


                text_orderlist_addrss_dr.setText(jsonObj_address.getString("address"));
                text_orderlist_addrss_dr1.setText("Name: "+jsonObj_address.getString("vname")+"\n"+jsonObj_address.getString("vaddress")+"\n"+"Mobile: "+jsonObj_address.getString("vmobile"));


            } catch (JSONException e) {
                e.printStackTrace();
            }
            addRow();
        }

    }
    public  void  addRow()
    {

        Integer totals = 0;
        ll.removeAllViews();
        for (int i = 0; i < Array_orderlist.length(); i++)
        {
            final View view1 = inflater1.inflate(R.layout.list_revieworderlist, null);
            final ImageView image_vend = (ImageView) view1.findViewById(R.id.image_rvorder);
            final TextView text_title_cart = (TextView) view1.findViewById(R.id.text_title_rvor);
            final TextView text_title_vend_price = (TextView) view1.findViewById(R.id.text_title_rvor_price);
            final TextView text_title_rvor_kgs = (TextView) view1.findViewById(R.id.text_title_rvor_kgs1);


            try
            {

                JSONObject obj_val = new JSONObject(String.valueOf(Array_orderlist.getJSONObject(i)));
                text_title_cart.setText(obj_val.getString("name"));

                if (pref.getString("type","").equalsIgnoreCase("cust"))                {
                    text_title_vend_price.setText("₹ " + obj_val.getString("amount") + "/" + obj_val.getString("weights"));
                }
                else {
                    text_title_vend_price.setText("₹ " + obj_val.getString("amount") + "/"+ obj_val.getString("weightv"));
                }
                text_title_rvor_kgs.setText(obj_val.getString("purch"));
                final String str_imageurl = obj_val.getString("image");
                totals +=Integer.parseInt(obj_val.getString("subtotal"));
                str_totals = String.valueOf(totals);
                //  text_orderlist_totalamt.setText("Rs "+String.valueOf(totals));
                text_orderlist_totalamt.setText(Html.fromHtml( " <font color=#414141>  "+"Total amount: "+ " </font> <font color=#00b33c> <b> "+"₹"+String.valueOf(totals)+ " </b> </font>"));

                Picasso.with(OrderlistActivity.this)
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
    public  void  addRowfarmer()
    {

        Integer totals = 0;
        ll.removeAllViews();
        for (int i = 0; i < Array_orderlist.length(); i++)
        {
            final View view1 = inflater1.inflate(R.layout.list_revieworderlist, null);
            final ImageView image_vend = (ImageView) view1.findViewById(R.id.image_rvorder);
            final TextView text_title_cart = (TextView) view1.findViewById(R.id.text_title_rvor);
            final TextView text_title_vend_price = (TextView) view1.findViewById(R.id.text_title_rvor_price);
            final TextView text_title_rvor_kgs = (TextView) view1.findViewById(R.id.text_title_rvor_kgs1);


            try
            {

                JSONObject obj_val = new JSONObject(String.valueOf(Array_orderlist.getJSONObject(i)));
                text_title_cart.setText(obj_val.getString("pname"));

                if (pref.getString("type","").equalsIgnoreCase("cust"))                {
                    text_title_vend_price.setText("₹ " + obj_val.getString("totamt"));
                }
                else {
                    text_title_vend_price.setText("₹ " + obj_val.getString("faramt") + "/Kg");
                }
                text_title_rvor_kgs.setText(obj_val.getString("qty"));
                final String str_imageurl = obj_val.getString("image");
              //  totals +=Integer.parseInt(obj_val.getString("subtotal"));
              //  str_totals = String.valueOf(totals);

               // text_orderlist_totalamt.setText(Html.fromHtml( " <font color=#414141>  "+"Total amount: "+ " </font> <font color=#00b33c> <b> "+"₹"+String.valueOf(totals)+ " </b> </font>"));

                text_orderlist_totalamt.setText(Html.fromHtml( " <font color=#414141>  "+"Total amount: "+ " </font> <font color=#00b33c> <b> "+"₹"+obj_val.getString("netamt")+ " </b> </font>"));

                Picasso.with(OrderlistActivity.this)
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
                    Intent intent1 = new Intent("ordercancel");
                    LocalBroadcastManager.getInstance(OrderlistActivity.this).sendBroadcast(intent1);

                    finish();

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
