package info.wkweb.com.vmart;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
import androidx.appcompat.app.AlertDialog;

public class ChangePasswordActivity extends AppCompatActivity {

    TextView   text_back_chnage,text_change_save;
    EditText edit_change_confirmpass,edit_change_newpass,
            edit_change_oldpass,edit_change_email;
    String str_email,str_newpass,str_oldpass,str_confirm;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    AlertDialog alert11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        text_back_chnage = (TextView)findViewById(R.id.text_back_chnage);
        text_change_save = (TextView)findViewById(R.id.text_change_save);
        edit_change_confirmpass = (EditText)findViewById(R.id.edit_change_confirmpass);
        edit_change_newpass = (EditText)findViewById(R.id.edit_change_newpass);
        edit_change_oldpass = (EditText)findViewById(R.id.edit_change_oldpass);
        edit_change_email = (EditText)findViewById(R.id.edit_change_email);

        text_change_save.setEnabled(false);
        text_change_save.setBackgroundResource(R.drawable.roubdedgray);
        text_change_save.setTextColor(getResources().getColor(R.color.black));

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        str_email = "";
        str_newpass = "";
        str_oldpass = "";
        str_confirm = "";

        text_back_chnage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        text_change_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (str_newpass.equalsIgnoreCase(str_confirm))
                {

                    if (str_email.equalsIgnoreCase(pref.getString("userid","")))
                    {
                        progressDialog = new ProgressDialog(ChangePasswordActivity.this);
                        progressDialog.setMessage("Loading..."); // Setting Message
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                        progressDialog.show(); // Display Progress Dialog
                        progressDialog.setCancelable(false);

                        new communication_changepassword().execute();
                    }
                    else
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(ChangePasswordActivity.this);
                        builder2.setTitle("Oops");
                        builder2.setMessage("The you enterd email address do not match to register email . Please try again.");
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

                    AlertDialog.Builder builder2 = new AlertDialog.Builder(ChangePasswordActivity.this);
                    builder2.setTitle("Oops");
                    builder2.setMessage("The confirm password not match to new password. Please try again.");
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
        });




        edit_change_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                str_email = String.valueOf(edit_change_email.getText());

                if (str_confirm.length() !=0 && str_email.length() !=0 && str_oldpass.length() !=0 && str_newpass.length() !=0)
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
        edit_change_oldpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                str_oldpass = String.valueOf(edit_change_oldpass.getText());
                if (str_confirm.length() !=0 && str_email.length() !=0 && str_oldpass.length() !=0 && str_newpass.length() !=0)
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
        edit_change_newpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                str_newpass = String.valueOf(edit_change_newpass.getText());
                if (str_confirm.length() !=0 && str_email.length() !=0 && str_oldpass.length() !=0 && str_newpass.length() !=0)
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
        edit_change_confirmpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                str_confirm = String.valueOf(edit_change_confirmpass.getText());



                if (str_confirm.length() !=0 && str_email.length() !=0 && str_oldpass.length() !=0 && str_newpass.length() !=0)
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
        text_change_save.setEnabled(false);
        text_change_save.setBackgroundResource(R.drawable.roubdedgray);
        text_change_save.setTextColor(getResources().getColor(R.color.black));


    }

    public  void TextChangetrue()
    {
        text_change_save.setEnabled(true);
        text_change_save.setBackgroundResource(R.drawable.roundedgreen);
        text_change_save.setTextColor(getResources().getColor(R.color.white));
    }

    public class communication_changepassword extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... args0) {

            try {
                URL url = new URL(Urlclass.changepassword);
                JSONObject postDataParams = new JSONObject();


                postDataParams.put("email",pref.getString("userid",""));
                postDataParams.put("oldpassword",str_oldpass);
                postDataParams.put("newpassword",str_newpass);
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
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(ChangePasswordActivity.this);
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
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(ChangePasswordActivity.this);
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
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(ChangePasswordActivity.this);
                        builder2.setTitle("Oops");
                        builder2.setMessage("Your name and mobile number has updated.");
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
                    if (result.equalsIgnoreCase("updateerror"))
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(ChangePasswordActivity.this);
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
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(ChangePasswordActivity.this);
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
                AlertDialog.Builder builder2 = new AlertDialog.Builder(ChangePasswordActivity.this);
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
