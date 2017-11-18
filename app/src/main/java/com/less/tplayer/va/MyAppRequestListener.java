package com.less.tplayer.va;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.lody.virtual.client.core.InstallStrategy;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.remote.InstallResult;

import java.io.IOException;

/**
 * @author Lody
 */

public class MyAppRequestListener implements VirtualCore.AppRequestListener {

    private final Context context;

    public MyAppRequestListener(Context context) {
        this.context = context;
    }

    @Override
    public void onRequestInstall(String path) {
        Log.i("server", "install:" + path);
        InstallResult res = VirtualCore.get().installPackage(path, InstallStrategy.UPDATE_IF_EXIST);
        if (res.isSuccess) {
            try {
                VirtualCore.get().preOpt(res.packageName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (res.isUpdate) {
                sendBroadcast(VApplication.ACTION_PACKAGE_UPDATE, res.packageName);
            } else {
                sendBroadcast(VApplication.ACTION_PACKAGE_ADD, res.packageName);
            }
        } else {
            Toast.makeText(context, "Install failed: " + res.error, Toast.LENGTH_SHORT).show();
        }
    }

    private void sendBroadcast(String action, String pkg) {
        context.sendBroadcast(new Intent(action,
                Uri.parse("pacakge:" + pkg))
                .setPackage(context.getPackageName()));
    }

    @Override
    public void onRequestUninstall(String pkg) {
        Log.i("server", "uninstall:" + pkg);
        sendBroadcast(VApplication.ACTION_PACKAGE_REMOVE, pkg);
    }
}