package com.newsee.lib

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.newsee.common.ui.ShowBigImageActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener {
            ShowBigImageActivity.startActivity(
                this,
                urlList = arrayListOf(
                    "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=141398218,1910486794&fm=26&gp=0.jpg",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1600778691527&di=7ca2e901d3bf7f121431a99a5d42e097&imgtype=0&src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201407%2F10%2F152657k8l2cry72yrul7s5.jpg"
                )
            )
        }

    }

}