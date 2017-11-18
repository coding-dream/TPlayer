package com.less.tplayer.va;

import android.content.Context;
import android.os.RemoteException;

import com.lody.virtual.client.core.VirtualCore.PackageObserver;

/**
 * Created by deeper on 2017/11/17.
 */

public class MyPackageObserver extends PackageObserver {
    private Context mContext;
    public MyPackageObserver(Context context) {
        mContext = context;
    }

    @Override
    public void onPackageInstalled(String s) throws RemoteException {

    }

    @Override
    public void onPackageUninstalled(String s) throws RemoteException {

    }

    @Override
    public void onPackageInstalledAsUser(int i, String s) throws RemoteException {

    }

    @Override
    public void onPackageUninstalledAsUser(int i, String s) throws RemoteException {

    }
}
