package nature

import java.awt.Graphics2D
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D

import core.Screen
import gametype.Game
import math.vec2

class Rock(p: vec2) : Nature() {
    internal var range: Ellipse2D? = null
    internal var resources = 200
    private val appearance: Int

    companion object {
        private val EDGE_LENGTH = 16
    }

    init {
        super.p = p
        this.appearance = Nature.r.nextInt(4)
        this.field = Rectangle2D.Float(p.x - EDGE_LENGTH / 2, p.y - EDGE_LENGTH / 2, EDGE_LENGTH.toFloat(), EDGE_LENGTH.toFloat())
    }

    override fun render(g2d: Graphics2D) {
        Screen.drawTile(g2d, 8, 3, p.x.toInt() - EDGE_LENGTH / 2, p.y.toInt() - EDGE_LENGTH / 2 + 2, EDGE_LENGTH, EDGE_LENGTH)
        Screen.drawTile(g2d, 7 + appearance, 7, p - EDGE_LENGTH / 2, EDGE_LENGTH, EDGE_LENGTH)
    }

    override fun update() {
        if (resources <= 0) die()
    }

    fun die() {
        Game.natureList.remove(this)
    }

    override fun gatherResources(amount: Int) {
        resources -= amount
    }
}
