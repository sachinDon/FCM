package info.wkweb.com.vmart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import androidx.appcompat.app.AlertDialog;

public class LoginActivity extends Activity {

    private TextView text_signup, txt_forgotpass;
    private EditText eEmail, ePassword;
    private Button buton_login;
    FrameLayout framelogin;
    String str_emailchk="no",str_emailid;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    AlertDialog alert11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


       text_signup = (TextView) findViewById(R.id.text_signup);
        buton_login = (Button) findViewById(R.id.btn_login_fragment);
        eEmail = (EditText) findViewById(R.id.eEmail);
        ePassword = (EditText) findViewById(R.id.ePassword);
        txt_forgotpass = (TextView) findViewById(R.id.txt_forgotpass);
        //framelogin = (FrameLayout) findViewById(R.id.framelogin);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        editor.putString("login","no");
        editor.commit();
        buton_login.setEnabled(false);
        buton_login.setTextColor(getResources().getColor(R.color.lighit_gray_sys));



//        GradientDrawable bgShape1 = (GradientDrawable)ePassword.getBackground();
//        bgShape1.mutate();
//        bgShape1.setColor(getResources().getColor(R.color.white));

        text_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intents = new Intent(LoginActivity.this,SiginupActivity.class);
                startActivity(intents);

            }
        });

        txt_forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater =LoginActivity.this.getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.custom_textview_popup, null);
                TextView textview_title = alertLayout.findViewById(R.id.textview_alert_invite_title);
                TextView textview_title_dialog = alertLayout.findViewById(R.id.textview_alert_invite_message);
                final EditText textview_email_No_enter = alertLayout.findViewById(R.id.textview_emailtext);
                final TextView textview_canced = alertLayout.findViewById(R.id.textview_alert_msg_cancel);
                final TextView textview_ok = alertLayout.findViewById(R.id.textview_alert_msg_ok);

                RelativeLayout backround_view = alertLayout.findViewById(R.id.cutom_dilaog_invited2);

                textview_title.setText("Forgot Password");
                textview_title_dialog.setText("To send your password, please enter your registered email address.");
                textview_email_No_enter.setHint("Enter email address");

                InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
                final AlertDialog dialog1 = alert.create();
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.show();


                GradientDrawable bgShape = (GradientDrawable)backround_view.getBackground();
                bgShape.mutate();
                bgShape.setColor(Color.WHITE);

                GradientDrawable bgShape1 = (GradientDrawable)textview_email_No_enter.getBackground();
                bgShape1.mutate();
                bgShape1.setColor(Color.WHITE);

                textview_ok.setText("Send");
                textview_ok.setEnabled(false);
                textview_ok.setTextColor(getResources().getColor(R.color.txt_lightgray));

                final String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


                textview_email_No_enter.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                        String emailValues= String.valueOf(textview_email_No_enter.getText());
                        if (emailValues.matches(emailPattern))
                        {
                            textview_ok.setEnabled(true);
                            textview_ok.setTextColor(getResources().getColor(R.color.colorPrimary));

                        } else
                        {
                            textview_ok.setEnabled(false);
                            textview_ok.setTextColor(getResources().getColor(R.color.txt_lightgray));

                        }


                    }
                    @Override
                    public void afterTextChanged(Editable s) {

                    }

                });


                textview_canced.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {


                        str_emailid="";
                        dialog1.dismiss();

                    }
                });

                textview_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        str_emailid=textview_email_No_enter.getText().toString();
                        //   str_emailAdd="";
                        dialog1.dismiss();

                        progressDialog = new ProgressDialog(LoginActivity.this);
                        progressDialog.setMessage("Loading..."); // Setting Message
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                        progressDialog.show(); // Display Progress Dialog
                        progressDialog.setCancelable(false);

                        new communication_forgotpassword().execute();

                    }
                });
            }
        });

        buton_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Loading..."); // Setting Message
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);                                             progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);
                progressDialog.show();

                new Communication_Signup().execute();


            }
        });


        str_emailchk="no";

        final String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        // Mate
        ePassword.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (ePassword.getText().length() !=0 && str_emailchk.equalsIgnoreCase("yes"))
                {

                    buton_login.setEnabled(true);
                    buton_login.setTextColor(Color.WHITE);
                }

                else
                {
                    buton_login.setEnabled(false);
                    buton_login.setTextColor(getResources().getColor(R.color.lighit_gray_sys));

                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        eEmail.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String emailValues= String.valueOf(eEmail.getText());
                if (emailValues.matches(emailPattern))
                {
                    str_emailchk="yes";
                    if (ePassword.getText().length() !=0 &&  str_emailchk.equalsIgnoreCase("yes") )
                    {

                        buton_login.setEnabled(true);
                        buton_login.setTextColor(Color.WHITE);
                    }

                    else
                    {
                        buton_login.setEnabled(false);
                        buton_login.setTextColor(getResources().getColor(R.color.lighit_gray_sys));

                    }
                    // Toast.makeText(MainActivity.this, "visible", Toast.LENGTH_SHORT).show();
                } else
                {
                    str_emailchk="no";
                    eEmail.setHint("Email address");
                    eEmail.setError("Invalid email address");
                    buton_login.setEnabled(false);
                    buton_login.setTextColor(getResources().getColor(R.color.lighit_gray_sys));

                }


            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    public class Communication_Signup extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.login);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("email",  String.valueOf(eEmail.getText()));
                postDataParams.put("password",  String.valueOf(ePassword.getText()));

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
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(LoginActivity.this);
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


                } else if (result.equalsIgnoreCase("error"))

                {
                    AlertDialog.Builder builder5 = new AlertDialog.Builder(LoginActivity.this);
                    builder5.setMessage("You have been entered wrong username and password, Please try again");
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
                            JSONObject object = jsonarray.getJSONObject(0);
                        if (jsonarray !=null) {

                            String str_type = object.getString("type");
                            String str_name = object.getString("name");
                            //String str_register = object.getString("register");
//                            if (str_register.equalsIgnoreCase("yes"))
//                            {

                            editor.putString("userid", String.valueOf(eEmail.getText()));
                            editor.putString("type",str_type);
                            editor.putString("name",str_name);
                            editor.putString("login","yes");
                            editor.putString("address","no");
                            JSONArray array_addAddress = new JSONArray();
                            editor.putString("array_address",array_addAddress.toString());
                            editor.commit();
                            editor.commit();

                            if (str_type.equalsIgnoreCase("farmer"))
                            {
                                Intent intent = new Intent(LoginActivity.this, FarmerActivity.class);
                                startActivity(intent);
                            }
                            else
                            {

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }

//                            }
//                            else
//                            {
//                                OTPActivity.Strmobileno = object.getString("mobile");
//                                OTPActivity.Stremail = object.getString("email");
//                                editor.putString("userid", String.valueOf(eEmail.getText()));
//                                editor.commit();
//                                Intent intent = new Intent(LoginActivity.this,OTPActivity.class);
//                                startActivity(intent);
//                            }
                        }
                        else
                        {
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this);
                            builder2.setTitle("Oops");
                            builder2.setMessage("could not login.");
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

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this);
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
                AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this);
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
    public class communication_forgotpassword extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... args0) {

            try {
                URL url = new URL(Urlclass.forgotpassword);
                JSONObject postDataParams = new JSONObject();


                postDataParams.put("email",str_emailid);
                Log.e("params", postDataParams.toString());
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

                    if (result.equalsIgnoreCase("noemail"))
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this);
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
                    else if (result.equalsIgnoreCase("sent"))
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this);
                        builder2.setTitle("Oops");
                        builder2.setMessage("Your password has been sent to your registered email address. Thank-you!");
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
                    if (result.equalsIgnoreCase("updateerror"))
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this);
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

                }
                else
                {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this);
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
                AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this);
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

    public void onBackPressed() {

    }
}
