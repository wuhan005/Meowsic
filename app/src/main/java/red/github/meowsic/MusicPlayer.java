package red.github.meowsic;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class MusicPlayer extends AppCompatActivity {

    private String uuid = "233";
    private String mid;
    private String albumURLPrefix = "http://y.gtimg.cn/music/photo_new/T002R800x800M000";
    private String musicURLPrefix = "http://dl.stream.qqmusic.qq.com/M500";

    private boolean tag2 = false;

    private MusicService musicService;

    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");

    private Button btnPlayOrPause;
    private SeekBar seekBar;
    private TextView musicTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        this.loadInfo();
    }

    // Get the music info.
    private void loadInfo(){
        // Refresh UI
        Intent searchIntent =  getIntent();
        this.mid = searchIntent.getStringExtra("mid");
        this.setUI();

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

                    // Start playing.
                    String musicURL = musicURLPrefix + mid + ".mp3?vkey=" + vkey + "&guid=" + uuid + "&fromtag=1";
                    bindServiceConnection(musicURL);
                    setMusicListener();

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

    // Connect the service with activity.
    private void bindServiceConnection(String musicURL) {
        Intent intent = new Intent(MusicPlayer.this, MusicService.class);
        // Put the music URL.
        intent.putExtra("url", musicURL);

        startService(intent);
        Boolean a = bindService(intent, this.serviceConnection, BIND_AUTO_CREATE);
        System.out.println(a);
        System.out.println("bind service");
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((MusicService.MyBinder) (service)).getService();
            btnPlayOrPause.setEnabled(true);
            System.out.println("Service start.");
//            musicTotal.setText(time.format(musicService.mediaPlayer.getDuration()));
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };

    private void setMusicListener() {
        btnPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicService.mediaPlayer != null) {
                    seekBar.setProgress(musicService.mediaPlayer.getCurrentPosition());
                    seekBar.setMax(musicService.mediaPlayer.getDuration());
                }
                //  由tag的变换来控制事件的调用
                if (musicService.tag != true) {
                    btnPlayOrPause.setText("暂停");
                    //musicStatus.setText("Playing");
                    musicService.playOrPause();
                    musicService.tag = true;

                } else {
                    btnPlayOrPause.setText("播放");
                    //musicStatus.setText("Paused");
                    musicService.playOrPause();
                    musicService.tag = false;
                }
                if (tag2 == false) {
                    handler.post(runnable);
                    tag2 = true;
                }
            }
        });

//        btnStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                musicStatus.setText("Stopped");
//                btnPlayOrPause.setText("PLAY");
//                musicService.stop();
//                animator.pause();
//                musicService.tag = false;
//            }
//        });

        //  停止服务时，必须解除绑定，写入btnQuit按钮中
//        btnQuit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handler.removeCallbacks(runnable);
//                unbindService(serviceConnection);
//                Intent intent = new Intent(MainActivity.this, MusicService.class);
//                stopService(intent);
//                try {
//                    MainActivity.this.finish();
//                } catch (Exception e) {
//
//                }
//            }
//        });

    }

    // Used to refresh the status of the UI.
    public Handler handler = new Handler();
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            musicTime.setText(time.format(musicService.mediaPlayer.getCurrentPosition()));
            seekBar.setProgress(musicService.mediaPlayer.getCurrentPosition());
            seekBar.setMax(musicService.mediaPlayer.getDuration());
            //musicTotal.setText(time.format(musicService.mediaPlayer.getDuration()));
            handler.postDelayed(runnable, 200);

        }
    };

    private void setUI(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                // .showStubImage(R.mipmap.ic_launcher)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)  // 设置图片Uri为空或是错误的时候显示的图片
                //.showImageOnFail(R.mipmap.ic_launcher)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                     // 设置下载的图片是否缓存在SD卡中
                .build();                               // 创建配置过得DisplayImageOption对象

        Intent searchIntent =  getIntent();

        final TextView musicName = findViewById(R.id.musicName);
        musicName.setText(searchIntent.getStringExtra("musicName"));

        TextView albumName = findViewById(R.id.albumName);
        albumName.setText(searchIntent.getStringExtra("albumName"));

        TextView artistName = findViewById(R.id.artistName);
        artistName.setText(searchIntent.getStringExtra("artistName"));

        ImageView albumImage = findViewById(R.id.albumPicture);
        ImageLoader.getInstance().displayImage(this.albumURLPrefix + searchIntent.getStringExtra("albumPicture") + ".jpg", albumImage, options);

        // Music player UI.
        this.btnPlayOrPause = findViewById(R.id.playOrPause);
        btnPlayOrPause.setEnabled(false);
        this.seekBar = findViewById(R.id.seekBar);
        this.musicTime = findViewById(R.id.musicTime);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true) {
                    musicService.mediaPlayer.seekTo(progress);
                    musicService.mediaPlayer.start();   // Start playing after control.
                    btnPlayOrPause.setText("暂停");
                    musicService.tag = true;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop the music after exit.
        musicService.stop();
        handler.removeCallbacks(runnable);
        unbindService(serviceConnection);
        Intent intent = new Intent(this, MusicService.class);
        stopService(intent);
    }
}
