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

class Tower(x: Int, y: Int, owner: Fraction, teamNumber: Int) : Building() {
    private val EDGE_LENGTH = 32
    private val RANGE = 300
    val damage = 10
    override var range: Rectangle2D? = null
    var target: vec2
    internal var health = 200

    init {
        this.x = x
        this.y = y
        this.owner = owner
        this.teamNumber = teamNumber
        this.target = vec2(x.toFloat(), y.toFloat())
        field = Rectangle2D.Float((x - EDGE_LENGTH / 2).toFloat(), (y - EDGE_LENGTH / 2).toFloat(), EDGE_LENGTH.toFloat(), EDGE_LENGTH.toFloat())
        range = Rectangle2D.Float((x - RANGE / 2).toFloat(), (y - RANGE / 2).toFloat(), RANGE.toFloat(), RANGE.toFloat())
        owner.buildingList.add(this)
        owner.towerList.add(this)
    }

    override fun render(g2d: Graphics2D) {
        Screen.drawTile(g2d, 0, 2, x - EDGE_LENGTH / 2, y - EDGE_LENGTH / 2, EDGE_LENGTH, EDGE_LENGTH)
        g2d.color = Color.RED
        if (target.x != x.toFloat() && target.y != y.toFloat())
            g2d.draw(Line2D.Float(x.toFloat(), y.toFloat(), target.x, target.y))
        Building.drawBar(g2d, x, y, health, 200, Color.RED)
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
                if (entity.teamIndex == teamNumber) break
                if (this.range!!.contains(entity.field!!)) {
                    target.x = entity.x
                    target.y = entity.y
                    entity.hurt(damage)
                    return
                }
            }
        }
        target.x = x.toFloat()
        target.y = y.toFloat()
    }

    private fun getNearestEnemy() {
        for (i in Game.fractionList.indices) {
            val fraction = Game.fractionList[i]
            for (j in fraction.entityList.indices) {
                val entity = fraction.entityList[j]
                if (entity.teamIndex == teamNumber) break
                if (range!!.contains(entity.field!!)) {
                    target.x = entity.x
                    target.y = entity.y
                    return
                }
            }
        }
        target.x = x.toFloat()
        target.y = y.toFloat()
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
