package net.kelmer.correostracker.list.featuredialog

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import net.kelmer.correostracker.list.R
import net.kelmer.correostracker.list.databinding.RvFeatureBinding

class FeatureListItem constructor(
    private val feature: Feature,
) : BindableItem<RvFeatureBinding>() {
    override fun bind(viewBinding: RvFeatureBinding, position: Int) {
        viewBinding.featureText.text = viewBinding.featureText.context.getString(feature.text)
        viewBinding.featureVersion.text = feature.version
    }

    override fun getLayout(): Int = R.layout.rv_feature

    override fun initializeViewBinding(view: View): RvFeatureBinding = RvFeatureBinding.bind(view)
}
