package Adaptor;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import Fragments.JobListingFragment;
import Fragments.MyJobFragment;

public class ModelVP extends FragmentStateAdapter {
    public ModelVP(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new MyJobFragment();
        }
        return new JobListingFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
