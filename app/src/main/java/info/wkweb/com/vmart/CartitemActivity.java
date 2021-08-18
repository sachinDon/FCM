package info.wkweb.com.vmart;

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
import android.os.Handler;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class CartitemActivity extends AppCompatActivity {

    JSONArray array_addcart;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;

    LayoutInflater inflater1;
    LinearLayout ll;
    String str_id,str_upid,str_upsubtotal,str_uppurch,str_updatecart;
    Integer int_tag_val;
    ProgressDialog progressDialog;
    TextView text_cart_subtotal,text_cart_buy,textview_back;
    ProgressBar progress_vendor_cart;
TextView text_result_cust_cartsitems;
    long delay = 1000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartitem);


        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
         LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(getApplication().LAYOUT_INFLATER_SERVICE);
        inflater1 = inflater;
        str_updatecart = "no";


        progress_vendor_cart = (ProgressBar) findViewById(R.id.progress_venders_cart);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.relative_dyanamicrow);
        ScrollView sv = (ScrollView) findViewById(R.id.scrollView_cart);
        ll = (LinearLayout) findViewById(R.id.linear_dyanamicrow);
        text_cart_subtotal = (TextView) findViewById(R.id.text_cart_subtotal);
        text_cart_buy = (TextView) findViewById(R.id.text_cart_buy);
        textview_back= (TextView) findViewById(R.id.text_back_cartitem);

        text_cart_subtotal.setText("");
        text_cart_subtotal.setVisibility(View.VISIBLE);
        text_cart_buy.setVisibility(View.INVISIBLE);

        text_cart_buy.setTextColor(getResources().getColor(R.color.white));
        GradientDrawable bgShape1 = (GradientDrawable)text_cart_buy.getBackground();
        bgShape1.mutate();
        bgShape1.setColor(getResources().getColor(R.color.color_green));
        bgShape1.setStroke(text_cart_buy.getWidth(),getResources().getColor(R.color.color_green));
        text_result_cust_cartsitems = (TextView) findViewById(R.id.text_result_cust_cartsitems1);
        text_result_cust_cartsitems.setVisibility(View.INVISIBLE);

        LocalBroadcastManager.getInstance(CartitemActivity.this).registerReceiver(cartupdate,
                new IntentFilter("cartupdate"));


        array_addcart = new JSONArray();
        new Communication_addcart().execute();
        textview_back
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        finish();
                    }
                });

        text_cart_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(pref.getString("address","").equalsIgnoreCase("yes"))
                {
                    //  ReviewOrderActivity.Array_cartArray = array_addcart;
                    Intent intens = new Intent(CartitemActivity.this,ReviewOrderActivity.class);
                    startActivity(intens);
                }
                else
                {
                    MyAddressActivity.str_selectactivity = "no";
                    Intent intens = new Intent(CartitemActivity.this,MyAddressActivity.class);
                    startActivity(intens);
                }

            }
        });
    }


    private BroadcastReceiver cartupdate = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent)
        {


            array_addcart = new JSONArray();
            SingletonObject.Instance().setArray_Addcart(array_addcart);
            ll.removeAllViews();
            text_cart_buy.setVisibility(View.INVISIBLE);
            text_cart_subtotal.setText("");
            text_cart_subtotal.setVisibility(View.INVISIBLE);
            text_result_cust_cartsitems.setVisibility(View.VISIBLE);
            Intent intent1 = new Intent("cartcustomerupdate");
            LocalBroadcastManager.getInstance(CartitemActivity.this).sendBroadcast(intent1);

        }
    };
    public class Communication_addcart extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.addcart1);

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

            if (progressDialog !=null)
            {
                progressDialog.dismiss();
            }
            progress_vendor_cart.setVisibility(View.INVISIBLE);
            text_result_cust_cartsitems.setVisibility(View.INVISIBLE);
            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {



                } else if (result.equalsIgnoreCase("error"))

                {
                    text_result_cust_cartsitems.setVisibility(View.INVISIBLE);

                }
                else if (result.equalsIgnoreCase("nodata"))

                {
                    text_result_cust_cartsitems.setVisibility(View.VISIBLE);

                }
                else
                {
                    if (!result.equalsIgnoreCase("") && result.length()>=50) {
                        try {
                            array_addcart = new JSONArray(result);
                            if (array_addcart !=null) {
                                text_result_cust_cartsitems.setVisibility(View.INVISIBLE);
                                SingletonObject.Instance().setArray_Addcart(array_addcart);
                                addRow();
                                text_cart_subtotal.setVisibility(View.VISIBLE);
                                text_cart_buy.setVisibility(View.VISIBLE);


                            }
                            else
                            {
                                text_result_cust_cartsitems.setVisibility(View.VISIBLE);


                            }

                            if (array_addcart.length() == 0)
                            {
                                text_result_cust_cartsitems.setVisibility(View.VISIBLE);
                                ll.removeAllViews();
                                text_cart_subtotal.setText("");
                                text_cart_buy.setVisibility(View.INVISIBLE);
                            }
                            else
                            {
                                text_result_cust_cartsitems.setVisibility(View.INVISIBLE);
                                text_cart_subtotal.setVisibility(View.VISIBLE);
                                text_cart_buy.setVisibility(View.VISIBLE);
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

    public  void  addRow()
    {

        Integer totals = 0;
        ll.removeAllViews();
        for (int i = 0; i < array_addcart.length(); i++)
        {
            final View view1 = inflater1.inflate(R.layout.list_cart_items, null);
            final TextView text_delete = (TextView) view1.findViewById(R.id.text_cart_delete);
            final ImageView image_vend = (ImageView) view1.findViewById(R.id.image_vend);
            final TextView text_title_cart = (TextView) view1.findViewById(R.id.text_title_cart);
            final TextView text_title_vend_price = (TextView) view1.findViewById(R.id.text_title_vend_price);
            final TextView text_title_vend_pricetotal = (TextView) view1.findViewById(R.id.text_title_vend_pricetotal);
            final EditText edit_vender_purch = (EditText) view1.findViewById(R.id.edit_vender_purch);
            text_delete.setTag(i);
            edit_vender_purch.setTag(i);
            text_title_vend_pricetotal.setTag(i);
            try {
                JSONObject obj_val = new JSONObject(String.valueOf(array_addcart.getJSONObject(i)));

                text_title_cart.setText(obj_val.getString("name"));
                // text_title_vend_price.setText("Price: Rs"+obj_val.getString("amount")+"/kg");
                edit_vender_purch.setText(obj_val.getString("purch"));
                //  text_title_vend_pricetotal.setText("Total: "+obj_val.getString("purch") + " x " + obj_val.getString("amount") +" = Rs "+obj_val.getString("subtotal"));

                text_title_vend_price.setText(Html.fromHtml( " <font color=#414141>  "+"Price: "+ " </font> <font color=#00b33c> <b> "+"₹"+obj_val.getString("amount")+"/"+obj_val.getString("weightv")+ " </b> </font>"));

                text_title_vend_pricetotal.setText(Html.fromHtml( " <font color=#414141>  "+"Total: "+obj_val.getString("purch") + " x " + obj_val.getString("amount")+ " </font> <font color=#00b33c> <b> "+" = ₹ "+obj_val.getString("subtotal")+ " </b> </font>"));


                final String str_imageurl = obj_val.getString("image");


                totals +=Integer.parseInt(obj_val.getString("subtotal"));

                // text_cart_subtotal.setText("Subtotal( "+array_addcart.length()+" items ): Rs "+String.valueOf(totals));

                // String str_msg1="Subtotal( ",str_msg2 = String.valueOf(array_addcart.length()),str_msg3=" items ): Rs ",str_msg4=String.valueOf(totals);

                String str_msg1 = "Subtotal( "+array_addcart.length()+" items ): ";
                String str_msg2 = "₹ "+String.valueOf(totals);
                text_cart_subtotal.setText(Html.fromHtml( " <font color=#414141>  "+str_msg1+ " </font> <font color=#00b33c> <b> "+str_msg2+ " </b> </font>"));


                edit_vender_purch.addTextChangedListener(new TextWatcher()
                {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    {
                        String edit_purchval= String.valueOf(edit_vender_purch.getText());
                        if (edit_purchval.length() == 0)
                        {
                            edit_purchval = "0";
                        }


                        try {
                            JSONObject objectval = new JSONObject(String.valueOf(array_addcart.getJSONObject((Integer) edit_vender_purch.getTag())));

                            Integer totalval = Integer.parseInt(edit_purchval) * Integer.parseInt(objectval.getString("amount"));
                            str_upsubtotal = String.valueOf(totalval);
                            str_upid = objectval.getString("id");
                            str_uppurch = edit_purchval;
                            objectval.put("purch",edit_purchval);
                            objectval.put("subtotal",String.valueOf(totalval));
                            array_addcart.put((Integer) edit_vender_purch.getTag(),objectval);
                            //addRow();


                            TextView text_title_vend_pricetotal1 = (TextView)view1.findViewWithTag(edit_vender_purch.getTag());;
                            text_title_vend_pricetotal1.setText("Total: "+edit_purchval + " x " + objectval.getString("amount") +" = ₹ "+  String.valueOf(totalval));


                            text_title_vend_pricetotal1.setText(Html.fromHtml( " <font color=#414141>  "+"Total: "+objectval.getString("purch") + " x " + objectval.getString("amount")+ " </font> <font color=#00b33c> <b> "+" = ₹ "+objectval.getString("subtotal")+ " </b> </font>"));

                            Integer totals = 0;
                            for (int i =0 ; i<array_addcart.length();i++)
                            {
                                try {
                                    JSONObject obj_val = new JSONObject(String.valueOf(array_addcart.getJSONObject(i)));
                                    totals += Integer.parseInt(obj_val.getString("subtotal"));
                                   // text_cart_subtotal.setText("Subtotal( " + array_addcart.length() + " items ): ₹ " + String.valueOf(totals));
                                    String str_msg11 = "Subtotal( "+array_addcart.length()+" items ): ";
                                    String str_msg21 = "₹ "+String.valueOf(totals);
                                    text_cart_subtotal.setText(Html.fromHtml( " <font color=#414141>  "+str_msg11+ " </font> <font color=#00b33c> <b> "+str_msg21+ " </b> </font>"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        progressDialog = new ProgressDialog(context);
//                        progressDialog.setMessage("Update cart..."); // Setting Message
//                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);                                             progressDialog.show(); // Display Progress Dialog
//                        progressDialog.setCancelable(false);
//                        progressDialog.show();
                        new Communication_updateaddcart().execute();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {


                        if (s.length() > 0) {
                            last_text_edit = System.currentTimeMillis();
                            handler.postDelayed(input_finish_checker, delay);
                        } else {

                        }


                    }
                });
                Picasso.with(CartitemActivity.this)
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


            text_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    progressDialog = new ProgressDialog(CartitemActivity.this);
                    progressDialog.setMessage("Delete cart..."); // Setting Message
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);                                             progressDialog.show(); // Display Progress Dialog
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    try {
                        JSONObject obj_val = new JSONObject(String.valueOf(array_addcart.getJSONObject((Integer) v.getTag())));

                        str_id = obj_val.getString("id");
                        int_tag_val = (Integer) v.getTag();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new Communication_deletecart().execute();
                }
            });


            ll.addView(view1);
        }
    }
    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                // TODO: do what you need here
                // ............
                // ............
                Log.d("finsh textediting..","finisth editing");
            }
        }
    };
    public class Communication_updateaddcart extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.updateaddcart);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("email", pref.getString("userid",""));
                postDataParams.put("type", pref.getString("type",""));
                postDataParams.put("id",str_upid);
                postDataParams.put("purch",str_uppurch);
                postDataParams.put("subtotal",str_upsubtotal);


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


            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {
                    progressDialog.dismiss();
                    addRow();

                }
                else if (result.equalsIgnoreCase("error"))
                {

                    progressDialog.dismiss();
                    addRow();
                }
                else if (result.equalsIgnoreCase("updated"))
                {
                    // new Communication_addcart().execute();

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


    public class Communication_deletecart extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.deleteaddcart);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("email", pref.getString("userid",""));
                postDataParams.put("type", pref.getString("type",""));
                postDataParams.put("id",str_id);


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


            if (result !=null)
            {
                Intent intent1 = new Intent("cartcustomerupdate");
                LocalBroadcastManager.getInstance(CartitemActivity.this).sendBroadcast(intent1);

                if (result.equalsIgnoreCase("nullerror"))

                {

                    progressDialog.dismiss();

                } else if (result.equalsIgnoreCase("error"))
                {

                    progressDialog.dismiss();
                }
                else if (result.equalsIgnoreCase("deleted"))
                {

//                    array_addcart.remove(int_tag_val);
//                    new Communication_addcart().execute();
//                    if (array_addcart.length() == 0)
//                    {
//                        ll.removeAllViews();
//                        text_cart_subtotal.setText("");
//                    }

                    array_addcart.remove(int_tag_val);
                    new Communication_addcart().execute();
                    if (array_addcart.length() == 0)
                    {
                        ll.removeAllViews();
                        text_cart_subtotal.setText("");
                        text_cart_buy.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        ll.removeViewAt(int_tag_val);
                        text_cart_buy.setVisibility(View.VISIBLE);
                    }

//
//                    ll.removeViewAt(int_tag_val);
//                    Integer totals = 0;
//                    for (int i =0 ; i<array_addcart.length();i++)
//                    {
//                        try {
//                            JSONObject obj_val = new JSONObject(String.valueOf(array_addcart.getJSONObject(i)));
//                            totals += Integer.parseInt(obj_val.getString("subtotal"));
//                            text_cart_subtotal.setText("Subtotal( " + array_addcart.length() + " items ): Rs " + String.valueOf(totals));
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }

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

//    public void onBackPressed() {
//
//    }
}
