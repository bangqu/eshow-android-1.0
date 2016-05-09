package cn.org.eshow.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.org.eshow.R;
import cn.org.eshow.common.CommonActivity;
import cn.org.eshow.common.SharedPrefUtil;

/**
 * 蓝牙连接功能页面
 */
@EActivity(R.layout.activity_bluetooth)
public class BluetoothActivity extends CommonActivity {
    private static final String tag="BT_BluetoothActivity";
    private Context mContext = BluetoothActivity.this;

    public static BluetoothActivity instance;//标识   页面
    //-----顶部标题
    @ViewById(R.id.rlBack)
    RelativeLayout mRlMenu;
    @ViewById(R.id.material_back_button)
    MaterialMenuView mMaterialBackButton;
    @ViewById(R.id.tvTitle)
    TextView mTvTitle;

    //---输入框
    @ViewById(R.id.et_BluetoothStart)
    EditText et_BluetoothStart;
    @ViewById(R.id.et_BluetoothPin)
    EditText et_BluetoothPin;
    @ViewById(R.id.iv_InputPIN)
    ImageView iv_InputPIN;

    @ViewById(R.id.iv_Circle1)
    ImageView iv_Circle1;
    @ViewById(R.id.iv_Circle2)
    ImageView iv_Circle2;
    @ViewById(R.id.iv_Circle3)
    ImageView iv_Circle3;
    @ViewById(R.id.iv_Circle4)
    ImageView iv_Circle4;
    @ViewById(R.id.tv_ConnMessage)
    TextView tv_ConnMessage;

    //=-----最近连接设备
    @ViewById(R.id.ll_NearConn)
    LinearLayout ll_NearConn;
    @ViewById(R.id.tv_NearConn)
    TextView tv_NearConn;
    @ViewById(R.id.iv_Local_BtConn)
    ImageView iv_Local_BtConn;

    //---暂无可连接的蓝牙设备
    @ViewById(R.id.tv_NoBluetooth)
    TextView tv_NoBluetooth;
    //--已搜索到的设备列表
    @ViewById(R.id.lv_BluetoothList)
    ListView lv_BluetoothList;

    private static final int SHOW_CIRCLE=11;
    public static final int BLUETOOTH_CONN_SECUSSED=22;
    public static final int BLUETOOTH_CONN_FAILED=33;
    private static final int BLUETOOTH_CONN_ING=44;
    public static final int BLUETOOTH_LOCAL=55;
    private static final int CANCLE_PRE=66;//
    private static final int CANCLE_CURR=77;//

    int bluetoothTime=-1;
    private Timer bluetoothTimer = new Timer();
    private BluetoothTimerTask bluetoothTask;

    class BluetoothTimerTask extends TimerTask {
        @Override
        public void run() {
            //Log.i(tag, "bluetoothTask,bluetoothTime ="+bluetoothTime);
            bluetoothHandler.sendEmptyMessage(SHOW_CIRCLE);
        }
    };

    private void cancleTimer(){//取消计时器以及计时器中的任务，置连接状态的圆圈不显示
        if(bluetoothTask!=null){//
            bluetoothTask.cancel();
            bluetoothTask=null;
        }
        if(bluetoothTimer!=null){//
            bluetoothTimer.purge();//移除 已取消 的 计时器任务
            bluetoothTimer.cancel();
            bluetoothTimer=null;
        }

        iv_Circle1.setVisibility(View.INVISIBLE);
        iv_Circle2.setVisibility(View.INVISIBLE);
        iv_Circle3.setVisibility(View.INVISIBLE);
        bluetoothTime=-1;//重置计时器时间 为 初始值
    }

    private void startTimer(){
        if(bluetoothTimer!=null) {
            if(bluetoothTask!=null) {
                Log.i(tag,"---1---");
                bluetoothTimer.schedule(bluetoothTask, 0, 300);//启动计时器
            }else {
                Log.i(tag,"---2---");
                bluetoothTask=new BluetoothTimerTask();
                bluetoothTimer.schedule(bluetoothTask, 0, 300);//启动计时器
            }
        }else {
            bluetoothTimer=new Timer();
            if(bluetoothTask!=null) {
                Log.i(tag,"---3---");
                bluetoothTimer.schedule(bluetoothTask, 0, 300);//启动计时器
            }else {
                Log.i(tag,"---4---");
                bluetoothTask=new BluetoothTimerTask();
                bluetoothTimer.schedule(bluetoothTask, 0, 300);//启动计时器
            }
        }
    }

