package com.market.aching.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.market.aching.R;
import com.market.aching.ui.base.BaseActivity;

import butterknife.BindView;
import me.wangyuwei.particleview.ParticleView;


/**
 * Created by Administrator on 2016/11/5.
 */

public class SplashActivity extends BaseActivity
{
    @BindView(R.id.pv_logo)
    ParticleView particleView;

    @Override
    protected int getLayoutId()
    {
        return R.layout.activity_splash;
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        particleView.setOnParticleAnimListener(new ParticleView.ParticleAnimListener()
        {
            @Override
            public void onAnimationEnd()
            {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        particleView.startAnim();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (null != particleView)
        {
            particleView = null;
        }
    }
}
