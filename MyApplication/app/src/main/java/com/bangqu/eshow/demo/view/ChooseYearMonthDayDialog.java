package com.bangqu.eshow.demo.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.util.ESDateUtil;
import com.bangqu.eshow.util.ESStrUtil;
import com.bangqu.eshow.util.ESViewUtil;
import com.bangqu.eshow.view.wheel.ESNumericWheelAdapter;
import com.bangqu.eshow.view.wheel.ESWheelView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


/**
 * 年、月、日选择对话框
 */
public class ChooseYearMonthDayDialog extends Dialog {
    Context context;

    LinearLayout llParent;
    TextView tvFinish;
    ESWheelView mWheelViewY;
    ESWheelView mWheelViewM;
    ESWheelView mWheelViewD;

    TextView inputTimeText;
    int startYear;
    int endYearOffset;

    public ChooseYearMonthDayDialog(Context context) {
        super(context, R.style.confirm_dialog);
        this.context = context;

    }

    public ChooseYearMonthDayDialog(Context context, final TextView mText, final int startYear, int endYearOffset) {
        super(context, R.style.confirm_dialog);
        this.context = context;

        inputTimeText = mText;
        this.startYear = startYear;
        this.endYearOffset = endYearOffset;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_chooseday);
        ESViewUtil.scaleContentView((LinearLayout) findViewById(R.id.llParent));
        getWindow().setBackgroundDrawable(new BitmapDrawable());
        getWindow().setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        getWindow().setWindowAnimations(R.style.ShowShareDialogAni);  //添加动画
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        llParent = (LinearLayout) findViewById(R.id.llParent);
        tvFinish = (TextView) findViewById(R.id.tvFinish);
        tvFinish.setOnClickListener(clickListener);
        mWheelViewY = (ESWheelView) findViewById(R.id.wvYear);
        mWheelViewM = (ESWheelView) findViewById(R.id.wvMonth);
        mWheelViewD = (ESWheelView) findViewById(R.id.wvDay);

        initWheelDatePicker();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvFinish:
                    int indexYear = mWheelViewY.getCurrentItem();
                    String year = mWheelViewY.getAdapter().getItem(indexYear);

                    int indexMonth = mWheelViewM.getCurrentItem();
                    String month = mWheelViewM.getAdapter().getItem(indexMonth);

                    int indexDay = mWheelViewD.getCurrentItem();
                    String day = mWheelViewD.getAdapter().getItem(indexDay);

                    inputTimeText.setText(ESStrUtil.dateTimeFormat(year + "-" + month + "-" + day));

