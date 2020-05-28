package com.taichuan.code.mvp.view;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by gui on 2016/11/28.
 * 封装了运行时权限请求的Activity
 */
public class PermissionBaseFragment extends Fragment {
//    private Map<Integer, OnPermissionResultListener> listenerMap = new HashMap<>();

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        OnPermissionResultListener onPermissionResultListener = listenerMap.get(requestCode);
//        // 循环判断权限，只要有一个拒绝了，则回调onReject()。 全部允许时才回调onAllow()
//        for (int i = 0; i < grantResults.length; i++) {
//            listenerMap.remove(requestCode);
//            Log.d(TAG, "onRequestPermissionsResult: " + grantResults[i]);
//            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {// 拒绝权限
//                // 如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
//                // 注：如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 "不再提醒" 选项，此方法将返回 false。
//                // 如果用户第一次申请权限，此方法返回false
//                if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[i])) {
//                    // 拒绝选了"不再提醒"，一般提示跳转到权限设置页面
//                }
//                showTipDialog(permissions[i], onPermissionResultListener);
//                return;
//            }
//        }
//        onPermissionResultListener.onAllow();
//    }

    public void checkPermissions(final String[] permissions, PermissionBaseActivity.OnPermissionResultListener onPermissionResultListener) {
        Activity activity = getActivity();
        if (activity instanceof PermissionBaseActivity) {
            ((PermissionBaseActivity) activity).checkPermissions(permissions, onPermissionResultListener);
        } else {
            throw new ClassCastException("Want to use checkPermissions, The fragment's Activity must extends PermissionBaseActivity");
        }
//        if (Build.VERSION.SDK_INT < 23 || permissions.length == 0) {
//            if (onPermissionResultListener != null)
//                onPermissionResultListener.onAllow();
//        } else {
//            int size = listenerMap.size();
//            if (onPermissionResultListener != null) {
//                listenerMap.put(size, onPermissionResultListener);
//            }
//            ActivityCompat.requestPermissions(getActivity(), permissions, size);
//        }
    }

//    private void showTipDialog(String permisssion, final OnPermissionResultListener onPermissionResultListener) {
//        if (tipDialog == null) {
//            tipDialog = new Dialog(getContext(), R.style.Dialog_No_Border);
//            View permissionDialogRootView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_nide_permission, null, true);
//            // 按钮
//            permissionDialogRootView.findViewById(R.id.rltOK).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    tipDialog.cancel();
//                }
//            });
//            //
//            tipDialog.setContentView(permissionDialogRootView);
//        }
//        tipDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                onPermissionResultListener.onReject();
//            }
//        });
//        TextView textView = (TextView) tipDialog.findViewById(R.id.tvNotify);
//        textView.setText(PermissionUtil.getTip(permisssion));
//        tipDialog.show();
//    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (tipDialog != null) {
//            tipDialog.cancel();
//            tipDialog = null;
//        }
//        listenerMap.clear();
//        listenerMap = null;
//    }
}
