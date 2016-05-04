package cn.org.eshow.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import cn.org.eshow.demo.R;
import cn.org.eshow.demo.common.CommonActivity;
import cn.org.eshow.util.ESFileUtil;
import cn.org.eshow.util.ESImageUtil;
import cn.org.eshow.util.ESViewUtil;
import cn.org.eshow.view.cropimage.CropImage;
import cn.org.eshow.view.cropimage.CropImageView;

import java.io.File;


/**
 * 裁剪图片界面
 */
public class CropImageActivity extends CommonActivity implements OnClickListener {
    private Context mContext = CropImageActivity.this;
    private static final String TAG = "CropImageActivity";
    private CropImageView mImageView;
    private Bitmap mBitmap;

    private CropImage mCrop;

    RelativeLayout rlBack;
    MaterialMenuView materialBackButton;
    TextView tvTitle;
    TextView tvSubTitle;
    ImageView ivShare;

    private String mPath = "CropImageActivity";
    public int screenWidth = 0;
    public int screenHeight = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    break;

            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropimage);
        init();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBitmap != null) {
            mBitmap = null;
        }
    }

    private void init() {
        getWindowWH();
        ESViewUtil.scaleContentView((LinearLayout) findViewById(R.id.llParent));

        mPath = getIntent().getStringExtra("PATH");
        mImageView = (CropImageView) findViewById(R.id.crop_image);

        rlBack = (RelativeLayout) findViewById(R.id.rlBack);
        rlBack.setOnClickListener(this);
        materialBackButton = (MaterialMenuView) findViewById(R.id.material_back_button);
        materialBackButton.setState(MaterialMenuDrawable.IconState.ARROW);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("头像修改");
        tvSubTitle = (TextView) findViewById(R.id.tvSubTitle);
        tvSubTitle.setVisibility(View.VISIBLE);
        tvSubTitle.setText("保存");
        tvSubTitle.setOnClickListener(this);

        //相册中原来的图片
        File mFile = new File(mPath);
        try {
            mBitmap = ESFileUtil.getBitmapFromSD(mFile, ESImageUtil.SCALEIMG, 1000, 1000);
            if (mBitmap == null) {
                Toast.makeText(CropImageActivity.this, "没有找到图片", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                resetImageView(mBitmap);
            }
        } catch (Exception e) {
            Toast.makeText(CropImageActivity.this, "没有找到图片", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * 获取屏幕的高和宽
     */
    private void getWindowWH() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    private void resetImageView(Bitmap b) {
        mImageView.clear();
        mImageView.setImageBitmap(b);
        mImageView.setImageBitmapResetBase(b, true);
        mCrop = new CropImage(this, mImageView, mHandler);
        mCrop.crop(b);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                finish();
                break;
            case R.id.tvSubTitle:
                String path = mCrop.saveToLocal(mCrop.cropAndSave());
                Intent intent = new Intent();
                intent.putExtra("PATH", path);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
