package com.ahtat204.gitlab.presentation.components

fun <K, V> LinkedHashMap<K, V>.removeAfterKey(targetKey: K) {
    if(this.size<=1) return
    if (!this.containsKey(targetKey)) return // Key not found, do nothing

    var removeFlag = false
    val iterator = this.keys.iterator()

    while (iterator.hasNext()) {
        val key = iterator.next()
        if (removeFlag) {

            iterator.remove() // Safe removal during iteration

        }
        if (key == targetKey) {
            removeFlag = true
        }
    }
}