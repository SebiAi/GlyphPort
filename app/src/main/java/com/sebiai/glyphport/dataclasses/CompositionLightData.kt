package com.sebiai.glyphport.dataclasses

import kotlin.math.min

/**
 * @property lightData The raw light data. Outer list must contains all rows, inner list must
 * contain the elements in the row.
 *
 * @throws EmptyLightDataException If the light data is empty.
 * @throws InconsistentDataLength If rows in the light data have an inconsistent size.
 */
class CompositionLightData(
    lightData: List<List<UInt>>
): Iterable<List<UInt>> {
    open class InvalidMetadataException(message: String): Exception(message)
    class EmptyLightDataException: InvalidMetadataException("Light data is empty")
    class InconsistentDataLength: InvalidMetadataException("Different lengths for lines in light data")

    private val flatLightData: List<UInt>
    val columns: Int
    private val rows: Int
    init {
        if (lightData.isEmpty()) throw EmptyLightDataException()
        columns = lightData.first().size
        if (lightData.any { it.size != columns }) throw InconsistentDataLength()
        rows = lightData.size

        // Since it is an UInt it can't be less than 0 => only upper range check
        // Technically the highest value should be 4095 but some compositions
        // use 4096 as upper range??
        flatLightData = lightData.map { it.map { value -> min(value, 4095u) } }.flatten()
    }

    fun getRow(rowIndex: Int): List<UInt> {
        require(rowIndex >= 0) { "rowIndex must be 0 or greater" }
        if (rowIndex >= rows) throw IndexOutOfBoundsException()

        val fromIndex = rowIndex * rowIndex
        return flatLightData.subList(fromIndex, fromIndex + columns)
    }

    fun getLightData(): List<List<UInt>> {
        assert(flatLightData.size % columns == 0)
        return flatLightData.chunked(columns)
    }

    override fun iterator(): Iterator<List<UInt>> {
        return object: Iterator<List<UInt>>{
            private var rowIndex = 0

            override fun next(): List<UInt> {
                val result = getRow(rowIndex)
                rowIndex++
                return result
            }

            override fun hasNext(): Boolean {
                return rowIndex < rows
            }
        }
    }
}