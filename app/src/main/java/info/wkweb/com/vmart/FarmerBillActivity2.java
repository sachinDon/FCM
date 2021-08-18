package info.wkweb.com.vmart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import java.net.URLEncoder;
import java.util.Iterator;

public class FarmerBillActivity2 extends AppCompatActivity {

    public static JSONArray Array_bill;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    LayoutInflater inflater1;
    LinearLayout ll;
    Integer int_tag_val;
    TextView text_invoice_farmerbill,text_back,text_subtotal_farmer,text_name_farmerbill,text_date_farmerbill,text_address_farmerbill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_bill2);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        RelativeLayout rl = (RelativeLayout)findViewById(R.id.relative_dyanamicrow_cu);
        ScrollView sv = (ScrollView)findViewById(R.id.scrollView_farmerbill);
        ll = (LinearLayout)findViewById(R.id.linear_dyanamicrow_farmerbill);
        text_back = (TextView)findViewById(R.id.text_back_farmer);
        text_name_farmerbill = (TextView)findViewById(R.id.text_name_farmerbill);
        text_invoice_farmerbill= (TextView)findViewById(R.id.text_invoice_farmerbill);
        text_date_farmerbill= (TextView)findViewById(R.id.text_date_farmerbill);
        text_address_farmerbill= (TextView)findViewById(R.id.text_address_farmerbill);

        text_subtotal_farmer= (TextView)findViewById(R.id.text_subtotal_farmer);
        LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(getApplication().LAYOUT_INFLATER_SERVICE);
        inflater1 = inflater;



        text_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        finish();
                    }
                });
        addRow();
    }

    public  void  addRow()
    {

        Integer totals = 0;
        ll.removeAllViews();
        for (int i = 0; i < Array_bill.length(); i++)
        {
            final View view1 = inflater1.inflate(R.layout.list_farmerbillrow, null);





            TextView text_itemname = (TextView) view1.findViewById(R.id.text_items_name1);
            TextView text_rate = (TextView) view1.findViewById(R.id.text_rate1);
            TextView text_qty = (TextView) view1.findViewById(R.id.text_qty1);
            TextView text_total = (TextView) view1.findViewById(R.id.text_totalfarmer1);


            text_itemname.setTag(i);
            text_rate.setTag(i);
            text_qty.setTag(i);
            text_total.setTag(i);

            try {
                JSONObject obj_val = new JSONObject(String.valueOf(Array_bill.getJSONObject(i)));

                text_itemname.setText(obj_val.getString("pname"));
                text_rate.setText("₹"+obj_val.getString("rate"));
                text_qty.setText(obj_val.getString("qty"));
                text_total.setText("₹"+obj_val.getString("totamt"));
                text_subtotal_farmer.setText("Net Total: "+"₹"+obj_val.getString("netamt"));

                text_name_farmerbill.setText("Invoice No: "+obj_val.getString("invoiceno"));
                text_invoice_farmerbill.setText("Orderid :"+obj_val.getString("orderid"));
                text_date_farmerbill.setText("Date: "+obj_val.getString("date"));
                //text_address_farmerbill.setText("Address: "+obj_val.getString("shipaddress"));
                text_address_farmerbill.setVisibility(View.GONE);
             //   totals +=Integer.parseInt(obj_val.getString("subtotal"));

            } catch (JSONException e)
            {
                e.printStackTrace();
            }


            ll.addView(view1);
        }
    }
}