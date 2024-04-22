package uz.toshmatov.currency.core.logger

import timber.log.Timber

/**
 * Logs an info message using the provided lambda expression as the message.
 *
 * @param content lambda that returns the string message to log
 */
inline fun logInfo(content: () -> String) {
    Timber.i(content())
}

/**
 * Logs a debug message using the provided lambda expression as the message.
 *
 * @param content lambda that returns the string message to log
 */
inline fun logDebug(content: () -> String) {
    Timber.d(content())
}

/**
 * Logs a warning message using the provided lambda expression as the message.
 *
 * @param content lambda that returns the string message to log
 */
inline fun logWarning(content: () -> String) {
    Timber.w(content())
}

/**
 * Logs an error message using the provided lambda expression as the message.
 *
 * @param content lambda that returns the string message to log
 */
inline fun logError(content: () -> String) {
    Timber.e(content())
}
