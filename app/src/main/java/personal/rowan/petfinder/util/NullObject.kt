package personal.rowan.petfinder.util

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * Created by Rowan Hall
 */

object NullObject {

    fun <T> create(clazz: Class<T>): T {
        val localClassLoader = clazz.classLoader
        val localNullInvocationHandler = NullInvocationHandler()
        return clazz.cast(Proxy.newProxyInstance(localClassLoader, arrayOf<Class<*>>(clazz), localNullInvocationHandler))
    }

    private class NullInvocationHandler : InvocationHandler {
        @Throws(Throwable::class)
        override fun invoke(`object`: Any, method: Method, args: Array<Any>): Any? {
            return null
        }
    }

}
