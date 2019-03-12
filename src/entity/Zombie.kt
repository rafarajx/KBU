package entity

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Input
import core.Screen
import fraction.Fraction
import gametype.Game
import math.AABB
import math.vec2
import sound.SimpleSound
import kotlin.math.sqrt

class Zombie(p: vec2, owner: Fraction, teamNumber: Int) : Entity() {
	
	init {
		super.p = p
		this.owner = owner
		this.teamNumber = teamNumber
		edgeLength = 16
		health = 50
		damage = 5
		speed = 0.4f
		field = AABB(p, edgeLength / 2)
	}
	
	override fun render(g2d: Graphics2D) {
		Screen.drawTile(g2d, 3, 0, p - edgeLength / 2, edgeLength, edgeLength)
		if (Game.showHealth) {
			Entity.drawBar(g2d, p, health, 50, Color.RED)
		}
	}
	
	override fun update() {
		if (health < 0) die()
		field = AABB(p, edgeLength / 2)
		if (tick % 15 == 0) target = getNearestEnemy(p, teamNumber)
		if (target != null) {
			val delta = target!! - p
			val d = sqrt(delta.square().sum())
			move = if (d == 0.0f) vec2(0.0f, 0.0f) else delta / d
			p += move * speed
		}
		if (tick % 60 == 0) fight(5, teamNumber, field as AABB)
		tick++
	}
	
	override fun die() {
		SimpleSound.die.play()
		owner!!.entityList.remove(this)
	}
	
	override fun hurt(dmg: Int) {
		this.health -= dmg
	}
}
