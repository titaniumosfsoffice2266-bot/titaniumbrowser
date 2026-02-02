package com.titanium.browser;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.dnsoverhttps.DnsOverHttps;
import java.net.InetAddress;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private EditText urlInput;
    private Button btnGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        urlInput = findViewById(R.id.url_input);
        btnGo = findViewById(R.id.btn_go);

        setupBrowserSettings();
        setupCustomDNS();

        btnGo.setOnClickListener(v -> {
            String url = urlInput.getText().toString().trim();
            if (!url.isEmpty()) {
                if (!url.startsWith("http")) {
                    url = "https://" + url;
                }
                webView.loadUrl(url);
            }
        });
        
        webView.loadUrl("https://www.google.com");
    }

    private void setupBrowserSettings() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        webView.setWebViewClient(new WebViewClient());
    }

    private void setupCustomDNS() {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                .dns(new DnsOverHttps.Builder()
                    .client(new OkHttpClient())
                    .url(HttpUrl.get("https://cloudflare-dns.com/dns-query"))
                    .bootstrapDnsHosts(Collections.singletonList(InetAddress.getByName("1.1.1.1")))
                    .build())
                .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}