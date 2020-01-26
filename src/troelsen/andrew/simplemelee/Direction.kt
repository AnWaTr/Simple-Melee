package troelsen.andrew.simplemelee

// OK - so enums can take a constructor, here we are
// passing in a Coordinate.
enum class Direction(private val coordinate: Coordinate)
{
    // Based on the Coordinate - we return an enum value (?)
    NORTH(Coordinate(0, -1)),
    EAST(Coordinate(1, 0)),
    SOUTH(Coordinate(0, 1)),
    WEST(Coordinate(-1, 0));        // NOTE: Last item in the enum has a ; to terminate.

    // Functions on an enum are called from an enumerated type value.
    // Ex: Direction.EAST.updateCoordinate(Coordinate(1, 0))

    // Without overloaded ops.
    //fun updateCoordinate(playerCoordinate: Coordinate) =
    //    Coordinate(playerCoordinate.x + coordinate.x,
    //        playerCoordinate.y + coordinate.y)

    // With overloaded ops.
    fun updateCoordinate(playerCoordinate: Coordinate) =
        coordinate + playerCoordinate
}