    private void showCircle(){
        bluetoothTime++;
        if (bluetoothTime == 0) {
            iv_Circle3.setVisibility(View.VISIBLE);
        }else if (bluetoothTime==1){
            iv_Circle2.setVisibility(View.VISIBLE);
        }else if(bluetoothTime==2){
            iv_Circle1.setVisibility(View.VISIBLE);
        }else if(bluetoothTime>=3){
            iv_Circle1.setVisibility(View.INVISIBLE);
            iv_Circle2.setVisibility(View.INVISIBLE);
            iv_Circle3.setVisibility(View.INVISIBLE);
            bluetoothTime=-1;
        }
    }

    private void showBluetoothList(){
        if(BluetoothConnService.deviceList!=null&& BluetoothConnService.deviceList.size()>0){
            if((boolean)ll_NearConn.getTag()){//如果 最近连接 tag 为 true，
                tv_ConnMessage.setText("一键连接");
            }else {
                tv_ConnMessage.setText("搜索完毕");
            }
            tv_NoBluetooth.setVisibility(View.GONE);

            if(bluetoothListAdapter==null) {
                bluetoothListAdapter = new BluetoothListAdapter(BluetoothConnService.deviceList, mContext);
                lv_BluetoothList.setAdapter(bluetoothListAdapter);
                lv_BluetoothList.setVisibility(View.VISIBLE);
            }else {
                bluetoothListAdapter.notifyDataSetChanged();
                lv_BluetoothList.setVisibility(View.VISIBLE);
            }
        }else {
            Log.i(tag,"ll_NearConn.getTag is "+ll_NearConn.getTag());
            if((boolean)ll_NearConn.getTag()){//如果最近连接 tag 为 true，设置字体
                iv_Local_BtConn.performClick();
                //tv_ConnMessage.setText("一键连接");
            }else {
                lv_BluetoothList.setVisibility(View.GONE);
                tv_NoBluetooth.setVisibility(View.VISIBLE);
            }
        }
    }

