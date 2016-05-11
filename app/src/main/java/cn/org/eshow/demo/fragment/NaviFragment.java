package cn.org.eshow.demo.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import cn.org.eshow.demo.R;
import cn.org.eshow.demo.activity.InfoFormActivity_;
import cn.org.eshow.demo.activity.LoginActivity_;
import cn.org.eshow.demo.common.SharedPrefUtil;
import cn.org.eshow_framwork.fragment.AbFragment;
import cn.org.eshow_framwork.util.AbViewUtil;

/**
 * 导航分页
 * Created by daikting on 16/2/19.
 */
public class NaviFragment extends AbFragment implements View.OnClickListener {
    private Context mContext;
    final int radioIds[] = {R.id.radio0, R.id.radio1};
    RadioButton radios[] = new RadioButton[radioIds.length];
    Button buttonExit;
    TextView tvName;
    private NaviCallbacks naviCallbacks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = this.getActivity();
        View view = inflater.inflate(R.layout.fragment_navi, null);
        AbViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.llParent));
        for (int i = 0; i < radioIds.length; ++i) {
            radios[i] = (RadioButton) view.findViewById(radioIds[i]);
            radios[i].setOnClickListener(this);
        }
        radios[0].setChecked(true);

        tvName = (TextView) view.findViewById(R.id.tvName);
        updateUserInfo();
        buttonExit = (Button) view.findViewById(R.id.buttonExit);
        buttonExit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonExit) {
            if (naviCallbacks != null) {
                naviCallbacks.onNaviItemSelected(2);
            }
            return;
        }
        for (int i = 0; i < radioIds.length; i++) {
            if (v.getId() == radioIds[i]) {
                selectItem(i);
            } else {
                radios[i].setChecked(false);
            }
        }
    }

    private void selectItem(int position) {
        if (naviCallbacks != null) {
            naviCallbacks.onNaviItemSelected(position);
        }
    }

    /**
     * 侧边栏的回调
     *
     * @param naviCallbacks
     */
    public void setNaviCallbacks(NaviCallbacks naviCallbacks) {
        this.naviCallbacks = naviCallbacks;
    }

    public interface NaviCallbacks {
        void onNaviItemSelected(int position);
    }

    public void updateUserInfo(){
        if(SharedPrefUtil.isLogin(mContext)){
            String userName = SharedPrefUtil.getUser(mContext).getUsername();
            tvName.setText(userName);
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InfoFormActivity_.intent(mContext).start();
                    Activity curActivity = (Activity) mContext;
                    curActivity.overridePendingTransition(R.anim.scroll_in_re, R.anim.scroll_out_re);

                }
            });
        }else{
            tvName.setText("登录/注册");
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginActivity_.intent(mContext).start();
                }
            });
        }
    }

}
