package building

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.awt.geom.Rectangle2D

import core.Resources
import core.Screen
import entity.Worker.Miller
import fraction.Fraction
import gametype.Game
import math.vec2
import nature.Wheat

class Mill(p: vec2, owner: Fraction, teamIndex: Int) : Building() {
    override var field: Rectangle2D? = null
    override var range: Rectangle2D? = null
    override var teamNumber: Int = 0
    var AF: AffineTransform? = null
    var theta = 0.0
    var health = 200
    var miller = arrayOfNulls<Miller>(2)

    companion object {
        val COST = Resources(30, 0, 0, 0)
        private val EDGE_LENGTH = 32
        private val RANGE = 150
    }

    init {
        super.p = p
        this.owner = owner
        this.teamNumber = teamIndex
        this.field = Rectangle2D.Float(p.x - EDGE_LENGTH / 2, p.y - EDGE_LENGTH / 2, EDGE_LENGTH.toFloat(), EDGE_LENGTH.toFloat())
        this.range = Rectangle2D.Float(p.x - RANGE / 2, p.y - RANGE / 2, RANGE.toFloat(), RANGE.toFloat())
        owner.buildingList.add(this)
        owner.millList.add(this)
    }

    override fun render(g2d: Graphics2D) {
        AF = g2d.transform
        Screen.drawTile(g2d, 0, 1, p - EDGE_LENGTH / 2, EDGE_LENGTH, EDGE_LENGTH)
        g2d.rotate(theta, p.x.toDouble(), p.y.toDouble())
        Screen.drawTile(g2d, 7, 2, p - EDGE_LENGTH / 2, EDGE_LENGTH, EDGE_LENGTH)
        g2d.transform = AF
        Building.drawBar(g2d, p, health, 200, Color.RED)
    }

    override fun update() {
        if (health <= 0) die()
        if (tick % 300 == 0) {
            val x1 = Building.random.nextInt(120) - 60 + p.x
            val y1 = Building.random.nextInt(120) - 60 + p.y
            val r = Rectangle2D.Float(x1 - 8.0f, y1 - 8.0f, 16.0f, 16.0f)
            for (i in Game.fractionList.indices) {
                val fraction = Game.fractionList[i]
                for (j in fraction.buildingList.indices) {
                    val building = fraction.buildingList[j]
                    if (building.field!!.intersects(r)) return
                }
            }
            for (i in Game.natureList.indices) {
                val n = Game.natureList[i]
                if (n.field!!.intersects(r)) return
            }
            Game.natureList.add(Wheat(vec2(x1, y1)))
        }
        if (tick % 15L == 1L) {
            theta += 0.15
        }
        tick++
    }

    fun die() {
        owner!!.buildingList.remove(this)
        owner!!.millList.remove(this)
    }

    override fun hurt(dmg: Int) {
        health -= dmg
    }
}
