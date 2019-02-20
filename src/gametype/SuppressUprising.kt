package gametype

import java.awt.Color
import java.awt.Graphics2D

import core.Resources
import fraction.Player
import fraction.StandardAI
import fraction.Uprising

class SuppressUprising(friendsNumber: Int, difficulty: Int) : Game() {

    init {
        var difficulty = difficulty
        difficulty = 5 - difficulty
        setupMenuBar()
        Game.natureList.clear()
        Game.fractionList.clear()
        Game.setupNature(friendsNumber + 1)
        var startX = Game.random.nextInt(2000)
        var startY = Game.random.nextInt(2000)
        Game.player = Player(startX, startY, "PLAYER", 0, Color.BLUE, Resources(100, 60, 8, 30), 0)
        Game.fractionList.add(Game.player)
        for (i in 0 until friendsNumber) {
            val red = Game.random.nextInt(255)
            val green = Game.random.nextInt(255)
            val blue = Game.random.nextInt(255)
            val c = Color(red, green, blue)
            startX = Game.random.nextInt(2000)
            startY = Game.random.nextInt(2000)
            Game.fractionList.add(
                    StandardAI(
                            startX, startY, "AI", i + 1, c,
                            Resources(50 * difficulty, 30 * difficulty, 5 * difficulty, 50 * difficulty),
                            60 * (5 - difficulty), (5 - difficulty) * 50, 0)
            )
        }
        startX = 1000
        startY = 1000
        Game.fractionList.add(Uprising(startX, startY, "AI2", friendsNumber + 1, friendsNumber + 1))
    }

    override fun render(g2d: Graphics2D) {
        Game.drawObjects(g2d)
        Game.drawInterface(g2d)
    }

    override fun update() {
        Game.updateObjects()
        Game.updateInput()
        Game.tick++
    }
}
