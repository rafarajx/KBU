package nature

import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Screen
import gametype.Game

class Tree(x: Float, y: Float) : Nature() {
    var range: Rectangle2D? = null
    var resources = 200
    var num: Int = 0

    init {
        this.x = x
        this.y = y
        this.num = Nature.r.nextInt(2)
        this.field = Rectangle2D.Double(x - 16.0, y - 16.0, 32.0, 32.0)
    }

    override fun render(g2d: Graphics2D) {
        Screen.drawTile(g2d, 8, 3, this.x.toInt() - 16, this.y.toInt() - 16 + 7, 32, 32)
        Screen.drawTile(g2d, this.resources / 15, 15 - this.num, this.x.toInt() - 16, this.y.toInt() - 16, 32, 32)
    }

    override fun update() {
        if (this.resources <= 0) {
            die()
        }
    }

    fun die() {
        Game.natureList.remove(this)
    }

    override fun gatherResources(num: Int) {
        this.resources -= num
    }

    companion object {
        const val EDGE_LENGTH = 32
    }
}
