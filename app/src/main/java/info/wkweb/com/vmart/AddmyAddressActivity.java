package info.wkweb.com.vmart;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class AddmyAddressActivity extends AppCompatActivity {

    EditText edit_addd_fullname,edit_addd_home,
            edit_adddress,edit_landmark_addd,edit_pncode_addd,edit_mobilenumber_addd;
    TextView text_addd_save,text_back_addd;

    String str_fullname,str_addhome,str_address,str_landmark,str_pincode,str_mobileno,str_id;
   public  static  String str_selecttype;
   public  static Integer indexArray;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    AlertDialog alert11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmy_address);

        text_back_addd = (TextView)findViewById(R.id.text_back_addd);
        edit_addd_fullname = (EditText)findViewById(R.id.edit_addd_fullname);
        edit_addd_home = (EditText)findViewById(R.id.edit_addd_home);
        edit_adddress = (EditText)findViewById(R.id.edit_adddress);
        edit_landmark_addd = (EditText)findViewById(R.id.edit_landmark_addd);
        edit_pncode_addd = (EditText)findViewById(R.id.edit_pncode_addd);
        edit_mobilenumber_addd = (EditText)findViewById(R.id.edit_mobilenumber_addd);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();


        str_fullname="";
        str_addhome="";
        str_address="";
        str_landmark="";
        str_pincode="";
        str_mobileno="";
        str_id = "";

        text_addd_save = (TextView)findViewById(R.id.text_addd_save);
        text_addd_save.setEnabled(false);
        text_addd_save.setBackgroundResource(R.drawable.roubdedgray);
        text_addd_save.setTextColor(getResources().getColor(R.color.black));

        if (str_selecttype.equalsIgnoreCase("update"))
        {
            text_addd_save.setEnabled(true);
            text_addd_save.setBackgroundResource(R.drawable.roundedgreen);
            text_addd_save.setTextColor(getResources().getColor(R.color.white));
            try {


                JSONObject obj_val = new JSONObject(String.valueOf(SingletonObject.instance.getArray_MyAddress().getJSONObject(indexArray)));

                edit_addd_fullname.setText(obj_val.getString("fullname"));
                edit_addd_home.setText(obj_val.getString("home"));
                edit_adddress.setText(obj_val.getString("address"));
                edit_landmark_addd.setText(obj_val.getString("landmark"));
                edit_pncode_addd.setText(obj_val.getString("pincode"));
                edit_mobilenumber_addd.setText(obj_val.getString("mobile"));
                str_id = obj_val.getString("id");
                str_fullname = obj_val.getString("fullname");
                str_addhome=obj_val.getString("home");
                str_address=obj_val.getString("address");
                str_landmark=obj_val.getString("landmark");
                str_pincode=obj_val.getString("pincode");
                str_mobileno=obj_val.getString("mobile");


            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        }

        text_back_addd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        text_addd_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(AddmyAddressActivity.this);
                progressDialog.setMessage("Loading..."); // Setting Message
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);

                new communication_AddAddress().execute();
            }
        });


        edit_addd_fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                str_fullname = String.valueOf(edit_addd_fullname.getText());

                if (str_fullname.length() !=0 && str_addhome.length() !=0 && str_address.length() !=0 && str_landmark.length() !=0 && str_pincode.length() !=0 && str_mobileno.length() !=0)
                {
                    TextChangetrue();

                } else
                {
                    TextChangefalse();

                }



            }
            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        edit_addd_home.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                str_addhome = String.valueOf(edit_addd_home.getText());

                if (str_fullname.length() !=0 && str_addhome.length() !=0 && str_address.length() !=0 && str_landmark.length() !=0 && str_pincode.length() !=0 && str_mobileno.length() !=0)
                {
                   TextChangetrue();

                } else
                {
                    TextChangefalse();

                }




            }
            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        edit_adddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                str_address = String.valueOf(edit_adddress.getText());

                if (str_fullname.length() !=0 && str_addhome.length() !=0 && str_address.length() !=0 && str_landmark.length() !=0 && str_pincode.length() !=0 && str_mobileno.length() !=0)
                {
                    TextChangetrue();

                } else
                {
                    TextChangefalse();

                }



            }
            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        edit_landmark_addd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                str_landmark = String.valueOf(edit_landmark_addd.getText());

                if (str_fullname.length() !=0 && str_addhome.length() !=0 && str_address.length() !=0 && str_landmark.length() !=0 && str_pincode.length() !=0 && str_mobileno.length() !=0)
                {
                  TextChangetrue();

                } else
                {
                    TextChangefalse();

                }



            }
            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        edit_pncode_addd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                str_pincode = String.valueOf(edit_pncode_addd.getText());

                if (str_fullname.length() !=0 && str_addhome.length() !=0 && str_address.length() !=0 && str_landmark.length() !=0 && str_pincode.length() !=0 && str_mobileno.length() !=0)
                {
                    TextChangetrue();

                } else
                {
                    TextChangefalse();

                }



            }
            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        edit_mobilenumber_addd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                str_mobileno = String.valueOf(edit_mobilenumber_addd.getText());

                if (str_fullname.length() !=0 && str_addhome.length() !=0 && str_address.length() !=0 && str_landmark.length() !=0 && str_pincode.length() !=0 && str_mobileno.length() !=0)
                {
                  TextChangetrue();

                } else
                {
                    TextChangefalse();

                }



            }
            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }


    public  void TextChangefalse()
    {
        text_addd_save.setEnabled(false);
        text_addd_save.setBackgroundResource(R.drawable.roubdedgray);
        text_addd_save.setTextColor(getResources().getColor(R.color.black));


    }

    public  void TextChangetrue()
    {
        text_addd_save.setEnabled(true);
        text_addd_save.setBackgroundResource(R.drawable.roundedgreen);
        text_addd_save.setTextColor(getResources().getColor(R.color.white));
    }
    public class communication_AddAddress extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... args0) {

            try {
                URL url = new URL(Urlclass.address);
                JSONObject postDataParams = new JSONObject();


                postDataParams.put("email",pref.getString("userid",""));
                postDataParams.put("fullname",str_fullname);
                postDataParams.put("home",str_addhome);
                postDataParams.put("address",str_address);
                postDataParams.put("landmark",str_landmark);
                postDataParams.put("pincode",str_pincode);
                postDataParams.put("mobile",str_mobileno);
                postDataParams.put("id",str_id);
                postDataParams.put("select",str_selecttype);



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

            Log.d("resuult", result);
            progressDialog.dismiss();
            if (result !=null)
            {
                if (!result.equalsIgnoreCase(""))
                {

                    if (result.equalsIgnoreCase("nomatch"))
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(AddmyAddressActivity.this);
                        builder2.setTitle("Oops");
                        builder2.setMessage("The email address you have entered is not registered in our system or your account has been deactivated. Please try again.");
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
                    else if (result.equalsIgnoreCase("updateerror"))
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(AddmyAddressActivity.this);
                        builder2.setTitle("Oops");
                        builder2.setMessage("Connection time out. Please try again.");
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
                    else if (result.equalsIgnoreCase("sent"))
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(AddmyAddressActivity.this);
                        builder2.setTitle("Regester");
                        builder2.setMessage("Your address has been register.");
                        builder2.setCancelable(false);
                        builder2.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                        finish();
                                    }
                                });
                        alert11 = builder2.create();
                        alert11.show();


                    }
                    else if (result.equalsIgnoreCase("updated"))
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(AddmyAddressActivity.this);
                        builder2.setTitle("Updated!");
                        builder2.setMessage("Your address has been updated.");
                        builder2.setCancelable(false);
                        builder2.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                        try {

                                            JSONObject obj_val = new JSONObject(String.valueOf(SingletonObject.instance.getArray_MyAddress().getJSONObject(indexArray)));

                                            obj_val.put("fullname",str_fullname);
                                            obj_val.put("home",str_addhome);
                                            obj_val.put("address",str_address);
                                            obj_val.put("landmark",str_landmark);
                                            obj_val.put("pincode",str_pincode);
                                            obj_val.put("mobile",str_mobileno);
                                            SingletonObject.Instance().setArray_MyAddress(SingletonObject.Instance().getArray_MyAddress().put(indexArray,obj_val));

                                            Intent intent1 = new Intent("updateaddress");
                                            LocalBroadcastManager.getInstance(AddmyAddressActivity.this).sendBroadcast(intent1);

                                        } catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }


                                        finish();
                                    }
                                });
                        alert11 = builder2.create();
                        alert11.show();


                    }
                   else if (result.equalsIgnoreCase("updateerror"))
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(AddmyAddressActivity.this);
                        builder2.setTitle("Oops");
                        builder2.setMessage("Connection time out. Please try again.");
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
                    else
                    {
                        if (!result.equalsIgnoreCase("") && result.length()>=50) {
                            try
                            {

                                JSONArray  array_addAddress = new JSONArray(result);
                                if (array_addAddress !=null)
                                {
                                    JSONObject objectval = new JSONObject(String.valueOf(array_addAddress.getJSONObject(0)));
                                    SingletonObject.Instance().setArray_MyAddress(SingletonObject.Instance().getArray_MyAddress().put(objectval));
                                    Intent intent1 = new Intent("updateaddress");
                                    LocalBroadcastManager.getInstance(AddmyAddressActivity.this).sendBroadcast(intent1);

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            finish();
                        }
                        else
                        {
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(AddmyAddressActivity.this);
                            builder2.setTitle("Oops");
                            builder2.setMessage("Server could not found.");
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
                else
                {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(AddmyAddressActivity.this);
                    builder2.setTitle("Oops");
                    builder2.setMessage("Server could not found.");
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
            else
            {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(AddmyAddressActivity.this);
                builder2.setTitle("Oops");
                builder2.setMessage("Server could not found.");
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
