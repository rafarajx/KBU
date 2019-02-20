package entity.Worker

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import building.Mill
import core.Input
import core.Resources
import core.Screen
import entity.Entity
import fraction.Fraction
import gametype.Game
import math.vec2
import nature.Wheat
import sound.SimpleSound

class Miller(x: Int, y: Int, owner: Fraction, teamIndex: Int) : Entity() {
    internal var wheat = 0
    var mill: Mill? = null

    val nearestWheat: vec2
        get() {
            var D = Integer.MAX_VALUE.toDouble()
            val target = vec2(x, y)
            for (i in Game.natureList.indices) {
                val nature = Game.natureList[i]
                if (nature.javaClass.getName() == Wheat::class.java!!.getName()) {
                    if (Math.pow((mill!!.x - nature.x).toDouble(), 2.0) + Math.pow((mill!!.y - nature.y).toDouble(), 2.0) > 120 * 120) continue
                    val d = Math.pow((nature.x - x).toDouble(), 2.0) + Math.pow((nature.y - y).toDouble(), 2.0)
                    if (D > d) {
                        D = d
                        target.x = nature.x
                        target.y = nature.y
                    }
                }
            }
            return target
        }

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
            if (wheat == 0) {
                drawAnimatedEntity(g2d, 1, 8)
            } else {
                drawAnimatedEntity(g2d, 1, 9)
                Screen.drawTile(g2d, 9, 0, x.toInt() - edgeLength / 2, y.toInt() - edgeLength / 2 - 6, edgeLength, edgeLength)
            }
        }
        if (Input.isKeyDown(32)) {
            Entity.drawBar(g2d, x.toInt(), y.toInt(), health, 50, Color.RED)
        }
    }

    override fun update() {
        if (health < 0) die()
        field = Rectangle2D.Double((x - edgeLength / 2).toDouble(), (y - edgeLength / 2).toDouble(), edgeLength.toDouble(), edgeLength.toDouble())
        if (mill == null) {
            for (i in owner!!.buildingList.indices) {
                val building = owner!!.buildingList[i]
                if (building.javaClass.getName() == Mill::class.java!!.getName()) {
                    if ((building as Mill).miller == null) {
                        mill = building
                        mill!!.miller = this
                        break
                    }
                }
            }
        } else {
            if (wheat == 0) {
                if (tick % 15 == 0) {
                    target = nearestWheat
                }
                if (tick % 100 == 0) {
                    gatherWheat()
                }
            } else {
                if (tick % 15 == 0) {
                    target = vec2(mill!!.x.toFloat(), mill!!.y.toFloat())
                }
                if (tick % 100 == 0) {
                    leaveWheat()
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
        }
        if (tick % 1200 == 0) {
            owner!!.resources!!.pay(Resources(0, 0, 0, 1))
        }
        tick++
    }

    private fun leaveWheat() {
        if (mill!!.field!!.intersects(field!!)) {
            owner!!.resources!!.gain(Resources(0, 0, 0, wheat))
            wheat = 0
            return
        }
    }

    private fun gatherWheat() {
        for (i in Game.natureList.indices) {
            val nature = Game.natureList[i]
            if (nature::class.simpleName == Wheat::class.simpleName && nature.field!!.intersects(field!!)) {
                wheat = (nature as Wheat).gather()
                return
            }
        }
    }

    override fun die() {
        SimpleSound.Die.play()
        owner!!.population--
        owner!!.entityList.remove(this)
        mill!!.miller = null
    }
}
