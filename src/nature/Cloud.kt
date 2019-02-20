package nature

import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Screen
import gametype.Game

class Cloud(x: Int, y: Int) : Nature() {
    var SpreadLevel = 0
    var FieldSize = 16

    init {
        this.x = x.toFloat()
        this.y = y.toFloat()
        this.field = Rectangle2D.Float(this.x, this.y, 1.0f, 1.0f)
    }

    override fun render(g2d: Graphics2D) {
        Screen.drawTile(g2d, 7 + this.SpreadLevel, 1, this.x.toInt(), this.y.toInt(), this.FieldSize, this.FieldSize)
    }

    override fun update() {
        if (tick % 30 == 0) {
            SpreadLevel++
        }
        if (tick % 20 == 0) {
            x += (Nature.r.nextInt(4) - 1).toFloat()
            y -= (Nature.r.nextInt(1) + 3).toFloat()
        }
        if (this.SpreadLevel == 5) {
            Game.natureList.remove(this)
        }
        tick++
    }

    override fun gatherResources(num: Int) {}
}
