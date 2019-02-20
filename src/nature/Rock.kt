package nature

import java.awt.Graphics2D
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D

import core.Screen
import gametype.Game

class Rock(x: Float, y: Float) : Nature() {
    internal var range: Ellipse2D? = null
    internal var resources = 200
    private val appearance: Int

    init {
        this.x = x
        this.y = y
        this.appearance = Nature.r.nextInt(4)
        this.field = Rectangle2D.Double((x - EDGE_LENGTH / 2).toDouble(), (y - EDGE_LENGTH / 2).toDouble(), EDGE_LENGTH.toDouble(), EDGE_LENGTH.toDouble())
    }

    override fun render(g2d: Graphics2D) {
        Screen.drawTile(g2d, 8, 3, x.toInt() - EDGE_LENGTH / 2, y.toInt() - EDGE_LENGTH / 2 + 2, EDGE_LENGTH, EDGE_LENGTH)
        Screen.drawTile(g2d, 7 + appearance, 7, x.toInt() - EDGE_LENGTH / 2, y.toInt() - EDGE_LENGTH / 2, EDGE_LENGTH, EDGE_LENGTH)
    }

    override fun update() {
        if (resources <= 0) die()
    }

    fun die() {
        Game.natureList.remove(this)
    }

    override fun gatherResources(num: Int) {
        resources -= num
    }

    companion object {
        private val EDGE_LENGTH = 16
    }
}
