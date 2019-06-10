package red.github.meowsic;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MusicAdapter extends ArrayAdapter<MusicItem> {
    private int resourceId;

    private String albumURLPrefix = "http://y.gtimg.cn/music/photo_new/T002R800x800M000";

    public MusicAdapter(Context context, int textViewResourceId, List<MusicItem> objects){
        super(context, textViewResourceId, objects);

        this.resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        MusicItem musicItem = getItem(position);           //获取当前项的实例
        View view= LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        MyImageView albumImage = view.findViewById(R.id.albumPicture);
        albumImage.setImageURL(this.albumURLPrefix + musicItem.getAlbumPicture() + ".jpg");

        TextView musicName = view.findViewById(R.id.musicName);
        musicName.setText(musicItem.getMusicName());

        TextView artistName = view.findViewById(R.id.musicArtist);
        artistName.setText(musicItem.getMusicArtist());

        TextView albumName = view.findViewById(R.id.albumName);
        albumName.setText(musicItem.getAlbumName());

        return view;
    }
}
