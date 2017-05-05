package com.market.aching.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.market.aching.R;
import com.market.aching.ui.base.BaseActivity;
import com.market.aching.ui.fragment.HomeFragment;
import com.market.aching.ui.fragment.ShoppingCarFragment;
import com.market.aching.ui.fragment.SettingFragment;

import butterknife.BindView;

public class MainActivity extends BaseActivity
{
//    @BindView(R.id.toolbar)
//    Toolbar mToolbar;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
//    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    private static final int EXIT_APP_DELAY = 1000;
    private long lastTime = 0;

    @Override
    protected int getLayoutId()
    {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
//        setSupportActionBar(mToolbar);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
//            log("onNavigationItemSelected " + item);
            switch (item.getItemId())
            {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_notifications:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_dashboard:
                    mViewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }

    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener()
    {
        private MenuItem prevMenuItem;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {

        }

        @Override
        public void onPageSelected(int position)
        {
//            log("onPageSelected " + position);
            if (prevMenuItem != null)
            {
                prevMenuItem.setChecked(false);
            } else
            {
                mNavigation.getMenu().getItem(0).setChecked(false);
            }
            mNavigation.getMenu().getItem(position).setChecked(true);
            prevMenuItem = mNavigation.getMenu().getItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state)
        {

        }
    };


    /**
     * A placeholder fragment containing a simple view.
     */
//    public static class PlaceholderFragment extends Fragment
//    {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        public PlaceholderFragment()
//        {
//        }
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber)
//        {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState)
//        {
//            View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
////            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//            return rootView;
//        }
//    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        private SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment = null;
            switch (position)
            {
                case 0:
                    fragment = HomeFragment.newInstance();
                    break;
                case 1:
                    fragment = ShoppingCarFragment.newInstance();
                    break;
                case 2:
                    fragment = SettingFragment.newInstance();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount()
        {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position)
            {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    @Override
    public void onBackPressed()
    {
        if ((System.currentTimeMillis() - lastTime) > EXIT_APP_DELAY)
        {
            Snackbar.make(mViewPager, getString(R.string.press_twice_exit), Snackbar.LENGTH_SHORT)
                    .setAction(R.string.exit_directly, v -> super.onBackPressed()).show();
            lastTime = System.currentTimeMillis();
        }
        else
        {
//            moveTaskToBack(true);
            super.onBackPressed();
        }
    }
}
