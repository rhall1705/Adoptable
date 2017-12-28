package personal.rowan.petfinder.application

/**
 * Created by Rowan Hall
 */
class Resource<T>(val progress: Boolean = false, val paginate: Boolean = false, private val resourceData: T? = null, private val error: Throwable? = null) {

    fun hasData(): Boolean {
        return resourceData != null
    }

    fun data(): T {
        return resourceData!!
    }

    fun hasError(): Boolean {
        return error != null
    }

    fun error(): Throwable {
        return error!!
    }

    companion object {
        fun <T> starting(): Resource<T> {
            return Resource(false, false, null, null)
        }

        fun <T> progress(resource: Resource<T>, paginate: Boolean): Resource<T> {
            return Resource(true, paginate, resource.resourceData, resource.error)
        }

        fun <T> success(resourceData: T): Resource<T> {
            return Resource(false, false, resourceData, null)
        }

        fun <T> failure(resource: Resource<T>, e: Throwable): Resource<T> {
            return Resource(false, false, resource.resourceData, e)
        }
    }

}