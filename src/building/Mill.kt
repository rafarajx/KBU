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
import nature.Wheat

class Mill(x: Int, y: Int, owner: Fraction, teamIndex: Int) : Building() {
    override var field: Rectangle2D? = null
    override var range: Rectangle2D? = null
    override var teamNumber: Int = 0
    var AF: AffineTransform? = null
    var theta = 0.0
    var health = 200
    var miller: Miller? = null

    init {
        this.x = x
        this.y = y
        this.owner = owner
        this.teamNumber = teamIndex
        this.field = Rectangle2D.Double((x - EDGE_LENGTH / 2).toDouble(), (y - EDGE_LENGTH / 2).toDouble(), EDGE_LENGTH.toDouble(), EDGE_LENGTH.toDouble())
        this.range = Rectangle2D.Double((x - RANGE / 2).toDouble(), (y - RANGE / 2).toDouble(), RANGE.toDouble(), RANGE.toDouble())
        owner.buildingList.add(this)
        owner.millList.add(this)
    }

    override fun render(g2d: Graphics2D) {
        AF = g2d.transform
        Screen.drawTile(g2d, 0, 1, x - EDGE_LENGTH / 2, y - EDGE_LENGTH / 2, EDGE_LENGTH, EDGE_LENGTH)
        g2d.rotate(theta, x.toDouble(), y.toDouble())
        Screen.drawTile(g2d, 7, 2, x - EDGE_LENGTH / 2, y - EDGE_LENGTH / 2, EDGE_LENGTH, EDGE_LENGTH)
        g2d.transform = AF
        Building.drawBar(g2d, x, y, health, 200, Color.RED)
    }

    override fun update() {
        if (health <= 0) die()
        if (tick % 300 == 0) {
            val x1 = (Building.random.nextInt(120) - 60 + x).toFloat()
            val y1 = (Building.random.nextInt(120) - 60 + y).toFloat()
            val r = Rectangle2D.Double(x1 - 8.0, y1 - 8.0, 16.0, 16.0)
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
            Game.natureList.add(Wheat(x1, y1))
        }
        if (tick % 15L == 1L) {
            theta += 0.15
        }
        tick++
    }

    fun die() {
        if (miller != null)
            miller!!.mill = null
        owner!!.buildingList.remove(this)
        owner!!.millList.remove(this)
    }

    override fun hurt(dmg: Int) {
        health -= dmg
    }

    companion object {
        val COST = Resources(30, 0, 0, 0)
        private val EDGE_LENGTH = 32
        private val RANGE = 150
    }
}
