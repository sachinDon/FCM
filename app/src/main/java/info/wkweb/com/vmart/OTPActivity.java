package info.wkweb.com.vmart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static android.content.ContentValues.TAG;

public class OTPActivity extends AppCompatActivity {


    public static String Strmobileno;
    public static String Stremail;
    Button submit;
    EditText phnTxt;
    TextView t1,contrty;
    String mVerificationId;
    ProgressDialog progressDialog;

    String f;
   public static String Strmobileno1;
    //ProgressBar Variable

    public SharedPreferences pref;
    SharedPreferences.Editor editor;
   AlertDialog alertDialog_Box;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private boolean mVerificationInProgress = false;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();

    public String getCountryDialCode() {
        String contryId = null;
        String contryDialCode = null;

        TelephonyManager telephonyMngr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        contryId = telephonyMngr.getSimCountryIso().toUpperCase();
        //String locale = getApplicationContext().getResources().getConfiguration().locale.getCountry();
        String[] arrContryCode = this.getResources().getStringArray(R.array.DialingCountryCode);
        for (int i = 0; i < arrContryCode.length; i++) {
            String[] arrDial = arrContryCode[i].split(",");
            if (arrDial[1].trim().equals(contryId.trim())) {
                contryDialCode = arrDial[0];
                break;
            }
        }
        return contryDialCode;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);


        phnTxt = (EditText) findViewById(R.id.phn_editText);
        contrty = (TextView) findViewById(R.id.phn_editText1);

        submit = (Button) findViewById(R.id.submit_btn);

        t1 = (TextView) findViewById(R.id.text1);


        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        f = getCountryDialCode();

        phnTxt.setText(Strmobileno);
        Strmobileno1 = Strmobileno;

        Handler refresh = new Handler(Looper.getMainLooper());
        refresh.post(new Runnable() {
            public void run() {
                if (f ==null)
                {
                    f="+";
                }
                else   if (f.length()==0)
                {
                    f="+";
                }
                contrty.setText(f);
            }
        });





submit.setEnabled(false);
        mAuth = FirebaseAuth.getInstance();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        phnTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_white_border));
                submit.setEnabled(true);
            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                Log.d("aa", "onVerificationCompleted:" + credential);

              signInWithPhoneAuthCredential(credential);
            }


            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("cc", "onVerificationFailed", e);
                progressDialog.dismiss();
                AlertDialog.Builder alert1 = new AlertDialog.Builder(OTPActivity.this);
                alert1.setTitle("Oops");
                alert1.setMessage("Could not send you a verification code. Please check your mobile no. and try again.");

                alert1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alert1.create();
                alertDialog.show();
              //  Toast.makeText(OTPActivity.this, "verification fail", Toast.LENGTH_LONG).show();


                if (e instanceof FirebaseAuthInvalidCredentialsException) {
// Toast.makeText(OTPActivity.this, "invalid no.", Toast.LENGTH_LONG).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    progressDialog.dismiss();
                    AlertDialog.Builder alert = new AlertDialog.Builder(OTPActivity.this);
                    alert.setTitle("");
                    alert.setMessage("Quata over");

                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog1 = alert.create();
                    alertDialog1.show();

                   // Toast.makeText(OTPActivity.this, "Quata over", Toast.LENGTH_LONG).show();
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;
                progressDialog.dismiss();
                Intent i=new Intent(OTPActivity.this,OtpVerifyActivity.class);
                i.putExtra("verificationid",mVerificationId.toString());
                i.putExtra("mobileno",Strmobileno);
                startActivity(i);


            }
        };

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String a=plustxt.toString();
                progressDialog = new ProgressDialog(OTPActivity.this);
                progressDialog.setMessage("Sending..."); // Setting Message
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);
                hideKeyboard(OTPActivity.this);
                String b = (f + phnTxt.getText().toString());
                Log.d("dfdf", b);
                PhoneAuthProvider.getInstance().verifyPhoneNumber(contrty.getText().toString() + phnTxt.getText().toString(),   // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        OTPActivity.this,               // Activity (for callback binding)
                        mCallbacks);// OnVerificationStateChangedCallbacks
                Strmobileno = contrty.getText().toString() + phnTxt.getText().toString();
                Log.d("Strmobileno1111", Strmobileno);



            }
        });


        phnTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mobilrnoString1 = phnTxt.getText().toString();
                if (mobilrnoString1.length() < 1) {
                    submit.setEnabled(false);
                    submit.setBackgroundDrawable(getResources().getDrawable(R.drawable.blackborderbtn));
                } else {
                    submit.setEnabled(true);
                    submit.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_white_border));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });



    }
    public void hideKeyboard(OTPActivity mainActivity) {
        // Check if no view has focus:
        View view = OTPActivity.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) OTPActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            progressDialog.dismiss();
                            new SendPostRequest().execute();
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            //Toast.makeText(OTPActivity.this, "Verification done", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {


            try {

                URL url = new URL(Urlclass.verifymobile);
                JSONObject postDataParams = new JSONObject();


                postDataParams.put("email",Stremail);
                postDataParams.put("mobile",Strmobileno1);


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(60000 /* milliseconds */);
                conn.setConnectTimeout(60000 /* milliseconds */);
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

            progressDialog.dismiss();

            JSONArray jsonarray=null;
            JSONObject Jsonobject=null;
            try {





                if (result.equalsIgnoreCase("nullerror"))
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(OTPActivity.this);
                    builder1.setTitle("Oops");
                    builder1.setMessage("Your Facebook Account Id seems to be absent. Please login and try again.");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alertDialog_Box = builder1.create();
                    alertDialog_Box.show();




                }
                else if (result.equalsIgnoreCase("updateerror"))
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(OTPActivity.this);
                    builder1.setTitle("Oops");
                    builder1.setMessage("Server encountered an error in verifying your mobile number. Please try again later.");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alertDialog_Box = builder1.create();
                    alertDialog_Box.show();


                }
                else if (result.equalsIgnoreCase("alreadyregistered"))
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(OTPActivity.this);
                    builder1.setTitle("Already registered");
                    builder1.setMessage("Sorry your mobile number has already been registered by some other user. Please try using another mobile number.");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alertDialog_Box = builder1.create();
                    alertDialog_Box.show();


                }
                else
                {
                    jsonarray = new JSONArray(result);
                    Jsonobject = jsonarray.getJSONObject(0);

                    if (jsonarray !=null) {

                        String str_register = Jsonobject.getString("register");
                        if (str_register.equalsIgnoreCase("yes"))
                        {
                            editor.putString("userid", String.valueOf( Jsonobject.getString("email")));
                            editor.commit();
                            Intent intent = new Intent(OTPActivity.this,MainActivity.class);
                            startActivity(intent);

                        }
                        else
                        {
                            Intent intent = new Intent(OTPActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                    else
                    {
                       AlertDialog.Builder builder1 = new AlertDialog.Builder(OTPActivity.this);
                        builder1.setTitle("Oops");
                        builder1.setMessage("Server encountered an error in verifying your mobile number. Please try again later.");
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        // finish();
                                    }
                                });
                        alertDialog_Box = builder1.create();
                        alertDialog_Box.show();
                    }





                }
//                else
//                {
//                    android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(OtpVerifyActivity.this);
//                    builder1.setTitle("Server timeout");
//                    builder1.setMessage("Server timeout");
//                    builder1.setCancelable(false);
//                    builder1.setPositiveButton("Ok",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                    // finish();
//                                }
//                            });
//                    alertDialog_Box = builder1.create();
//                    alertDialog_Box.show();
//                }


            } catch (JSONException e) {
                e.printStackTrace();
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

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }

}










