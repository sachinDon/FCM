package info.wkweb.com.vmart;

import android.annotation.SuppressLint;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;

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
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import me.relex.circleindicator.CircleIndicator;

import static android.content.Context.MODE_PRIVATE;

public class CustomersActivity extends AppCompatActivity {


    String str_id,str_name,str_amount,str_purch,str_image,str_subtotal,str_weights,str_weights1,str_weights2,str_weights3,str_weights4,str_amount1,str_amount2,str_amount3,str_amount4;
    Integer int_tag_val;
    public static View viewitem;
    ProgressDialog progressDialog;
    SearchView searchbar;
    JSONArray array_vegitables,array_vegitables1,array_root,array_ground,array_underground,array_leaf,array_fruits;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    JSONArray jsonArr_bannerimages;
    ListView listview_customer_veg;
    TextView text_setcat_custcat,text_back_custh,text_cartcusti_cart,text_result_cust_act;

    JSONObject object_values_cust = null;
    ListviewAdaptercart listview_customadpte;
    AlertDialog alert11;
    ProgressBar progress_customer;
    RelativeLayout relatve_category;
    String str_category_select;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);


        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        relatve_category=(RelativeLayout) findViewById(R.id.relative_slects_custcat);
        progress_customer=(ProgressBar)findViewById(R.id.progress_customer);
        searchbar = (SearchView) findViewById(R.id.cust_searchView);
        listview_customer_veg=(ListView)findViewById(R.id.listview_customer_veg);
        text_back_custh=(TextView) findViewById(R.id.text_back_custh);
        text_cartcusti_cart=(TextView) findViewById(R.id.text_cartcusti_cartval);
        text_result_cust_act=(TextView) findViewById(R.id.text_result_cust_act);
        text_result_cust_act.setVisibility(View.INVISIBLE);

