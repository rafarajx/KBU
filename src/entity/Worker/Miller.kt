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
import kotlin.math.sqrt

class Miller(p: vec2, owner: Fraction, teamNumber: Int) : Entity() {
    private var wheat = 0
    var mill: Mill? = null

    init {
        super.p = p
        super.owner = owner
        super.teamNumber = teamNumber
        edgeLength = 16
        health = 50
        damage = 10
        speed = 0.75f
        owner.population++
        field = Rectangle2D.Float(p.x - edgeLength / 2, p.y - edgeLength / 2, edgeLength.toFloat(), edgeLength.toFloat())
    }

    private fun nearestWheat(): vec2 {
        var max = Float.MAX_VALUE
        var target = p.copy()
        for (wheat in Game.wheatList) {
            if (vec2(mill!!.p.x - wheat.p.x, mill!!.p.y - wheat.p.y).square().sum() > 120.0f * 120.0f) continue
            val d = (wheat.p - p).length()
            if (max > d) {
                max = d
                target = wheat.p.copy()
            }
        }
        return target
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
            for (m in owner!!.millList) {
                if (m.miller[0] == null) {
                    mill = m
                    mill!!.miller[0] = this
                    break
                } else if (m.miller[1] == null) {
                    mill = m
                    mill!!.miller[1] = this
                    break
                }
            }
        } else {
            if (wheat == 0) {
                if (tick % 15 == 0) {
                    target = nearestWheat()
                }
                if (tick % 400 == 0) {
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
                val d = sqrt(delta.square().sum())
                if (d == 0.0f) {
                    move = vec2(0.0f, 0.0f)
                } else {
                    move = delta / d
                }
                p += move * speed
            }
        }
        if (tick % 1500 == 0) {
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
        if(mill != null) {
            if (mill!!.miller[0] != null) {
                mill!!.miller[0] = null
            } else if (mill!!.miller[1] != null) {
                mill!!.miller[1] = null
            }
        }
    }
}
