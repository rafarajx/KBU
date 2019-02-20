package building

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D
import java.util.Random

import fraction.Fraction

open class Building {
    var owner: Fraction? = null

    open var x: Int = 0
    open var y: Int = 0
    open var field: Rectangle2D? = null
    open var range: Rectangle2D? = null
    open var teamNumber: Int = -1
    open var tick : Int = 1

    open fun render(g2d: Graphics2D) {}

    open fun update() {}

    open fun hurt(dmg: Int) {}

    companion object {
        var random = Random()

        const val HOUSE = 0
        const val MILL = 1
        const val TOWER = 2
        const val WOODCAMP = 3
        const val QUARRY = 4
        const val BARRACK = 5
        const val BARRICADE = 6

        fun drawBar(g2d: Graphics2D, x: Int, y: Int, current: Int, max: Int, c: Color) {
            if (current == max) return
            g2d.color = c
            g2d.fillRect(x - 13, y, (current.toFloat() / max * 26).toInt(), 4)
            g2d.color = Color.BLACK
            g2d.drawRect(x - 13, y, (current.toFloat() / max * 26).toInt(), 4)
        }
    }
}
