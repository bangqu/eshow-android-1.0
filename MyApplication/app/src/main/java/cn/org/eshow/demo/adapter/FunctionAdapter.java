package cn.org.eshow.demo.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.org.eshow.demo.R;
import cn.org.eshow.demo.bean.Enum_FunctionsInMain;
import cn.org.eshow.demo.bean.Intent_FunctionInMain;
import cn.org.eshow.util.ESViewUtil;

import java.util.List;

/**
 * 主页功能列表适配器
 * Created by daikting on 16/2/18.
 */
public class FunctionAdapter extends BaseAdapter {
    private Context mContext;
    private List<Enum_FunctionsInMain> functions;

    public FunctionAdapter(Context context, List<Enum_FunctionsInMain> functions) {
        this.mContext = context;
        this.functions = functions;
    }

    @Override
    public int getCount() {
        return functions.size();
    }

    @Override
    public Object getItem(int position) {
        return functions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHold viewHold;
        if (convertView == null) {
            viewHold = new ViewHold();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_function, null);
            ESViewUtil.scaleContentView((LinearLayout) convertView.findViewById(R.id.llParent));
            viewHold.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHold.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            viewHold.rlItem = (RelativeLayout) convertView.findViewById(R.id.rlItem);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        viewHold.tvName.setText(functions.get(position).toString());
        viewHold.ivIcon.setImageDrawable(mContext.getResources().getDrawable(functions.get(position).getDrawableId()));
        viewHold.rlItem.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new Intent_FunctionInMain(mContext, functions.get(position));
                        Activity activity = (Activity) mContext;
                        activity.overridePendingTransition(R.anim.scroll_in_re, R.anim.scroll_out_re);

                    }
                }
        );
        return convertView;
    }

    class ViewHold {
        TextView tvName;
        ImageView ivIcon;
        RelativeLayout rlItem;
    }
}
