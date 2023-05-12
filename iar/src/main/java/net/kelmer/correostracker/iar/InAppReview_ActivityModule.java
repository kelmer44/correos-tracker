package net.kelmer.correostracker.iar;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

@Module
@InstallIn(ActivityComponent.class)
abstract class InAppReview_ActivityModule {
    @Binds
    abstract InAppReviewService bindsInAppReview(InAppReviewServiceImpl impl);
}
