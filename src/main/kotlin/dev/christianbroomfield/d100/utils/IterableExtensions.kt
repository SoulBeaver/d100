package dev.christianbroomfield.d100.utils

fun <T> Iterable<T>.takeWhileUpToMax(max: Int, predicate: (T) -> Boolean): List<T> {
    var elementsTaken = 0
    return takeWhile { entry -> predicate(entry) && ++elementsTaken <= max }
}