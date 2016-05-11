package cn.org.eshow.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuView;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.org.eshow.demo.R;
import cn.org.eshow.demo.common.CommonActivity;

/**
 * Created by lijinlin on 16/5/9.
 */
/**
 *
 */
@EActivity(R.layout.activity_images)
public class ImagesActivity extends CommonActivity {
    private static final String tag="ImagesActivity";
    private Context mContext = ImagesActivity.this;

    //-----顶部标题
    @ViewById(R.id.rlBack)
    RelativeLayout mRlMenu;
    @ViewById(R.id.material_back_button)
    MaterialMenuView mMaterialBackButton;
    @ViewById(R.id.tvTitle)
    TextView mTvTitle;

    //---放置相册
    @ViewById(R.id.picGridView)
    GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int arg2, long arg3) {
                //-----
            }
        });
    }
}
