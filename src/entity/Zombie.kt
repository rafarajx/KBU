package entity

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Input
import core.Screen
import fraction.Fraction
import math.vec2
import sound.SimpleSound

class Zombie(p: vec2, owner: Fraction, teamNumber: Int) : Entity() {

    init {
        super.p = p
        this.owner = owner
        this.teamNumber = teamNumber
        edgeLength = 16
        health = 50
        damage = 5
        speed = 0.4f
        update()
    }

    override fun render(g2d: Graphics2D) {
        Screen.drawTile(g2d, 3, 0, p - edgeLength / 2, edgeLength, edgeLength)
        if (Input.isKeyDown(32)) {
            Entity.drawBar(g2d, p, health, 50, Color.RED)
        }
    }

    override fun update() {
        if (health < 0) die()
        field = Rectangle2D.Float(p.x - edgeLength / 2, p.y - edgeLength / 2, edgeLength.toFloat(), edgeLength.toFloat())
        if (this.tick % 15 == 0) {
            target = getNearestEnemy(p, teamNumber)
        }
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
            fight(5, teamNumber, field as Rectangle2D)
        }
        tick++
    }

    override fun die() {
        SimpleSound.Die.play()
        owner!!.entityList.remove(this)
    }

    override fun hurt(dmg: Int) {
        this.health -= dmg
    }
}
