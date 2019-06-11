package red.github.meowsic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.FileInputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getList();
    }

    // 顶部右侧菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.header, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // 选择菜单选项
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // 搜索按钮
            case R.id.search:
                Intent searchIntent = new Intent();
                searchIntent.setClass(this, SearchActivity.class);
                startActivity(searchIntent);
                break;
            // 关于按钮
            case R.id.about:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getList(){
        String fileName = "play_history.txt";

        try {
            //Get the file first.
            FileInputStream fis = this.openFileInput(fileName);
            int length = fis.available();       // Get the length of the file.
            byte[] buffer = new byte[length];
            fis.read(buffer);
            System.out.println("Text:::");
            String content = new String(buffer, "UTF-8");
            System.out.println(content);

        }catch (Exception e){
            System.out.println("Out put:");
            System.out.println(e);
        }
    }
}
