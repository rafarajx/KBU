package entity

import java.awt.Color
import java.awt.Graphics2D
import java.awt.event.KeyEvent
import java.awt.geom.Rectangle2D

import core.Input
import core.Screen
import fraction.Fraction
import sound.SimpleSound

class Knight(x: Int, y: Int, owner: Fraction, teamIndex: Int) : Entity() {

    init {
        super.x = x.toFloat()
        super.y = y.toFloat()
        super.owner = owner
        super.teamIndex = teamIndex
        edgeLength = 16
        health = 100
        damage = 20
        speed = 0.6f
        owner.population++
        update()
    }

    override fun render(g2d: Graphics2D) {
        if (target == null) {
            Screen.drawTile(g2d, 1, 10, x.toInt() - edgeLength / 2, y.toInt() - edgeLength / 2, edgeLength, edgeLength)
        } else {
            drawAnimatedEntity(g2d, 1, 10)
            if (Input.isKeyDown(KeyEvent.VK_SPACE)) {
                Entity.drawBar(g2d, x.toInt(), y.toInt(), health, 100, Color.RED)
            }
        }
    }

    override fun update() {
        if (health < 0) die()
        field = Rectangle2D.Float(x - edgeLength / 2, y - edgeLength / 2, edgeLength.toFloat(), edgeLength.toFloat())
        if (this.tick % 20 == 0)
            target = getNearestEnemy(x, y, teamIndex)

        if (target != null) {
            val dx = target!!.x - x
            val dy = target!!.y - y
            val d = Math.hypot(dx.toDouble(), dy.toDouble()).toFloat()
            if (d == 0f) {
                my = 0f
                mx = my
            } else {
                mx = dx / d
                my = dy / d
            }
            x += mx * speed
            y += my * speed
        }
        if (tick % 60 == 0) {
            fight(20, teamIndex, field as Rectangle2D)
        }
        if (tick % 1200 == 0) {
            if (owner!!.resources!!.food > 0)
                owner!!.resources!!.pay(0, 0, 0, 1)
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
