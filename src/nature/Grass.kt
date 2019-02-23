package nature

import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Screen
import math.vec2

class Grass(p: vec2) : Nature() {
    internal var num = 0
    internal var FieldSize = 16

    init {
        super.p = p
        super.field = Rectangle2D.Float(p.x, p.y, 1.0f, 1.0f)
    }

    override fun render(g2d: Graphics2D) {
        Screen.drawTile(g2d, 8, 3, p.x.toInt() - FieldSize / 2, p.y.toInt() - FieldSize / 2 + 2, FieldSize, FieldSize)
        Screen.drawTile(g2d, 7 + num, 5, p.x.toInt() - FieldSize / 2, p.y.toInt() - FieldSize / 2, FieldSize, FieldSize)
    }

    override fun update() {
        if (this.tick % 1000 == 0 && this.num < 7) {
            this.num += 1
        }
        this.tick += 1
    }

    override fun gatherResources(amount: Int) {}
}
