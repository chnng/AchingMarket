package com.market.aching.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.market.aching.R;
import com.market.aching.util.ScreenManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/11/5.
 */

public abstract class BaseActivity extends AppCompatActivity
{
    private Unbinder unbinder;
    public static BaseActivity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(getLayoutId());
        ScreenManager.getScreenManager().pushActivity(this);
        unbinder = ButterKnife.bind(this);
        //初始化控件
        initViews(savedInstanceState);
        //初始化ToolBar
//        initToolBar();
    }

    protected abstract int getLayoutId();

    protected abstract void initViews(Bundle savedInstanceState);

//    protected abstract void initToolBar();

//    /**
//     * 菜单按钮初始化
//     *
//     * @param menu
//     * @return
//     */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_empty, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        switch (item.getItemId())
//        {
//            case android.R.id.home:
//                this.finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    protected void onResume()
    {
        super.onResume();
        activity = this;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        activity = null;
    }

    @Override
    protected void onDestroy()
    {
        unbinder.unbind();
        super.onDestroy();
    }
}
