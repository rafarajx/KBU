package nature

import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Screen

class Grass(x: Float, y: Float) : Nature() {
    internal var num = 0
    internal var FieldSize = 16

    init {
        this.x = x
        this.y = y
        this.field = Rectangle2D.Double(x.toDouble(), y.toDouble(), 1.0, 1.0)
    }

    override fun render(g2d: Graphics2D) {
        Screen.drawTile(g2d, 8, 3, this.x.toInt() - this.FieldSize / 2, this.y.toInt() - this.FieldSize / 2 + 2,
                this.FieldSize, this.FieldSize)
        Screen.drawTile(g2d, 7 + this.num, 5, this.x.toInt() - this.FieldSize / 2, this.y.toInt() - this.FieldSize / 2,
                this.FieldSize, this.FieldSize)
    }

    override fun update() {
        if (this.tick % 1000 == 0 && this.num < 7) {
            this.num += 1
        }
        this.tick += 1
    }

    override fun gatherResources(num: Int) {}
}
