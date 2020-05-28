package com.taichuan.code.app;

import android.app.Activity;
import android.util.Log;

import java.util.Stack;


/**
 * @author gui
 */
public class AppManager {
    private static final String TAG = "AppManager";
    private static Stack<Activity> mStack;

    private AppManager() {
    }

    private static class Holder {
        private static final AppManager INSTANCE = new AppManager();
    }

    public static AppManager getInstance() {
        return Holder.INSTANCE;
    }

    public Stack<Activity> getStack() {
        return mStack;
    }

    /**
     * 添加activity到栈
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (mStack == null) {
            mStack = new Stack<Activity>();
        }
        mStack.add(activity);
    }

    /**
     * 获取当前activity
     *
     * @return
     */
    public Activity getCurrentActivity() {
        return mStack.lastElement();
    }

    /**
     * 结束当前activity
     */
    public void finishCurrentActivity() {
        if (mStack != null && mStack.size() > 0) {
            Activity activity = mStack.lastElement();
            if (activity != null) {
                mStack.remove(activity);
            } else {
                Log.e(TAG, "finishCurrentActivity: " + "activity is null");
            }
        } else {
            Log.e(TAG, "finishCurrentActivity: " + "mStack is null");
        }
    }

    /**
     * 结束指定activity
     */
    public void finishActivity(Activity activity) {
        if (mStack != null && activity != null) {
            mStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定activity
     */
    public void removeActivity(Activity activity) {
        if (mStack != null && activity != null) {
            mStack.remove(activity);
        }
    }

    /**
     * 结束指定activity
     */
    public void finishActivity(Class<?> cls) {
        if (mStack != null) {
            for (Activity activity : mStack) {
                if (activity.getClass().equals(cls)) {
                    mStack.remove(activity);
                    activity.finish();
                }
            }
        }
    }

    /**
     * 结束所有activity并推出
     */
    public void finishAllActivity() {
        if (mStack != null) {
            for (Activity activity : mStack) {
                if (activity != null) {
                    if (!activity.isFinishing()) {
                        activity.finish();
                    }
                }
            }
            mStack.clear();
        }
    }

    /**
     * 退出栈中其他所有Activity
     *
     * @param cls Class 类名
     */
    @SuppressWarnings("rawtypes")
    public void finishOtherActivity(Class cls) {
        if (null == cls) {
            Log.e(TAG, "cls is null");
            return;
        }
        if (mStack != null && mStack.size() > 0) {
            for (Activity activity : mStack) {
                if (activity.getClass().equals(cls)) {
                    continue;
                }
                Log.d(TAG, "finishOtherActivity: " + activity.getClass().getName());
                activity.finish();
            }
            Log.d(TAG, "activity num is : " + mStack.size());
        }
    }


    public int getStackSize() {
        if (mStack != null) {
            return mStack.size();
        }
        return -1;
    }

    /**
     * 仅保留栈顶
     */
    public void popAllBottomAty() {
        if (mStack != null && mStack.size() > 1) {
            int size = mStack.size();
            for (int i = size - 2; i >= 0; i--) {
                Activity aty = mStack.get(i);
                if (aty != null) {
                    aty.finish();
                }
                mStack.remove(i);
            }
            Log.d(TAG, "popAllTopAty: size = " + mStack.size());
        }
    }
}
