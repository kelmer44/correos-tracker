package net.kelmer.correostracker.ui.activity

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import net.kelmer.correostracker.service.iap.InAppReviewService
import net.kelmer.correostracker.service.iap.InAppReviewServiceImpl

@Module
@InstallIn(ActivityComponent::class)
abstract class MainActivityModule {

    @Binds
    abstract fun bindsInAppReviewService(impl: InAppReviewServiceImpl): InAppReviewService
}
