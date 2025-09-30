package com.sebiai.glyphport.dataclasses

/**
 * @property lightData The raw light data. Outer list must contains all rows, inner list must
 * contain the elements in the row.
 *
 * @throws EmptyLightDataException If the light data is empty.
 * @throws InconsistentDataLength If rows in the light data have an inconsistent size.
 */
class CompositionLightData(
    lightData: ShortArray,
    val columns: Int
): Iterable<ShortArray> {
    constructor(lightData: List<Short>, columns: Int) :
            this(lightData = lightData.toShortArray(), columns = columns)
    constructor(lightData: List<List<Short>>) :
            this(lightData = lightData.flatten().toShortArray(),
                columns = lightData.firstOrNull()?.size ?: 0) {
                // Still checking here because if one row is one longer and one is one shorter
                // init won't throw
                if (lightData.any { it.size != columns }) throw InconsistentDataLength()
    }

    open class InvalidMetadataException(message: String): Exception(message)
    class EmptyLightDataException: InvalidMetadataException("Light data is empty")
    class InconsistentDataLength: InvalidMetadataException("Different lengths for lines in light data")

    private val flatLightData: ShortArray
    val rows: Int

    init {
        if (lightData.isEmpty() || columns <= 0) throw EmptyLightDataException()
        if (lightData.size % columns != 0) throw InconsistentDataLength()
        rows = lightData.size / columns

        // Since it is an UShort it can't be less than 0 => only upper range check
        // Technically the highest value should be 4095 but some compositions
        // use 4096 as upper range??
        val upperRange: Short = 4095
        flatLightData = lightData.map { maxOf(0, minOf(it, upperRange)) }.toShortArray()
    }

    fun getRow(rowIndex: Int): ShortArray {
        require(rowIndex >= 0) { "rowIndex must be 0 or greater" }
        if (rowIndex >= rows) throw IndexOutOfBoundsException()

        val fromIndex = rowIndex * columns
        return flatLightData.sliceArray(fromIndex..<fromIndex+columns)
    }

    override fun iterator(): Iterator<ShortArray> {
        return object: Iterator<ShortArray>{
            private var rowIndex = 0

            override fun next(): ShortArray {
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
        if (!flatLightData.contentEquals(other.flatLightData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = columns
        result = 31 * result + rows
        result = 31 * result + flatLightData.contentHashCode()
        return result
    }
}