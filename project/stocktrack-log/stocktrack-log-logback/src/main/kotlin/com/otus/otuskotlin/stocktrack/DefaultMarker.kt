package com.otus.otuskotlin.stocktrack

import org.slf4j.Marker

data class DefaultMarker(
    private val name: String,
    private val subMarkers: List<Marker> = emptyList()
) : Marker {
    override fun getName(): String = name

    override fun add(reference: Marker) {}

    override fun remove(reference: Marker): Boolean = false

    @Deprecated("Deprecated in Java", ReplaceWith("hasReferences()"))
    override fun hasChildren(): Boolean = hasReferences()

    override fun hasReferences(): Boolean = subMarkers.isNotEmpty()

    override fun iterator(): Iterator<Marker> = subMarkers.iterator()

    override fun contains(other: Marker): Boolean = subMarkers.contains(other)

    override fun contains(name: String): Boolean = subMarkers.any { it.name == name }

    override fun toString(): String = arrayOf(name, *subMarkers.toTypedArray()).joinToString(",")
}
