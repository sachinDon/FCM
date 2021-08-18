package info.wkweb.com.vmart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

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
import java.util.Calendar;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SiginupActivity extends AppCompatActivity {


    public SharedPreferences pref;
    SharedPreferences.Editor editor;

    private TextView btn_login;
    Context context;
    String str_emailchk="no";
    private EditText edit_name, edit_email, edit_password, edit_mobile;
    private Button button_signup;
    ProgressDialog progressDialog;

    AlertDialog alert11;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siginup);

        edit_name = (EditText) findViewById(R.id.name);
        edit_email = (EditText) findViewById(R.id.email);
        edit_password = (EditText) findViewById(R.id.password);
        edit_mobile = (EditText) findViewById(R.id.sig_mobile);
        btn_login = (TextView) findViewById(R.id.btn_login);
        button_signup = (Button) findViewById(R.id.button_signup);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        editor.putString("login","no");
        editor.commit();

        str_emailchk="no";
        button_signup.setEnabled(false);
        button_signup.setTextColor(getResources().getColor(R.color.lighit_gray_sys));
        final String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        edit_name.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (edit_name.getText().length() !=0 && str_emailchk.equalsIgnoreCase("yes") && edit_password.getText().length() !=0 && edit_mobile.getText().length() !=0  )
                {

                    button_signup.setEnabled(true);
                    button_signup.setTextColor(Color.WHITE);
                }

                else
                {
                    button_signup.setEnabled(false);
                    button_signup.setTextColor(getResources().getColor(R.color.lighit_gray_sys));

                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        edit_email.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String emailValues= String.valueOf(edit_email.getText());
                if (emailValues.matches(emailPattern))
                {
                    str_emailchk="yes";
                    if (edit_name.getText().length() !=0 &&  str_emailchk.equalsIgnoreCase("yes") && edit_password.getText().length() !=0 && edit_mobile.getText().length() !=0  )
                    {

                        button_signup.setEnabled(true);
                        button_signup.setTextColor(Color.WHITE);
                    }

                    else
                    {
                        button_signup.setEnabled(false);
                        button_signup.setTextColor(getResources().getColor(R.color.lighit_gray_sys));

                    }
                    // Toast.makeText(MainActivity.this, "visible", Toast.LENGTH_SHORT).show();
                } else
                {
                    str_emailchk="no";
                    edit_email.setHint("Email address ");
                    edit_email.setError("Invalid email address");
                    button_signup.setEnabled(false);
                    button_signup.setTextColor(getResources().getColor(R.color.lighit_gray_sys));

                }


            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        edit_password.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (edit_name.getText().length() !=0 &&  str_emailchk.equalsIgnoreCase("yes") && edit_password.getText().length() !=0 && edit_mobile.getText().length() !=0  )
                {

                    button_signup.setEnabled(true);
                    button_signup.setTextColor(Color.WHITE);
                }

                else
                {
                    button_signup.setEnabled(false);
                    button_signup.setTextColor(getResources().getColor(R.color.lighit_gray_sys));

                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        edit_mobile.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (edit_name.getText().length() !=0 &&  str_emailchk.equalsIgnoreCase("yes") && edit_password.getText().length() !=0 && edit_mobile.getText().length() !=0  )
                {

                    button_signup.setEnabled(true);
                    button_signup.setTextColor(Color.WHITE);
                }

                else
                {
                    button_signup.setEnabled(false);
                    button_signup.setTextColor(getResources().getColor(R.color.lighit_gray_sys));

                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            finish();

            }
        });

        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {




                progressDialog = new ProgressDialog(SiginupActivity.this);
                progressDialog.setMessage("Loading..."); // Setting Message
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);                                             progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);
                progressDialog.show();
                new Communication_Signup().execute();

            }


        });
    }

    public class Communication_Signup extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.register);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("email", edit_email.getText());
                postDataParams.put("password", edit_password.getText());
                postDataParams.put("name", edit_name.getText());
                postDataParams.put("mobile", edit_mobile.getText());
                postDataParams.put("register","no");
                postDataParams.put("type", "cust");
//                FirebaseAuth mAuth = FirebaseAuth.getInstance();
//                String devicetoken = FirebaseInstanceId.getInstance().getToken();
                postDataParams.put("devicetoken", "");
                postDataParams.put("platform", "android");
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

                if (result.equalsIgnoreCase("emexist"))
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(SiginupActivity.this);
                    builder1.setTitle("Oops");
                    builder1.setMessage("You already have  account registered with this email id and mobile number. Please login with another email id or mobile number.");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alert11 = builder1.create();
                    alert11.show();



                }  else if (result.equalsIgnoreCase("emailexist"))

                {
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(SiginupActivity.this);
                    builder3.setMessage("You already have an Account registered with this email id. Please login with your registered email.");
                    builder3.setCancelable(false);
                    builder3.setTitle("Oops");
                    builder3.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alert11 = builder3.create();
                    alert11.show();





                }
                else if (result.equalsIgnoreCase("mobileexist"))

                {
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(SiginupActivity.this);
                    builder3.setMessage("You have already registered your mobile number.");
                    builder3.setCancelable(false);
                    builder3.setTitle("Oops");
                    builder3.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alert11 = builder3.create();
                    alert11.show();





                }
                else if (result.equalsIgnoreCase("nullerror"))

                {
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(SiginupActivity.this);
                    builder4.setTitle("Oops");
                    builder4.setMessage("Could not retrieve one of the Account Ids. Please login and try again.");
                    builder4.setCancelable(false);
                    builder4.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alert11 = builder4.create();
                    alert11.show();


                } else if (result.equalsIgnoreCase("inserterror"))

                {
                    AlertDialog.Builder builder5 = new AlertDialog.Builder(SiginupActivity.this);
                    builder5.setMessage("Could not insert  Please try again.");
                    builder5.setTitle("Oops");
                    builder5.setCancelable(false);
                    builder5.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alert11 = builder5.create();
                    alert11.show();

                }
                else
                {
                    if (!result.equalsIgnoreCase("") && result.length()>=50) {
                        try {
                            JSONArray jsonarray = new JSONArray(result);

                            JSONObject obj_values=new JSONObject(String.valueOf(jsonarray.getString(0)));

                            Log.d("register", String.valueOf(obj_values));

if (jsonarray !=null)
{
//    OTPActivity.Strmobileno = obj_values.getString("mobile");
//    editor.putString("userid", String.valueOf(obj_values.getString("mobile")));
//    editor.commit();
//    Intent intent = new Intent(SiginupActivity.this,OTPActivity.class);
//    startActivity(intent);

    String str_type = obj_values.getString("type");
    String str_name = obj_values.getString("name");
    String str_register = obj_values.getString("email");

    editor.putString("name",str_name);
    editor.putString("userid",str_register);
    editor.putString("type",str_type);
    editor.putString("login","yes");
    editor.putString("address","no");
    JSONArray array_addAddress = new JSONArray();
    editor.putString("array_address",array_addAddress.toString());
    editor.commit();

    Intent intent = new Intent(SiginupActivity.this,HomeActivity.class);
    startActivity(intent);
    editor.putString("login","yes");
    editor.commit();

}





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(SiginupActivity.this);
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
                AlertDialog.Builder builder2 = new AlertDialog.Builder(SiginupActivity.this);
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
