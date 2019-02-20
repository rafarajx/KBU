package nature

import java.awt.Graphics2D
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D

import core.Screen

class Flowers(x: Float, y: Float) : Nature() {
    private val EDGE_LENGTH = 16
    internal var Range: Ellipse2D? = null
    var appearance = 0

    init {
        this.x = x
        this.y = y
        this.appearance = Nature.r.nextInt(2)
        this.field = Rectangle2D.Double((x - EDGE_LENGTH / 2).toDouble(), (y - EDGE_LENGTH / 2).toDouble(), EDGE_LENGTH.toDouble(), EDGE_LENGTH.toDouble())
    }

    override fun render(g2d: Graphics2D) {
        Screen.drawTile(g2d, 7 + appearance, 4, x.toInt() - EDGE_LENGTH / 2, y.toInt() - EDGE_LENGTH / 2, EDGE_LENGTH, EDGE_LENGTH)
    }

    override fun update() {
        field = Rectangle2D.Double(x - 8.0, y - 8.0, 16.0, 16.0)
    }

    override fun gatherResources(num: Int) {}
}
