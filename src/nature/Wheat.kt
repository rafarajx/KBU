package nature

import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Screen
import gametype.Game
import math.vec2

class Wheat(p: vec2) : Nature() {
    var num = 1

    companion object {
        const val EDGE_SIZE = 16
    }

    init {
        super.p = p
        field = Rectangle2D.Float(p.x - EDGE_SIZE / 2 , p.y - EDGE_SIZE / 2, EDGE_SIZE.toFloat(), EDGE_SIZE.toFloat())
    }

    override fun render(g2d: Graphics2D) {
        Screen.drawTile(g2d, 8, 3, p.x.toInt() - EDGE_SIZE / 2, p.y.toInt() - EDGE_SIZE / 2 + 2, EDGE_SIZE, EDGE_SIZE)
        Screen.drawTile(g2d, 7 + num, 6, p.x.toInt() - EDGE_SIZE / 2, p.y.toInt() - EDGE_SIZE / 2, EDGE_SIZE, EDGE_SIZE)
    }


    override fun update() {
        if (tick % 1200 == 0 && num < 7) num++
        tick++
    }

    fun gather(): Int {
        remove()
        return num + 1
    }

    override fun gatherResources(amount: Int) {
        num -= amount
        if (num < 0) remove()
    }
    
    fun add(){
        Game.natureList.add(this)
        Game.wheatList.add(this)
    }
    
    fun remove(){
        Game.natureList.remove(this)
        Game.wheatList.remove(this)
    }
}
