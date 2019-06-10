package red.github.meowsic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {

    private Button searchButton;
    private TextView searchText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("搜索音乐");
        setContentView(R.layout.activity_search);

        this.searchText = findViewById(R.id.searchText);

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
    }
}
