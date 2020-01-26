package troelsen.andrew.simplemelee

import kotlin.system.exitProcess

// A list of Monsters in the game (global)
// This is filled in main().
val monsters = mutableMapOf<String, Int>()

// When the kill count = 5, all rooms have been cleared and the
// player has won the game!
var killCount : Int = 0
const val ALL_DEAD : Int = 5

// See how many turns it takes to complete the game.
var turn_count : Int = 0

// So the "object" keyword is just like the "class"
// keyword, but it declares a singleton. It also appears
// to make all items static? Yes it does.
//
// Objects can not take constructors.
object Game
{
    // The current player.
    private var currentPlayer : Player

    // The player starts off in the town square.
    private var currentRoom : Room = TownSquare()

    // The amazing world of Simple Melee.
    /*
        [Town Square (0,0)] [Tavern (0,1)] [Back Room (0,2)]
        [Long Corridor (1,0)] [Room of Death (1,1)]
     */
    private var worldMap = listOf(
        listOf(currentRoom, Tavern("data/drinkList.txt"), Room("Tavern Back Room")),
        listOf(Room("Long Corridor"), Room("Room of Death!!")))

    // Object declarations don't have constructors, which
    // the runtime makes it for me when the app starts up.
    // So I need an init block to prep everything.
    init
    {
        welcomeBanner()
        preamble()
        currentPlayer = setUpPlayer()
        printDivider()
    }

    private fun preamble()
    {
        printDivider()
        println("Welcome mortal, do you have what it takes to play...")
        println("~~ SIMPLE MELEE?! ~~")
        println("You are about to enter a vast, complex, world of 5 rooms.")
        println("If you kill all monsters in each room - you win.")
        println("OR - you die...painfully.")
        println("Good luck.")
        printDivider()
    }

    // This is going to be the game loop.
    fun startGameLoop() {
        // First show the player stats.
        currentPlayer.showPlayerStats()

        // Every iteration of a loop is a turn.
        while (true)
        {
            printDivider()

            // If player in the the Tavern, ask if they want a drink!
            if(currentRoom is Tavern)
            {
                print("Welcome To The Tavern!  Want a Drink? {Y or N}: ")
                var ans = readLine().toString().toLowerCase()
                if(ans == "y") {
                    val t = currentRoom as Tavern
                    t.placeOrder(currentPlayer)
                }
                else
                {
                    println("OK-BYE")
                }
            }

            println("You are in ${currentRoom.description()}")

            // Get player command.
            println(message = "Command options: move {north, south, east, west}, map, mystats, roomstats, fight, quit")
            print("Enter your command: ")

            val result = GameInput(readLine()).processCommand()
            println(result)

            if (result == "quit") {
                println("Thanks for playing!")
                break
            }

            if (killCount == ALL_DEAD)
            {
                gameWinner()
                break
            }

            // Increase the turn count.
            turn_count++

            // Did we level up?
            if (currentPlayer.playerXP >= 50)
            {
                println("You leveled up!")
                println("You get 3 gold pieces!")
                currentPlayer.playerGold += 3
                println("You get 2 health!")
                currentPlayer.playerHealth += 2

                // Reset XP.
                currentPlayer.playerXP = 0
            }
        }
    }

    private fun gameWinner() {
        printDivider()
        println(">>>>>>>>>>>>>>>>>>>>>>")
        println("YOU HAVE WON THE GAME!")
        println("<<<<<<<<<<<<<<<<<<<<<<")
        println()
        println("It took you $turn_count to finish!")
    }

    // This is a private nested class which encapsulates the
    // logic of looking for a proper command. So the command comes
    // in as a ctor argument, which could be null.
    // Some commands might have two parts (move north) so we
    // separate them via split().
    private class GameInput(arg: String?)
    {
        private val input = arg ?: ""

