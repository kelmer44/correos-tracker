package net.kelmer.correostracker.list;

import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;

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
}
