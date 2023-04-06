package net.kelmer.correostracker.list;

import android.app.UiModeManager;

import androidx.fragment.app.Fragment;

import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.FragmentComponent;

@Module
@InstallIn(FragmentComponent.class)
abstract class ParcelList_FragmentModule
{
    @Provides
    static GroupAdapter<GroupieViewHolder> providesGroupieAdapter() {
        return new GroupAdapter<GroupieViewHolder>();
    }

    @Singleton
    @Provides
    static String providesString() { return "!!##$@$"; }
}
