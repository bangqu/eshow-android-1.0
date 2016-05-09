package cn.org.eshow.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.org.eshow.R;
import cn.org.eshow.common.CommonActivity;
import cn.org.eshow_structure.util.ESViewUtil;

/**
 * 设置页面
 * Created by daikting on 16/3/1.
 */
@EActivity(R.layout.activity_scan_textresult)
public class ScanTextResultActivity extends CommonActivity {
    public static final String INTENT_RESULT = "TextResult";
    private Context mContext = ScanTextResultActivity.this;
    @ViewById(R.id.rlBack)
    RelativeLayout mRlMenu;
    @ViewById(R.id.material_back_button)
    MaterialMenuView mMaterialBackButton;
    @ViewById(R.id.tvTitle)
    TextView mTvTitle;

    @ViewById(R.id.tvScanResult)
    TextView tvScanResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
        ESViewUtil.scaleContentView((RelativeLayout) findViewById(R.id.rlParent));

        mTvTitle.setText(getTitle());
        mMaterialBackButton.setState(MaterialMenuDrawable.IconState.ARROW);

        String result = getIntent().getStringExtra(INTENT_RESULT);
        tvScanResult.setText(result);
    }

    @Click(R.id.rlBack)
    void onBack(){
        finish();
    }
}
