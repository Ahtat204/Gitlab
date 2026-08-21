package com.ahtat204.gitlab.presentation.components

/**
 * Removes all entries in the [LinkedHashMap] that occur after the specified [targetKey].
 *
 * This function preserves the insertion order up to and including the target key,
 * while safely purging all subsequent elements. If the collection contains 1 or fewer
 * elements, or if the target key is not present, the map remains unchanged.
 *
 * ### Example:
 * ```kotlin
 * val map = linkedMapOf("A" to 1, "B" to 2, "C" to 3, "D" to 4)
 * map.removeAfterKey("B")
 * println(map) // Output: {A=1, B=2}
 * ```
 *
 * @param K The type of keys maintained by this map.
 * @param V The type of mapped values.
 * @param targetKey The key after which all subsequent map entries will be deleted.
 */
fun <K, V> LinkedHashMap<K, V>.removeAfterKey(targetKey: K) {
    if (this.size <= 1) return
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