package uz.toshmatov.currency.core.utils

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {

    class Loading<T> : Resource<T>()

    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)

    class Empty<T> : Resource<T>()

    class Success<T>(data: T) : Resource<T>(data)
}

fun <T> success(data: T) = Resource.Success(data)

fun <T> empty() = Resource.Empty<T>()

fun <T> errorData(message: String?, data: T? = null) = Resource.Error(message, data)

fun <T> loading() = Resource.Loading<T>()
