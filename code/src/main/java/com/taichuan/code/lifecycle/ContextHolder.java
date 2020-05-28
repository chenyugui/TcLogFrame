package com.taichuan.code.lifecycle;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.app.Service;
import android.content.Context;
import android.os.Build;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by gui on 02/03/2018.
 */

public class ContextHolder<T extends LifeCycle> extends WeakReference<T> {
    public ContextHolder(T referent) {
        super(referent);
    }

    public boolean isAlive() {
        T ref = get();
        if (ref == null) {
            return false;
        } else {
            return ref.isAlive();
        }
    }

    private boolean isServiceAlive(Service candidate) {
        if (candidate == null)
            return false;
        ActivityManager manager = (ActivityManager) candidate.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = manager.getRunningServices(Integer.MAX_VALUE);
        if (services == null)
            return false;
        for (ActivityManager.RunningServiceInfo service : services) {
            if (candidate.getClass().getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private boolean isActivityAlive(Activity a) {
        if (a == null)
            return false;
        if (a.isFinishing())
            return false;
        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private boolean isFragmentAlive(Fragment fragment) {
        boolean ret = isActivityAlive(fragment.getActivity());
        if (!ret)
            return false;
        if (fragment.isDetached())
            return false;
        return true;
    }

    private boolean isV4FragmentAlive(android.support.v4.app.Fragment fragment) {
        boolean ret = isActivityAlive(fragment.getActivity());
        if (!ret)
            return false;
        if (fragment.isDetached())
            return false;
        return true;
    }

    private boolean isImageAlive(ImageView imageView) {
        Context context = imageView.getContext();
        if (context instanceof Service)
            return isServiceAlive((Service) context);
        if (context instanceof Activity)
            return isActivityAlive((Activity) context);
        return true;
    }
}
