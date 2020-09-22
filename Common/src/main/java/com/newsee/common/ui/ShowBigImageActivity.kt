package com.newsee.common.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.github.chrisbanes.photoview.PhotoView
import com.newsee.common.R
import com.newsee.common.base.BaseActivity
import kotlinx.android.synthetic.main.activity_show_big_image.*

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2020/9/22 17:00
 * 说明: 显示大图
 * ====================================
 */
class ShowBigImageActivity : BaseActivity() {

    companion object {
        /**
         * 传入单张图片
         */
        const val EXTRA_URL = "extra_url"

        /**
         * 传入图片数组
         */
        const val EXTRA_URL_LIST = "extra_url_list"

        /**
         * 默认选中的位置
         */
        const val EXTRA_CURR_INDEX = "extra_curr_index"

        /**
         * 查看大图
         */
        fun startActivity(
            context: Context,
            url: String = "",
            urlList: ArrayList<String> = ArrayList(),
            index: Int = 0
        ) {
            var intent = Intent(context, ShowBigImageActivity::class.java);
            if (!url.isNullOrEmpty()) {
                urlList.add(url);
            }
            if (urlList.isEmpty()) {
                return
            }
            var bundle = Bundle()
            bundle.putSerializable(EXTRA_URL_LIST, urlList)
            intent.putExtras(bundle)
            intent.putExtra(EXTRA_CURR_INDEX, index)
            context.startActivity(intent);
        }

    }

    /**
     * 图片列表
     */
    private var mUrlList = mutableListOf<String>()
    private var mIndex = 0

    override fun initView() {

        if (Build.VERSION.SDK_INT >= 21) {
            var decorView = window.decorView
            var option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option;
            window.statusBarColor = Color.TRANSPARENT;
        }

        supportActionBar?.hide()
        actionBar?.hide()

        if (intent.hasExtra(EXTRA_URL)) {
            mUrlList.add(intent.getStringExtra(EXTRA_URL))
        }
        if (intent.hasExtra(EXTRA_URL_LIST)) {
            mUrlList.addAll(intent.getStringArrayListExtra(EXTRA_URL_LIST))
        }
        if (mUrlList.isEmpty()) {
            Toast.makeText(mContext, "请输入图片地址", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        if (intent.hasExtra(EXTRA_CURR_INDEX)) {
            mIndex = intent.getIntExtra(EXTRA_CURR_INDEX, mIndex)
        }

        initViewPager()
    }

    private fun initViewPager() {
        viewPager.adapter = object : PagerAdapter() {

            override fun isViewFromObject(view: View, any: Any): Boolean {
                return view == any
            }

            override fun getCount(): Int {
                return mUrlList.size
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                var imageView = PhotoView(mContext)
                imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                var params = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                imageView.layoutParams = params
                imageView.setBackgroundColor(Color.BLACK)

                var options = RequestOptions()
                options.diskCacheStrategy(DiskCacheStrategy.ALL)

                Glide.with(mContext)
                    .load(mUrlList[position])
                    .apply(options)
                    .into(imageView)

                container.addView(imageView)
                return imageView
            }

            override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
                container.removeView(any as View?)
            }
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(position: Int) {
                tvPage.text = "${position + 1}/${mUrlList.size}"
            }

        })


        if (mIndex >= mUrlList.size || mIndex < 0) {
            mIndex = 0
        }

        viewPager.currentItem = mIndex
        tvPage.text = "${mIndex + 1}/${mUrlList.size}"
    }

    override fun initData() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_show_big_image
    }

}