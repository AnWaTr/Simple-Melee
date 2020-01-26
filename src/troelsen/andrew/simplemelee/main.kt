package troelsen.andrew.simplemelee

// Yep - Kotlin can use Java APIs directly.
import java.io.File

// Program entry Point.
fun main()
{
    // Load up the monsters from file.
    File("data/monsterList.txt").readLines().forEach { line ->
        var (name, str) = line.split(",")
        monsters[name] = str.toInt()
    }

    // Kick off the game loop.
    Game.startGameLoop()
}