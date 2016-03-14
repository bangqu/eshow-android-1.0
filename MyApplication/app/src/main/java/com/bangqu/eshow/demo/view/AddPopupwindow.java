package com.bangqu.eshow.demo.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.demo.activity.ScanActivity;
import com.bangqu.eshow.util.ESViewUtil;

/**
 * 主页加号按钮点击弹出的浮窗
 * Created by daikting on 16/3/9.
 */
public class AddPopupwindow extends PopupWindow {
    private View contentView;
    private RelativeLayout rlParent;
    private LinearLayout llScan,llSystemInfo,llTransformInfo;

    public AddPopupwindow(final Activity context){
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = layoutInflater.from(context).inflate(R.layout.pop_more,null);
        this.setContentView(contentView);

        rlParent = (RelativeLayout) contentView.findViewById(R.id.rlParent);
        ESViewUtil.scaleContentView(rlParent);

        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);

        llScan = (LinearLayout) contentView.findViewById(R.id.llScan);
        llScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ScanActivity.class);
                context.startActivity(intent);
                AddPopupwindow.this.dismiss();
            }
        });
        llSystemInfo = (LinearLayout) contentView.findViewById(R.id.llSystemInfo);
        llSystemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddPopupwindow.this.dismiss();
            }
        });
        llTransformInfo = (LinearLayout) contentView.findViewById(R.id.llTransInfo);
        llTransformInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddPopupwindow.this.dismiss();
            }
        });

    }

    public void show(Context context,final View parent){

        if (!AddPopupwindow.this.isShowing()) {
            int x = -130;
            int y = -20;
            // 以下拉方式显示popupwindow
            AddPopupwindow.this.showAsDropDown(parent,ESViewUtil.scaleValue(context,x),ESViewUtil.scaleValue(context,y));
        } else {
            AddPopupwindow.this.dismiss();
        }
    }
}
