package troelsen.andrew.simplemelee

import java.io.File

// This will be a compile time constant.
const val MAX_HEALTH = 100

// Remember - I can add properties automatically in the primary constructor like so.
// This will make a backing field and get/set for each one automatically.
//
// Note how we implement interfaces. Oddly, once an interface is added to a class, I
// need to use the override keyword on each member of the interface.
class Player(var playerName: String = "", var playerRace: String = "",
             override var playerHealth: Int = MAX_HEALTH,
             var playerGold: Int = 20, var playerXP: Int = 0) : Any(), IFightable
{
    override val diceCount: Int = 3
    override val diceSides: Int = 6

    override fun attack(opponent: IFightable): Int {
        val damageDealt = damageRoll
        opponent.playerHealth -= damageDealt
        playerXP += damageDealt

        return damageDealt
    }

    // Logic to set up the player's home town.
    // Note how I am assigning this to a f(x) call.
    private val playerHomeTown = selectHometown()

    private fun selectHometown(): String = File("data/hometown.txt")
                                            .readText()
                                            .split("\n")
                                            .shuffled()
                                            .first()

    // Shows where the play currently is in the map.
    var currentPosition = Coordinate(0, 0)

    fun showPlayerStats()
    {
        println("You are $playerName, a $playerRace${getRaceDetails()}")
        println("You come from the town of $playerHomeTown.")
        println("Your current stats:")
        println("-> Health: $playerHealth (${getHealthDetailsWHEN()})\n-> Gold: " +
                "$playerGold\n-> XP: $playerXP\n-> Kill Count: $killCount")
     }

    private fun getHealthDetailsWHEN() : String
    {
        // A 'when' statement is preferred to
        // multi-nest if/else.
        return when (playerHealth) {
            100 -> "You are in excellent condition!"
            in 99 downTo 89 -> "You have a few scratches."
            in 88 downTo 75 -> "You are OK...but hurt"
            in 74 downTo 30 -> "You are in rough shape!!"
            in 29 downTo 15 -> "Oh no!!  Very close to death! Get a drink!"
            else -> "You are just about dead!"
        }
    }

    // Single expression function example.
    fun castFireball(times : Int) =
        println("Casting (x$times) fireballs!")

    private fun getRaceDetails() : String
    {
        return when (playerRace.toLowerCase()) {
            "dwarf" -> ", keeper of the Mines."
            "gnome" -> ", keeper of the Tunnels."
            "orc" -> ", free person of the Rolling Hills."
            "human" -> ", free person of all the land."
            else -> ", a race of great mystery...."
        }
    }
}