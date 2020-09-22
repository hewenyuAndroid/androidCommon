package com.newsee.common.mvp;

import com.newsee.common.mvp.util.MvpUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

/**
 * ================================================
 * 作者: hewenyu
 * 日期: 2018/9/17 17:30
 * 说明:
 * ================================================
 */
public abstract class MvpPresenter<V extends IMvpView, M extends IMvpModel> implements IMvpPresenter<V, M> {

    private M mModel;

    private V mView, mProxyView;

    @Override
    public void attach(V view) {
        this.mView = view;
        createProxy();
        createModel();
    }

    /**
     * 创建数据层
     */
    private void createModel() {
        Type[] params = MvpUtil.getSuperClassGenricTypeArray(this.getClass());
        if (params == null || params.length == 0) {
            return;
        }
        try {
            mModel = (M) ((Class) params[1]).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建View层的代理对象
     */
    private void createProxy() {

        if (mView == null) {
            throw new RuntimeException("IMvpView is empty object reference!");
        }

        this.mProxyView = (V) Proxy.newProxyInstance(mView.getClass().getClassLoader(),
                mView.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // 判空处理
                        if (mView == null) {
                            return null;
                        }
                        return method.invoke(mView, args);
                    }
                });
    }

    @Override
    public void detach() {
        this.mView = null;
    }

    protected M getModel() {
        return mModel;
    }

    protected V getView() {
        return mProxyView;
    }

}
