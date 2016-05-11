package cn.org.eshow_framwork.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
/**
 * 弹出框
 *
 */
public class AbAlertDialogFragment extends DialogFragment {
	
	int mIcon;
	String mTitle;
	String mMessage;
	static View mContentView;
	static AbDialogOnClickListener mOnClickListener;
	
	/**
	 * Create a new instance of AbDialogFragment.
	 */
	public static AbAlertDialogFragment newInstance(int icon,String title,String message,View view,AbDialogOnClickListener onClickListener) {
		AbAlertDialogFragment f = new AbAlertDialogFragment();
		mOnClickListener = onClickListener;
		mContentView = view;
		
		Bundle args = new Bundle();
		args.putInt("icon", icon);
		args.putString("title", title);
		args.putString("message", message);
		f.setArguments(args);

		return f;
	}
	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mIcon = getArguments().getInt("icon");
		mTitle = getArguments().getString("title");
		mMessage = getArguments().getString("message");
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT);
		if(mIcon > 0){
			builder.setIcon(mIcon);
		}
		
		if(mTitle != null){
			builder.setTitle(mTitle);
		}
		
		if(mMessage != null){
			builder.setMessage(mMessage);
			
		}
		if(mContentView!=null){
			builder.setView(mContentView);
		}
		
		if(mOnClickListener != null){
			builder.setPositiveButton("确认",
	            new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	if(mOnClickListener != null){
	                		mOnClickListener.onPositiveClick();
	                	}
	                }
	            }
		     );
		     builder.setNegativeButton("取消",
	            new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	if(mOnClickListener != null){
	                		mOnClickListener.onNegativeClick();
	                	}
	                }
	            }
		    );
		}
		
	    return builder.create();
	}
	
	
	/**
     * Dialog事件的接口.
     */
    public interface AbDialogOnClickListener {
    	
    	public void onPositiveClick();
   	    
     	public void onNegativeClick();
    }
	
}
