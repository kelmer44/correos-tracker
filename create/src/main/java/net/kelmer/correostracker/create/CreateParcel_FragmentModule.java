package net.kelmer.correostracker.create;

import androidx.fragment.app.Fragment;

import net.kelmer.correostracker.dataApi.repository.local.LocalParcelRepository;
import net.kelmer.correostracker.util.SchedulerProvider;
import net.kelmer.correostracker.viewmodel.ViewModelUtils;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.FragmentComponent;

@Module
@InstallIn(FragmentComponent.class)
abstract class CreateParcel_FragmentModule {

    @Provides
    static CreateParcelViewModel provideViewModel(Fragment fragment,
                                                  LocalParcelRepository localParcelRepository,
                                                  SchedulerProvider schedulerProvider
    ) {
        return ViewModelUtils.getViewModel(
                fragment,
                CreateParcelViewModel.class,
                () -> new CreateParcelViewModel(localParcelRepository, schedulerProvider)
        );
    }

}
