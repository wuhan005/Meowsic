package red.github.meowsic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity {

    private Button searchButton;
    private TextView searchText;

    private JSONArray hotList;

    private Button[] buttons;

    private LinearLayout buttonLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("搜索音乐");
        setContentView(R.layout.activity_search);

        this.searchText = findViewById(R.id.searchText);
        this.buttonLayout = findViewById(R.id.buttonLayout);

        // 搜索按钮
        this.searchButton = findViewById(R.id.searchButton);
        this.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent();
                searchIntent.setClass(SearchActivity.this, SearchListActivity.class);
                searchIntent.putExtra("keyword", searchText.getText().toString());      // 传递搜索词到下个界面
                startActivity(searchIntent);
            }
        });

        // Get hotkey list.
        String hotKeyURL = "https://c.y.qq.com/splcloud/fcgi-bin/gethotkey.fcg?loginUin=0&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(hotKeyURL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] bytes) {
                // Try to convert the JSON data.
                String content = new String(bytes);

                try {
                    JSONObject jsonData = new JSONObject(content);
                    hotList = jsonData.getJSONObject("data").getJSONArray("hotkey");

                    createButton();
                    System.out.println(hotList);
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

    private void createButton(){
        try {

            for(int i = 0; i < this.hotList.length(); i++){
                final String keyword = this.hotList.getJSONObject(i).getString("k");

                Button btn = new Button(this);
                btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                btn.setText(keyword);
                btn.setBackgroundColor(0);
                btn.setTextAppearance(this, R.style.Widget_AppCompat_Button_Borderless_Colored);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent searchIntent = new Intent();
                        searchIntent.setClass(SearchActivity.this, SearchListActivity.class);
                        searchIntent.putExtra("keyword", keyword);
                        startActivity(searchIntent);
                    }
                });

                this.buttonLayout.addView(btn);
            }
        }catch (Exception e){
            System.out.println(e);
        }

    }
}
