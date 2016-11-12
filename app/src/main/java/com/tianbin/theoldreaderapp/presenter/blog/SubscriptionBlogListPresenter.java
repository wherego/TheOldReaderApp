package com.tianbin.theoldreaderapp.presenter.blog;

import com.tianbin.theoldreaderapp.common.wrapper.AppLog;
import com.tianbin.theoldreaderapp.contract.blog.BlogListContract;
import com.tianbin.theoldreaderapp.data.api.BlogApi;
import com.tianbin.theoldreaderapp.data.module.BlogIdItemList;
import com.tianbin.theoldreaderapp.data.module.BlogList;
import com.tianbin.theoldreaderapp.data.rx.ResponseObserver;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * class des
 * Created by tianbin on 16/11/12.
 */

public class SubscriptionBlogListPresenter extends BlogListBasePresenter implements BlogListContract.Presenter {

    private String mSubscriptionId;

    @Inject
    public SubscriptionBlogListPresenter(BlogApi blogApi) {
        mBlogApi = blogApi;
    }

    @Override
    public void fetchBlogs(final BlogListContract.FetchType type) {
        mBlogApi.getSubscriptionBlogIds(mSubscriptionId)
                .subscribeOn(Schedulers.io())
                .map(new Func1<BlogIdItemList, List<String>>() {
                    @Override
                    public List<String> call(BlogIdItemList blogIdItemList) {
                        return getBlogIdList(blogIdItemList);
                    }
                })
                .flatMap(new Func1<List<String>, Observable<BlogList>>() {
                    @Override
                    public Observable<BlogList> call(List<String> idList) {
                        return mBlogApi.getUnReadContents(idList);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<BlogList>() {
                    @Override
                    public void onSuccess(BlogList blogList) {
                        AppLog.d("fetch unread blog success");

                        mContinuation = blogList.getContinuation();
                        switch (type) {
                            case INIT:
                                mView.fetchNewsSuccess(blogList.getItems());
                                break;
                            case REFRESH:
                                appendNewData(blogList);
                                mView.pullDownRefreshSuccess();
                                break;
                            case LOAD_MORE:
                                AppLog.e("continuation --- " + mContinuation);
                                if (mContinuation != 0) {
                                    mView.loadMoreNewsSuccess(blogList.getItems());
                                } else {
                                    mView.loadMoreNewsCompleted();
                                }
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.d(e.toString());
                        mView.fetchNewsFailed(e);
                    }
                });
    }

    @Override
    public void attachView(BlogListContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    public void setSubscriptionId(String subscriptionId) {
        mSubscriptionId = subscriptionId;
    }
}
