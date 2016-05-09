package cn.org.eshow.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import cn.org.eshow.common.SharedPrefUtil;

/**
 * Created by lijinlin on 16/4/25.
 */
public class BluetoothConnService extends Service {
    private static final String tag="BT_BluetoothConnService";
    private Context mContext=BluetoothConnService.this;

    public static BluetoothConnService instance;//
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static BluetoothSocket socket = null;

    public static ArrayList<BluetoothDevice> deviceList=new ArrayList<BluetoothDevice>();//用来接收搜索到的设备
    private static BluetoothAdapter bluetoothAdapter =BluetoothAdapter.getDefaultAdapter();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        instance=this;

        BluetoothIntentFilter();   //设置系统蓝牙消息

        super.onCreate();
    }

    //搜索设备
    private static String searchKey=null;
    public static void SearchBlutooth(String key) {
        // 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
        Log.i(tag,"searchKey is = "+key);
        searchKey=key;
        //-----------
        closeSocket();//

        deviceList.clear();
        //BluetoothActivity.instance.bluetoothHandler.sendEmptyMessage(DISCOVERY_FINDING);
        if(!bluetoothAdapter.isDiscovering()) {
            Log.i(tag,"bluetoothAdapter.isDiscovering()  is false");
            bluetoothAdapter.startDiscovery();
        }else {
            Log.i(tag,"bluetoothAdapter.isDiscovering()  is true");
            bluetoothAdapter.cancelDiscovery();
            bluetoothAdapter.startDiscovery();
        }
    }
    //－－－－－－设置蓝牙接收的系统消息
    String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
    private void BluetoothIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);           //发现未配对蓝牙
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);    //判断蓝牙是否断开

        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED); //蓝牙适配器状态改变消息
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); //蓝牙适配器状态改变消息
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//蓝牙搜索结束

        intentFilter.addAction(ACTION_PAIRING_REQUEST);

        registerReceiver(receiver, intentFilter);                        //需要上下文
    }
    //－－－－－－－－蓝牙系统消息接收
    private static boolean findLocal=true;
    public static final int DISCOVERY_FINISH=1;
    public static final int DISCOVERY_START=2;
    public static final int DISCOVERY_FINDING=3;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                    Log.i(tag, "蓝牙已经打开");
                } else if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                    Log.i(tag, "蓝牙已经关闭");
                    bluetoothAdapter.enable();
                }
            } else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){

                Log.i(tag, "蓝牙开始搜索");
                BluetoothActivity.instance.bluetoothHandler.sendEmptyMessage(DISCOVERY_START);

            }else if(BluetoothDevice.ACTION_FOUND.equals(action)) {//---------
                BluetoothDevice btd=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //搜索 蓝牙设备
                if(btd.getName()!=null && btd.getAddress() != null) {
                    Log.i(tag, "发现蓝牙设备：name :" + btd.getName() + ",addr :" + btd.getAddress()+",searchKey is == "+searchKey);

                    if(!StringUtil.isBlank(searchKey)){//有搜索关键字
                        if(btd.getName().contains(searchKey)) {
                            if (!StringUtil.isBlank(SharedPrefUtil.getBluetoothAddr(mContext))) {//有本地蓝牙设备
                                if (!SharedPrefUtil.getBluetoothAddr(mContext).equals(btd.getAddress())) {//与本地蓝牙设备不同
                                    Log.i(tag, "1.(有本地蓝牙设备)发现蓝牙设备：name :" + btd.getName() + ",addr :" + btd.getAddress());
                                    deviceList.add(btd);
                                    BluetoothActivity.instance.bluetoothHandler.sendEmptyMessage(DISCOVERY_FINDING);
                                }else {//
                                    if (findLocal) {
                                        findLocal = false;//
                                        BluetoothActivity.instance.bluetoothHandler.sendEmptyMessage(BluetoothActivity.BLUETOOTH_LOCAL);
                                    }
                                }
                            } else {//没有本地蓝牙设备
                                Log.i(tag, "1.(没有本地蓝牙设备)发现蓝牙设备：name :" + btd.getName() + ",addr :" + btd.getAddress());
                                deviceList.add(btd);
                                BluetoothActivity.instance.bluetoothHandler.sendEmptyMessage(DISCOVERY_FINDING);
                            }
                        }
                    }else {//没有搜索关键字
                        if (!StringUtil.isBlank(SharedPrefUtil.getBluetoothAddr(mContext))) {//有本地蓝牙设备
                            if (!SharedPrefUtil.getBluetoothAddr(mContext).equals(btd.getAddress())) {//与本地蓝牙设备不同
                                Log.i(tag, "(2.有本地蓝牙设备)发现蓝牙设备：name :" + btd.getName() + ",addr :" + btd.getAddress());
                                deviceList.add(btd);
                                BluetoothActivity.instance.bluetoothHandler.sendEmptyMessage(DISCOVERY_FINDING);
                            }else {//
                                if (findLocal) {
                                    findLocal = false;//
                                    BluetoothActivity.instance.bluetoothHandler.sendEmptyMessage(BluetoothActivity.BLUETOOTH_LOCAL);
                                }
                            }
                        } else {//没有本地蓝牙设备
                            Log.i(tag, "2.(没有本地蓝牙设备)发现蓝牙设备：name :" + btd.getName() + ",addr :" + btd.getAddress());
                            deviceList.add(btd);
                            BluetoothActivity.instance.bluetoothHandler.sendEmptyMessage(DISCOVERY_FINDING);
                        }
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){   //搜索结束
                Log.i(tag, "搜索设备结束，搜索到设备数：" + deviceList.size());
                bluetoothAdapter.cancelDiscovery();
                if(!findLocal) {
                    findLocal = true;//
                }
                if(deviceList.size()==1) {  //
                    Log.i(tag, "发现一个设备");
                }
                else if(deviceList.size()<1) {
                    Log.i(tag, "没有发现设备");
                }
                else {
                    Log.i(tag, "发现多个设备");
                }
                BluetoothActivity.instance.bluetoothHandler.sendEmptyMessage(DISCOVERY_FINISH);

            } else if(BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //发送消息通知蓝牙断开
                //Log.i(tag, "蓝牙断开");
                //BluetoothActivity.instance.bluetoothHandler.sendEmptyMessage(BluetoothActivity.BLUETOOTH_CONN_FAILED);//蓝牙设备连接断开，暂定为 连接失败

            }else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){
                Log.i(tag, "BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(????),action is = "+action);

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        Log.d(tag, "正在配对......");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Log.d(tag, "完成配对");
                        connect(device);//连接设备
                        break;
                    case BluetoothDevice.BOND_NONE:
                        Log.d(tag, "取消配对");
                    default:
                        break;
                }
            }
            if(ACTION_PAIRING_REQUEST.equals(action)){
                Log.e(tag, "ACTION_PAIRING_REQUEST is "+action);
//                BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                try {
//                    ClsUtils.setPin(btDevice.getClass(), btDevice, "0000"); // 手机和蓝牙采集器配对
//                    ClsUtils.createBond(btDevice.getClass(), btDevice);
//                    ClsUtils.cancelPairingUserInput(btDevice.getClass(), btDevice);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        }
    };
    //＝＝正式开始连接蓝牙设备，以传进蓝牙设备
    public void connect(final BluetoothDevice retBluetoothDev) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(tag, "正式开始连接蓝牙设备,传进蓝牙设备:Name is = " + retBluetoothDev.getName() + ",Address is = " + retBluetoothDev.getAddress());
                    socket = retBluetoothDev.createRfcommSocketToServiceRecord(MY_UUID);
                    Log.d(tag, "1.开始连接...");
                    socket.connect();
                    Log.i(tag, "1.蓝牙连接成功");
                    BluetoothActivity.instance.bluetoothHandler.sendEmptyMessage(BluetoothActivity.BLUETOOTH_CONN_SECUSSED);//蓝牙设备连接成功
                    if(!retBluetoothDev.getAddress().equals(SharedPrefUtil.getBluetoothAddr(mContext))) {
                        SharedPrefUtil.setBluetooth(mContext, retBluetoothDev.getAddress(), retBluetoothDev.getName());//保存连接成功的蓝牙设备地址，设备名称
                    }else {
                        if (retBluetoothDev.getName() == null) {
                            SharedPrefUtil.setBluetooth(mContext, retBluetoothDev.getAddress(), SharedPrefUtil.getBluetoothName(mContext));//保存连接成功的蓝牙设备地址,设备名称
                        }else {
                            SharedPrefUtil.setBluetooth(mContext, retBluetoothDev.getAddress(), retBluetoothDev.getName());//保存连接成功的蓝牙设备地址，设备名称
                        }
                    }

                    try {
                        blueInStream = socket.getInputStream();
                        blueOutStream = socket.getOutputStream();
                    } catch (Exception e1) {
                        Log.i(tag, "1.取得读取输出流异常.e1:" + e1.toString());
                        closeSocket();
                    }
                    //启动接受数据
                    new Thread(new Runnable() {

                        int nRecv; // bytes returned from read()
                        byte[] bufRecv = new byte[1024];// buffer store for the stream
                        boolean bConnect=true;
                        public void run() {
                            while (bConnect) {
                                try {
                                    nRecv=blueInStream.read(bufRecv);

                                } catch (Exception e) {
                                    Log.e(tag, "1.治疗过程意外终止，发送 数据异常消息。e:" + e.toString());

                                    closeSocket();
                                    e.printStackTrace();
                                    bConnect = false;///数据异常 置连接状态为 false
                                    BluetoothActivity.instance.bluetoothHandler.sendEmptyMessage(BluetoothActivity.BLUETOOTH_CONN_FAILED);//蓝牙设备连接异常，暂定为 连接失败
                                }
                            }
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(tag, "1.蓝牙连接异常");
                    BluetoothActivity.instance.bluetoothHandler.sendEmptyMessage(BluetoothActivity.BLUETOOTH_CONN_FAILED);//蓝牙设备连接异常，暂定为 连接失败
                }
            }
        }).start();
    }
    //＝＝以传进蓝牙地址连接蓝牙设备
    public void connBluetoothDevice(String addr){
        Log.i(tag, "正式开始连接蓝牙设备,传进 蓝牙地址 :Addr is = " + addr);
        new ClientThread(addr,mContext).start();
    }
    //开启蓝牙连接客户端
    private static InputStream blueInStream=null;
    private static OutputStream blueOutStream=null;

    private static class ClientThread extends Thread {
        private String bluetoothAddr;
        private Context mmContext;
        public ClientThread(String address,Context context){
            bluetoothAddr=address;
            mmContext=context;
        }
        @Override
        public void run() {
            try {
                BluetoothDevice btDev = bluetoothAdapter.getRemoteDevice(bluetoothAddr);//用蓝牙地址字符串获取蓝牙设备
                //创建一个Socket连接：只需要服务器在注册时的UUID号
                socket = btDev.createRfcommSocketToServiceRecord(MY_UUID);
                //连接
                Log.i(tag, "2.开始连接...");
                socket.connect();
                Log.i(tag, "2.蓝牙连接成功");
                BluetoothActivity.instance.bluetoothHandler.sendEmptyMessage(BluetoothActivity.BLUETOOTH_CONN_SECUSSED);//蓝牙设备连接成功
                SharedPrefUtil.setBluetooth(mmContext,bluetoothAddr,SharedPrefUtil.getBluetoothName(mmContext));//保存连接成功的蓝牙设备地址

                try {
                    blueInStream = socket.getInputStream();
                    blueOutStream = socket.getOutputStream();
                } catch (Exception e1) {
                    Log.i(tag, "2.取得读取输出流异常.e1:" + e1.toString());
                    closeSocket();//
                }
                //启动接受数据
                new Thread(new Runnable() {

                    int nRecv; // bytes returned from read()
                    byte[] bufRecv = new byte[1024];// buffer store for the stream
                    boolean bConnect=true;
                    public void run() {
                        while (bConnect) {
                            try {
                                nRecv=blueInStream.read(bufRecv);

                            } catch (Exception e) {
                                Log.e(tag, "2.治疗过程意外终止，发送 数据异常消息。e:" + e.toString());

                                closeSocket();//
                                e.printStackTrace();
                                bConnect = false;///数据异常 置连接状态为 false
                                BluetoothActivity.instance.bluetoothHandler.sendEmptyMessage(BluetoothActivity.BLUETOOTH_CONN_FAILED);//蓝牙设备连接异常，暂定为 连接失败
                            }
                        }
                    }
                }).start();
            } catch (IOException e) {
                Log.i(tag, "2.蓝牙连接异常");
                BluetoothActivity.instance.bluetoothHandler.sendEmptyMessage(BluetoothActivity.BLUETOOTH_CONN_FAILED);//蓝牙设备连接异常，暂定为 连接失败
            }
        }
    };

    private static void closeStream(){
        try {
            if(blueInStream!=null) {
                blueInStream.close();
            }
            if(blueOutStream!=null) {
                blueOutStream.close();
            }
            blueInStream=null;
            blueOutStream=null;
        } catch (Exception e1) {
            Log.i(tag,"closeStream,,socket close exception");
            e1.printStackTrace();
            blueInStream=null;
            blueOutStream=null;
        }
    }
    public static void closeSocket(){
        closeStream();

        if(socket!=null) {
            Log.i(tag,"closeSocket,,socket!=null");
            try {
                socket.close();
            } catch (Exception e) {
                Log.i(tag,"closeSocket,,socket close exception");
                e.printStackTrace();
                socket=null;
            }
        }
    }
    @Override
    public void onDestroy() {
        Log.i(tag,"onDestroy");
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
