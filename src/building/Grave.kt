package building

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Resources
import core.Screen
import entity.Zombie
import fraction.Fraction
import math.vec2

class Grave(p: vec2, owner: Fraction, teamIndex: Int) : Building() {
    private val EDGE_LENGTH = 16
    private val RANGE = 150
    override var range: Rectangle2D? = null
    internal var health = 500

    init {
        super.p = p
        this.owner = owner
        this.teamNumber = teamIndex
        this.field = Rectangle2D.Float(p.x - EDGE_LENGTH / 2, p.y - EDGE_LENGTH / 2, EDGE_LENGTH.toFloat(), EDGE_LENGTH.toFloat())
        this.range = Rectangle2D.Float(p.x - RANGE / 2, p.y - RANGE / 2, RANGE.toFloat(), RANGE.toFloat())
        owner.buildingList.add(this)
    }

    override fun render(g2d: Graphics2D) {
        Screen.drawTile(g2d, 2, 0, p - EDGE_LENGTH / 2, EDGE_LENGTH, EDGE_LENGTH)
        Building.drawBar(g2d, p.x, p.y - 10, health, 500, Color.RED)
    }

    override fun update() {
        if (this.health <= 0) {
            Die()
        }
        if (this.tick % 1100 == 0) {
            owner!!.entityList.add(Zombie(p, owner!!, teamNumber))
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
