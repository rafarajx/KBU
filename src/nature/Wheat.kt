package nature

import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Screen
import gametype.Game

class Wheat(x: Float, y: Float) : Nature() {
    var num = 0

    init {
        this.x = x
        this.y = y
        field = Rectangle2D.Double(x.toDouble(), y.toDouble(), 1.0, 1.0)
    }

    override fun render(g2d: Graphics2D) {
        Screen.drawTile(g2d, 8, 3, x.toInt() - EDGE_SIZE / 2, y.toInt() - EDGE_SIZE / 2 + 2, EDGE_SIZE, EDGE_SIZE)
        Screen.drawTile(g2d, 7 + num, 6, x.toInt() - EDGE_SIZE / 2, y.toInt() - EDGE_SIZE / 2, EDGE_SIZE, EDGE_SIZE)
    }

    override fun update() {
        if (tick % 1200 == 0 && num < 7) num++
        tick++
    }

    fun gather(): Int {
        Game.natureList.remove(this)
        return num + 1
    }

    override fun gatherResources(n: Int) {
        num -= n
        if (num < 0) Game.natureList.remove(this)
    }

    companion object {
        val EDGE_SIZE = 16
    }
}
