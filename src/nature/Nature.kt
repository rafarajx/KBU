package nature

import java.awt.Graphics2D
import java.awt.geom.Rectangle2D
import java.util.Random

open class Nature {

    var x: Float = 0.0f
    var y: Float = 0.0f
    var field: Rectangle2D? = null
    var tick : Int = 1

    open fun render(paramGraphics2D: Graphics2D) { }

    open fun update() { }

    open fun gatherResources(paramInt: Int) { }

    companion object {
        var r = Random()
    }
}
