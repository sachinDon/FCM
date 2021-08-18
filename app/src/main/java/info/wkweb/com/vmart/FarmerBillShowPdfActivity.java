package info.wkweb.com.vmart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FarmerBillShowPdfActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar progressBar;
    TextView text_back_aboutus;
    public static  String str_pdfpath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_bill_show_pdf);

        webView = (WebView)findViewById(R.id.webView_ps);
        progressBar = (ProgressBar)findViewById(R.id.progressBar1_ps);
        text_back_aboutus=(TextView)findViewById(R.id.text_back_aboutus_ps);

        text_back_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.loadUrl(str_pdfpath);
        webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + str_pdfpath);
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
}