package gametype

import java.awt.Color
import java.awt.Graphics2D

import core.Resources
import fraction.StandardAI
import math.vec2

class FFA(opponentsNumber: Int, difficulty: Int) : Game() {

    init {
        setupMenuBar()
        Game.natureList.clear()
        Game.fractionList.clear()
        Game.setupNature(opponentsNumber + 1)
        Game.fractionList.add(Game.player)
        for (i in 0 until opponentsNumber) {
            val color = Color.getHSBColor(1.0f / opponentsNumber * i, 1.0f, 1.0f)
            val start = vec2(
                (-Math.sin(Math.PI * 2 / (opponentsNumber + 1) * (i + 1)) * 900).toInt() + 1000,
                (-Math.cos(Math.PI * 2 / (opponentsNumber + 1) * (i + 1)) * 900).toInt() + 1000)
            Game.fractionList.add(
                    StandardAI(
                            start, "AI", i + 1, color,
                            Resources(50 * difficulty, 30 * difficulty, 5 * difficulty, 100 * difficulty),
                            60 * (5 - difficulty), difficulty * 50, i + 1
                    )
            )
        }
    }

    override fun render(g2d: Graphics2D) {
        Game.drawObjects(g2d)
        Game.drawInterface(g2d)
    }

    override fun update() {
        updateObjects()
        updateInput()
        tick++
    }
}