//
        LocalBroadcastManager.getInstance(CustomersActivity.this).registerReceiver(cartupdate,
                new IntentFilter("cartcustomerupdate"));

        str_category_select ="All";
        array_vegitables1 = new JSONArray();
        array_vegitables = new JSONArray();
        array_fruits = new JSONArray();
        array_ground = new JSONArray();
        array_root = new JSONArray();
        array_leaf = new JSONArray();
        array_underground = new JSONArray();

        new Communication_vegitables().execute();



        text_setcat_custcat= (TextView) findViewById(R.id.text_setcat_custcat);
        int id = searchbar.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText textView = (EditText) searchbar.findViewById(id);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(Color.WHITE);
        text_cartcusti_cart.setText(HomeActivity.str_count_cartval);
        text_cartcusti_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intens = new Intent(CustomersActivity.this,CartCustomerActivity.class);
                startActivity(intens);
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

                        if (str_category_select.equalsIgnoreCase("All"))
                        {
                            array_vegitables = array_vegitables1;
                        }
                        if (str_category_select.equalsIgnoreCase("Root"))
                        {
                            array_vegitables = array_root;
                        }
                        if (str_category_select.equalsIgnoreCase("Ground"))
                        {
                            array_vegitables = array_ground;
                        }
                        if (str_category_select.equalsIgnoreCase("Leaf"))
                        {
                            array_vegitables = array_leaf;
                        }
                        if (str_category_select.equalsIgnoreCase("Beans"))
                        {
                            array_vegitables = array_underground;
                        }
                        if (str_category_select.equalsIgnoreCase("Fruits"))
                        {
                            array_vegitables = array_fruits;
                        }

                    } else {

                        for (int i = 0; i < array_vegitables1.length(); i++) {

                            try {
                                String string = array_vegitables1.getJSONObject(i).getString("name");
                                String str_category = array_vegitables1.getJSONObject(i).getString("category");
                                // str_search_txt = newText;
                                if ((string.toLowerCase()).contains(newText.toLowerCase())) {

                                    if (str_category_select.equalsIgnoreCase("All"))
                                    {
                                        array_vegitables.put(array_vegitables1.getJSONObject(i));
                                    }
                                    if (str_category_select.equalsIgnoreCase("Root"))
                                    {
                                        if (str_category.equalsIgnoreCase("root")) {
                                            array_vegitables.put(array_vegitables1.getJSONObject(i));
                                        }
                                    }
                                    if (str_category_select.equalsIgnoreCase("Ground"))
                                    {
                                        if (str_category.equalsIgnoreCase("ground")) {
                                            array_vegitables.put(array_vegitables1.getJSONObject(i));
                                        }
                                    }
                                    if (str_category_select.equalsIgnoreCase("Leaf")) {
                                        if (str_category.equalsIgnoreCase("leaf")) {
                                            array_vegitables.put(array_vegitables1.getJSONObject(i));
                                        }
                                    }
                                    if (str_category_select.equalsIgnoreCase("Beans")) {
                                        if (str_category.equalsIgnoreCase("beans")) {
                                            array_vegitables.put(array_vegitables1.getJSONObject(i));
                                        }
                                    }
                                    if (str_category_select.equalsIgnoreCase("Fruits"))
                                    {
                                        if (str_category.equalsIgnoreCase("fruit")) {
                                            array_vegitables.put(array_vegitables1.getJSONObject(i));
                                        }
                                    }


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }


                    }
                    if (array_vegitables != null) {

                        listview_customadpte = new ListviewAdaptercart();
                        listview_customer_veg.setAdapter(listview_customadpte);
                    }


                }
                else
                {
                    listview_customadpte = new ListviewAdaptercart();
                    listview_customer_veg.setAdapter(listview_customadpte);
                }
                return false;
            }
        });
        text_back_custh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        relatve_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {




                PopupMenu menu = new PopupMenu(CustomersActivity.this, v);


                menu.getMenu().add("All");
                menu.getMenu().add("Root");
                menu.getMenu().add("Leaf");
                menu.getMenu().add("Ground");
                menu.getMenu().add("Beans");
                menu.getMenu().add("Fruits");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        text_setcat_custcat.setText(String.valueOf(item));
                        str_category_select = String.valueOf(item);
                        if (str_category_select.equalsIgnoreCase("All"))
                        {
                            array_vegitables = array_vegitables1;
                        }
                        if (str_category_select.equalsIgnoreCase("Root"))
                        {
                            array_vegitables = array_root;
                        }
                        if (str_category_select.equalsIgnoreCase("Ground"))
                        {
                            array_vegitables = array_ground;
                        }
                        if (str_category_select.equalsIgnoreCase("Leaf"))
                        {
                            array_vegitables = array_leaf;
                        }
                        if (str_category_select.equalsIgnoreCase("Beans"))
                        {
                            array_vegitables = array_underground;
                        }
                        if (str_category_select.equalsIgnoreCase("Fruits"))
                        {
                            array_vegitables = array_fruits;
                        }


                        if (array_vegitables != null) {

                            listview_customadpte = new ListviewAdaptercart();
                            listview_customer_veg.setAdapter(listview_customadpte);
                        }
                        return false;
                    }
                });

                menu.show();
            }
        });




    }

    private class ListviewAdaptercart extends BaseAdapter {
        private LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        private View view_slider = null;
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

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            JSONObject object_values_cust = null;
            try {
                object_values_cust = new JSONObject(String.valueOf(array_vegitables.getJSONObject(i)));


//                if (object_values_cust.getString("rowtype").equalsIgnoreCase("banner")) {
//
//                    jsonArr_bannerimages = new JSONArray(object_values_cust.getString("image"));
//
////                    if (view_slider == null) {
//                    view = getLayoutInflater().inflate(R.layout.list_banner_sliders, null, false);
//
//                    final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
//                    CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
//
//                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(CustomersActivity.this);
//                    viewPager.setAdapter(viewPagerAdapter);
//                    indicator.setViewPager(viewPager);
//
//                    final Handler handler = new Handler();
//
//                    final Runnable update = new Runnable() {
//                        public void run()
//                        {
//
//                            viewPager.setCurrentItem(currentPage, true);
//                            if(currentPage == jsonArr_bannerimages.length()-1)
//                            {
//                                currentPage = 0;
//                            }
//                            else
//                            {
//                                ++currentPage ;
//                            }
//                        }
//                    };
//
//
//                    timer = new Timer();
//                    timer.schedule(new TimerTask() {
//
//                        @Override
//                        public void run() {
//                            handler.post(update);
//                        }
//                    }, 500, 3500);
//
//                    view_slider = view;
////                    84
//
//                } else {

                    view = getLayoutInflater().inflate(R.layout.list_customer_veg, null, false);

                    final View view2 = view;
                    //  RelativeLayout relative_slects_custveg=(RelativeLayout)view.findViewById(R.id.relative_slects_custveg);
                    ImageView image_custveg = (ImageView) view.findViewById(R.id.image_custveg);
                    final TextView text_title_custveg = (TextView) view.findViewById(R.id.text_title_custveg);
                    final TextView text_price_custveg = (TextView) view.findViewById(R.id.text_price_custveg);
                    //   final TextView text_setprice_custveg=(TextView)view.findViewById(R.id.text_setprice_custveg);
                    final TextView text_addlist_cutveg = (TextView) view.findViewById(R.id.text_addlist_cutveg);

                    //    relative_slects_custveg.setTag(i);
                    text_addlist_cutveg.setTag(i);
                    //    text_setprice_custveg.setTag(i);
                    text_price_custveg.setTag(i);

                    text_addlist_cutveg.setTextColor(getResources().getColor(R.color.white));
                    GradientDrawable bgShape1 = (GradientDrawable) text_addlist_cutveg.getBackground();
                    bgShape1.mutate();
                    bgShape1.setColor(getResources().getColor(R.color.color_green));
                    bgShape1.setStroke(text_addlist_cutveg.getWidth(), getResources().getColor(R.color.color_green));


                    text_title_custveg.setText(object_values_cust.getString("name"));
                    String str_imageurl = object_values_cust.getString("image");
                    //   text_setprice_custveg.setText(object_values_cust.getString("cust1"));
                    text_price_custveg.setText("Price: ₹" + object_values_cust.getString("cust2") + "/" + object_values_cust.getString("cust1"));

                    if (object_values_cust.getString("cart").equalsIgnoreCase("yes")) {
                        text_addlist_cutveg.setVisibility(View.INVISIBLE);
                    } else {
                        text_addlist_cutveg.setVisibility(View.VISIBLE);
                    }

                    Picasso.with(CustomersActivity.this)
                            .load(str_imageurl)
                            .placeholder(R.drawable.default1)
                            .into(image_custveg, new Callback() {
                                @Override
                                public void onSuccess() {


                                }

                                @Override
                                public void onError() {

                                }
                            });

                    text_addlist_cutveg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            progressDialog = new ProgressDialog(CustomersActivity.this);
                            progressDialog.setMessage("Add to cart..."); // Setting Message
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.show(); // Display Progress Dialog
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            try {
                                JSONObject obj_val = new JSONObject(String.valueOf(array_vegitables.getJSONObject((Integer) v.getTag())));

                                str_id = obj_val.getString("id");
                                str_name = obj_val.getString("name");
                                str_image = obj_val.getString("image");
                                str_amount = obj_val.getString("cust2");
                                str_purch = "1";
                                str_subtotal = obj_val.getString("cust2");
                                str_weights = obj_val.getString("cust1");
                                int_tag_val = (Integer) v.getTag();
                                str_weights1 = obj_val.getString("weights1");
                                str_weights2 = obj_val.getString("weights2");
                                str_weights3 = obj_val.getString("weights3");
                                str_weights4 = obj_val.getString("weights4");
                                str_amount1 = obj_val.getString("amount1");
                                str_amount2 = obj_val.getString("amount2");
                                str_amount3 = obj_val.getString("amount3");
                                str_amount4 = obj_val.getString("amount4");
                                Integer int_tag_val;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            new Communication_addcart().execute();


                        }
                    });



               // }
