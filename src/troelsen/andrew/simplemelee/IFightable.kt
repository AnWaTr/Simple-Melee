package troelsen.andrew.simplemelee

import java.util.*

// Any class which supports this interface
// can engage in a fight.
interface IFightable
{
    // Properties.
    var playerHealth: Int
    val diceCount: Int
    val diceSides: Int

    // Like Java, Kotlin interface members can have
    // default implementations (which is just so wrong).
    val damageRoll: Int
        get() = (0 until diceCount).map {
            Random().nextInt(diceSides + 1)
        }.sum()

    fun attack(opponent: IFightable): Int
}
