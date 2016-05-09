package cn.org.eshow.music;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.List;
import cn.org.eshow.R;
import cn.org.eshow.bean.Song;

public class MusicDowningAdapter extends BaseAdapter implements View.OnClickListener {
    private List<Song> data;
    private Context mContext;
    private float downX;  //点下时候获取的x坐标
    private float upX;   //手指离开时候的x坐标
    private Button button; //用于执行删除的button
    private Animation animation;  //删除时候的动画
    private View view;
    boolean result = false;
    boolean isOpen = false;
    ViewHolder viewHolder = null;
    public MusicDowningAdapter(Context mContext, List<Song> data) {
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

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_musicdowning, null);
            viewHolder = new ViewHolder();
            viewHolder.tvMusicName = (TextView) convertView.findViewById(R.id.tvMusicName);
            viewHolder.tvMusicState = (TextView) convertView.findViewById(R.id.tvMusicState);
            viewHolder.tvDowning = (TextView) convertView.findViewById(R.id.tvDowning);
            viewHolder.tvDowmSize = (TextView) convertView.findViewById(R.id.tvDowmSize);
            viewHolder.llDowning = (LinearLayout) convertView.findViewById(R.id.llDowning);
            viewHolder.tvDelete = (TextView) convertView.findViewById(R.id.tvDelete);
            viewHolder.tvDelete.setOnClickListener(this);
            viewHolder.llLeft = (LinearLayout) convertView.findViewById(R.id.llLeft);
            viewHolder.llLeft.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return handlerTouch(v, event);
                }
            });
            viewHolder.sbDowning = (SeekBar) convertView.findViewById(R.id.sbDowning);
            viewHolder.sbDowning.setEnabled(false);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        llLeft.setOnTouchListener(new View.OnTouchListener() {  //为每个item设置setOnTouchListener事件
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final ViewHolder holder = (ViewHolder) v.getTag();  //获取滑动时候相应的ViewHolder，以便获取button按钮
////				switch (event.getAction()) {
////					case MotionEvent.ACTION_DOWN:  //手指按下
////						downX = event.getX(); //获取手指x坐标
//////						if (button != null) {
//////							holder.llDelete.setVisibility(View.GONE);  //影藏显示出来的button
//////						}
////						break;
////					case MotionEvent.ACTION_UP:  //手指离开
////						upX = event.getX(); //获取x坐标值
////						break;
////				}
////				if (holder.llDelete != null) {
////					if (downX - upX >= 200) {  //手指离开减去开始的大于200，认为是向左滑动
////						if (holder.llDelete.getVisibility()==View.GONE){
////							holder.llDelete.setVisibility(View.VISIBLE);  //显示删除button
////						}
//////						button = holder.llDelete;  //赋值给全局button，一会儿用
////						view=v; //得到itemview，在上面加动画
////						return true; //终止事件
////					}
////					if (downX - upX <-200) {  ////手指离开减去开始的小与-200，认为是向右滑动
////						if (holder.llDelete.getVisibility()==View.VISIBLE){
////							holder.llDelete.setVisibility(View.GONE);  //隐藏删除button
////						}
//////						button = holder.llDelete;  //赋值给全局button，一会儿用
////						view=v; //得到itemview，在上面加动画
////						return true; //终止事件
////					}
////					return false;  //释放事件，使onitemClick可以执行
////				}
//            }
//        });
        final Song song = data.get(position);
        if (song != null) {
            viewHolder.sbDowning.setProgress(0);
            viewHolder.tvMusicName.setText(song.getName());
            viewHolder.tvDowning.setText("");
            viewHolder.tvDowmSize.setText("");
//            final ViewHolder finalViewHolder = viewHolder;
//            viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finalViewHolder.llDelete.setVisibility(View.GONE);  //点击删除按钮后，影藏按钮
//                    Message msg = new Message();
//                    msg.what = 3;
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("position", position);
//                    msg.setData(bundle);
//                    MusicDownActivity.instants.handler.sendMessage(msg);
//                    data.remove(position);  //把数据源里面相应数据删除
//                    notifyDataSetChanged();
//                }
//            });
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

    protected boolean handlerTouch(View v, MotionEvent event) {
       // final ViewHolder holder = (ViewHolder) v.getTag();  //获取滑动时候相应的ViewHolder，以便获取button按钮
        if (viewHolder.tvDelete != null) {
            int bottomWidth = viewHolder.tvDelete.getWidth() + viewHolder.tvDelete.getWidth();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = (int) event.getRawX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int dx = (int) (event.getRawX() - downX);
                    if (isOpen) {
                        // 打开状态
                        // 向右滑动
                        if (dx > 0 && dx < bottomWidth) {
                            v.setTranslationX(dx - bottomWidth);
                            // 允许移动，阻止点击
                            Log.i("BT-MusicDowningAdapter", "(dx > 0 && dx < bottomWidth");
                            result = true;
                        }
                    } else {
                        // 闭合状态
                        // 向左移动
                        if (dx < 0 && Math.abs(dx) < bottomWidth) {
                            v.setTranslationX(dx);
                            // 允许移动，阻止点击
                            Log.i("BT-MusicDowningAdapter", "dx < 0 && Math.abs(dx) < bottomWidth");
                            result = true;
                        }
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    // 获取已经移动的
                    float ddx = v.getTranslationX();
                    // 判断打开还是关闭
                    if (ddx <= 0 && ddx > -(bottomWidth / 2)) {
                        Log.i("BT-MusicDowningAdapter", "ddx <= 0 && ddx > -(bottomWidth / 2)");
                        // 关闭
                        ObjectAnimator oa1 = ObjectAnimator.ofFloat(v, "translationX", ddx, 0).setDuration(100);
                        oa1.start();
                        oa1.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                isOpen = false;
                                result = false;
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                                isOpen = false;
                                result = false;
                            }
                        });
                    }
                    if (ddx <= -(bottomWidth / 2) && ddx > -bottomWidth) {
                        // 打开
                        Log.i("BT-MusicDowningAdapter", "ddx <= -(bottomWidth / 2) && ddx > -bottomWidth");
                        ObjectAnimator oa1 = ObjectAnimator.ofFloat(v, "translationX", ddx, -bottomWidth)
                                .setDuration(100);
                        oa1.start();
                        result = true;
                        isOpen = true;
                    }
                    break;
            }
        } else {
            Log.i("BT-MusicDowningAdapter", "tvDelete=null");
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llLeft:
                Log.i("BT-MusicDowningAdapter", "点击了item");
                break;
            case R.id.tvDelete:
                Log.i("BT-MusicDowningAdapter", "点击了删除");
                break;
            default:
                break;
        }
    }

    public static class ViewHolder {
        public TextView tvMusicName, tvMusicState, tvDowning, tvDowmSize, tvDelete;
        public LinearLayout llDowning, llLeft;
        public SeekBar sbDowning;
    }
}
