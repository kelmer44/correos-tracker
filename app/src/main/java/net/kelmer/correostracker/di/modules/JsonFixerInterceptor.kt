package net.kelmer.correostracker.di.modules

import okhttp3.Interceptor
import okhttp3.Response

class JsonFixerInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {


        val proceed = chain.proceed(chain.request())
        val string = proceed.body?.string()




        return proceed

    }
}