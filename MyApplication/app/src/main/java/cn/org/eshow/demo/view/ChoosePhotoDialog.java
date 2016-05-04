package cn.org.eshow.demo.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import cn.org.eshow.demo.R;
import cn.org.eshow.util.ESViewUtil;


/**
 * 选择照片对话框
 */
public class ChoosePhotoDialog extends Dialog {
    Context context;

    Button choose_album;
    Button choose_cam;
    Button choose_cancel;

    public ChoosePhotoDialog(Context context) {
        super(context, R.style.confirm_dialog);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_chooseavatar);
        ESViewUtil.scaleContentView((LinearLayout) findViewById(R.id.llParent));
        getWindow().setBackgroundDrawable(new BitmapDrawable());
        getWindow().setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        getWindow().setWindowAnimations(R.style.ShowShareDialogAni);  //添加动画
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        choose_album = (Button) findViewById(R.id.choose_album);
        choose_cam = (Button) findViewById(R.id.choose_cam);
        choose_cancel = (Button) findViewById(R.id.choose_cancel);
        choose_album.setOnClickListener(clickListener);
        choose_cam.setOnClickListener(clickListener);
        choose_cancel.setOnClickListener(clickListener);

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.choose_album:
                    if (onChooseListener != null) {
                        onChooseListener.onLocalChooseListener();
                    }
                    dismiss();
                    break;
                case R.id.choose_cam:
                    if (onChooseListener != null) {
                        onChooseListener.onTakePhotoListener();
                    }
                    dismiss();
                    break;
                case R.id.choose_cancel:
                    dismiss();
                    break;
            }
        }
    };

    OnChooseListener onChooseListener;

    public void setOnChooseListener(OnChooseListener onChooseListener) {
        this.onChooseListener = onChooseListener;
    }

    public interface OnChooseListener {
        public void onLocalChooseListener();

        public void onTakePhotoListener();

    }
}
