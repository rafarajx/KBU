package entity

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Input
import core.Screen
import fraction.Fraction
import sound.SimpleSound

class Zombie(x: Int, y: Int, owner: Fraction, teamIndex: Int) : Entity() {

    val teamNumber: Int

    init {
        this.x = x.toFloat()
        this.y = y.toFloat()
        this.owner = owner
        this.teamNumber = teamIndex
        edgeLength = 16
        health = 50
        damage = 5
        speed = 0.4f
        update()
    }

    override fun render(g2d: Graphics2D) {
        Screen.drawTile(g2d, 3, 0, x.toInt() - edgeLength / 2, y.toInt() - edgeLength / 2, edgeLength, edgeLength)
        if (Input.isKeyDown(32)) {
            Entity.drawBar(g2d, x.toInt(), y.toInt(), health, 50, Color.RED)
        }
    }

    override fun update() {
        if (health < 0) die()
        field = Rectangle2D.Float(x - edgeLength / 2, y - edgeLength / 2, edgeLength as Float, edgeLength as Float)
        if (this.tick % 15 == 0) {
            target = getNearestEnemy(x, y, teamNumber)
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
