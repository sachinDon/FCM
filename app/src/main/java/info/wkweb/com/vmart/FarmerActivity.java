package info.wkweb.com.vmart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;

import androidx.appcompat.app.AppCompatActivity;

public class FarmerActivity extends AppCompatActivity {

    TextView text_f_order,text_f_bill,text_f_his,text_f_profile,text_f_aboutus;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        text_f_order=(TextView)findViewById(R.id.text_f_order);
        text_f_bill=(TextView)findViewById(R.id.text_f_bill);
        text_f_his=(TextView)findViewById(R.id.text_f_his);
       text_f_profile=(TextView)findViewById(R.id.text_f_profile);
       // text_f_aboutus=(TextView)findViewById(R.id.text_f_aboutus);

        text_f_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FarmerActivity.this,FarmerOrderActivity.class);
                startActivity(intent);
            }
        });
        text_f_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(FarmerActivity.this,FarmerBillListActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(FarmerActivity.this,NewPdfFarmerbillActivity.class);
                startActivity(intent);

            }
        });
//        text_f_aboutus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(FarmerActivity.this,AboutUsActivity.class);
//                startActivity(intent);
//            }
//        });
        text_f_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString("login","no");
                editor.putString("address","no");
                JSONArray array_addAddress = new JSONArray();
                if (pref.contains("array_address"))
                {
                    editor.putString("array_address", array_addAddress.toString());
                }
                editor.commit();
                Intent intent = new Intent(FarmerActivity.this,LoginActivity.class);
                startActivity(intent);

//                Intent intent = new Intent(FarmerActivity.this,ProfileActivity.class);
//                startActivity(intent);
            }
        });

        text_f_his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FarmerActivity.this,FarmerItemReportActivity.class);
                startActivity(intent);
            }
        });

    }
    public void onBackPressed() {

    }
}
