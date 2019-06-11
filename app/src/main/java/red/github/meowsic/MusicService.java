package red.github.meowsic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service {
    public MediaPlayer mediaPlayer;
    public boolean tag = false;

    private String sourceURL;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Get the music URL.
        this.sourceURL = intent.getStringExtra("url");

        mediaPlayer = new MediaPlayer();
        System.out.println(this.sourceURL);
        try {
            mediaPlayer.setDataSource(this.sourceURL);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
        } catch (Exception e){

        }

        return super.onStartCommand(intent, flags, startId);
    }

    public MusicService(){

    }

    // Used to connected with activity.
    public MyBinder binder = new MyBinder();
    public class MyBinder extends Binder{
        MusicService getService(){
            return MusicService.this;
        }
    }

    public void playOrPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    public void stop(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(this.sourceURL);
                mediaPlayer.prepare();
                mediaPlayer.seekTo(0);
            } catch (Exception e){

            }
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
