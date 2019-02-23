package nature

import math.vec2
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D
import java.util.Random

open class Nature {

    var p = vec2(0.0f, 0.0f)
    var field: Rectangle2D? = null
    var tick : Int = 1

    open fun render(g2d: Graphics2D) { }

    open fun update() { }

    open fun gatherResources(amount: Int) { }

    companion object {
        var r = Random()
    }
}