                    if(onFinishListener != null){
                        onFinishListener.onFinishListener();
                    }
                    dismiss();
                    break;
            }
        }
    };

    /**
     * 描述：默认的年月日的日期选择器
     *
     * @throws
     */
    private void initWheelDatePicker() {
        int defaultYear = 0;
        int defaultMonth = 0;
        int defaultDay = 0;
        int endYear = startYear + endYearOffset;
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};
        //时间选择可以这样实现
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);

        String initTime = inputTimeText.getText().toString();
        if(ESStrUtil.isEmpty(initTime)){
            defaultYear = year;
            defaultMonth = month;
            defaultDay = day;
        }else{
                String[] date = initTime.split("-");
                if(date != null && date.length == 3){
                    defaultYear = Integer.valueOf(date[0]);
                    defaultMonth = Integer.valueOf(date[1]);
                    defaultDay = Integer.valueOf(date[2]);

                }else {
                    defaultYear = year;
                    defaultMonth = month;
                    defaultDay = day;
                }
        }

        inputTimeText.setText(ESStrUtil.dateTimeFormat(defaultYear + "-" + defaultMonth + "-" + defaultDay));
        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

        //设置"年"的显示数据
        mWheelViewY.setAdapter(new ESNumericWheelAdapter(startYear, endYear));
        mWheelViewY.setCyclic(true);// 可循环滚动
        mWheelViewY.setLabel("年");  // 添加文字
        mWheelViewY.setCurrentItem(defaultYear - startYear);// 初始化时显示的数据
        mWheelViewY.setValueTextSize(32);
        mWheelViewY.setLabelTextSize(30);
        mWheelViewY.setLabelTextColor(0x80000000);
        //mWheelViewY.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));

        // 月
        mWheelViewM.setAdapter(new ESNumericWheelAdapter(1, 12));
        mWheelViewM.setCyclic(true);
        mWheelViewM.setLabel("月");
        mWheelViewM.setCurrentItem(defaultMonth - 1);
        mWheelViewM.setValueTextSize(32);
        mWheelViewM.setLabelTextSize(30);
        mWheelViewM.setLabelTextColor(0x80000000);
        //mWheelViewM.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));

        // 日
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (list_big.contains(String.valueOf(month + 1))) {
            mWheelViewD.setAdapter(new ESNumericWheelAdapter(1, 31));
        } else if (list_little.contains(String.valueOf(month + 1))) {
            mWheelViewD.setAdapter(new ESNumericWheelAdapter(1, 30));
        } else {
            // 闰年
            if (ESDateUtil.isLeapYear(year)) {
                mWheelViewD.setAdapter(new ESNumericWheelAdapter(1, 29));
            } else {
                mWheelViewD.setAdapter(new ESNumericWheelAdapter(1, 28));
            }
        }
        mWheelViewD.setCyclic(true);
        mWheelViewD.setLabel("日");
        mWheelViewD.setCurrentItem(defaultDay - 1);
        mWheelViewD.setValueTextSize(32);
        mWheelViewD.setLabelTextSize(30);
        mWheelViewD.setLabelTextColor(0x80000000);
        //mWheelViewD.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));

        // 添加"年"监听
        ESWheelView.AbOnWheelChangedListener wheelListener_year = new ESWheelView.AbOnWheelChangedListener() {

            @Override
            public void onChanged(ESWheelView wheel, int oldValue, int newValue) {
                int year_num = newValue + startYear;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(mWheelViewM.getCurrentItem() + 1))) {
                    mWheelViewD.setAdapter(new ESNumericWheelAdapter(1, 31));
                } else if (list_little.contains(String.valueOf(mWheelViewM.getCurrentItem() + 1))) {
                    mWheelViewD.setAdapter(new ESNumericWheelAdapter(1, 30));
                } else {
                    if (ESDateUtil.isLeapYear(year_num))
                        mWheelViewD.setAdapter(new ESNumericWheelAdapter(1, 29));
                    else
                        mWheelViewD.setAdapter(new ESNumericWheelAdapter(1, 28));
                }
            }
        };
        // 添加"月"监听
        ESWheelView.AbOnWheelChangedListener wheelListener_month = new ESWheelView.AbOnWheelChangedListener() {

            @Override
            public void onChanged(ESWheelView wheel, int oldValue, int newValue) {
                int month_num = newValue + 1;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(month_num))) {
                    mWheelViewD.setAdapter(new ESNumericWheelAdapter(1, 31));
                } else if (list_little.contains(String.valueOf(month_num))) {
                    mWheelViewD.setAdapter(new ESNumericWheelAdapter(1, 30));
                } else {
                    int year_num = mWheelViewY.getCurrentItem() + startYear;
                    if (ESDateUtil.isLeapYear(year_num))
                        mWheelViewD.setAdapter(new ESNumericWheelAdapter(1, 29));
                    else
                        mWheelViewD.setAdapter(new ESNumericWheelAdapter(1, 28));
                }
            }
        };
        mWheelViewY.addChangingListener(wheelListener_year);
        mWheelViewM.addChangingListener(wheelListener_month);
    }

    OnFinishListener onFinishListener;

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    public interface OnFinishListener {
        public void onFinishListener();
    }
}
