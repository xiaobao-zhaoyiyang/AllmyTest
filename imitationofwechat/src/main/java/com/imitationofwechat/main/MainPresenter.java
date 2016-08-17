package com.imitationofwechat.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import chats.ChatsFragment;
import chats.ChatsPresenter;
import contacts.ContactsFragment;
import contacts.ContactsPresenter;
import data.source.ChatsLoader;
import data.source.ChatsRepository;
import data.source.ContactsLoader;
import data.source.local.ChatsLocalDataSource;
import discover.DiscoverFragment;
import discover.DiscoverPresenter;
import me.MeFragment;
import me.MePresenter;
import util.CommonUtil;

/**
 * Created by lbf on 2016/7/29.
 */
public class MainPresenter implements MainContract.Presenter {
    private Context mContext;
    private MainContract.View mView;

    public MainPresenter(Context context,MainContract.View view) {
        mContext = context;
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mView.setPresenter(this);
    }

    @Override
    public Fragment getFragmentForPage(int page) {
        ChatsRepository repository = ChatsRepository.getInstance(null, ChatsLocalDataSource.getInstance(mContext));
        switch (page){
            case MainFragment.TAB_PAGE_CHATS:
                ChatsFragment chatsFragment = new ChatsFragment();
                ChatsLoader chatsLoader = new ChatsLoader(mContext,repository);
                ChatsPresenter chatsPresenter = new ChatsPresenter(
                        ((AppCompatActivity)mContext).getSupportLoaderManager(),
                        chatsLoader,
                        chatsFragment,
                        repository
                );
                return chatsFragment;
            case MainFragment.TAB_PAGE_CONTACTS:
                ContactsFragment contactsFragment = new ContactsFragment();
                ContactsLoader contactsLoader = new ContactsLoader(mContext,repository);
                ContactsPresenter contactsPresenter = new ContactsPresenter(
                        ((AppCompatActivity)mContext).getSupportLoaderManager(),
                        contactsLoader,
                        contactsFragment,
                        repository
                );
                return contactsFragment;
            case MainFragment.TAB_PAGE_DISCOVER:
                DiscoverFragment discoverFragment = new DiscoverFragment();
                DiscoverPresenter discoverPresenter = new DiscoverPresenter(discoverFragment);
                return discoverFragment;
            case MainFragment.TAB_PAGE_ME:
                MeFragment meFragment = new MeFragment();
                MePresenter mePresenter = new MePresenter(meFragment);
                return meFragment;

        }
        return new Fragment();
    }

    @Override
    public void start() {
    }
}
