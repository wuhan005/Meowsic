package red.github.meowsic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class MusicAdapter extends ArrayAdapter<MusicItem> {
    private int resourceId;

    public MusicAdapter(Context context, int textViewResourceId, List<MusicItem> objects){
        super(context, textViewResourceId, objects);

        this.resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        MusicItem musicItem = getItem(position);           //获取当前项的实例
        View view= LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
//        ImageView fruitImage = (ImageView)view.findViewById(R.id.fruit_image);
//        TextView fruitName=(TextView) view.findViewById(R.id.fruit_name);
//        fruitImage.setImageResource(fruit.getImageId());
//        fruitName.setText(fruit.getName());
        return view;
    }
}