        // If we had "move north" then
        // the command is "move" and the arg is "north".
        // If the command is one word (quit) then the arg is "".
        val command = input.split(" ")[0]
        val argument = input.split(" ").getOrElse(1, { "" })

        // If we don't understand a command.
        private fun commandNotFound() = "I'm not quite sure what you're trying to do!"

        // This will process all the commands.
        fun processCommand() = when (command.toLowerCase()) {
            "move" -> movePlayer(argument)
            "quit" -> quit()
            "map" -> printMap()
            "mystats" -> showPlayerStats()
            "roomstats" -> showRoomStats()
            "fight" -> fight()
            else -> commandNotFound()
        }
    }

    private fun showRoomStats(): String
    {
        println(currentRoom.description())
        return ""
    }

    private fun showPlayerStats(): String
    {
        currentPlayer.showPlayerStats()
        return ""
    }

    private fun quit(): String = "quit"

    private fun printDivider()
    {
        Thread.sleep(500)
        println()
        println("--------------------")
        println()
    }

    private fun welcomeBanner()
    {
        val omSymbol = '\u0950'
        println("\n${omSymbol.toString().repeat(21)}")
        println("         Welcome To Simple Melee!")
        println("${omSymbol.toString().repeat(21)}")
    }


    // First check if the room has a monster.
    // If so, do a round of combat as long as someone is still
    // alive.
    private fun fight() = currentRoom.monster?.let {
        while (currentPlayer.playerHealth > 0 && it.playerHealth > 0) {
            slay(it)
            Thread.sleep(1000)
        }
        "Combat complete."
    } ?: "There's nothing here to fight."

    private fun slay(monster: Monster) {
        println("${monster.description} did ${monster.attack(currentPlayer)} damage!")
        println("${currentPlayer.playerName} did ${currentPlayer.attack(monster)} damage!")

        // Game Over Man!
        if (currentPlayer.playerHealth <= 0) {
            println(">>>> You have died! Thanks for playing. <<<<")
            println(currentPlayer.showPlayerStats())
            exitProcess(0)
        }

        // Killed the monster!
        if (monster.playerHealth <= 0) {
            println(">>>> ${monster.description} has been defeated! <<<<")
            println("You got XP!")
            killCount ++
            currentRoom.monster = null
        }
    }

    private fun setUpPlayer() : Player
    {
        print("Enter your player's name: ")
        val name = readLine().toString()

        print("Enter your player's race {Human, Orc, Gnome, Dwarf...or other...}: ")
        val race = readLine().toString()

        return Player(name, race, MAX_HEALTH)
    }

    // We send in a String that corresponds to the Direction Enum.
    // So I type north, south, east, west.
    private fun movePlayer(directionInput: String) =
        try
        {
            // Get upper case version of Direction enum.
            val direction = Direction.valueOf(directionInput.toUpperCase())

            val newPosition = direction.updateCoordinate(currentPlayer.currentPosition)
            if (!newPosition.isInBounds)
            {
                throw IllegalStateException("$direction is out of bounds.")
            }

            val newRoom = worldMap[newPosition.y][newPosition.x]
            currentPlayer.currentPosition = newPosition
            currentRoom = newRoom
            "OK, you move $direction to the ${newRoom.name}."
        }
        catch (e: Exception)
        {
            "Sorry, you hit a wall when you go $directionInput."
        }

    // This works - but is hard coded to the size of the current game map.
    // Kotlin lists of lists are weird....not sure how to make this general yet.
    private fun printMap() : String
    {
        printDivider()
        // Make a string map the same size as the world map (hard coded)
         var theMap = mutableListOf(
             mutableListOf("O", "O", "O"),
             mutableListOf("O", "O"))

        // Now put an X where the player is.
        var (x, y) = currentPlayer.currentPosition
        theMap[y][x] = "X"

        for (item in theMap[0])
            print(item)
        println()
        for (item in theMap[1])
            print(item)
        return ""
    }
}


