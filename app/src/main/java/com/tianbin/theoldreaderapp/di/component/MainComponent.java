package com.tianbin.theoldreaderapp.di.component;

import com.tianbin.theoldreaderapp.PerActivity;
import com.tianbin.theoldreaderapp.data.api.BlogApi;
import com.tianbin.theoldreaderapp.data.api.SubscriptionApi;
import com.tianbin.theoldreaderapp.di.module.ActivityModule;
import com.tianbin.theoldreaderapp.di.module.BlogModule;
import com.tianbin.theoldreaderapp.di.module.SubscriptionModule;
import com.tianbin.theoldreaderapp.ui.module.blog.NewsFragment;
import com.tianbin.theoldreaderapp.ui.module.subscription.SubscriptionsFragment;

import dagger.Component;

/**
 * class des
 * Created by tianbin on 16/11/11.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class
        , modules = {ActivityModule.class, SubscriptionModule.class, BlogModule.class})
public interface MainComponent {

    void inject(SubscriptionApi subscriptionApi);

    void inject(BlogApi blogApi);

    void inject(SubscriptionsFragment subscriptionsFragment);

    void inject(NewsFragment newsFragment);
}
