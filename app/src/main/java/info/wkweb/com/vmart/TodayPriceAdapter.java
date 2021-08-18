package info.wkweb.com.vmart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by learntodrill on 11/02/20.
 */

public class TodayPriceAdapter extends RecyclerView.Adapter<TodayPriceAdapter.ViewHolder>
{
    private JSONArray listdata;
    private  Context context;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    String str_id,str_name,str_amount,str_purch,str_image,str_subtotal,str_weightv;
    Integer int_tag_val;
    public static View viewitem;
    ProgressDialog progressDialog;

    // RecyclerView recyclerView;
    public TodayPriceAdapter(JSONArray listdata , Context context)
    {
        this.listdata = listdata;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        pref = context.getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            JSONObject obj_val = new JSONObject(String.valueOf(listdata.getJSONObject(position)));

            holder.textView.setText(obj_val.getString("name"));
            holder.textView_price.setText("Price: ₹"+obj_val.getString("price")+"/"+obj_val.getString("weightv"));
            final String str_imageurl = obj_val.getString("image");
            holder.textview_addcart.setTag(position);

            if (obj_val.getString("cart").equalsIgnoreCase("yes"))
            {
                holder.textview_addcart.setVisibility(View.INVISIBLE);
            }
            else
            {
                holder.textview_addcart.setVisibility(View.VISIBLE);
            }

            holder.textview_addcart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Add to cart..."); // Setting Message
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);                                             progressDialog.show(); // Display Progress Dialog
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    try {
                        JSONObject obj_val = new JSONObject(String.valueOf(listdata.getJSONObject((Integer) v.getTag())));

                        str_id = obj_val.getString("id");
                        str_name = obj_val.getString("name");
                        str_image = obj_val.getString("image");
                        str_amount = obj_val.getString("price");
                        str_weightv  = obj_val.getString("weightv");
                        str_purch = "1";
                        str_subtotal = obj_val.getString("price");
                        int_tag_val = (Integer)v.getTag();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new Communication_addcart().execute();
                }
            });

            Picasso.with(context)
                    .load(str_imageurl)
                    .placeholder(R.drawable.default1)
                    .into( holder.imageView, new Callback() {
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

    }



    @Override
    public int getItemCount() {
        return listdata.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView,textview_addcart,textView_price;
        public RelativeLayout relativeLayout;
        public  View viewit;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.image_vend);
            this.textView = (TextView) itemView.findViewById(R.id.text_title_vend);
            this.textView_price = (TextView) itemView.findViewById(R.id.text_title_vend_price);
            this.textview_addcart = (TextView) itemView.findViewById(R.id.text_title_vend_Addcart);

            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
            this.viewit = itemView;

            viewitem = this.viewit;
        }
    }

    public class Communication_addcart extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.addcart);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("email", pref.getString("userid",""));
                postDataParams.put("id",str_id);
                postDataParams.put("amount",str_amount);
                postDataParams.put("purch",str_purch);
                postDataParams.put("subtotal",str_subtotal);
                postDataParams.put("image",str_image);
                postDataParams.put("name",str_name);
                postDataParams.put("weightv",str_weightv);



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

            progressDialog.dismiss();
            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {



                } else if (result.equalsIgnoreCase("error"))
                {


                }
                else if (result.equalsIgnoreCase("added"))
                {

                    try {
                        JSONObject obj_val = new JSONObject(String.valueOf(listdata.getJSONObject(int_tag_val)));

                        obj_val.put("cart","yes");
                        listdata.put(int_tag_val,obj_val);
                        // TextView textview_addcart = (TextView)viewitem.findViewWithTag(int_tag_val);
                        // textview_addcart.setVisibility(int_tag_val);
                        notifyItemChanged(int_tag_val);
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
