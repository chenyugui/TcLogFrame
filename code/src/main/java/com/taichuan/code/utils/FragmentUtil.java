package com.taichuan.code.utils;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * 添加和移除Fragment的工具类，注意是android.support.v4.app包下的
 */
public class FragmentUtil {

    /**
     * 方 法：AddFragment <br>
     * 描 述：将Fragment添加到Activity的resID中 <br>
     *
     * @param fragmentActivity 当前的Activity
     * @param fragment         碎片
     * @param resId            资源容器ID
     * @param isAddToBack      是否增加历史记录
     */
    public static void addFragment(FragmentActivity fragmentActivity, Fragment fragment, int resId, boolean isAddToBack) {
        checkNotNull(fragmentActivity);
        checkNotNull(fragment);
        if (!fragmentActivity.isFinishing()) {
            FragmentManager fm = fragmentActivity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(resId, fragment);
            if (isAddToBack)
                ft.addToBackStack(null);
            ft.commitAllowingStateLoss();
        }
    }

    /**
     * 方 法：ReplaceFragment <br>
     * 描 述：替换Fragment <br>
     *
     * @param fragmentActivity 相关的Activity
     * @param fragment         碎片
     * @param resId            资源容器ID
     * @param isAddToBack      是否添加历史记录
     * @param curName          当前的名称
     */
    public static void replaceFragment(FragmentActivity fragmentActivity, Fragment fragment,
                                       int resId, boolean isAddToBack, String curName) {
        if (!fragmentActivity.isFinishing()) {
            FragmentManager fm = fragmentActivity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (isAddToBack)
                ft.addToBackStack(curName);
            ft.replace(resId, fragment);
            ft.commitAllowingStateLoss();
        }
    }

    /**
     * 方 法：RemoveFragment <br>
     * 描 述：删除Fragment <br>
     */
    public static void RemoveFragment(FragmentActivity fragmentActivity, Fragment fragment) {
        // if (act.) {
        //
        // }
        FragmentManager fm = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        } else {
            return reference;
        }
    }

    public static void clearFragment(FragmentManager fm, List<? extends Fragment> fragmentList) {
        if (fm != null && fragmentList!=null) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : fragmentList) {
                ft.remove(f);
            }
            ft.commit();
            ft = null;
            fm.executePendingTransactions();
        }
    }
}
