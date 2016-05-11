package cn.org.eshow.demo.music;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.org.eshow.demo.R;
import cn.org.eshow.demo.bean.Song;

public class MusicDownedAdapter extends BaseAdapter {
    private List<Song> data;
    private Context mContext;

    public MusicDownedAdapter(Context mContext, List<Song> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_musicdowned, null);
            viewHolder = new ViewHolder();
            viewHolder.tvMusicName = (TextView) convertView.findViewById(R.id.tvMusicName);
            viewHolder.tvMusicSize = (TextView) convertView.findViewById(R.id.tvMusicSize);
            viewHolder.tvMusicSinger = (TextView) convertView.findViewById(R.id.tvMusicSinger);
            viewHolder.llPlaying = (LinearLayout) convertView.findViewById(R.id.llPlaying);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Song song = data.get(position);
        Log.i("MusicDownedAdapter", "song.name:" + song.getName() + "  singer:" + song.getArtist().getName()
                + "  filePath:" + song.getFilePath() + "  size:" + MusicDownActivity.formatParameters(song.getSize()));
        if (song != null) {
            viewHolder.tvMusicName.setText(song.getName());
            viewHolder.tvMusicSinger.setText(song.getArtist().getName());
            viewHolder.tvMusicSize.setText(MusicDownActivity.formatParameters(song.getSize()));
        } else {
            Log.i("BT-MusicDowningAdapter", "song=null");
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    public static class ViewHolder {
        public TextView tvMusicName, tvMusicSize, tvMusicSinger;
        public LinearLayout llPlaying;
    }
}
