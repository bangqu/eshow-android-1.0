package cn.org.eshow_structure.global;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * 用于处理退出程序时可以退出所有的activity，而编写的通用类
 */
public class ESActivityManager {

	private List<Activity> activityList = new LinkedList<Activity>();
	private static ESActivityManager instance;

	private ESActivityManager() {
	}

	/**
	 * 单例模式中获取唯一的AbActivityManager实例.
	 * @return
	 */
	public static ESActivityManager getInstance() {
		if (null == instance) {
			instance = new ESActivityManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到容器中.
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	
	/**
	 * 移除Activity从容器中.
	 * @param activity
	 */
	public void removeActivity(Activity activity) {
		activityList.remove(activity);
	}


	public List<Activity> getActivityList(){
		return activityList;
	}
	/**
	 * 遍历所有Activity并finish.
	 */
	public void clearAllActivity() {
			for (Activity activity : activityList) {
				try{
					activity.finish();
				}catch (Exception e){
					e.printStackTrace();
				}
			}
	}
}