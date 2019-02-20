package entity

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D
import java.util.Random

import core.Screen
import fraction.Fraction
import gametype.Game
import math.vec2
import sound.SimpleSound

open class Entity {
    var x: Float = 0.0f
    var y: Float = 0.0f
    protected var mx: Float = 0.0f
    protected var my: Float = 0.0f
    protected var owner: Fraction? = null
    var field: Rectangle2D? = null
    protected var range: Ellipse2D? = null
    var teamIndex: Int = 0
    protected var health: Int = 0
    protected var damage: Int = 0
    protected var speed: Float = 0.0f
    protected var target: vec2? = null
    protected var edgeLength: Int = 0
    protected var tick = 1

    open fun render(g2d: Graphics2D) {}

    fun drawAnimatedEntity(g2d: Graphics2D, tileX: Int, tileY: Int) {
        val sx = x.toInt() - edgeLength / 2
        val sy = y.toInt() - edgeLength / 2
        if (mx == 0f && my == 0f) {
            Screen.drawTile(g2d, tileX, tileY, sx, sy, edgeLength, edgeLength)
            return
        }
        val frame = tick % 48 / 16
        var offset = 0
        if (my < 0 && my > Math.abs(mx)) offset = 3 //up
        if (my > 0 && my > Math.abs(mx)) offset = 0 //down
        if (mx < 0 && mx > Math.abs(my)) offset = 9 //left
        if (mx > 0 && mx > Math.abs(my)) offset = 6 //right
        Screen.drawTile(g2d, tileX + offset + frame, tileY, sx, sy, edgeLength, edgeLength)
    }

    open fun update() {

    }

    open fun hurt(dmg: Int) {
        health -= dmg
    }

    fun getNearestEnemy(x: Float, y: Float, TeamIndex: Int): vec2 {
        var D = Integer.MAX_VALUE.toFloat()
        val targetPosition = vec2(x, y)
        for (i in Game.fractionList.indices) {
            val fraction = Game.fractionList[i]
            for (j in fraction.entityList.indices) {
                val entity = fraction.entityList[j]
                if (entity.teamIndex == TeamIndex) break
                val dx = entity.x - x
                val dy = entity.y - y
                val d = dx * dx + dy * dy
                if (D > d) {
                    D = d
                    targetPosition.x = entity.x
                    targetPosition.y = entity.y
                }
            }
            for (j in fraction.buildingList.indices) {
                val building = fraction.buildingList[j]
                if (building.teamNumber == TeamIndex) break
                val dx = building.x - x
                val dy = building.y - y
                val d = dx * dx + dy * dy
                if (D > d) {
                    D = d
                    targetPosition.x = building.x.toFloat()
                    targetPosition.y = building.y.toFloat()
                }
            }
        }
        return targetPosition
    }

    fun getNearestNature(name: String?): vec2 {
        var D = Integer.MAX_VALUE.toFloat()
        val target = vec2(x, y)
        for (i in Game.natureList.indices) {
            val nature = Game.natureList[i]
            if (nature::class.simpleName == name) {
                val dx = nature.x - x
                val dy = nature.y - y
                val d = dx * dx + dy * dy
                if (D > d) {
                    D = d
                    target.x = nature.x
                    target.y = nature.y
                }
            }
        }
        return target
    }

    fun getNearestBuilding(name: String?): vec2 {
        var D = Integer.MAX_VALUE.toFloat()
        val target = vec2(x, y)
        for (i in owner!!.buildingList.indices) {
            val building = owner!!.buildingList[i]
            if (building::class.simpleName == name) {
                val dx = building.x - x
                val dy = building.y - y
                val d = dx * dx + dy * dy
                if (D > d) {
                    D = d
                    target.x = building.x.toFloat()
                    target.y = building.y.toFloat()
                }
            }
        }
        return target
    }

    fun fight(damage: Int, TeamIndex: Int, field: Rectangle2D) {
        for (i in Game.fractionList.indices) {
            val fraction = Game.fractionList[i]
            for (j in fraction.entityList.indices) {
                val entity = fraction.entityList[j]
                if (entity.teamIndex == TeamIndex) break
                if (entity.field!!.intersects(field)) {
                    entity.hurt(damage)
                    return
                }
            }
            for (j in fraction.buildingList.indices) {
                val building = fraction.buildingList[j]
                if (building.teamNumber == TeamIndex) break
                if (building.field!!.intersects(field)) {
                    building.hurt(damage)
                    return
                }
            }
        }
    }

    open fun die() {
        SimpleSound.Die.play()
        owner!!.population--
        owner!!.entityList.remove(this)
    }

    companion object {
        var random = Random()

        fun drawBar(g2d: Graphics2D, x: Int, y: Int, current: Int, max: Int, c: Color) {
            g2d.color = c
            g2d.fillRect(x - 6, y, (current.toFloat() / max * 13).toInt(), 3)
            g2d.color = Color.BLACK
            g2d.drawRect(x - 6, y, (current.toFloat() / max * 13).toInt(), 3)
        }
    }
}
