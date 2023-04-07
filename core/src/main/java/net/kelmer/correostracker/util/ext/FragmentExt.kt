@file:Suppress("TooManyFunctions")

package com.bamtechmedia.dominguez.core.utils

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.LifecycleOwner
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDisposable
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.Serializable
import java.util.concurrent.TimeUnit
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


/**
 * Syntactic sugar for [ParcelableFragmentArgumentDelegate]
 *
 * You can use the by defining `val someParcelable by parcelableArgument<MyParcelable>("someKey")` in any [Fragment].
 */
fun <T : Parcelable> parcelableArgument(key: String, defaultValueProvider: (() -> T)? = null) =
    ParcelableFragmentArgumentDelegate(key, defaultValueProvider)

/**
 * Syntactic sugar for [SerializableFragmentArgumentDelegate]
 *
 * You can use the by defining `val someSerializable by serializableArgument<MySerializable>("someKey")`
 * in any [Fragment].
 */
fun <T : Serializable> serializableArgument(key: String, defaultValueProvider: (() -> T)? = null) =
    SerializableFragmentArgumentDelegate(key, defaultValueProvider)

/**
 * Syntactic sugar for [ParcelableArrayListFragmentArgumentDelegate]
 *
 * You can use the by defining `val someParcelableList by parcelableArrayListArgument<MyParcelable>("someKey")` in any
 * [Fragment].
 */
fun <T : Parcelable> parcelableArrayListArgument(key: String, defaultValueProvider: (() -> List<T>)? = null) =
    ParcelableArrayListFragmentArgumentDelegate(key, defaultValueProvider)

/**
 * Syntactic sugar for [ParcelableFragmentArgumentDelegate]
 *
 * You can use the by defining `val someParcelable by parcelableArgument<MyParcelable>("someKey")` in any [Fragment].
 */
fun <T : Parcelable> optionalParcelableArgument(key: String, defaultValueProvider: (() -> T)? = null) =
    OptionalParcelableFragmentArgumentDelegate(key, defaultValueProvider)

/**
 * Syntactic sugar for [StringFragmentArgumentDelegate] which gives the possibility to read a string argument using
 * [android.os.Bundle.getString]
 *
 * You can use the by defining `val someString by stringArgument("someKey")` in any [Fragment].
 */
fun stringArgument(stringKey: String, defaultValue: String? = null) =
    StringFragmentArgumentDelegate(stringKey, defaultValue)

/**
 * Syntactic sugar for [StringArrayFragmentArgumentDelegate].
 *
 * You can use the by defining `val stringArray by stringArrayArgument("someKey")` in any [Fragment].
 */
fun stringArrayArgument(stringKey: String) = StringArrayFragmentArgumentDelegate(stringKey)

/**
 * Syntactic sugar for [StringArrayListFragmentArgumentDelegate].
 *
 * You can use the by defining `val stringArrayList by stringArrayListArgument("someKey")` in any [Fragment].
 */
fun stringArrayListArgument(stringKey: String) = StringArrayListFragmentArgumentDelegate(stringKey)

/**
 * Syntactic sugar for [StringArrayListFragmentArgumentDelegate].
 *
 * You can use the by defining `val stringArrayList by stringArrayListArgument("someKey")` in any [Fragment].
 */
fun optionalStringArrayListArgument(stringKey: String) = OptionalStringArrayListFragmentArgumentDelegate(stringKey)

/**
 * Syntactic sugar for [OptionalStringFragmentArgumentDelegate] which gives the possibility to read an optional string
 * argument using [android.os.Bundle.getString]
 *
 * You can use the by defining `val someString by optionalStringArgument("someKey")` in any [Fragment].
 */
fun optionalStringArgument(stringKey: String, defaultValue: String? = null) =
    OptionalStringFragmentArgumentDelegate(stringKey, defaultValue)

/**
 * Syntactic sugar for [IntFragmentArgumentDelegate].
 *
 * You can use the by defining `val someInt by intArgument("someKey")` in any [Fragment].
 */
fun intArgument(key: String, defaultValue: Int? = null) =
    IntFragmentArgumentDelegate(key, defaultValue)

/**
 * Syntactic sugar for [BooleanFragmentArgumentDelegate].
 *
 * You can use the by defining `val someBoolean by booleanArgument("someKey")` in any [Fragment].
 */
fun booleanArgument(key: String, defaultValue: Boolean? = null) =
    BooleanFragmentArgumentDelegate(key, defaultValue)

/**
 * [ReadOnlyProperty] for fetching a [Parcelable] of specified [key] from [Fragment.getArguments].
 */
class ParcelableFragmentArgumentDelegate<T : Parcelable>(
    private val key: String,
    private val defaultValueProvider: (() -> T)?
) : ReadOnlyProperty<Fragment, T> {
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
        thisRef.arguments?.getParcelable(key)
            ?: defaultValueProvider?.invoke()
            ?: throw IllegalArgumentException("'$key' must be specified")
}

/**
 * [ReadOnlyProperty] for fetching a list of [Parcelable]s of specified [key] from [Fragment.getArguments].
 */