    ImageView ivIsConnecting;
    private BluetoothListAdapter bluetoothListAdapter;
    public Handler bluetoothHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {//------
                case SHOW_CIRCLE:
                    showCircle();
                    break;
                case BluetoothConnService.DISCOVERY_START:
                    Log.i(tag, "BluetoothConnService.DISCOVERY_START");
                    tv_ConnMessage.setText("搜索附近的蓝牙设备");

                    startTimer();
                    break;
                case BluetoothConnService.DISCOVERY_FINDING:
                    Log.i(tag, "BluetoothConnService.DISCOVERY_FINDING");
                    showBluetoothList();
                    break;
                case BluetoothConnService.DISCOVERY_FINISH:
                    Log.i(tag, "BluetoothConnService.DISCOVERY_FINISH");
                    cancleTimer();
                    showBluetoothList();
                    break;
                case BLUETOOTH_CONN_SECUSSED:
                    if(currPosition!=-1&&currPosition!=-2) {
                        ivIsConnecting = (ImageView) getViewByPosition(currPosition, lv_BluetoothList).findViewById(R.id.iv_BluetoothConn);
                        ivIsConnecting.setImageResource(R.drawable.ic_blueteeth_linked);
                    }else if(currPosition==-1){
                        iv_Local_BtConn.setImageResource(R.drawable.ic_blueteeth_linked);
                    }
                    break;
                case BLUETOOTH_CONN_FAILED:
                    tv_ConnMessage.setText("");
                    if(currPosition!=-1&&currPosition!=-2) {
                        ivIsConnecting = (ImageView) getViewByPosition(currPosition, lv_BluetoothList).findViewById(R.id.iv_BluetoothConn);
                        ivIsConnecting.setImageResource(R.drawable.ic_blueteeth_unlink);
                    }else if(currPosition==-1){
                        iv_Local_BtConn.setImageResource(R.drawable.ic_blueteeth_unlink);
                    }
                    break;
                case BLUETOOTH_CONN_ING:
                    Log.i(tag,"BLUETOOTH_CONN_ING,currPosition = "+currPosition);
                    if(currPosition!=-1&&currPosition!=-2) {
                        ivIsConnecting = (ImageView) getViewByPosition(currPosition, lv_BluetoothList).findViewById(R.id.iv_BluetoothConn);
                        showPlaying(ivIsConnecting);
                    }else if(currPosition==-1){
                        Log.i(tag,"Connecting Local BluetoothDevice");
                        showPlaying(iv_Local_BtConn);
                    }
                    break;
                case BLUETOOTH_LOCAL:
                    Log.i(tag,"BLUETOOTH_LOCAL");
                    tv_NearConn.setText(SharedPrefUtil.getBluetoothName(mContext));
                    ll_NearConn.setTag(true);//如果搜索到 有 最近连接的设备，设置 tag 为 true
                    ll_NearConn.setVisibility(View.VISIBLE);//
                    break;
                case CANCLE_PRE:
                    Log.i(tag,"CANCLE_PRE,currentPosition = "+currPosition+",,tempPosition = "+tempPosition);
                    if(tempPosition!=-1) {
                        ivIsConnecting = (ImageView) getViewByPosition(tempPosition, lv_BluetoothList).findViewById(R.id.iv_BluetoothConn);
                        ivIsConnecting.setImageResource(R.drawable.ic_blueteeth_unlink);
                    }else if(tempPosition==-1){
                        iv_Local_BtConn.setImageResource(R.drawable.ic_blueteeth_unlink);
                    }
                    break;
                case CANCLE_CURR:
                    Log.i(tag,"CANCLE_CURR,currentPosition = "+currPosition+",,tempPosition = "+tempPosition);
                    if(currPosition!=-1&&currPosition!=-2) {
                        ivIsConnecting = (ImageView) getViewByPosition(currPosition, lv_BluetoothList).findViewById(R.id.iv_BluetoothConn);
                        ivIsConnecting.setImageResource(R.drawable.ic_blueteeth_unlink);
                    }else if(currPosition==-1){
                        iv_Local_BtConn.setImageResource(R.drawable.ic_blueteeth_unlink);
                    }
                    currPosition=-2;
                    break;
            }
        }
    };

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;
        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
    //当前Connect,设置动画
    AnimationDrawable animationDrawable;
    private void showPlaying(ImageView id) {
        id.setVisibility(View.VISIBLE);
        id.setImageResource(R.drawable.animation_musicplying);
        animationDrawable = (AnimationDrawable) id.getDrawable();
        animationDrawable.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance=this;

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            Toast.makeText(this, "本设备没有蓝牙模块", Toast.LENGTH_LONG).show();
            finish();
        }
        //检测蓝牙是否开启
        if (!btAdapter.isEnabled()){
            //调用方法开启
//            bluetoothAdapter.enable();
            //通过意图开启
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 0);
        }
        startService(new Intent(BluetoothActivity.this,BluetoothConnService.class));
    }

    BluetoothAdapter btAdapter;
    @AfterViews
    void init() {
        mTvTitle.setText(getTitle());
        mMaterialBackButton.setState(MaterialMenuDrawable.IconState.ARROW);

        iv_InputPIN.setTag(false);//设置是否手动输入PIN码，初始值 设置为 false
        et_BluetoothPin.setText("1234");//设置初始PIN码 为 1234
        et_BluetoothPin.setTextSize(15f);//设置初始PIN码字体大小 为 15f
        et_BluetoothPin.setTextColor(getResources().getColor(R.color.close_pin));//设置初始PIN码字体颜色

        ll_NearConn.setTag(false);//初始化最近连接 tag

        if(bluetoothListAdapter!=null){
            lv_BluetoothList.setAdapter(bluetoothListAdapter);
        }
    }

    @Click(R.id.iv_Circle4)
    void startSearch(){
        Log.i(tag,"search bluetoothdevice");
        if((boolean)ll_NearConn.getTag()){//－－一键连接功能

            iv_Local_BtConn.performClick();//
            ll_NearConn.setTag(false);//一键连接后，重置 tag，恢复Circle4 搜索功能

        }else {//---------搜索功能
            if (currPosition != -2) {
                bluetoothHandler.sendEmptyMessage(CANCLE_CURR);
            }
            BluetoothConnService.SearchBlutooth(et_BluetoothStart.getText().toString().trim());
        }
    }

    @Click(R.id.iv_InputPIN)
    void onInputPin(){
        if((boolean)iv_InputPIN.getTag()){
            iv_InputPIN.setTag(false);
            iv_InputPIN.setImageResource(R.drawable.btn_blueteeth_close);
            et_BluetoothPin.setText("1234");
            et_BluetoothPin.setTextSize(15f);
            et_BluetoothPin.setTextColor(getResources().getColor(R.color.close_pin));
        }else {
            iv_InputPIN.setTag(true);
            iv_InputPIN.setImageResource(R.drawable.btn_blueteeth_open);
            et_BluetoothPin.setTextColor(getResources().getColor(R.color.open_pin));
        }
    }

    //private boolean isLocalBt=false;
    @Click(R.id.iv_Local_BtConn)
    void onLocalBtConn(){
        BluetoothConnService.closeSocket();//

        ClsUtils.pairAddr(SharedPrefUtil.getBluetoothAddr(mContext), et_BluetoothPin.getText().toString().trim());

        if(currPosition!=-1&&currPosition!=-2) {
            tempPosition=currPosition;
            currPosition = -1;
            bluetoothHandler.sendEmptyMessage(CANCLE_PRE);//发送取消上一个设备连接动画
        }else if(currPosition==-2){
            currPosition=-1;
        }
        bluetoothHandler.sendEmptyMessage(BLUETOOTH_CONN_ING);//
        tv_ConnMessage.setText(SharedPrefUtil.getBluetoothName(mContext));
    }

    private int currPosition=-2;//
    private int tempPosition=-1;//
    class BluetoothListAdapter extends BaseAdapter{
        private List<BluetoothDevice> myList;
        private Context myContext;

        public BluetoothListAdapter(List<BluetoothDevice> list, Context context){
            myList=list;
            myContext=context;
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public Object getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHold viewHold;
            if (convertView == null) {
                viewHold = new ViewHold();

                convertView = LayoutInflater.from(myContext).inflate(R.layout.item_bluetooth, null);
                viewHold.tv_BluetoothName = (TextView) convertView.findViewById(R.id.tv_BluetoothName);
                viewHold.iv_BluetoothConn = (ImageView) convertView.findViewById(R.id.iv_BluetoothConn);
                convertView.setTag(viewHold);
            } else {
                viewHold = (ViewHold) convertView.getTag();
            }
            viewHold.tv_BluetoothName.setText(myList.get(position).getName());

            viewHold.iv_BluetoothConn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(tag, "position = " + position + ",Connect BluetoothDevice :" + myList.get(position).getName() + "---" + myList.get(position).getAddress());

                    BluetoothConnService.closeSocket();
                    if (currPosition != position) {//用户不等当前连接过程完成，就更换设备连接
                        tempPosition = currPosition;//将当前连接位置缓存下来，用于稍后取消该设备的连接状态
                        currPosition = position;//重置 当前连接位置
                        bluetoothHandler.sendEmptyMessage(CANCLE_PRE);//发送取消上一个设备连接动画
                    }
                    bluetoothHandler.sendEmptyMessage(BLUETOOTH_CONN_ING);//

                    if (myList.get(position).getBondState() == BluetoothDevice.BOND_BONDED) {//当设备是已绑定设备

                        BluetoothConnService.instance.connect(myList.get(position));

                    } else if (myList.get(position).getBondState() == BluetoothDevice.BOND_NONE) {//当设备为未绑定设备

                        ClsUtils.pairDevice(myList.get(position), et_BluetoothPin.getText().toString().trim());
                    }

                    tv_ConnMessage.setText(myList.get(position).getName().toString());
                }
            });
            return convertView;
        }

        class ViewHold {
            TextView tv_BluetoothName;
            ImageView iv_BluetoothConn;
        }
    }
    @Click(R.id.rlBack)
    void onBack() {
        finish();
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(BluetoothActivity.this,BluetoothConnService.class));
        super.onDestroy();
    }

}
