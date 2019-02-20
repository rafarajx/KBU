package building

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Resources
import core.Screen
import entity.Knight
import fraction.Fraction

class Barrack(x: Int, y: Int, owner: Fraction, teamIndex: Int) : Building() {

    override var teamNumber: Int = 0
    override var field: Rectangle2D? = null
    override var range: Rectangle2D? = null
    internal var health = 200

    init {
        this.x = x
        this.y = y
        this.owner = owner
        this.teamNumber = teamIndex
        this.field = Rectangle2D.Double((x - EDGE_LENGTH / 2).toDouble(), (y - EDGE_LENGTH / 2).toDouble(), EDGE_LENGTH.toDouble(), EDGE_LENGTH.toDouble())
        this.range = Rectangle2D.Double((x - RANGE / 2).toDouble(), (y - RANGE / 2).toDouble(), RANGE.toDouble(), RANGE.toDouble())
        owner.buildingList.add(this)
        owner.barrackList.add(this)
    }

    override fun render(g2d: Graphics2D) {
        Screen.drawTile(g2d, 0, 5, x - 16, y - 16, EDGE_LENGTH, EDGE_LENGTH)
        Building.drawBar(g2d, x, y - 10, health, 200, Color.RED)
        if (owner!!.resources.food > 20 && owner!!.population < owner!!.maxPopulation)
            Building.drawBar(g2d, x, y, tick % 600, 600, Color.BLUE)
    }

    override fun update() {
        if (health <= 0) die()
        if (tick % 600 == 0) {
            if (owner!!.resources.enough(0, 0, 1, 5) && owner!!.population < owner!!.maxPopulation) {
                owner!!.entityList.add(Knight(x, y, owner!!, teamNumber))
                owner!!.resources.pay(0, 0, 1, 5)
            }
        }
        tick++
    }

    fun die() {
        owner!!.buildingList.remove(this)
        owner!!.barrackList.remove(this)
    }

    override fun hurt(dmg: Int) {
        health -= dmg
    }

    companion object {
        val COST = Resources(30, 80, 10, 0)
        private val EDGE_LENGTH = 32
        private val RANGE = 150
    }
}
