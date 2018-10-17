package jp.fluct.adidsample.adidsampleapp_java;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    public static String adid = "";
    public static String limitAdTracking = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = findViewById(R.id.bannerWebView);
        ShowAdAsyncTask task = new ShowAdAsyncTask(this.getApplicationContext(), webView);
        task.execute(getApplicationContext());
    }

    static class ShowAdAsyncTask extends AsyncTask<Context, Void, AdvertisingIdClient.Info> {
        private final WeakReference<Context> mContext;
        private final WeakReference<WebView> mWebView;

        ShowAdAsyncTask(Context context, WebView webView) {
            mContext = new WeakReference<>(context);
            mWebView = new WeakReference<>(webView);
        }

        @Override
        protected AdvertisingIdClient.Info doInBackground(Context... contexts) {
            AdvertisingIdClient.Info adInfo = null;
            Context context = mContext.get();
            try {
                adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
            } catch (GooglePlayServicesNotAvailableException |
                    GooglePlayServicesRepairableException | IOException e) {
                e.printStackTrace();
            }

            return adInfo;
        }

        @Override
        protected void onPostExecute(AdvertisingIdClient.Info info) {
            super.onPostExecute(info);

            if (info != null) {
                adid = info.getId();
                limitAdTracking = info.isLimitAdTrackingEnabled()? "1" : "0";
            }

            showAd();
        }

        @SuppressLint("JavascriptInterface")
        private void showAd() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true); // Debug用
            }
            WebView webview = mWebView.get();
            if (webview == null) {
                return;
            }

            webview.getSettings().setDomStorageEnabled(true);
            webview.getSettings().setJavaScriptEnabled(true);
            // require API level over 17
            webview.addJavascriptInterface(this, "Android");
            webview.loadUrl("file:///android_asset/base.html");
        }

        @JavascriptInterface
        public String getAdId() {
            return adid;
        }

        @JavascriptInterface
        public String getLimitAdTracking() {
            return limitAdTracking;
        }

        @JavascriptInterface
        public  String getPackage() {
            Context context = mContext.get();
            if (context != null) {
                return context.getPackageName();
            }
            return "";
        }
    }
}
