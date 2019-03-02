package dev.christianbroomfield.d100.utils

/**
 * Take until either the condition returns false or the maximum number of elements has been taken.
 *
 * @param max how many elements to take
 * @param predicate condition to continue taking the next element
 * @return a list of elements
 */
fun <T> Iterable<T>.takeWhileUpToMax(max: Int, predicate: (T) -> Boolean): List<T> {
    var elementsTaken = 0
    return takeWhile { entry -> predicate(entry) && ++elementsTaken <= max }
}