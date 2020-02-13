package xyz.webflutter.moviecatalogue.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import xyz.webflutter.moviecatalogue.fragments.*;

public class FragmentPageAdapter extends FragmentPagerAdapter {
    private int listTab;

    public FragmentPageAdapter(FragmentManager fm, int listTab) {
        super(fm);
        this.listTab = listTab;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FavFragment();
            case 1:
                return new FavTvShowFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return listTab;
    }
}
