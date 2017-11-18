package com.less.tplayer.va;

import com.lody.virtual.client.hook.delegate.PhoneInfoDelegate;

/**
 * Created by deeper on 2017/11/18.
 */

public class MyPhoneInfoDelegate implements PhoneInfoDelegate {

    @Override
    public String getDeviceId(String oldDeviceId, int userId) {
        return oldDeviceId;
    }

    @Override
    public String getBluetoothAddress(String oldBluetoothAddress, int userId) {
        return oldBluetoothAddress;
    }

    @Override
    public String getMacAddress(String oldMacAddress, int userId) {
        return oldMacAddress;
    }
}
