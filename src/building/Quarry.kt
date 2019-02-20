package building

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Resources
import core.Screen
import fraction.Fraction

class Quarry(x: Int, y: Int, owner: Fraction, teamIndex: Int) : Building() {
    private val EDGE_LENGTH = 32
    private val RANGE = 150
    override var teamNumber: Int = 0
    override var field: Rectangle2D? = null
    override var range: Rectangle2D? = null
    internal var health = 200

    init {
        this.x = x
        this.y = y
        this.owner = owner
        this.teamNumber = teamIndex
        this.field = Rectangle2D.Float((x - EDGE_LENGTH / 2).toFloat(), (y - EDGE_LENGTH / 2).toFloat(), EDGE_LENGTH.toFloat(), EDGE_LENGTH.toFloat())
        this.range = Rectangle2D.Float((x - RANGE / 2).toFloat(), (y - RANGE / 2).toFloat(), RANGE.toFloat(), RANGE.toFloat())
        owner.buildingList.add(this)
        owner.quarryList.add(this)
    }

    override fun render(g2d: Graphics2D) {
        Screen.drawTile(g2d, 0, 4, x - EDGE_LENGTH / 2, y - EDGE_LENGTH / 2, EDGE_LENGTH, EDGE_LENGTH)
        Building.drawBar(g2d, x, y, health, 200, Color.RED)
    }

    override fun update() {
        if (health <= 0) die()
    }

    fun die() {
        owner!!.buildingList.remove(this)
        owner!!.quarryList.remove(this)
    }

    override fun hurt(dmg: Int) {
        this.health -= dmg
    }

    companion object {
        val COST = Resources(15, 20, 0, 0)
    }
}
