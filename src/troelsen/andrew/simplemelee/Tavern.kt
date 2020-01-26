package troelsen.andrew.simplemelee

import java.io.File

const val TAVERN_NAME = "Taernyl's Folly"

class Tavern(private val pathToDrinkList : String, name: String = TAVERN_NAME) : Room(name)
{
    private var drinkList = mutableListOf<String>()

    // Called after the constructor.
    init
    {
        // Load in all the lines to the List<String>
        for (line in File(pathToDrinkList).readLines())
        {
            drinkList.add(line)
        }
    }

    private fun printMenu()
    {
        println("\n***** Menu *****")
        drinkList.forEachIndexed { index, data ->
            // data.replace(",", " : ")
            val (type, name, price) = data.split(',')
            println("$index : $$price\t$name")
        }
        println("****************\n")
    }

    // Menu item format: "shandy,Dragon's Breath,5.91"
    fun placeOrder(p: Player)
    {
        // Print the menu.
        printMenu()

        // Print out some info about the upcoming transaction.
        val indexOfApostrophe = TAVERN_NAME.indexOf('\'')
        val tavernMaster = TAVERN_NAME.substring(0 until indexOfApostrophe)
        println("${p.playerName} speaks with $tavernMaster about their order.")

        // Get drink order.
        print("Enter the number of the item you want: ")
        val orderID : Int = readLine()?.toInt() ?: 0

        // Sort of how like Python can return multiple values,
        // Kotlin supports "deconstructing" of a return value.
        val (type, name, price) = drinkList[orderID].split(',')

        // See if the player has enough gold.
        if (p.playerGold >= price.toInt())
        {
            val message = "${p.playerName} buys a $name ($type) for $price."
            println(message)
            p.playerGold -= price.toInt()
            println("${p.playerName} now has ${p.playerGold} left.")
        }
        else
        {
            println("You have no money!!")
        }

        // If they buy a drink they can get some health.
        if (p.playerHealth < MAX_HEALTH)
        {
            // Make it so the price of the drink gives more
            // bang for the buck.
            val healthPoints = (2..7).random() + orderID
            p.playerHealth += healthPoints
            println("You gained $healthPoints health!")

            if (p.playerHealth > MAX_HEALTH)
            {
                p.playerHealth == MAX_HEALTH
            }
            println("New health: ${p.playerHealth}")
        }
        println()
    }
}