package troelsen.andrew.simplemelee

// Here is a sub-class that passes data to the parent class.
// Note that we use the : to denote the parent.
class TownSquare : Room("The Magnificent Town Square")
{
    private var bellSound = "GONG!!!!!"
    private fun ringBell() = "The bell tower announces your arrival. $bellSound"
}