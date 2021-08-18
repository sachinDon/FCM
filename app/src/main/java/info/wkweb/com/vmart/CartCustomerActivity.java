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

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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

public class CartCustomerActivity extends AppCompatActivity {

    JSONArray array_addcart;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    LayoutInflater inflater1;
    LinearLayout ll;
    String str_id,str_upid,str_upsubtotal,str_uppurch,str_amount,str_weights;
    Integer int_tag_val;
    ProgressDialog progressDialog;
    TextView text_cart_subtotal,text_cart_buy,textview_back,text_result_cust_cartsitems;
    ProgressBar progress_customer_cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_customer);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        progress_customer_cart = (ProgressBar) findViewById(R.id.progress_customer_cart);

//        listview_cart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                view.setSelected(true);
//
//
//            }
//        });
//

        text_result_cust_cartsitems = (TextView) findViewById(R.id.text_result_cust_cartsitems12);
        text_result_cust_cartsitems.setVisibility(View.INVISIBLE);

        RelativeLayout rl = (RelativeLayout)findViewById(R.id.relative_dyanamicrow_cu);
        ScrollView sv = (ScrollView)findViewById(R.id.scrollView_cart_cu);
        ll = (LinearLayout)findViewById(R.id.linear_dyanamicrow_cu);
        text_cart_subtotal = (TextView)findViewById(R.id.text_cart_subtotal_cu);
        text_cart_buy = (TextView)findViewById(R.id.text_cart_buy_cu);
        textview_back = (TextView)findViewById(R.id.text_back_cartcusti);

        text_cart_subtotal.setText("");
        text_cart_subtotal.setVisibility(View.VISIBLE);
        text_cart_buy.setVisibility(View.INVISIBLE);
        text_cart_buy.setTextColor(getResources().getColor(R.color.white));
        GradientDrawable bgShape1 = (GradientDrawable)text_cart_buy.getBackground();
        bgShape1.mutate();
        bgShape1.setColor(getResources().getColor(R.color.color_green));
        bgShape1.setStroke(text_cart_buy.getWidth(),getResources().getColor(R.color.color_green));

        LocalBroadcastManager.getInstance(CartCustomerActivity.this).registerReceiver(cartupdate,
                new IntentFilter("cartupdate"));




        LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(getApplication().LAYOUT_INFLATER_SERVICE);
        inflater1 = inflater;

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
                    Intent intens = new Intent(CartCustomerActivity.this,ReviewOrderActivity.class);
                    startActivity(intens);
                }
                else
                {
                    MyAddressActivity.str_selectactivity = "no";
                    Intent intens = new Intent(CartCustomerActivity.this,MyAddressActivity.class);
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

        }
    };






    public class Communication_addcart extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.addcart2);

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
            progress_customer_cart.setVisibility(View.INVISIBLE);
            text_result_cust_cartsitems.setVisibility(View.INVISIBLE);
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
                    text_result_cust_cartsitems.setVisibility(View.VISIBLE);

                }
                else
                {
                    if (!result.equalsIgnoreCase("") && result.length()>=50)
                    {
                        try {
                            array_addcart = new JSONArray(result);
                            if (array_addcart !=null) {

                                SingletonObject.Instance().setArray_Addcart(array_addcart);
                                addRow();
                                //   text_cart_subtotal.setVisibility(View.VISIBLE);
                                text_cart_buy.setVisibility(View.VISIBLE);
                            }
                            else
                            {

                            }

                            if (array_addcart.length() == 0)
                            {
                                ll.removeAllViews();
                                text_cart_subtotal.setText("");
                                text_cart_buy.setVisibility(View.INVISIBLE);
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
                LocalBroadcastManager.getInstance(CartCustomerActivity.this).sendBroadcast(intent1);
                if (result.equalsIgnoreCase("nullerror"))

                {

                    progressDialog.dismiss();

                } else if (result.equalsIgnoreCase("error"))
                {

                    progressDialog.dismiss();
                }
                else if (result.equalsIgnoreCase("deleted"))
                {
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


                    Integer totals = 0;
                    for (int i =0 ; i<array_addcart.length();i++)
                    {
                        try {
                            JSONObject obj_val = new JSONObject(String.valueOf(array_addcart.getJSONObject(i)));
                            totals += Integer.parseInt(obj_val.getString("subtotal"));
                            text_cart_subtotal.setText("Subtotal( " + array_addcart.length() + " items ): ₹ " + String.valueOf(totals));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                postDataParams.put("weights",str_weights);
                postDataParams.put("amount",str_amount);


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
                    new Communication_addcart().execute();


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
    public  void  addRow()
    {

        Integer totals = 0;
        ll.removeAllViews();
        for (int i = 0; i < array_addcart.length(); i++)
        {
            final View view1 = inflater1.inflate(R.layout.list_cart_today, null);


            RelativeLayout relative_slects_custveg=(RelativeLayout)view1.findViewById(R.id.relative_slects_custveg1);
            final TextView text_setprice_custveg=(TextView)view1.findViewById(R.id.text_setprice_custveg);


            ImageView imageView = (ImageView) view1.findViewById(R.id.image_cart_cust);
            TextView text_title_cart_cust = (TextView) view1.findViewById(R.id.text_title_cart_cust);
            TextView text_cart_cust1 = (TextView) view1.findViewById(R.id.text_cart_cust1);
            TextView text_cart_cust2 = (TextView) view1.findViewById(R.id.text_cart_cust2);
            TextView text_cart_cust3 = (TextView) view1.findViewById(R.id.text_cart_cust3);

            TextView text_cart_minus = (TextView) view1.findViewById(R.id.text_cart_minus);
            TextView text_cart_qtytitle = (TextView) view1.findViewById(R.id.text_cart_qtytitle);
            TextView text_cart_plus = (TextView) view1.findViewById(R.id.text_cart_plus);

            TextView text_cart_delete = (TextView) view1.findViewById(R.id.text_cart_delete);

            relative_slects_custveg.setTag(i);
            text_setprice_custveg.setTag(i);
            text_cart_delete.setTag(i);
            text_cart_minus.setTag(i);
            text_cart_plus.setTag(i);
            try {
                JSONObject obj_val = new JSONObject(String.valueOf(array_addcart.getJSONObject(i)));

                text_cart_qtytitle.setText(obj_val.getString("purch"));
                text_title_cart_cust.setText(obj_val.getString("name"));
                text_setprice_custveg.setText(obj_val.getString("weights"));
                text_cart_cust1.setText("Price: ₹"+obj_val.getString("amount")+" / "+obj_val.getString("weights"));
                text_cart_cust1.setText(Html.fromHtml( " <font color=#414141>  "+"Price: "+ " </font> <font color=#00b33c> <b> "+"₹ "+obj_val.getString("amount")+"/"+obj_val.getString("weights")+ " </b> </font>"));

                text_cart_cust2.setText("Qty: "+obj_val.getString("purch") + " x " + obj_val.getString("amount"));

                text_cart_cust3.setText("Total: ₹"+obj_val.getString("subtotal"));

                String str_msg11 = "Total: ";
                String str_msg22 = "₹ "+ obj_val.getString("subtotal");
                text_cart_cust3.setText(Html.fromHtml( " <font color=#414141>  "+str_msg11+ " </font> <font color=#00b33c> <b> "+str_msg22+ " </b> </font>"));


                final String str_imageurl = obj_val.getString("image");


                totals +=Integer.parseInt(obj_val.getString("subtotal"));

                // text_cart_subtotal.setText("Subtotal( "+array_addcart.length()+" items ): Rs "+String.valueOf(totals));

                // String str_msg1="Subtotal( ",str_msg2 = String.valueOf(array_addcart.length()),str_msg3=" items ): Rs ",str_msg4=String.valueOf(totals);

                String str_msg1 = "Subtotal( "+array_addcart.length()+" items ): ";
                String str_msg2 = "₹ "+String.valueOf(totals);
                text_cart_subtotal.setText(Html.fromHtml( " <font color=#414141>  "+str_msg1+ " </font> <font color=#00b33c> <b> "+str_msg2+ " </b> </font>"));

                text_cart_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            JSONObject objectval = new JSONObject(String.valueOf(array_addcart.getJSONObject((Integer) v.getTag())));

                            Integer value = Integer.parseInt(objectval.getString("purch"));
                            if (value > 1 )
                            {
                                value--;
                                Integer totalval = value * Integer.parseInt(objectval.getString("amount"));

                                str_upsubtotal = String.valueOf(totalval);
                                str_upid = objectval.getString("id");
                                str_uppurch = String.valueOf(value);
                                str_weights=  objectval.getString("weights");
                                str_amount= objectval.getString("amount");
                                objectval.put("purch",value);
                                objectval.put("subtotal",String.valueOf(totalval));
                                array_addcart.put((Integer) v.getTag(),objectval);
                                //addRow();

                                progressDialog = new ProgressDialog(CartCustomerActivity.this);
                                progressDialog.setMessage("Update cart..."); // Setting Message
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);                                             progressDialog.show(); // Display Progress Dialog
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                new Communication_updateaddcart().execute();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//

                    }
                });
                text_cart_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            JSONObject objectval = new JSONObject(String.valueOf(array_addcart.getJSONObject((Integer) v.getTag())));

                            Integer value = Integer.parseInt(objectval.getString("purch"));
                            if (value >= 1 )
                            {
                                value++;
                                Integer totalval = value * Integer.parseInt(objectval.getString("amount"));

                                str_upsubtotal = String.valueOf(totalval);
                                str_upid = objectval.getString("id");
                                str_uppurch = String.valueOf(value);
                                str_weights=  objectval.getString("weights");
                                str_amount= objectval.getString("amount");
                                objectval.put("purch",value);
                                objectval.put("subtotal",String.valueOf(totalval));
                                array_addcart.put((Integer) v.getTag(),objectval);
                                //addRow();

                                progressDialog = new ProgressDialog(CartCustomerActivity.this);
                                progressDialog.setMessage("Update cart..."); // Setting Message
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);                                             progressDialog.show(); // Display Progress Dialog
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                new Communication_updateaddcart().execute();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//

                    }
                });

                relative_slects_custveg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {


                        final Integer tagid = (Integer)v.getTag();


                        JSONArray jsonArr_key = new JSONArray();
                        JSONArray jsonArr_values = new JSONArray();
                        try {
                            JSONObject objectval = new JSONObject(String.valueOf(array_addcart.getJSONObject(tagid)));


                            str_upid = objectval.getString("id");
                            str_uppurch = objectval.getString("purch");




                            jsonArr_key = new JSONArray(objectval.getString("custdata"));
                            jsonArr_values = new JSONArray(objectval.getString("custdata1"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        PopupMenu menu = new PopupMenu(CartCustomerActivity.this, v);
                        for (int i =0;i<jsonArr_key.length();i++)
                        {
                            try {
                                //  menu.getMenu().add(jsonArr_key.getString(i));
                                menu.getMenu().add(Integer.parseInt(jsonArr_values.getString(i)),i,0,jsonArr_key.getString(i));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
//                        menu.getMenu().add("100gm");  // menus items
//                        menu.getMenu().add("250gm");  // menus items
//                        menu.getMenu().add("500gm");
//                        menu.getMenu().add("1kg");
                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                Integer int_index = item.getGroupId();
                                TextView text_setprice_custveg1 = (TextView)view1.findViewWithTag(tagid).findViewById(R.id.text_setprice_custveg);
                                text_setprice_custveg.setText(String.valueOf(item));


                                try {
                                    JSONObject objectval1 = new JSONObject(String.valueOf(array_addcart.getJSONObject(tagid)));

                                    str_amount = String.valueOf(int_index);
                                    str_weights =String.valueOf(item);
                                    Integer value = Integer.parseInt(str_uppurch);
                                    Integer totalval = value * Integer.parseInt(str_amount);
                                    str_upsubtotal = String.valueOf(totalval);
                                    objectval1.put("weights",String.valueOf(item));
                                    objectval1.put("amount",String.valueOf(int_index));
                                    objectval1.put("subtotal",str_upsubtotal);

                                    array_addcart.put(tagid,objectval1);
                                    progressDialog = new ProgressDialog(CartCustomerActivity.this);
                                    progressDialog.setMessage("Update cart..."); // Setting Message
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);                                             progressDialog.show(); // Display Progress Dialog
                                    progressDialog.setCancelable(false);
                                    progressDialog.show();
                                    new Communication_updateaddcart().execute();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                return false;
                            }
                        });

                        menu.show();
                    }
                });




//                edit_vender_purch.addTextChangedListener(new TextWatcher()
//                {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count)
//                    {
//                        String edit_purchval= String.valueOf(edit_vender_purch.getText());
//                        if (edit_purchval.length() == 0)
//                        {
//                            edit_purchval = "0";
//                        }
//
//
//                        try {
//                            JSONObject objectval = new JSONObject(String.valueOf(array_addcart.getJSONObject((Integer) edit_vender_purch.getTag())));
//
//                            Integer totalval = Integer.parseInt(edit_purchval) * Integer.parseInt(objectval.getString("amount"));
//                            str_upsubtotal = String.valueOf(totalval);
//                            str_upid = objectval.getString("id");
//                            str_uppurch = edit_purchval;
//                            objectval.put("purch",edit_purchval);
//                            objectval.put("subtotal",String.valueOf(totalval));
//                            array_addcart.put((Integer) edit_vender_purch.getTag(),objectval);
//                            //addRow();
//
//                            TextView text_title_vend_pricetotal1 = (TextView)view1.findViewWithTag(edit_vender_purch.getTag());;
//                            text_title_vend_pricetotal1.setText("Total: "+edit_purchval + " x " + objectval.getString("amount") +" = Rs "+  String.valueOf(totalval));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
////                        progressDialog = new ProgressDialog(context);
////                        progressDialog.setMessage("Update cart..."); // Setting Message
////                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);                                             progressDialog.show(); // Display Progress Dialog
////                        progressDialog.setCancelable(false);
////                        progressDialog.show();
//                        new Communication_updateaddcart().execute();
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//
//
//
//                    }
//                });
                Picasso.with(CartCustomerActivity.this)
                        .load(str_imageurl)
                        .placeholder(R.drawable.default1)
                        .into( imageView, new Callback() {
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


            text_cart_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    progressDialog = new ProgressDialog(CartCustomerActivity.this);
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
//    public void onBackPressed() {
//
//    }

}
