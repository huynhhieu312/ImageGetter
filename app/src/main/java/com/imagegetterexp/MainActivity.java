package com.imagegetterexp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.imagegetterexp.Components.URLImageParser;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

public class MainActivity extends AppCompatActivity {
    TextView a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        a = (TextView) findViewById(R.id.chebien_content);
      new  getHTML(MainActivity.this).execute();
    }



    public class getHTML extends AsyncTask<Object, Object, String> {

        ProgressDialog pDialog;
        Context mContext;
        String NoiDung ;
        public final String URLAPI = "http://unibeautiful.com.vn/API/content.php";
        public getHTML(Context ctx) {
            this.mContext = ctx;

        }
        @Override
        protected String doInBackground(Object... params) {
            try {
                NoiDung =  getPostsOnSever();

              //  loadText(a,NoiDung);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return NoiDung;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }



    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d("noidung",result);
        //a.setText(result);
        loadText(a,result);
        pDialog.dismiss();
    }
        public String getPostsOnSever() throws IOException {
            String url = URLAPI;
            String responseString="abc";
            try {
                Log.d("url",url);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(new HttpGet(url));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    responseString = out.toString();
                    Log.d(responseString,"jsonresponsive");
                    return  responseString;
                }
                else {
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());

                }
            } catch (Exception e) {
                e.printStackTrace();}
            return responseString;

        }

        public void loadText(TextView textView, String source) {
            if (source == null)
                return;
            URLImageParser urlParserChebien = new URLImageParser(textView,this.mContext);
            if (Build.VERSION.SDK_INT >= 24) {
                textView.setText(Html.fromHtml(source,
                        FROM_HTML_MODE_LEGACY, urlParserChebien, null));
            } else {
                textView.setText(Html.fromHtml(source, urlParserChebien, null));
            }
        }
    }


}
