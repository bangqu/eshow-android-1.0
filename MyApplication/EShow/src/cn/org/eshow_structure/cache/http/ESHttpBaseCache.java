package cn.org.eshow_structure.cache.http;


public class ESHttpBaseCache {
	
	/** AbHttpBaseCache 实例. */
	private static ESHttpBaseCache mHttpCache;
	
	/**
	 * 构造方法.
	 */
	public ESHttpBaseCache() {
		super();
	}
	
	/**
	 * 
	 * 获取单例的AbHttpBaseCache.
	 * @return
	 */
	public static ESHttpBaseCache getInstance() {
		if(mHttpCache == null){
			mHttpCache = new ESHttpBaseCache();
		}
		return mHttpCache;
	}
	
	/**
	 * 获取用于缓存的Key.
	 * @param url
	 * @return
	 */
    public String getCacheKey(String url) {
        return new StringBuilder(url.length()).append(url).toString();
    }

}
