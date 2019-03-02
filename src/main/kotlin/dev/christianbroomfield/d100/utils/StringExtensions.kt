package dev.christianbroomfield.d100.utils

/**
 * A composition of the removePrefix and removeSuffix functions.
 */
fun String.removePrefixAndSuffix(affix: CharSequence): String {
    return this.removePrefix(affix).removeSuffix(affix)
}

/**
 * Removes all chars from the start and end of the string. May remove multiple characters from the start or end of the string.
 *
 * @chars list of chars, each of which will be removed from the prefix and suffix sequentially.
 */
fun String.removeAllPrefixesAndSuffixesOf(chars: CharSequence): String {
    return chars.fold(this) { acc, char ->
        acc.removePrefixAndSuffix(char.toString())
    }
}