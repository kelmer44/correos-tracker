//package net.kelmer.correostracker.util.ext
//
//import android.app.Activity
//import android.os.Handler
//import android.os.Looper
//import android.view.ViewGroup
//import androidx.annotation.MainThread
//import androidx.core.view.children
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.DefaultLifecycleObserver
//import androidx.lifecycle.LifecycleOwner
//import androidx.viewbinding.ViewBinding
//import com.woovapp.utils.checkIsMainThread
//import kotlin.properties.ReadOnlyProperty
//import kotlin.reflect.KProperty
//import kotlin.reflect.full.functions
//
//@MainThread
//inline fun <reified T : ViewBinding> Fragment.viewBinding() =
//        object : ReadOnlyProperty<Fragment, T> {
//            private var binding: T? = null
//
//            private val handler = Handler(Looper.getMainLooper())
//
//            init {
//                checkIsMainThread()
//                viewLifecycleOwnerLiveData.observe(
//                        this@viewBinding,
//                        {
//                            it.lifecycle.addObserver(object : DefaultLifecycleObserver {
//                                override fun onDestroy(owner: LifecycleOwner) {
//                                    super.onDestroy(owner)
//                                    // So on the onDestroyView of the fragment you can still use the binding, otherwise you'll get an exception
//                                    handler.post { binding = null }
//                                }
//                            })
//                        }
//                )
//            }
//
//            override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
//                checkIsMainThread()
//                binding?.let { return it }
//                val method = T::class.functions.find { it.name == "bind" }
//                        ?: throw IllegalStateException("Couldn't find the bind method of this view binding class")
//                return (method.call(requireView()) as T).also { binding = it }
//            }
//        }
//
//@MainThread
//inline fun <reified T : ViewBinding> Activity.viewBinding() =
//        object : ReadOnlyProperty<Activity, T> {
//            private lateinit var binding: T
//
//            init {
//                checkIsMainThread()
//            }
//
//            override fun getValue(thisRef: Activity, property: KProperty<*>): T {
//                checkIsMainThread()
//                if (!::binding.isInitialized) {
//                    // We are using the bind method instead of the inflate method becuase for that we need
//                    // our activities to not declare the xml in the constructor since the inflate method
//                    // is used together with setContentView
//                    val method = T::class.functions.find { it.name == "bind" }
//                            ?: throw IllegalStateException("Couldn't find the bind method of this view binding class")
//                    binding = method.call(findViewById<ViewGroup>(android.R.id.content)?.children?.firstOrNull()) as T
//                }
//                return binding
//            }
//        }
