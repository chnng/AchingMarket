package com.market.aching.presenter.impl;


import com.market.aching.BaseApplication;
import com.market.aching.R;
import com.market.aching.model.BaseResponse;
import com.market.aching.model.BookListResponse;
import com.market.aching.presenter.ApiCompleteListener;
import com.market.aching.presenter.IBookListModel;
import com.market.aching.presenter.IBookListPresenter;
import com.market.aching.presenter.IBookListView;
import com.market.aching.ui.base.BaseActivity;

import static com.market.aching.util.AchingUtil.isConnected;

/**
 * Author   :hymanme
 * Email    :hymanme@163.com
 * Create at 2016/8/5
 * Description:
 */
public class BookListPresenterImpl implements IBookListPresenter, ApiCompleteListener
{
    private IBookListView mBookListView;
    private IBookListModel mBookListModel;

    public BookListPresenterImpl(IBookListView view) {
        mBookListView = view;
        mBookListModel = new BookListModelImpl();
    }

    /**
     * 加载数据
     */
    @Override
    public void loadBooks(String q, String tag, int start, int count, String fields) {
        if (!isConnected()) {
            mBookListView.showMessage(BaseApplication.getInstance().getString(R.string.poor_network));
            mBookListView.hideProgress();
//            return;
        }
        mBookListView.showProgress();
        mBookListModel.loadBookList(q, tag, start, count, fields, this);
    }

    @Override
    public void cancelLoading() {
        mBookListModel.cancelLoading();
    }

    /**
     * 访问接口成功
     *
     * @param result 返回结果
     */
    @Override
    public void onComplected(Object result) {
        if (result instanceof BookListResponse) {
            int index = ((BookListResponse) result).getStart();
            if (index == 0) {
                mBookListView.refreshData(result);
            } else {
                mBookListView.addData(result);
            }
            mBookListView.hideProgress();
        }
    }

    /**
     * 请求失败
     *
     * @param msg 错误信息
     */
    @Override
    public void onFailed(BaseResponse msg) {
        mBookListView.hideProgress();
        if (msg == null) {
            return;
        }
        mBookListView.showMessage(msg.getMsg());
    }
}
