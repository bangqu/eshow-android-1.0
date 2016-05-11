package cn.org.eshow.demo.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.org.eshow.demo.R;
import cn.org.eshow_framwork.util.AbViewUtil;


/**
 * 提示信息确认对话框
 */
public class ConfirmDialog extends Dialog {
    public ConfirmDialog(Context context) {
        super(context);
    }

    public ConfirmDialog(Context context, int theme) {
        super(context, theme);
    }

    protected ConfirmDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private String id;
    private String content;
    private String name;
    private String strLeft;
    private String strRight;
    private OnCustomDialogListener customDialogListener;
    TextView tvContent;
    TextView tvLeft;
    TextView tvRight;
    TextView tvName;
    TextView tv_Dialog_View;

    public ConfirmDialog(Context context, String name, String content, String strRight, String strLeft, OnCustomDialogListener customDialogListener) {
        super(context, R.style.confirm_dialog);
        this.name = name;
        this.customDialogListener = customDialogListener;
        this.strLeft = strLeft;
        this.strRight = strRight;
        this.content = content;
    }

    public ConfirmDialog(Context context, String id, String content, String strRight, String strLeft) {
        super(context, R.style.confirm_dialog);
        this.id = id;
        this.strLeft = strLeft;
        this.strRight = strRight;
        this.content = content;
    }
    public ConfirmDialog(Context context, String id, String name, String content, String strRight, String strLeft, OnCustomDialogListener customDialogListener) {
        super(context, R.style.confirm_dialog);
        this.id = id;
        this.name = name;
        this.customDialogListener = customDialogListener;
        this.strLeft = strLeft;
        this.strRight = strRight;
        this.content = content;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confim);
        AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.llParent));
        getWindow().setBackgroundDrawable(new BitmapDrawable());
        //设置标题
//        setTitle(name);
        tvName = (TextView) findViewById(R.id.dialog_title);
        tvContent = (TextView) findViewById(R.id.dialog_content);
        tvRight = (TextView) findViewById(R.id.dialog_btn_ok);
        tvLeft = (TextView) findViewById(R.id.dialog_btn_cancel);
        tv_Dialog_View=(TextView) findViewById(R.id.tv_dialog_view);

        tvContent.setVisibility(View.GONE);
        tvRight.setVisibility(View.GONE);
        tvLeft.setVisibility(View.GONE);

        if (null != strLeft) {
            tv_Dialog_View.setVisibility(View.VISIBLE);
            tvLeft.setText(strLeft);
            tvLeft.setVisibility(View.VISIBLE);
            tvLeft.setOnClickListener(clickListener);
        }else {
            tv_Dialog_View.setVisibility(View.GONE);
            tvLeft.setVisibility(View.GONE);
        }
        if (null != strRight) {
            tvRight.setText(strRight);
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setOnClickListener(clickListener);
        }
        if (null != name) {
            tvName.setText(name);
        } else {
            findViewById(R.id.dialog_lay_title).setVisibility(View.GONE);
//            findViewById(R.id.dialog_lay_split).setVisibility(View.GONE);
        }
        if (null != content) {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(content);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.dialog_btn_ok:
                    if (null != customDialogListener) {
                        customDialogListener.OnCustomDialogConfim("");
                    }
                    ConfirmDialog.this.dismiss();
                    break;
                case R.id.dialog_btn_cancel:
                    if (null != customDialogListener) {
                        customDialogListener.OnCustomDialogCancel("");
                    }
                    ConfirmDialog.this.dismiss();
                    break;
            }

        }
    };

    //定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener {
        public void OnCustomDialogConfim(String str);
        public void OnCustomDialogCancel(String str);
    }

}
