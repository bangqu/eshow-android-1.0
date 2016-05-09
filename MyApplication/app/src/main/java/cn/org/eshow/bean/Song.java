package cn.org.eshow.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 歌曲
 * */
public class Song implements Parcelable {

	private int id;// id
//	private Album album;// 专辑
	private Artist artist;// 歌手
	private String name;//歌曲名称
	private String displayName;// 文件名称[含后缀名]
	private String netUrl;// 网络路径
	private int durationTime;// 播放时间
	private int size;// 文件大小
	private boolean isLike;// 是否最爱
	private String lyricPath;// 歌词位置
	private String filePath;// 文件路径
	private String playerList;// 播放列表的Id集合，它们之间用’$id$’分隔
	private boolean isNet;// 是否是网络音乐
	private String mimeType;// MINE类型
	private boolean isDownFinish;// 是否是下载完成

	public Song() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
//
//	public Album getAlbum() {
//		return album;
//	}
//
//	public void setAlbum(Album album) {
//		this.album = album;
//	}

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getNetUrl() {
		return netUrl;
	}

	public void setNetUrl(String netUrl) {
		this.netUrl = netUrl;
	}

	public int getDurationTime() {
		return durationTime;
	}

	public void setDurationTime(int durationTime) {
		this.durationTime = durationTime;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isLike() {
		return isLike;
	}

	public void setLike(boolean isLike) {
		this.isLike = isLike;
	}

	public String getLyricPath() {
		return lyricPath;
	}

	public void setLyricPath(String lyricPath) {
		this.lyricPath = lyricPath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getPlayerList() {
		return playerList;
	}

	public void setPlayerList(String playerList) {
		this.playerList = playerList;
	}

	public boolean isNet() {
		return isNet;
	}

	public void setNet(boolean isNet) {
		this.isNet = isNet;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public boolean isDownFinish() {
		return isDownFinish;
	}

	public void setDownFinish(boolean isDownFinish) {
		this.isDownFinish = isDownFinish;
	}

	public static final Creator<Song> CREATOR = new Creator<Song>() {

		public Song createFromParcel(Parcel source) {
			Song song = new Song();
			song.id = source.readInt();
//			song.album = (Album) source.readSerializable();
			song.artist = (Artist) source.readSerializable();
			song.name=source.readString();
			song.displayName = source.readString();
			song.netUrl = source.readString();
			song.durationTime = source.readInt();
			song.size = source.readInt();
			boolean[] bools = new boolean[3];
			source.readBooleanArray(bools);
			song.isLike = bools[0];
			song.isNet = bools[1];
			song.isDownFinish = bools[2];
			song.lyricPath = source.readString();
			song.filePath = source.readString();
			song.playerList = source.readString();
			song.mimeType = source.readString();
			return song;
		}

		public Song[] newArray(int size) {
			return new Song[size];
		}

	};

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
//		dest.writeSerializable(album);
		dest.writeSerializable(artist);
		dest.writeString(name);
		dest.writeString(displayName);
		dest.writeString(netUrl);
		dest.writeInt(durationTime);
		dest.writeInt(size);
		dest.writeBooleanArray(new boolean[] { isLike, isNet, isDownFinish });
		dest.writeString(lyricPath);
		dest.writeString(filePath);
		dest.writeString(playerList);
		dest.writeString(mimeType);
	}

}