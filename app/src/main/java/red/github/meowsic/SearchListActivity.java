package red.github.meowsic;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SyncFailedException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;

import javax.sql.StatementEvent;

public class SearchListActivity extends AppCompatActivity {

    private String keyword;

    private int nowPage = 1;
    private int numPerPage = 10;

    private JSONObject jsonData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        // 获取搜索关键字
        Intent searchIntent =  getIntent();
        this.keyword = searchIntent.getStringExtra("keyword");
        this.setTitle(this.keyword + " - 搜索结果");

        this.searchKeyWord();
    }

    private void searchKeyWord(){
        final HttpURLConnection conn = null;

        AsyncHttpClient client = new AsyncHttpClient();

        String url = "http://c.y.qq.com/soso/fcgi-bin/search_cp?aggr=1&lossless=1&cr=1&format=json&n=" + this.numPerPage + "&p=" + this.nowPage + "&w=" + this.keyword;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] bytes) {
                // Try to convert the JSON data.
                String content = new String(bytes);

                try {
                    jsonData = new JSONObject(content);
                    setUI();
                    System.out.println(jsonData.getInt("code"));
                    System.out.println(jsonData.getJSONObject("data").getJSONObject("song").getInt("curnum"));
                }catch (Exception e){
                    System.out.println(e);
                }

            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {
                System.out.println("Error");
            }
        });
    }

    private void setUI(){
        
    }

}
