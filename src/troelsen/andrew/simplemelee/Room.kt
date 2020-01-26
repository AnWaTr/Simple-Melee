package troelsen.andrew.simplemelee

// This will be the base class for a number of
// different room types.
//
// Only a few sub-classes exist as they add additional functionality.
// A Room object on its own just has a description and monster.
//
// Marking the class as open means it can be extended.
// By default, classes can not be extended.
open class Room(val name: String)
{
    // A room may or may not have a monster in it.
    var monster: Monster? = null

    // Get the monster for this room.
    init {
        var listOfMonsters = monsters.toList()
        var (name, str) = listOfMonsters.shuffled().take(1)[0]
        monster = Monster(name, str)
    }

    // Print out a description of the room and if it contains any monsters.
    fun description() : String = "$name," +
            " which contains ${monster?.description ?: "nothing"}."
}