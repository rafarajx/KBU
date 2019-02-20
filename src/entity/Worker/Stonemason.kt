package entity.Worker

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import building.Quarry
import core.Input
import core.Resources
import core.Screen
import entity.Entity
import fraction.Fraction
import gametype.Game
import nature.Rock

class Stonemason(x: Int, y: Int, owner: Fraction, teamIndex: Int) : Entity() {
    var hasStone: Boolean = false

    init {
        this.x = x.toFloat()
        this.y = y.toFloat()
        this.owner = owner
        this.teamIndex = teamIndex
        edgeLength = 16
        health = 50
        damage = 10
        speed = 0.75f
        owner.population++
        update()
    }

    override fun render(g2d: Graphics2D) {
        if (target == null) {
            Screen.drawTile(g2d, 1, 8, x.toInt() - edgeLength / 2, y.toInt() - edgeLength / 2, edgeLength, edgeLength)
        } else {
            if (this.hasStone) {
                drawAnimatedEntity(g2d, 1, 9)
                Screen.drawTile(g2d, 8, 0, x.toInt() - 8, y.toInt() - 14, 16, 16)
            } else {
                drawAnimatedEntity(g2d, 1, 8)
            }
        }
        if (Input.isKeyDown(32)) {
            Entity.drawBar(g2d, x.toInt(), y.toInt(), health, 50, Color.RED)
        }
    }

    override fun update() {
        if (health < 0) die()
        field = Rectangle2D.Float(x - edgeLength / 2, y - edgeLength / 2, edgeLength.toFloat(), edgeLength.toFloat())
        if (hasStone) {
            if (tick % 15 == 0) {
                target = getNearestBuilding(Quarry::class.simpleName)
            }
            if (this.tick % 100 == 0) {
                leaveStone()
            }
        } else {
            if (tick % 15 == 0) {
                target = getNearestNature(Rock::class.simpleName)
            }
            if (this.tick % 100 == 0) {
                gatherStone()
            }
        }
        if (target != null) {
            val dx = (target!!.x - x).toDouble()
            val dy = (target!!.y - y).toDouble()
            val d = Math.sqrt(Math.pow(dx, 2.0) + Math.pow(dy, 2.0))
            if (d == 0.0) {
                my = 0f
                mx = my
            } else {
                mx = (dx / d).toFloat()
                my = (dy / d).toFloat()
            }
            x += mx * speed
            y += my * speed
        }
        if (tick % 1200 == 0) {
            owner!!.resources!!.pay(Resources(0, 0, 0, 1))
        }
        tick++
    }

    private fun gatherStone() {
        for (i in Game.natureList.indices) {
            val nature = Game.natureList[i]
            if (nature::class.simpleName == Rock::class.simpleName && nature.field!!.intersects(field!!)) {
                nature.gatherResources(1)
                hasStone = true
                return
            }
        }
    }

    private fun leaveStone() {
        for (i in owner!!.buildingList.indices) {
            val building = owner!!.buildingList[i]
            if (building.javaClass.simpleName == Quarry::class.simpleName && building.field!!.intersects(field!!)) {
                val n = Entity.random.nextInt(4)
                if (n == 0)
                    owner!!.resources.gain(Resources(0, 0, 1, 0))
                else
                    owner!!.resources.gain(Resources(0, 1, 0, 0))
                hasStone = false
                return
            }
        }
    }
}
