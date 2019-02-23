package entity

import java.awt.Color
import java.awt.Graphics2D
import java.awt.event.KeyEvent
import java.awt.geom.Rectangle2D

import core.Input
import core.Screen
import fraction.Fraction
import math.vec2
import sound.SimpleSound

class Knight(p: vec2, owner: Fraction, teamIndex: Int) : Entity() {

    init {
        super.p = p
        super.owner = owner
        super.teamNumber = teamIndex
        edgeLength = 16
        health = 100
        damage = 20
        speed = 0.6f
        owner.population++
        update()
    }

    override fun render(g2d: Graphics2D) {
        if (target == null) {
            Screen.drawTile(g2d, 1, 10, p - (edgeLength / 2), edgeLength, edgeLength)
        } else {
            drawAnimatedEntity(g2d, 1, 10)
            if (Input.isKeyDown(KeyEvent.VK_SPACE)) {
                Entity.drawBar(g2d, p, health, 100, Color.RED)
            }
        }
    }

    override fun update() {
        if (health < 0) die()
        field = Rectangle2D.Float(p.x - edgeLength / 2, p.y - edgeLength / 2, edgeLength.toFloat(), edgeLength.toFloat())
        if (this.tick % 20 == 0)
            target = getNearestEnemy(p, teamNumber)

        if (target != null) {
            val delta = target!! - p
            val d = delta.square().sum()
            if (d == 0.0f) {
                move = vec2(0.0f, 0.0f)
            } else {
                move = delta / d
            }
            p += move * speed
        }
        if (tick % 60 == 0) {
            fight(20, teamNumber, field as Rectangle2D)
        }
        if (tick % 1200 == 0) {
            if (owner!!.resources.food > 0)
                owner!!.resources.pay(0, 0, 0, 1)
            else
                health--
        }
        tick++
    }

    override fun die() {
        SimpleSound.Die.play()
        owner!!.population--
        owner!!.entityList.remove(this)
    }
}
