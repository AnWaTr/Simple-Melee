package troelsen.andrew.simplemelee

// Pass in monster type & health
class Monster(private val monsterType : String, override var playerHealth: Int) : IFightable {

    override val diceCount: Int = 2
    override val diceSides: Int = 6

    override fun attack(opponent: IFightable): Int
    {
        val damageDealt = damageRoll
        opponent.playerHealth -= damageDealt
        return damageDealt
    }

    var description : String = "A Wild $monsterType with a ST of $playerHealth!"
}