package com.newsee.common.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;

import com.newsee.common.R;
import com.newsee.common.dialog.listener.OnDialogActionListener;
import com.newsee.common.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2019/7/22 15:43
 * 说明: Dialog 管理工具类
 * ====================================
 */
public class DialogManager {

    private static DialogManager mInstance;

    private List<AlertDialog> mDialogList;

    private DialogManager() {
        mDialogList = new ArrayList<>();
    }

    public static DialogManager getInstance() {
        if (mInstance == null) {
            synchronized (DialogManager.class) {
                mInstance = new DialogManager();
            }
        }
        return mInstance;
    }

    // region ------------------------------------ 加载弹窗 ------------------------------------

    /**
     * 加载弹窗
     *
     * @param context
     * @param msg
     * @return
     */
    public AlertDialog showLoading(Context context, String msg) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setContentView(R.layout.layout_dialog_loading)
                .setText(R.id.tv_dialog_message, msg)
                .setCorner(DensityUtil.dp2px(context, 8))
                .setCancelable(true)
                .setCanceledOnTouchOutside(false)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // dialog销毁时需要从缓存列表中移除
                        closeDialog(dialog);
                    }
                }).build();

        dialog.show();

        // 增加到集合中管理
        mDialogList.add(dialog);

        return dialog;
    }


    // endregion ------------------------------------------------------------------------------

    // region ------------------------------------ 确认弹窗 ------------------------------------

    public AlertDialog showConfirmDialog(Context context, String message, String confirm, String cancel, OnDialogActionListener confirmClickListener) {
        return showConfirmDialog(context, message, confirm, cancel, false, confirmClickListener);
    }

    /**
     * 显示确认弹窗
     *
     * @param context
     * @param message
     * @param confirm
     * @param cancel               传入空串时隐藏取消按钮
     * @param cancelable           点击弹窗外面的区域是否dismiss
     * @param confirmClickListener
     * @return
     */
    public AlertDialog showConfirmDialog(Context context, String message, String confirm, String cancel, boolean cancelable, OnDialogActionListener confirmClickListener) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setContentView(R.layout.layout_dialog_confirm)
                .setText(R.id.tv_dialog_message, message)
                .setText(R.id.tv_dialog_cancel, cancel)
                .setText(R.id.tv_dialog_confirm, confirm)
                .setCorner(DensityUtil.dp2px(context, 8))
                .setWidthPercent(0.9f)
                .setCancelable(cancelable)
                .setCanceledOnTouchOutside(cancelable)
                .setOnClickListener(R.id.tv_dialog_confirm, confirmClickListener)
                .setOnClickListener(R.id.tv_dialog_cancel, new OnDialogActionListener() {
                    @Override
                    public void onAction(AlertDialog dialog, Object o) {
                        dialog.dismiss();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // dialog销毁时需要从缓存列表中移除
                        closeDialog(dialog);
                    }
                }).build();

        if (TextUtils.isEmpty(cancel)) {
            dialog.getView(R.id.tv_dialog_cancel).setVisibility(View.GONE);
            dialog.getView(R.id.line).setVisibility(View.GONE);
        }

        dialog.show();

        // 增加到集合中管理
        mDialogList.add(dialog);
        return dialog;
    }

    // endregion ------------------------------------------------------------------------------

    // region ----------------------- 单例对象释放 -----------------------

    /**
     * 页面关闭之前需要释放Dialog(onDestroy()中调用)
     *
     * @param context
     */
    public void releaseDialogWithContext(Context context) {
        if (context == null) {
            return;
        }

        for (int i = mDialogList.size() - 1; i >= 0; i--) {
            ContextThemeWrapper wrapperContext = (ContextThemeWrapper) mDialogList.get(i).getContext();
            if (wrapperContext.getBaseContext().equals(context)) {
                closeDialog(mDialogList.get(i));
            }
        }

    }

    /**
     * 关闭指定的Dialog
     *
     * @param dialog
     * @return
     */
    public boolean closeDialog(DialogInterface dialog) {
        if (dialog != null) {
            if (dialog instanceof AlertDialog) {
                AlertDialog alertDialog = (AlertDialog) dialog;
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
                return mDialogList.remove(alertDialog);
            }
        }
        return false;
    }

    /**
     * 关闭栈顶的Dialog
     *
     * @return
     */
    public boolean closeLastDialog() {
        if (!mDialogList.isEmpty()) {
            return closeDialog(mDialogList.get(mDialogList.size() - 1));
        }
        return false;
    }

    /**
     * 清空Dialog
     */
    public void clearDialog() {
        for (int i = mDialogList.size() - 1; i >= 0; i--) {
            closeDialog(mDialogList.get(i));
        }
    }

    // endregion ---------------------------------------------------------

}
