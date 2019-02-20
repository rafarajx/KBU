package building

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Resources
import core.Screen
import entity.Zombie
import fraction.Fraction

class Grave(x: Int, y: Int, owner: Fraction, teamIndex: Int) : Building() {
    private val EDGE_LENGTH = 16
    private val RANGE = 150
    override var range: Rectangle2D? = null
    internal var health = 500

    init {
        this.x = x
        this.y = y
        this.owner = owner
        this.teamNumber = teamIndex
        this.field = Rectangle2D.Float((x - EDGE_LENGTH / 2).toFloat(), (y - EDGE_LENGTH / 2).toFloat(), EDGE_LENGTH.toFloat(), EDGE_LENGTH.toFloat())
        this.range = Rectangle2D.Float((x - RANGE / 2).toFloat(), (y - RANGE / 2).toFloat(), RANGE.toFloat(), RANGE.toFloat())
        owner.buildingList.add(this)
    }

    override fun render(g2d: Graphics2D) {
        Screen.drawTile(g2d, 2, 0, x - EDGE_LENGTH / 2, y - EDGE_LENGTH / 2, EDGE_LENGTH, EDGE_LENGTH)
        Building.drawBar(g2d, x, y - 10, health, 500, Color.RED)
    }

    override fun update() {
        if (this.health <= 0) {
            Die()
        }
        if (this.tick % 1100 == 0) {
            owner!!.entityList.add(Zombie(x, y, owner as Fraction, teamNumber))
        }
        this.tick++
    }

    fun Die() {
        owner!!.buildingList.remove(this)
        //owner.graveList.remove(this);
    }

    override fun hurt(dmg: Int) {
        health -= dmg
    }

    companion object {
        val COST = Resources(0)
    }
}
