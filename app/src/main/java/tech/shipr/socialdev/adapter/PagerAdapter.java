package tech.shipr.socialdev.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import tech.shipr.socialdev.view.ChatChannel;
import tech.shipr.socialdev.view.ProfileFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new ChatChannel();
            case 1:
                return new ProfileFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
