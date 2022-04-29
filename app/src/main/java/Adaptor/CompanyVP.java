package Adaptor;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import Fragments.CompanyApplicationsFragment;
import Fragments.CompanyJobFragment;

public class CompanyVP extends FragmentStateAdapter {
    public CompanyVP(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new CompanyApplicationsFragment();
        }
        return new CompanyJobFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
