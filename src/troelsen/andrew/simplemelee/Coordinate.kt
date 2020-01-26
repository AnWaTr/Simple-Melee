package troelsen.andrew.simplemelee

// Data classes are simple classes which are meant to hold
// data.  They provide a custom (data aware) toString(),
// equals() and getHashCode().
data class Coordinate(val x: Int, val y: Int)
{
    // See if the player is the bounds of the I quadrant
    // of a cartesian plain.
    val isInBounds : Boolean = x >= 0 && y >= 0

    // Overloaded operators!
    operator fun plus(other: Coordinate) =
        Coordinate(this.x + other.x, this.y + other.y)

    operator fun minus(other: Coordinate) =
        Coordinate(this.x - other.x, this.y - other.y)
}