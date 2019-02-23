package entity.Worker

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import building.Mill
import core.Input
import core.Screen
import entity.Entity
import fraction.Fraction
import gametype.Game
import math.vec2
import nature.Wheat
import sound.SimpleSound
import kotlin.math.hypot

class Miller(p: vec2, owner: Fraction, teamIndex: Int) : Entity() {
    private var wheat = 0
    var mill: Mill? = null

    private fun nearestWheat(): vec2 {
        var max = Float.MAX_VALUE
        var target = p.copy()
        for (i in Game.natureList.indices) {
            val nature = Game.natureList[i]
            if (nature::class.simpleName == Wheat::class.simpleName) {
                if (vec2(mill!!.p.x - nature.p.x, mill!!.p.y - nature.p.y).square().sum() > 120.0f * 120.0f) continue
                val d = (nature.p - p).length()
                if (max > d) {
                    max = d
                    target = nature.p.copy()
                }
            }
        }
        return target
    }

    init {
        super.p = p
        super.owner = owner
        super.teamNumber = teamIndex
        edgeLength = 16
        health = 50
        damage = 10
        speed = 0.75f
        owner.population++
        update()
    }

    override fun render(g2d: Graphics2D) {
        if (target == null) {
            Screen.drawTile(g2d, 1, 8, p - edgeLength / 2, edgeLength, edgeLength)
        } else {
            if (wheat == 0) {
                drawAnimatedEntity(g2d, 1, 8)
            } else {
                drawAnimatedEntity(g2d, 1, 9)
                Screen.drawTile(g2d, 9, 0, p.x.toInt() - edgeLength / 2, p.y.toInt() - edgeLength / 2 - 6, edgeLength, edgeLength)
            }
        }
        if (Input.isKeyDown(32)) {
            Entity.drawBar(g2d, p, health, 50, Color.RED)
        }
    }

    override fun update() {
        if (health < 0) die()
        field = Rectangle2D.Float(p.x - edgeLength / 2, p.y - edgeLength / 2, edgeLength.toFloat(), edgeLength.toFloat())
        if (mill == null) {
            for (i in owner!!.millList.indices) {
                val m = owner!!.millList[i]
                if (m.miller[0] == null) {
                    mill = m
                    mill!!.miller[0] = this
                    break
                }
            }
        } else {
            if (wheat == 0) {
                if (tick % 15 == 0) {
                    target = nearestWheat()
                }
                if (tick % 100 == 0) {
                    gatherWheat()
                }
            } else {
                if (tick % 15 == 0) {
                    target = vec2(mill!!.p.x, mill!!.p.y)
                }
                if (tick % 100 == 0) {
                    leaveWheat()
                }
            }
            if (target != null) {
                val delta = (target!! - p)
                val d = hypot(delta.x, delta.y)
                if (d == 0.0f) {
                    move = vec2(0.0f, 0.0f)
                } else {
                    move = delta / d
                }
                p += move * speed
            }
        }
        if (tick % 1200 == 0) {
            owner!!.resources.food--
        }
        tick++
    }

    private fun leaveWheat() {
        if (mill!!.field!!.intersects(field!!)) {
            owner!!.resources.food++
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
        mill!!.miller[0] = null
    }
}
