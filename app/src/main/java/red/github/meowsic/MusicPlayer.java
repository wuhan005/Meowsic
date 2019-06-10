package red.github.meowsic;

import android.content.Intent;
import android.net.http.Headers;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

public class MusicPlayer extends AppCompatActivity {

    private String uuid = "233";
    private String mid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        this.loadInfo();
    }

    private void loadInfo(){
        Intent searchIntent =  getIntent();
        this.mid = searchIntent.getStringExtra("mid");

        String url = "https://u.y.qq.com/cgi-bin/musicu.fcg?g_tk=5381&loginUin=0&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0&data={\"req\":{\"module\":\"CDN.SrfCdnDispatchServer\",\"method\":\"GetCdnDispatch\",\"param\":{\"guid\":\"%s\",\"calltype\":0,\"userip\":\"\"}},\"req_0\":{\"module\":\"vkey.GetVkeyServer\",\"method\":\"CgiGetVkey\",\"param\":{\"guid\":\"%s\",\"songmid\":[\"%s\"],\"songtype\":[0],\"uin\":\"0\",\"loginflag\":1,\"platform\":\"20\"}},\"comm\":{\"uin\":0,\"format\":\"json\",\"ct\":20,\"cv\":0}}\n";
        url = String.format(url, this.uuid, this.uuid, this.mid);

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] bytes) {
                // Try to convert the JSON data.
                String content = new String(bytes);

                // Get music vkey.
                try {
                    JSONObject jsonData = new JSONObject(content);
                    String vkey = jsonData.getJSONObject("req").getJSONObject("data").getString("vkey");

                    System.out.println(vkey);

                }catch (Exception e){
                    System.out.println(e);
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {
                System.out.println("Error");
            }
        });

        System.out.println(this.mid);
    }
}
