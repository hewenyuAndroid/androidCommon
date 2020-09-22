package com.newsee.common.dialog.listener;

import com.newsee.common.dialog.AlertDialog;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2020/7/3 17:39
 * 说明:
 * ====================================
 */
public interface OnDialogActionListener<T> {

    void onAction(AlertDialog dialog, T t);

}
