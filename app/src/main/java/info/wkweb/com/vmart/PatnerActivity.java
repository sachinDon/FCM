package info.wkweb.com.vmart;


import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PatnerActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar progressBar;
    TextView text_back_patner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patner);

        webView = (WebView)findViewById(R.id.webView_patnar);
        progressBar = (ProgressBar)findViewById(R.id.progressBar1_patnar);
        text_back_patner = (TextView)findViewById(R.id.text_back_patner);

        text_back_patner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(Urlclass.aboutus);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);

                } else {
                    progressBar.setVisibility(View.VISIBLE);

                }
            }
        });
    }

//    public void onBackPressed() {
//
//    }
}
