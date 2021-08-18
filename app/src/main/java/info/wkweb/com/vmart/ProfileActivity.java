package info.wkweb.com.vmart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
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

import org.json.JSONArray;
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

public class ProfileActivity extends AppCompatActivity {


    String str_name,str_mobilenumber;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    AlertDialog alert11;

    TextView text_back_profile,profo_logo,pro_name,pro_editprofile,pro_address,pro_Cahngepass,pro_DeleteAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        text_back_profile = (TextView)findViewById(R.id.text_back_profile);
        profo_logo = (TextView)findViewById(R.id.profo_logo);
        pro_name = (TextView)findViewById(R.id.pro_name);
        pro_editprofile = (TextView)findViewById(R.id.pro_editprofile);
        pro_address = (TextView)findViewById(R.id.pro_address);
        pro_Cahngepass = (TextView)findViewById(R.id.pro_Cahngepass);
        pro_DeleteAccount = (TextView)findViewById(R.id.pro_DeleteAccount);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        str_name = "";
        str_mobilenumber = "";
        text_back_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        pro_name.setText(pref.getString("name",""));
        profo_logo.setText(pref.getString("name","").substring(0,1));


        pro_editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater =ProfileActivity.this.getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.popup_edit_values, null);
                TextView textview_title = alertLayout.findViewById(R.id.textview_alert_invite_title_edit);
                TextView textview_title_dialog = alertLayout.findViewById(R.id.textview_alert_invite_message_edit);
                final EditText textview_email_No_enter = alertLayout.findViewById(R.id.textview_emailtext_edit);
                final EditText textview_email_No_enter1 = alertLayout.findViewById(R.id.textview_emailtext_edit1);
                final TextView textview_canced = alertLayout.findViewById(R.id.textview_alert_msg_cancel_edit);
                final TextView textview_ok = alertLayout.findViewById(R.id.textview_alert_msg_ok_edit);

                RelativeLayout backround_view = alertLayout.findViewById(R.id.popup_dialog_edit_values);

                textview_title.setText("Edit Details");
                textview_title_dialog.setText("To update your name and mobile number, please enter your details.");
                textview_email_No_enter.setHint("Enter name");
                textview_email_No_enter1.setHint("Enter mobile number");

                InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);
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

                GradientDrawable bgShape2 = (GradientDrawable)textview_email_No_enter1.getBackground();
                bgShape2.mutate();
                bgShape2.setColor(Color.WHITE);

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


                        //String emailValues= String.valueOf(textview_email_No_enter.getText());
                        str_name = String.valueOf(textview_email_No_enter.getText());
                        if (str_name.length() !=0 && str_mobilenumber.length() !=0)
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

                textview_email_No_enter1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                        str_mobilenumber= String.valueOf(textview_email_No_enter1.getText());
                        if (str_name.length() !=0 && str_mobilenumber.length() !=0)
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


                        str_name="";
                        str_mobilenumber="";
                        dialog1.dismiss();

                    }
                });

                textview_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        str_name=textview_email_No_enter.getText().toString();
                        str_mobilenumber=textview_email_No_enter1.getText().toString();
                        //   str_emailAdd="";
                        dialog1.dismiss();

                        progressDialog = new ProgressDialog(ProfileActivity.this);
                        progressDialog.setMessage("Loading..."); // Setting Message
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                        progressDialog.show(); // Display Progress Dialog
                        progressDialog.setCancelable(false);

                        new communication_editdetails().execute();

                    }
                });
            }
        });

        pro_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyAddressActivity.str_selectactivity = "yes";
                Intent intens = new Intent(ProfileActivity.this,MyAddressActivity.class);
                startActivity(intens);

            }
        });

        pro_Cahngepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intens = new Intent(ProfileActivity.this,ChangePasswordActivity.class);
                startActivity(intens);
            }
        });

        pro_DeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString("login","no");
                editor.putString("address","no");
                JSONArray array_addAddress = new JSONArray();
                editor.putString("array_address",array_addAddress.toString());
                editor.commit();
                Intent intent = new Intent(ProfileActivity.this,LoginActivity.class);
                startActivity(intent);

//                    new AlertDialog.Builder(ProfileActivity.this)
//                            .setTitle("Delete Account")
//                            .setMessage("Are you sure you really want to delete your account?")
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    progressDialog = new ProgressDialog(ProfileActivity.this);
//                progressDialog.setMessage("Deleteing..."); // Setting Message
//                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);                                             progressDialog.show(); // Display Progress Dialog
//                progressDialog.setCancelable(false);
//                progressDialog.show();
//                new Communication_deleteacc().execute();
//
//
//
//
//
//                                }
//                            })
//                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                }
//                            })
//                            .show();




            }
        });
    }
    public class Communication_deleteacc extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.accountdelete);

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

            progressDialog.dismiss();
            if (result !=null)
            {

                if (result.equalsIgnoreCase("deleted"))
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ProfileActivity.this);
                    builder1.setTitle("Deleted!");
                    builder1.setMessage("You account has been deleted sucessfully");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    editor.putString("login","no");
                                    editor.putString("address","no");
                                    JSONArray array_addAddress = new JSONArray();
                                    editor.putString("array_address",array_addAddress.toString());
                                    editor.commit();
                                    Intent intent = new Intent(ProfileActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                }
                            });
                    alert11 = builder1.create();
                    alert11.show();



                }  else if (result.equalsIgnoreCase("deleteerror"))

                {
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(ProfileActivity.this);
                    builder3.setMessage("You Account could mot delete please contact to admin.");
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
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(ProfileActivity.this);
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


                }

                else
                {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(ProfileActivity.this);
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
                AlertDialog.Builder builder2 = new AlertDialog.Builder(ProfileActivity.this);
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


    public class communication_editdetails extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... args0) {

            try {
                URL url = new URL(Urlclass.editdetails);
                JSONObject postDataParams = new JSONObject();


                postDataParams.put("email",pref.getString("userid",""));
                postDataParams.put("name",str_name);
                postDataParams.put("mobile",str_mobilenumber);
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

                    if (result.equalsIgnoreCase("nomatch"))
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(ProfileActivity.this);
                        builder2.setTitle("Oops");
                        builder2.setMessage("The email address you have entered is not registered in our system or your account has been deactivated. Please try again.");
                        builder2.setCancelable(false);
                        builder2.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                    }
                                });
                        alert11 = builder2.create();
                        alert11.show();


                    }
                    else if (result.equalsIgnoreCase("updateerror"))
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(ProfileActivity.this);
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

                        editor.putString("name",str_name);
                        editor.commit();
                        pro_name.setText(str_name);
                        profo_logo.setText(str_name.substring(0,1));
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(ProfileActivity.this);
                        builder2.setTitle("Updated");
                        builder2.setMessage("Your name and mobile number has updated.");
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
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(ProfileActivity.this);
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
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(ProfileActivity.this);
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
                AlertDialog.Builder builder2 = new AlertDialog.Builder(ProfileActivity.this);
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
