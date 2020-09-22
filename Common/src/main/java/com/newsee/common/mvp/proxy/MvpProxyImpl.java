package com.newsee.common.mvp.proxy;

import com.newsee.common.mvp.IMvpPresenter;
import com.newsee.common.mvp.IMvpView;
import com.newsee.common.mvp.annotation.InjectPresenter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作者: hewenyu
 * 日期: 2018/9/17 17:59
 * 说明:
 * ================================================
 */
public class MvpProxyImpl<V extends IMvpView> implements IMvpProxy<IMvpView> {

    protected V mView;

    protected List<IMvpPresenter> mPresenters;

    @Override
    public void bindPresenter(IMvpView view) {
        if (view == null) {
            throw new RuntimeException("IMvpView is empty object reference!");
        }
        this.mView = (V) view;
        if (mPresenters == null) {
            mPresenters = new ArrayList<>();
        }
        createPresenters();
    }

    /**
     * 创建 Presenter 对象
     */
    private void createPresenters() {
        Field[] fields = mView.getClass().getDeclaredFields();
        for (Field field : fields) {
            InjectPresenter injectPresenter = field.getAnnotation(InjectPresenter.class);
            if (injectPresenter != null) {
                if (!IMvpPresenter.class.isAssignableFrom(field.getType())) {
                    // 注解标记的字段不是继承自 IMvpPresenter 对象
                    throw new RuntimeException("No support presenter type: " + field.getType().getName());
                }

                try {
                    IMvpPresenter basePresenter = (IMvpPresenter) field.getType().newInstance();
                    basePresenter.attach(mView);
                    field.setAccessible(true);
                    field.set(mView, basePresenter);
                    mPresenters.add(basePresenter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void unbindPresenter() {
        if (mPresenters != null) {
            for (int index = mPresenters.size() - 1; index >= 0; index--) {
                mPresenters.get(index).detach();
                mPresenters.remove(index);
            }
        }
        mView = null;
    }

}
