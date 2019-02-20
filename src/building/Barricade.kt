package building

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Resources
import core.Screen
import fraction.Fraction

class Barricade(x: Int, y: Int, owner: Fraction, teamNumber: Int) : Building() {

    private val EDGE_LENGTH = 16
    private val RANGE = 100
    override var teamNumber: Int = 0
    override var field: Rectangle2D? = null
    override var range: Rectangle2D? = null
    internal var health = 250

    init {
        this.x = x
        this.y = y
        this.owner = owner
        this.teamNumber = teamNumber
        this.field = Rectangle2D.Double((x - EDGE_LENGTH / 2).toDouble(), (y - EDGE_LENGTH / 2).toDouble(), EDGE_LENGTH.toDouble(), EDGE_LENGTH.toDouble())
        this.range = Rectangle2D.Double((x - RANGE / 2).toDouble(), (y - RANGE / 2).toDouble(), RANGE.toDouble(), RANGE.toDouble())
        owner.buildingList.add(this)
        owner.barricadeList.add(this)
    }

    override fun render(g2d: Graphics2D) {
        Screen.drawTile(g2d, 0, 6, x - EDGE_LENGTH / 2, y - EDGE_LENGTH / 2, EDGE_LENGTH, EDGE_LENGTH)
        Building.drawBar(g2d, x, y, health, 250, Color.RED)
    }

    override fun update() {
        if (health <= 0) die()
    }

    fun die() {
        owner!!.buildingList.remove(this)
        owner!!.barricadeList.remove(this)
    }

    override fun hurt(dmg: Int) {
        health -= dmg
    }

    companion object {
        val COST = Resources(10, 20, 1, 0)
    }
}
