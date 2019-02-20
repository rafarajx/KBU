package entity.Worker

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import building.WoodCamp
import core.Input
import core.Resources
import core.Screen
import entity.Entity
import fraction.Fraction
import gametype.Game
import nature.Tree

class Woodcutter(x: Int, y: Int, owner: Fraction, teamIndex: Int) : Entity() {
    private var hasWood: Boolean = false

    init {
        super.x = x.toFloat()
        super.y = y.toFloat()
        super.owner = owner
        super.teamIndex = teamIndex
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
            if (hasWood) {
                drawAnimatedEntity(g2d, 1, 9)
                Screen.drawTile(g2d, 7, 0, x.toInt() - 8, y.toInt() - 14, edgeLength, edgeLength)
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
        field = Rectangle2D.Double((x - edgeLength / 2).toDouble(), (y - edgeLength / 2).toDouble(), edgeLength.toDouble(), edgeLength.toDouble())
        if (hasWood) {
            if (tick % 15 == 0) {
                target = getNearestBuilding(WoodCamp::class.simpleName)
            }
            if (tick % 100 == 0) {
                leaveWood()
            }
        } else {
            if (tick % 15 == 0) {
                target = getNearestNature(Tree::class.simpleName)
            }
            if (tick % 100 == 0) {
                chopTree()
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

    private fun chopTree() {
        for (i in Game.natureList.indices) {
            val nature = Game.natureList[i]
            if (nature.field!!.intersects(field!!)) {
                nature.gatherResources(1)
                hasWood = true
                return
            }
        }
    }

    private fun leaveWood() {
        for (j in owner!!.buildingList.indices) {
            val building = owner!!.buildingList[j]
            if (building.javaClass.getName() == WoodCamp::class.java!!.getName() && building.field!!.intersects(field!!)) {
                owner!!.resources!!.gain(Resources(1, 0, 0, 0))
                hasWood = false
                return
            }
        }
    }
}