//                return view;
            } catch (JSONException e) {
                e.printStackTrace();
            }
           return view;
        }
    }

    public class Communication_vegitables extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.vegitable);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("email", pref.getString("userid",""));
                postDataParams.put("type","customer");

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

            progress_customer.setVisibility(View.INVISIBLE);
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
                    text_result_cust_act.setVisibility(View.VISIBLE);

                }
                else
                {
                    text_result_cust_act.setVisibility(View.INVISIBLE);
                    if (!result.equalsIgnoreCase("") && result.length()>=50) {
                        try {
                            array_vegitables = new JSONArray(result);
                            array_vegitables1 = new JSONArray(result);
                            array_fruits = new JSONArray();
                            array_ground = new JSONArray();
                            array_root = new JSONArray();
                            array_leaf = new JSONArray();
                            array_underground = new JSONArray();
                            if (array_vegitables !=null) {


                                for (int i = 0;i<array_vegitables1.length() ; i++) {
                                    JSONObject obj_values = new JSONObject(String.valueOf(array_vegitables1.getJSONObject(i)));

                                        String str_category = obj_values.getString("category");
                                        if (str_category.equalsIgnoreCase("root")) {
                                            array_root.put(obj_values);
                                        }
                                        if (str_category.equalsIgnoreCase("ground")) {
                                            array_ground.put(obj_values);
                                        }
                                        if (str_category.equalsIgnoreCase("leaf")) {
                                            array_leaf.put(obj_values);
                                        }
                                        if (str_category.equalsIgnoreCase("Beans")) {
                                            array_underground.put(obj_values);
                                        }
                                        if (str_category.equalsIgnoreCase("fruit")) {
                                            array_fruits.put(obj_values);
                                        }




                                    if (str_category_select.equalsIgnoreCase("All")) {
                                        array_vegitables = array_vegitables1;
                                    }
                                    if (str_category_select.equalsIgnoreCase("Root")) {
                                        array_vegitables = array_root;
                                    }
                                    if (str_category_select.equalsIgnoreCase("Ground")) {
                                        array_vegitables = array_ground;
                                    }
                                    if (str_category_select.equalsIgnoreCase("Leaf")) {
                                        array_vegitables = array_leaf;
                                    }
                                    if (str_category_select.equalsIgnoreCase("Beans")) {
                                        array_vegitables = array_underground;
                                    }
                                    if (str_category_select.equalsIgnoreCase("Fruits")) {
                                        array_vegitables = array_fruits;
                                    }
                                }


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

    public class Communication_addcart extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.custaddcart);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("email", pref.getString("userid",""));
                postDataParams.put("id",str_id);
                postDataParams.put("amount",str_amount);
                postDataParams.put("purch",str_purch);
                postDataParams.put("subtotal",str_subtotal);
                postDataParams.put("image",str_image);
                postDataParams.put("name",str_name);
                postDataParams.put("weights",str_weights);
                postDataParams.put("weights1",str_weights1);
                postDataParams.put("weights2",str_weights2);
                postDataParams.put("weights3",str_weights3);
                postDataParams.put("weights4",str_weights4);
                postDataParams.put("amount1",str_amount1);
                postDataParams.put("amount2",str_amount2);
                postDataParams.put("amount3",str_amount3);
                postDataParams.put("amount4",str_amount4);

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

            if (progressDialog !=null) {
                progressDialog.dismiss();
            }
            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {



                } else if (result.equalsIgnoreCase("error"))
                {


                }
                else if (result.equalsIgnoreCase("added"))
                {

                    AlertDialog.Builder builder4 = new AlertDialog.Builder(CustomersActivity.this);
                    builder4.setTitle("Sucessful");
                    builder4.setMessage("Your item added to list");
                    builder4.setCancelable(false);
                    builder4.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                    new Communication_vegitables().execute();
                                   new Communication_countaddcart().execute();
                                    Intent intent1 = new Intent("cartcustomerupdate1");
                                    LocalBroadcastManager.getInstance(CustomersActivity.this).sendBroadcast(intent1);

                                }
                            });
                    alert11 = builder4.create();
                    alert11.show();

                    try {
                        JSONObject obj_val = new JSONObject(String.valueOf(array_vegitables.getJSONObject(int_tag_val)));

                        obj_val.put("cart","yes");
                        array_vegitables.put(int_tag_val,obj_val);
                        // TextView textview_addcart = (TextView)viewitem.findViewWithTag(int_tag_val);
                        // textview_addcart.setVisibility(int_tag_val);
//                        notifyItemChanged(int_tag_val);
                        // listview_customadpte = new ListviewAdaptercart();
                        // listview_customer_veg.setAdapter(listview_customadpte);
                        listview_customadpte.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
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
    public class Communication_countaddcart extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.countcartval);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("email", pref.getString("userid",""));
                postDataParams.put("type",pref.getString("type",""));


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



                } else if (result.equalsIgnoreCase("error"))
                {


                }
                else
                {



                    try {

                        JSONArray jsonarray = new JSONArray(result);
                        if (jsonarray.length() !=0)
                        {
                            JSONObject obj_val = new JSONObject(String.valueOf(jsonarray.getJSONObject(0)));
                            HomeActivity.str_count_cartval=obj_val.getString("count");
                            text_cartcusti_cart.setText(obj_val.getString("count"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
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
    private BroadcastReceiver cartupdate = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent)
        {

            new Communication_vegitables().execute();
            new Communication_countaddcart().execute();

        }
    };



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
