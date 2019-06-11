package red.github.meowsic;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchListActivity extends AppCompatActivity {

    private String keyword;
    private ListView searchListView;

    private int nowPage = 1;
    private int numPerPage = 30;

    private JSONObject jsonData;

    private List<MusicItem> musicItem = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        this.searchListView = findViewById(R.id.searchList);

        // 获取搜索关键字
        Intent searchIntent =  getIntent();
        this.keyword = searchIntent.getStringExtra("keyword");
        this.setTitle(this.keyword + " - 搜索结果");

        this.searchKeyWord();
    }

    private void searchKeyWord(){
        AsyncHttpClient client = new AsyncHttpClient();

        String url = "http://c.y.qq.com/soso/fcgi-bin/search_cp?ct=24&aggr=1&lossless=1&cr=1&format=json&n=" + this.numPerPage + "&p=" + this.nowPage + "&w=" + this.keyword;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] bytes) {
                // Try to convert the JSON data.
                String content = new String(bytes);
                System.out.println(content);

                try {
                    jsonData = new JSONObject(content);
                    jsonData = jsonData.getJSONObject("data").getJSONObject("song");
                    setUI();

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
        // Set the data.
        try{
            JSONArray musicList = this.jsonData.getJSONArray("list");

            for(int i = 0; i < this.numPerPage; i++){
                JSONObject item = musicList.getJSONObject(i);
                musicItem.add(new MusicItem(
                        item.getString("songname"),
                        item.getJSONArray("singer").getJSONObject(0).getString("name"),
                        item.getString("albumname"),
                        item.getString("albummid"),
                        item.getString("songmid")
                ));
            }

            MusicAdapter adapter = new MusicAdapter(this, R.layout.music_item, musicItem);
            this.searchListView.setAdapter(adapter);

        }catch (Exception e){


        }

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Redirect to the music player page.
                Intent playerIntent = new Intent();
                playerIntent.setClass(SearchListActivity.this, MusicPlayer.class);

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

                savePlayHistory(historyObject);

                System.out.println(musicItem.get((int) id).getMid());

                startActivity(playerIntent);
            }
        });

    }

    private void savePlayHistory(JSONObject data){
        String fileName = "play_history.txt";
        String originContent = "";
        JSONArray jsonData;

        try {
            //Get the file first.
            FileInputStream fis = this.openFileInput(fileName);
            int length = fis.available();       // Get the length of the file.
            byte[] buffer = new byte[length];
            fis.read(buffer);
            originContent = new String(buffer, "UTF-8");

            jsonData = new JSONArray(originContent);


        }catch (Exception e){

            // Clean the file.

            try {
                FileOutputStream fos = this.openFileOutput(fileName, MODE_PRIVATE);
                byte[] bytes = "".getBytes();
                fos.write(bytes);
                fos.close();
            }catch (Exception err){

            }

            jsonData = new JSONArray();
        }

        try {
            jsonData.put(data);
            // Update file.

            try {
                FileOutputStream fos = this.openFileOutput(fileName, MODE_PRIVATE);
                byte[] bytes = jsonData.toString().getBytes();
                fos.write(bytes);
                fos.close();
            }catch (Exception err){

            }

        } catch (Exception e) {

        }
    }

}
