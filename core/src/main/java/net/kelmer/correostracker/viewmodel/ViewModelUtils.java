package net.kelmer.correostracker.viewmodel;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AbstractSavedStateViewModelFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.lifecycle.ViewTreeViewModelStoreOwner;

import javax.inject.Provider;

/**
 * Utility methods for getting a {@link ViewModel} from a {@link FragmentActivity} or a
 * {@link Fragment}, reducing the amount of boilerplate required in dagger modules.
 */
@SuppressWarnings({"unchecked", "TypeParameterHidesVisibleType"})
public final class ViewModelUtils {

   /**
    * Get a {@link ViewModel} of a given type, scoped to the given {@link ViewModelStoreOwner}.
    *
    * @param viewModelStoreOwner Used to create a {@link ViewModelProvider}
    * @param viewModelClass      Class of the {@link ViewModel} implementation.
    * @param provider            Way to instantiate a new instances if none exists in the viewModelStoreOwner's
    *                            ViewModelStore
    * @param <T>                 Type of the {@link ViewModel} implementation.
    * @return A new or existing instance of the given {@link ViewModel} class.
    */
   public static <T extends ViewModel> T getViewModel(ViewModelStoreOwner viewModelStoreOwner,
                                                      Class<T> viewModelClass,
                                                      Provider<T> provider) {
      return new ViewModelProvider(viewModelStoreOwner, new ViewModelProvider.Factory() {
         @NonNull
         @Override
         public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) provider.get();
         }
      }).get(viewModelClass);
   }

   /**
    * Overload of getViewModel(ViewModelStoreOwner, Class, Provider) which also allows defining a viewModelKey to allow
    * obtaining multiple different instances of the same ViewModel from the same ViewModelStoreOwner.
    * <p/>
    * Convenience method for retrieving a {@link ViewModel} for a {@link View}. Since this uses
    * {@link ViewTreeViewModelStoreOwner} which, in most cases, return the {@link Fragment} or {@link FragmentActivity}
    * that the {@link View} is nested in, the additional viewModelKey parameter required in case there are multiple
    * views requesting the same ViewModel from the same Fragment.
    *
    * @param view           Used to create a {@link ViewModelProvider}
    * @param viewModelKey   Used to obtain specific instances of viewModelClass.
    * @param viewModelClass Class of the {@link ViewModel} implementation.
    * @param provider       Way to instantiate a new instances if none exists in the viewModelStoreOwner's
    *                       ViewModelStore
    * @param <T>            Type of the {@link ViewModel} implementation.
    * @return A new or existing instance of the given {@link ViewModel} class.
    */
   public static <T extends ViewModel> T getViewModel(@NonNull View view,
                                                      @NonNull String viewModelKey,
                                                      @NonNull Class<T> viewModelClass,
                                                      @NonNull Provider<T> provider) {
      ViewModelStoreOwner viewModelStoreOwner = ViewTreeViewModelStoreOwner.get(view);
      if (viewModelStoreOwner == null) {
         throw new IllegalStateException(
                 "ViewModels can only be provided for Views that have a ViewModelStoreOwner"
         );
      }
      return getViewModel(viewModelStoreOwner, viewModelKey, viewModelClass, provider);
   }

   /**
    * Overload of getViewModel(ViewModelStoreOwner, Class, Provider) which also allows defining a viewModelKey to allow
    * obtaining multiple different instances of the same ViewModel from the same ViewModelStoreOwner.
    *
    * @param viewModelStoreOwner Used to create a {@link ViewModelProvider}
    * @param viewModelKey        Used to obtain specific instances of viewModelClass.
    * @param viewModelClass      Class of the {@link ViewModel} implementation.
    * @param provider            Way to instantiate a new instances if none exists in the viewModelStoreOwner's
    *                            ViewModelStore
    * @param <T>                 Type of the {@link ViewModel} implementation.
    * @return A new or existing instance of the given {@link ViewModel} class.
    */
   public static <T extends ViewModel> T getViewModel(@NonNull ViewModelStoreOwner viewModelStoreOwner,
                                                      @NonNull String viewModelKey,
                                                      @NonNull Class<T> viewModelClass,
                                                      @NonNull Provider<T> provider) {
      return new ViewModelProvider(viewModelStoreOwner, new ViewModelProvider.Factory() {
         @NonNull
         @Override
         public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) provider.get();
         }
      }).get(viewModelKey, viewModelClass);
   }

   /**
    * Get a {@link ViewModel}of a given type, scoped to the given {@link Fragment}. The difference
    * between this and {@link #getViewModel(ViewModelStoreOwner, Class, Provider)} is that this method will
    * return a {@code ViewModel} that utilizes the {@link SavedStateHandle}.
    *
    * @param fragment       Used to create a {@link ViewModelProvider}
    * @param viewModelClass Class of the {@link ViewModel} implementation.
    * @param defaultArgs    A nullable {@link Bundle} that pass in specific default arguments
    *                       to the ViewModel.
    * @param provider       Way to instantiate a new instances if none exists in the fragment's
    *                       ViewModelStore. See {@link SavedStateViewModelProvider}.
    * @param <T>            Type of the {@link ViewModel} implementation.
    * @return A new or existing instance of the given {@link ViewModel} class that can utilize
    *     a {@link SavedStateHandle}.
    */
   public static <T extends ViewModel> T getSavedStateViewModel(
           Fragment fragment,
           Class<T> viewModelClass,
           @Nullable Bundle defaultArgs,
           SavedStateViewModelProvider<T> provider
   ) {
      return new ViewModelProvider(fragment, new AbstractSavedStateViewModelFactory(fragment, defaultArgs) {
         @NonNull
         @Override
         protected <T extends ViewModel> T create(
                 @NonNull String key, @NonNull Class<T> modelClass, @NonNull SavedStateHandle handle
         ) {
            return (T) provider.get(handle);
         }
      }).get(viewModelClass);
   }


   /**
    * Get a {@link ViewModel}of a given type, scoped to the given {@link Fragment}. The difference
    * between this and {@link #getViewModel(ViewModelStoreOwner, Class, Provider)} is that this method will
    * return a {@code ViewModel} that utilizes the {@link SavedStateHandle}.
    *
    * @param activity       Used to create a {@link ViewModelProvider}
    * @param viewModelClass Class of the {@link ViewModel} implementation.
    * @param defaultArgs    A nullable {@link Bundle} that pass in specific default arguments
    *                       to the ViewModel.
    * @param provider       Way to instantiate a new instances if none exists in the fragment's
    *                       ViewModelStore. See {@link SavedStateViewModelProvider}.
    * @param <T>            Type of the {@link ViewModel} implementation.
    * @return A new or existing instance of the given {@link ViewModel} class that can utilize
    *     a {@link SavedStateHandle}.
    */
   public static <T extends ViewModel> T getSavedStateViewModel(
           FragmentActivity activity,
           Class<T> viewModelClass,
           @Nullable Bundle defaultArgs,
           SavedStateViewModelProvider<T> provider
   ) {
      ViewModelProvider.Factory factory
              = new AbstractSavedStateViewModelFactory(activity, defaultArgs) {
         @NonNull
         @Override
         protected <T extends ViewModel> T create(
                 @NonNull String key, @NonNull Class<T> modelClass, @NonNull SavedStateHandle handle
         ) {
            return (T) provider.get(handle);
         }
      };
      return new ViewModelProvider(activity, factory).get(viewModelClass);
   }

   private ViewModelUtils() {
   }

   /**
    * An interface that is used to offer a {@link SavedStateHandle} for the ViewModel.
    *
    * @param <T> The {@link ViewModel} that is to be returned.
    */
   public interface SavedStateViewModelProvider<T extends ViewModel> {
      /**
       * Returns an instance of a {@link ViewModel}, giving it the passed in
       * {@link SavedStateHandle}.
       *
       * @param handle The {@link SavedStateHandle} used to save state across process and
       *               configuration changes.
       * @return An implementation of {@link ViewModel}.
       */
      T get(SavedStateHandle handle);
   }

}
