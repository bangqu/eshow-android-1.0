package cn.org.eshow.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClsUtils {
    private static String tag="BT_ClsUtils";
    /**
     * 与设备配对 参考源码：platform/packages/apps/Settings.git
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     */
    static public boolean createBond(Class btClass, BluetoothDevice btDevice) throws Exception {
        Method createBondMethod = btClass.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        Log.i(tag,"与设备配对,returnValue is "+returnValue+",,returnValue.booleanValue()"+returnValue.booleanValue());

        return returnValue.booleanValue();
    }

    static public boolean setPin(Class btClass, BluetoothDevice btDevice, String str) throws Exception {
        try {
            Method removeBondMethod = btClass.getDeclaredMethod("setPin", new Class[]{byte[].class});
            Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice, new Object[]{str.getBytes()});
            Log.i(tag, "PIN码设定，returnValue is " + returnValue);
        } catch (SecurityException e) {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    // 取消用户输入
    static public boolean cancelPairingUserInput(Class btClass, BluetoothDevice device) throws Exception {
        Method createBondMethod = btClass.getMethod("cancelPairingUserInput");
        // cancelBondProcess()
        Boolean returnValue = (Boolean) createBondMethod.invoke(device);
        return returnValue.booleanValue();
    }

    public static final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    static public boolean pairAddr(String strAddr, String strPsw) {
        Log.i(tag,"pairAddr : addr = "+strAddr+",setPin ="+strPsw);
        boolean result = false;
        bluetoothAdapter.cancelDiscovery();
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
        if (!BluetoothAdapter.checkBluetoothAddress(strAddr)) { // 检查蓝牙地址是否有效
            Log.d(tag, "devAdd un effient!");
            return false;
        }
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);
        if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
            try {
                Log.d(tag, "NOT BOND_BONDED");
                ClsUtils.setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
                ClsUtils.createBond(device.getClass(), device);
                result = true;
            } catch (Exception e) {
                Log.d(tag, "setPiN failed!");
                e.printStackTrace();
            }
        } else {
            Log.d(tag, "HAS BOND_BONDED");
            try {
                ClsUtils.removeBond(device.getClass(), device);
                ClsUtils.setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
                ClsUtils.createBond(device.getClass(), device);
                result = true;
            } catch (Exception e) {
                Log.d(tag, "setPiN failed!");
                e.printStackTrace();
            }
        }
        return result;
    }

    static public boolean pairDevice(BluetoothDevice bluetoothDevice, String strPsw) {
        Log.d(tag, "pairDevice : bluetoothDevice is name = "+bluetoothDevice.getName()+", addr = "+bluetoothDevice.getAddress()+",,setPin = "+strPsw);
        boolean result = false;
        bluetoothAdapter.cancelDiscovery();
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
        if (!BluetoothAdapter.checkBluetoothAddress(bluetoothDevice.getAddress())) { // 检查蓝牙地址是否有效
            Log.d(tag, "devAdd un effient!");
            return false;
        }
        if (bluetoothDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
            try {
                Log.d(tag, "NOT BOND_BONDED");
                ClsUtils.setPin(bluetoothDevice.getClass(), bluetoothDevice, strPsw); // 手机和蓝牙采集器配对
                ClsUtils.createBond(bluetoothDevice.getClass(), bluetoothDevice);

                result = true;
            } catch (Exception e) {
                Log.d(tag, "setPiN failed!");
                e.printStackTrace();
            }
        } else {
            Log.d(tag, "HAS BOND_BONDED");
            try {
                ClsUtils.removeBond(bluetoothDevice.getClass(), bluetoothDevice);
                ClsUtils.setPin(bluetoothDevice.getClass(), bluetoothDevice, strPsw); // 手机和蓝牙采集器配对
                ClsUtils.createBond(bluetoothDevice.getClass(), bluetoothDevice);
                result = true;
            } catch (Exception e) {
                Log.d(tag, "setPiN failed!");
                e.printStackTrace();
            }
        }
        return result;
    }
    /**
     * 与设备解除配对 参考源码：platform/packages/apps/Settings.git
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     */
    static public boolean removeBond(Class btClass, BluetoothDevice btDevice) throws Exception {
        Method removeBondMethod = btClass.getMethod("removeBond");
        Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }
    // 取消配对
    static public boolean cancelBondProcess(Class btClass, BluetoothDevice device) throws Exception {
        Method createBondMethod = btClass.getMethod("cancelBondProcess");
        Boolean returnValue = (Boolean) createBondMethod.invoke(device);
        Log.i(tag,"取消用户输入,returnValue is "+returnValue+",,returnValue.booleanValue()"+returnValue.booleanValue());
        return returnValue.booleanValue();
    }
    /**
     * @param clsShow
     */
    static public void printAllInform(Class clsShow) {
        try {
            // 取得所有方法
            Method[] hideMethod = clsShow.getMethods();
            int i = 0;
            for (; i < hideMethod.length; i++) {
                Log.e("method name", hideMethod[i].getName() + ";and the i is:" + i);
            }
            // 取得所有常量
            Field[] allFields = clsShow.getFields();
            for (i = 0; i < allFields.length; i++) {
                Log.e("Field name", allFields[i].getName());
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
