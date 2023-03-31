package net.kelmer.correostracker.detail;

import androidx.fragment.app.Fragment;

import net.kelmer.correostracker.data.repository.correos.CorreosRepository;
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository;
import net.kelmer.correostracker.util.SchedulerProvider;
import net.kelmer.correostracker.viewmodel.ViewModelUtils;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.FragmentComponent;

@Module
@InstallIn(FragmentComponent.class)
abstract class Detail_FragmentModule {

   @Provides
   static ParcelDetailViewModel provideViewModel(Fragment fragment,
                                                 LocalParcelRepository localParcelRepository,
                                                 CorreosRepository correosRepository,
                                                 SchedulerProvider schedulerProvider) {

      String parcelCode = ((DetailFragment)fragment).getParcelCode();
      return ViewModelUtils.getViewModel(
              fragment,
              ParcelDetailViewModel.class,
              () -> new ParcelDetailViewModel(parcelCode, localParcelRepository, correosRepository, schedulerProvider)
      );
   }

}
