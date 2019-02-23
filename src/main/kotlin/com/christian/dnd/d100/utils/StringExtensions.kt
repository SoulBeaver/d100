package com.christian.dnd.d100.utils

fun String.removePrefixAndSuffix(affix: CharSequence): String {
    return this.removePrefix(affix).removeSuffix(affix)
}

fun String.removeAllPrefixesAndSuffixesOf(chars: CharSequence): String {
    return chars.fold(this) { acc, char ->
        acc.removePrefixAndSuffix(char.toString())
    }
}