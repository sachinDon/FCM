package info.wkweb.com.vmart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        if (pref.getString("type","").equalsIgnoreCase("farmer"))
        {
            if (pref.getString("login", "").equalsIgnoreCase("yes")) {

                Intent intent = new Intent(MainActivity.this, FarmerActivity.class);
                startActivity(intent);
            } else
                {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }
        else
            {
            if (pref.getString("login", "").equalsIgnoreCase("yes")) {

                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }

    }
//    public void onBackPressed() {
//
//    }
}
