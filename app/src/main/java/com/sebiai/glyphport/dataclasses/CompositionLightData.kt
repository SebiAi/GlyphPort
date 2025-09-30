package com.sebiai.glyphport.dataclasses

/**
 * @property lightData The raw light data. Outer list must contains all rows, inner list must
 * contain the elements in the row.
 *
 * @throws EmptyLightDataException If the light data is empty.
 * @throws InconsistentDataLength If rows in the light data have an inconsistent size.
 */
class CompositionLightData(
    lightData: List<List<UShort>>
): Iterable<List<UShort>> {
    open class InvalidMetadataException(message: String): Exception(message)
    class EmptyLightDataException: InvalidMetadataException("Light data is empty")
    class InconsistentDataLength: InvalidMetadataException("Different lengths for lines in light data")

    private val flatLightData: List<UShort>
    val columns: Int
    private val rows: Int
    init {
        if (lightData.isEmpty()) throw EmptyLightDataException()
        columns = lightData.first().size
        if (lightData.any { it.size != columns }) throw InconsistentDataLength()
        rows = lightData.size

        // Since it is an UShort it can't be less than 0 => only upper range check
        // Technically the highest value should be 4095 but some compositions
        // use 4096 as upper range??
        val upperRange: UShort = 4095u
        flatLightData = lightData.map { it.map { value -> minOf(value, upperRange) } }.flatten()
    }

    fun getRow(rowIndex: Int): List<UShort> {
        require(rowIndex >= 0) { "rowIndex must be 0 or greater" }
        if (rowIndex >= rows) throw IndexOutOfBoundsException()

        val fromIndex = rowIndex * columns
        return flatLightData.subList(fromIndex, fromIndex + columns)
    }

    fun getLightData(): List<List<UShort>> {
        assert(flatLightData.size % columns == 0)
        return flatLightData.chunked(columns)
    }

    override fun iterator(): Iterator<List<UShort>> {
        return object: Iterator<List<UShort>>{
            private var rowIndex = 0

            override fun next(): List<UShort> {
                val result = getRow(rowIndex)
                rowIndex++
                return result
            }

            override fun hasNext(): Boolean {
                return rowIndex < rows
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CompositionLightData

        if (columns != other.columns) return false
        if (rows != other.rows) return false
        if (flatLightData != other.flatLightData) return false

        return true
    }

    override fun hashCode(): Int {
        var result = columns
        result = 31 * result + rows
        result = 31 * result + flatLightData.hashCode()
        return result
    }


}