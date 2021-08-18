package info.wkweb.com.vmart;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

import androidx.appcompat.app.AppCompatActivity;

public class StockAvailabelActivity extends AppCompatActivity {

    TextView text_back_stock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_availabel);

        text_back_stock= (TextView)findViewById(R.id.text_back_stock);


        text_back_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
}
