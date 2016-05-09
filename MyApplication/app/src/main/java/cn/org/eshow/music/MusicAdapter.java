package cn.org.eshow.music;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import cn.org.eshow.R;
import cn.org.eshow.bean.Song;

public class MusicAdapter extends BaseAdapter {
	private List<Song> data;
	private Context mContext;
	public MusicAdapter(Context mContext, List<Song> data) {
		this.mContext = mContext;
		this.data = data;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_musiclist, null);
			viewHolder = new ViewHolder();
			viewHolder.tvMusic = (TextView) convertView.findViewById(R.id.tvMusic);
			viewHolder.tvMusicsinger = (TextView) convertView.findViewById(R.id.tvMusicsinger);
			viewHolder.tvSize = (TextView) convertView.findViewById(R.id.tvSize);
			viewHolder.ivMusicDowned = (ImageView) convertView.findViewById(R.id.ivMusicDowned);
			viewHolder.ivIsplaying = (ImageView) convertView.findViewById(R.id.ivIsplaying);
			viewHolder.ivSingerPhoto = (ImageView) convertView.findViewById(R.id.ivSingerPhoto);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final Song song=data.get(position);
		if (song!=null){
			viewHolder.tvMusic.setText(song.getName());
			viewHolder.tvMusicsinger.setText(song.getArtist().getName());
			if (song.isDownFinish()){
				Log.i("song.getSize()", MusicDownActivity.formatParameters(song.getSize()));
				viewHolder.ivMusicDowned.setVisibility(View.VISIBLE);
				viewHolder.tvSize.setText(MusicDownActivity.formatParameters(song.getSize()));
				viewHolder.tvSize.setVisibility(View.VISIBLE);
			}
		} else {
			Log.i("BT-MusicAdapter", "DATE=null");
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
		TextView  tvMusic;
		TextView tvMusicsinger;
		public TextView tvSize;
		ImageView ivSingerPhoto;
		public ImageView ivMusicDowned;
		public static ImageView ivIsplaying;
	}
}
