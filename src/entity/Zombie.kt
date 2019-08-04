package entity

import core.Screen
import core.Sprite
import fraction.Fraction
import gametype.Game
import math.AABB
import math.vec2
import sound.SimpleSound
import java.awt.Color
import java.awt.Graphics2D
import kotlin.math.sqrt

class Zombie(p: vec2, owner: Fraction, teamNumber: Int) : Entity(owner) {
	
	init {
		super.p = p
		this.owner = owner
		this.teamNumber = teamNumber
		edgelength = 16
		health = 50f
		damage = 5
		speed = 0.4f
		field = AABB(p, edgelength / 2)
	}
	
	var zombie = Sprite()
	
	override fun update() {
		if (health < 0) die()
		field = AABB(p, edgelength / 2)
		if (tick % 15 == 0) target = getNearestEnemy(teamNumber)
		if (target != null) {
			val delta = target!!.p - p
			val d = sqrt(delta.square().sum())
			move = if (d == 0.0f) vec2(0.0f, 0.0f) else delta / d
			p += move * speed
		}
		if (tick % 60 == 0) fight(5, teamNumber, field as AABB)
		tick++
	}
	
	override fun die() {
		SimpleSound.die.play()
		owner.entityList.remove(this)
	}

	override fun add() {
		owner.entityList.add(this)
	}
	
	override fun remove() {
		owner.entityList.remove(this)
	}
	
	override fun hurt(dmg: Int) {
		this.health -= dmg
	}
}
