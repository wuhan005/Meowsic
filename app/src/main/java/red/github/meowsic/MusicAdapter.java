package red.github.meowsic;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.List;

public class MusicAdapter extends ArrayAdapter<MusicItem> {
    private int resourceId;

    private String albumURLPrefix = "http://y.gtimg.cn/music/photo_new/T002R800x800M000";

    private DisplayImageOptions options;

    public MusicAdapter(Context context, int textViewResourceId, List<MusicItem> objects){
        super(context, textViewResourceId, objects);
        this.initImageLoader(context);

        this.options = new DisplayImageOptions.Builder()
                // .showStubImage(R.mipmap.ic_launcher)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)  // 设置图片Uri为空或是错误的时候显示的图片
                //.showImageOnFail(R.mipmap.ic_launcher)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                     // 设置下载的图片是否缓存在SD卡中
                .build();                               // 创建配置过得DisplayImageOption对象

        this.resourceId = textViewResourceId;
    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPoolSize(3);
        config.memoryCache(new WeakMemoryCache());
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        ImageLoader.getInstance().init(config.build());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        MusicItem musicItem = getItem(position);           //获取当前项的实例
        View view= LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        ImageView albumImage = view.findViewById(R.id.albumPicture);
//        albumImage.setImageURL();
        ImageLoader.getInstance().displayImage(this.albumURLPrefix + musicItem.getAlbumPicture() + ".jpg", albumImage,options);

        TextView musicName = view.findViewById(R.id.musicName);
        musicName.setText(musicItem.getMusicName());

        TextView artistName = view.findViewById(R.id.musicArtist);
        artistName.setText(musicItem.getMusicArtist());

        TextView albumName = view.findViewById(R.id.albumName);
        albumName.setText(musicItem.getAlbumName());

        return view;
    }
}
