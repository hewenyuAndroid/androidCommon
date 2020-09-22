package com.newsee.lib

import android.graphics.Color
import android.os.Environment
import android.widget.Toast
import com.alibaba.fastjson.JSONObject
import com.newsee.common.base.BaseActivity
import com.newsee.common.dialog.DialogManager
import com.newsee.common.http.observer.HttpObserver
import com.newsee.common.ui.ShowBigImageActivity
import com.newsee.common.utils.AppUtil
import com.newsee.lib.model.CommonModel
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : BaseActivity() {

    override fun initView() {

        setStatusBarDark(Color.WHITE, 0)

        btn.setOnClickListener {
            ShowBigImageActivity.startActivity(
                    this,
                    urlList = arrayListOf(
                            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=141398218,1910486794&fm=26&gp=0.jpg",
                            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1600778691527&di=7ca2e901d3bf7f121431a99a5d42e097&imgtype=0&src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201407%2F10%2F152657k8l2cry72yrul7s5.jpg"
                    )
            )
        }

        btnLogin.setOnClickListener {
            var model = CommonModel()
            model.login("susan", "susan123", object : HttpObserver<List<JSONObject>>() {
                override fun onSuccess(result: List<JSONObject>?) {
                    tvResult.text = JSONObject.toJSONString(result)
                }

                override fun onFailure(errorCode: String?, throwable: Throwable?) {
                    tvResult.text = "errorCode = ${errorCode}， ${throwable?.message}"
                }
            })
        }

        btnInstall.setOnClickListener {
            var file =
                    File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "AndroidToolV1.0.apk")
            if (!file.exists()) {
                Toast.makeText(mContext, "文件不存在", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            AppUtil.installApp(
                    this,
                    file.absolutePath
            )
        }

        btnLoading.setOnClickListener { showLoading() }

        btnShowDialog.setOnClickListener {
            DialogManager.getInstance()
                    .showConfirmDialog(mContext, "是否确定提交", "确定", "取消") { dialog, t -> dialog.dismiss() }
        }

    }

    override fun initData() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

}