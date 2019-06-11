package red.github.meowsic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String fileName = "play_history.txt";

    private JSONArray historyList;
    private ListView musicList;
    private List<MusicItem> musicItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.musicList = findViewById(R.id.musicList);
    }

    @Override
    protected void onResume() {
        super.onResume();
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

                // Clean list button.
            case R.id.cleanList:
                // Empty the file.
                try {
                    FileOutputStream fos = this.openFileOutput(fileName, MODE_PRIVATE);
                    byte[] bytes = "".getBytes();
                    fos.write(bytes);
                    fos.close();
                }catch (Exception err){

                }

                MusicAdapter adapter = new MusicAdapter(this, R.layout.music_item, new ArrayList<MusicItem>());
                this.musicList.setAdapter(adapter);

            // 关于按钮
            case R.id.about:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getList(){
        this.musicItem = new ArrayList<>();

        try {
            //Get the file first.
            FileInputStream fis = this.openFileInput(fileName);
            int length = fis.available();       // Get the length of the file.
            byte[] buffer = new byte[length];
            fis.read(buffer);
            String content = new String(buffer, "UTF-8");

            historyList = new JSONArray(content);

            this.setList();

        }catch (Exception e){
            System.out.println(e);
        }

    }

    private void setList(){
        try{
            System.out.println(this.historyList);
            for(int i = 0; i < this.historyList.length(); i++){
                JSONObject item = historyList.getJSONObject(this.historyList.length() - 1 - i);
                musicItem.add(new MusicItem(
                        item.getString("musicName"),
                        item.getString("artistName"),
                        item.getString("albumName"),
                        item.getString("albumPicture"),
                        item.getString("mid")
                ));
            }

            MusicAdapter adapter = new MusicAdapter(this, R.layout.music_item, musicItem);
            this.musicList.setAdapter(adapter);

        }catch (Exception e){
            System.out.println("Error!!!");
        }

        musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Redirect to the music player page.
                Intent playerIntent = new Intent();
                playerIntent.setClass(MainActivity.this, MusicPlayer.class);

                playerIntent.putExtra("mid", musicItem.get((int) id).getMid());      // Send the music mid.
                playerIntent.putExtra("musicName", musicItem.get((int) id).getMusicName());
                playerIntent.putExtra("albumName", musicItem.get((int) id).getAlbumName());
                playerIntent.putExtra("artistName", musicItem.get((int) id).getMusicArtist());
                playerIntent.putExtra("albumPicture", musicItem.get((int) id).getAlbumPicture());

                // Save history

                JSONObject historyObject = new JSONObject();
                try {
                    historyObject.put("mid", musicItem.get((int) id).getMid());
                    historyObject.put("musicName", musicItem.get((int) id).getMusicName());
                    historyObject.put("albumName", musicItem.get((int) id).getAlbumName());
                    historyObject.put("artistName", musicItem.get((int) id).getMusicArtist());
                    historyObject.put("albumPicture", musicItem.get((int) id).getAlbumPicture());
                }catch (Exception e){
                    System.out.println(e);
                }

                startActivity(playerIntent);
            }
        });

    }
}
