package com.bangqu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.bangqu.lib.R;

public class SideBar extends View {

    private Context mContext;
    private int backGroundResId, backGroundSelectResId, tvColor, tvSize;
    private boolean isBold;

    // 触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    // 26个字母
    public static String[] b = {"当前", "最近", "热门", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private int choose = -1;// 选中
    private Paint paint = new Paint();

    private TextView mTextDialog;
    private ListView mlistview;

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray sidebar = context.obtainStyledAttributes(attrs, R.styleable.SideBar);
        tvColor = sidebar.getColor(R.styleable.SideBar_tvColor, ContextCompat.getColor(mContext, R.color.balck));
        backGroundResId = sidebar.getResourceId(R.styleable.SideBar_backGroundResId, 0);
        tvSize = sidebar.getInt(R.styleable.SideBar_tvSize, 12);
        isBold = sidebar.getBoolean(R.styleable.SideBar_isBold, false);
    }

    public SideBar(Context context) {
        super(context);
    }

    /**
     * 重写这个方法
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变背景颜色.
        int height = getHeight();// 获取对应高度
        int width = getWidth(); // 获取对应宽度
        int singleHeight = height / b.length + 10;// 获取每一个字母的高度

        for (int i = 0; i < b.length; i++) {
            // paint.setColor(Color.rgb(33, 65, 98));
            paint.setColor(tvColor);
            // paint.setColor(Color.WHITE);
            if (isBold) {
                paint.setTypeface(Typeface.DEFAULT_BOLD);
            }
            paint.setAntiAlias(true);
            paint.setTextSize(28);
            // 选中的状态
            if (i == choose) {
                // paint.setColor(Color.parseColor("#3399ff"));
                paint.setColor(Color.parseColor("#CCCCCC"));
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();// 重置画笔
        }
        if (backGroundResId > 0) {
            setBackgroundResource(backGroundResId);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * b.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

        switch (action) {
            case MotionEvent.ACTION_UP:
                if (backGroundResId > 0) {
                    setBackgroundResource(backGroundResId);
                }
                choose = -1;//
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;

            default:
                if (backGroundSelectResId > 0) {
                    setBackgroundResource(backGroundSelectResId);
                }

                if (oldChoose != c) {
                    if (c >= 0 && c < b.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(b[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(b[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        choose = c;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 向外公开的方法
     *
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * 接口
     *
     * @author coder
     */
    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }

    /**
     * @param listView
     */
    public void setListView(ListView listView) {
        mlistview = listView;
    }
}