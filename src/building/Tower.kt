package building

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Line2D
import java.awt.geom.Rectangle2D

import core.Resources
import core.Screen
import fraction.Fraction
import gametype.Game
import math.vec2

class Tower(p: vec2, owner: Fraction, teamNumber: Int) : Building() {
    private val EDGE_LENGTH = 32
    private val RANGE = 300
    val damage = 10
    override var range: Rectangle2D? = null
    var target: vec2
    internal var health = 200

    init {
        super.p = p
        this.owner = owner
        this.teamNumber = teamNumber
        this.target = p.copy()
        field = Rectangle2D.Float(p.x - EDGE_LENGTH / 2, p.y - EDGE_LENGTH / 2, EDGE_LENGTH.toFloat(), EDGE_LENGTH.toFloat())
        range = Rectangle2D.Float(p.x - RANGE / 2, p.y - RANGE / 2, RANGE.toFloat(), RANGE.toFloat())
        owner.buildingList.add(this)
        owner.towerList.add(this)
    }

    override fun render(g2d: Graphics2D) {
        Screen.drawTile(g2d, 0, 2, p - EDGE_LENGTH / 2, EDGE_LENGTH, EDGE_LENGTH)
        g2d.color = Color.RED
        if (target.x != p.x && target.y != p.y)
            g2d.draw(Line2D.Float(p.x, p.y, target.x, target.y))
        Building.drawBar(g2d, p, health, 200, Color.RED)
    }

    override fun update() {
        if (health <= 0) die()
        if (tick % 2 == 0) {
            getNearestEnemy()
        }
        if (tick % 30 == 0) {
            shot()
        }
        tick++
    }

    private fun shot() {
        for (i in Game.fractionList.indices) {
            val fraction = Game.fractionList[i]
            for (j in fraction.entityList.indices) {
                val entity = fraction.entityList[j]
                if (entity.teamNumber == teamNumber) break
                if (this.range!!.contains(entity.field!!)) {
                    target = entity.p.copy()
                    entity.hurt(damage)
                    return
                }
            }
        }
        target = p.copy()
    }

    private fun getNearestEnemy() {
        for (i in Game.fractionList.indices) {
            val fraction = Game.fractionList[i]
            for (j in fraction.entityList.indices) {
                val entity = fraction.entityList[j]
                if (entity.teamNumber == teamNumber) break
                if (range!!.contains(entity.field!!)) {
                    target = entity.p.copy()
                    return
                }
            }
        }
        target = p.copy()
    }

    fun die() {
        owner!!.buildingList.remove(this)
        owner!!.towerList.remove(this)
    }

    override fun hurt(dmg: Int) {
        health -= dmg
    }

    companion object {
        val COST = Resources(20, 30, 5, 0)
    }
}
