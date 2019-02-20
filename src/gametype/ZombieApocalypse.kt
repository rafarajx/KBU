package gametype

import java.awt.Color
import java.awt.Graphics2D

import core.Resources
import core.Screen
import fraction.Player
import fraction.StandardAI
import fraction.ZombieAI
import sound.SimpleSound

class ZombieApocalypse(FriendsNumber: Int, difficulty: Int) : Game() {

    internal var ShowTime = true

    init {
        var difficulty = difficulty
        setupMenuBar()
        Game.natureList.clear()
        Game.fractionList.clear()
        Game.setupNature(FriendsNumber + 1)
        var StartX = 1000
        var StartY = 1000
        Game.fractionList.add(Player(StartX, StartY, "PLAYER", 0, Color.BLUE, Resources(100, 60, 8, 30), 0))
        difficulty++
        for (i in 0 until FriendsNumber) {
            val red = Game.random.nextInt(255)
            val green = Game.random.nextInt(255)
            val blue = Game.random.nextInt(255)
            val c = Color(red, green, blue)
            StartX = Game.random.nextInt(2000)
            StartY = Game.random.nextInt(2000)
            Game.fractionList.add(
                    StandardAI(
                            StartX, StartY, "AI1", i + 1, c, Resources(
                            200 / difficulty, 120 / difficulty, 20 / difficulty, 60 / difficulty),
                            60 * difficulty, (5 - difficulty) * 50, 0)
            )
        }
        Game.fractionList.add(ZombieAI(1000, 1000, "Zombies", FriendsNumber + 1, Color.RED, 1))
    }

    override fun render(g2d: Graphics2D) {
        Game.drawObjects(g2d)
        Game.drawInterface(g2d)
        if (!this.ShowTime) {
            return
        }
        val TicksLeft = Game.tick - Game.toTicks(intArrayOf(10))
        val TimeLeft = Game.toTime(TicksLeft)
        if (TicksLeft > 0) {
            SimpleSound.bossdeath.play()
            this.ShowTime = false
        }
        Screen.drawString(g2d, "Zombies will attack in: " + TimeLeft[0] + ":" + TimeLeft[1] + TimeLeft[2], 200, 100, 2.0)
    }

    override fun update() {
        Game.updateObjects()
        Game.updateInput()
        tick++
    }
}