class ParcelableArrayListFragmentArgumentDelegate<T : Parcelable>(
    private val key: String,
    private val defaultValueProvider: (() -> List<T>)?
) : ReadOnlyProperty<Fragment, List<T>> {
    override fun getValue(thisRef: Fragment, property: KProperty<*>): List<T> =
        thisRef.arguments?.getParcelableArrayList(key)
            ?: defaultValueProvider?.invoke()
            ?: throw IllegalArgumentException("'$key' must be specified")
}

/**
 * [ReadOnlyProperty] for fetching a [Parcelable] of specified [key] from [Fragment.getArguments] returns null if it
 * doesn't exist.
 */
class OptionalParcelableFragmentArgumentDelegate<T : Parcelable>(
    private val key: String,
    private val defaultValueProvider: (() -> T)?
) : ReadOnlyProperty<Fragment, T?> {
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T? =
        thisRef.arguments?.getParcelable(key)
            ?: defaultValueProvider?.invoke()
}

/**
 * [ReadOnlyProperty] for fetching a [String] of specified [stringKey] from [Fragment.getArguments].
 * This will first try to get a String using [stringKey] and [android.os.Bundle.getString] and return [defaultValue] if
 * that key doesn't exist.
 */
class StringFragmentArgumentDelegate(
    private val stringKey: String?,
    private val defaultValue: String?
) : ReadOnlyProperty<Fragment, String> {
    override fun getValue(thisRef: Fragment, property: KProperty<*>): String =
        thisRef.arguments?.getString(stringKey)
            ?: defaultValue
            ?: throw IllegalArgumentException("'$stringKey' must be specified")
}

/**
 * [ReadOnlyProperty] for fetching an [String] [Array] of specified [key] from [Fragment.getArguments].
 */
class StringArrayFragmentArgumentDelegate(
    private val key: String?
) : ReadOnlyProperty<Fragment, Array<String>> {
    override fun getValue(thisRef: Fragment, property: KProperty<*>): Array<String> =
        thisRef.arguments?.getStringArray(key)
            ?: throw IllegalArgumentException("'$key' must be specified")
}

/**
 * [ReadOnlyProperty] for fetching an [String] [ArrayList] of specified [key] from [Fragment.getArguments].
 */
class StringArrayListFragmentArgumentDelegate(
    private val key: String?
) : ReadOnlyProperty<Fragment, ArrayList<String>> {
    override fun getValue(thisRef: Fragment, property: KProperty<*>): ArrayList<String> =
        thisRef.arguments?.getStringArrayList(key)
            ?: throw IllegalArgumentException("'$key' must be specified")
}

/**
 * [ReadOnlyProperty] for fetching an [String] [ArrayList]? of specified [key] from [Fragment.getArguments].
 */
class OptionalStringArrayListFragmentArgumentDelegate(
    private val key: String?
) : ReadOnlyProperty<Fragment, ArrayList<String>?> {
    override fun getValue(thisRef: Fragment, property: KProperty<*>): ArrayList<String>? =
        thisRef.arguments?.getStringArrayList(key)
}

/**
 * Same as [StringFragmentArgumentDelegate] only possible to be null
 */
class OptionalStringFragmentArgumentDelegate(
    private val stringKey: String?,
    private val defaultValue: String?
) : ReadOnlyProperty<Fragment, String?> {
    override fun getValue(thisRef: Fragment, property: KProperty<*>): String? =
        thisRef.arguments?.getString(stringKey) ?: defaultValue
}

/**
 * [ReadOnlyProperty] for fetching an [Int] of specified [key] from [Fragment.getArguments].
 */
class IntFragmentArgumentDelegate(
    private val key: String?,
    private val defaultValue: Int?
) : ReadOnlyProperty<Fragment, Int> {
    override fun getValue(thisRef: Fragment, property: KProperty<*>): Int =
        thisRef.arguments?.getInt(key)
            ?: defaultValue
            ?: throw IllegalArgumentException("'$key' must be specified")
}

/**
 * [ReadOnlyProperty] for fetching an [Boolean] of specified [key] from [Fragment.getArguments].
 */
class BooleanFragmentArgumentDelegate(
    private val key: String?,
    private val defaultValue: Boolean?
) : ReadOnlyProperty<Fragment, Boolean> {
    override fun getValue(thisRef: Fragment, property: KProperty<*>): Boolean =
        thisRef.arguments?.getBoolean(key)
            ?: defaultValue
            ?: throw IllegalArgumentException("'$key' must be specified")
}

/**
 * [ReadOnlyProperty] for fetching a [Serializable] of specified [key] from [Fragment.getArguments].
 */
class SerializableFragmentArgumentDelegate<T : Serializable>(
    private val key: String,
    private val defaultValueProvider: (() -> T)?
) : ReadOnlyProperty<Fragment, T> {
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
        (thisRef.arguments?.getSerializable(key) as? T)
            ?: defaultValueProvider?.invoke()
            ?: throw IllegalArgumentException("'$key' must be specified")
}
