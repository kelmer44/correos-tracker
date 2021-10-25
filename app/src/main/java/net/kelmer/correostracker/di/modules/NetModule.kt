package net.kelmer.correostracker.di.modules

import android.app.Application
import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.kelmer.correostracker.R
import net.kelmer.correostracker.di.qualifiers.ForApplication
import net.kelmer.correostracker.di.qualifiers.NetworkLogger
import net.kelmer.correostracker.util.adapter.CorreosApiParcelAdapter
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * Created by gabriel on 25/03/2018.
 */
@Module(
    includes = [Interceptors::class]
)
@InstallIn(SingletonComponent::class)
open class NetModule {

    @Provides
    @Singleton
    fun provideTrustManagerFactory(@ForApplication context: Context): TrustManagerFactory {
        // Load CAs from an InputStream
        // (could be from a resource or ByteArrayInputStream or ...)
        val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
        // From https://www.washington.edu/itconnect/security/ca/load-der.crt
        val caInput: InputStream = context.resources.openRawResource(R.raw.entrust)
        val ca: X509Certificate = caInput.use {
            cf.generateCertificate(it) as X509Certificate
        }
        System.out.println("ca=" + ca.subjectDN)

        // Create a KeyStore containing our trusted CAs
        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType).apply {
            load(null, null)
            setCertificateEntry("ca", ca)
        }

        // Create a TrustManager that trusts the CAs inputStream our KeyStore
        val tmfAlgorithm: String = TrustManagerFactory.getDefaultAlgorithm()
        val tmf: TrustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm).apply {
            init(keyStore)
        }
        return tmf
    }

    @Provides
    @Singleton
    fun provideSSLContext(tmf: TrustManagerFactory): SSLContext {
        // Create an SSLContext that uses our TrustManager
        val context: SSLContext = SSLContext.getInstance("TLS").apply {
            init(null, tmf.trustManagers, null)
        }
        return context
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        cache: Cache,
        sslContext: SSLContext,
        tmf: TrustManagerFactory,
        @NetworkLogger loggingInterceptors: Set<@JvmSuppressWildcards
            Interceptor>
    ): OkHttpClient {

        val builder = OkHttpClient.Builder()
            .cache(cache)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .apply {
                loggingInterceptors.forEach {
                    addNetworkInterceptor(it)
                }
            }
        val trustManagers = tmf.trustManagers
        if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
            return builder.build()
        } else {
            val trustManager = trustManagers[0] as (X509TrustManager)
            return builder
                .sslSocketFactory(sslContext.socketFactory, trustManager)
                .build()
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        rxJavaCallAdapterFactory: RxJava2CallAdapterFactory,
        gsonConverterFactory: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://localizador.correos.es/canonico/")
            .addCallAdapterFactory(rxJavaCallAdapterFactory)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            //            .add(SingleToArrayAdapter.FACTORY)
            .add(CorreosApiParcelAdapter.FACTORY)
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .build()

    @Provides
    @Singleton
    fun provideRxJavaCallAdapter(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(moshi: Moshi): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    @Singleton
    fun provideOkHttpCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024L
        return Cache(application.getCacheDir(), cacheSize)
    }
}
