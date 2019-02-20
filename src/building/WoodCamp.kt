package building

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Resources
import core.Screen
import fraction.Fraction
import gametype.Game
import nature.Cloud

class WoodCamp(x: Int, y: Int, owner: Fraction, teamIndex: Int) : Building() {
    private val EDGE_LENGTH = 32
    private val RANGE = 150
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
        owner.woodCampList.add(this)
    }

    override fun render(g2d: Graphics2D) {
        Screen.drawTile(g2d, 0, 3, x - EDGE_LENGTH / 2, y - EDGE_LENGTH / 2, EDGE_LENGTH, EDGE_LENGTH)
        Building.drawBar(g2d, x, y, health, 200, Color.RED)
    }

    override fun update() {
        if (health <= 0) Die()

        if (tick % 100 == 1) {
            Game.natureList.add(Cloud(this.x - 3, this.y - 15))
        }
        if (tick % 173 == 2) {
            Game.natureList.add(Cloud(this.x - 3, this.y - 15))
        }
        tick++
    }

    fun Die() {
        owner!!.buildingList.remove(this)
        owner!!.woodCampList.remove(this)
    }

    override fun hurt(dmg: Int) {
        this.health -= dmg
    }

    companion object {
        val COST = Resources(20, 5, 0, 0)
    }
}
