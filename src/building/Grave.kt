package building

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Resources
import core.Screen
import entity.Zombie
import fraction.Fraction
import math.vec2

class Grave(p: vec2, owner: Fraction, teamIndex: Int) : Building() {
	internal var health = 500

	companion object {
		private const val EDGE_LENGTH = 16
		private const val RANGE = 150
		val COST = Resources(0)
	}
	
	init {
		super.p = p
		super.owner = owner
		super.teamNumber = teamIndex
		field = Rectangle2D.Float(p.x - EDGE_LENGTH / 2, p.y - EDGE_LENGTH / 2, EDGE_LENGTH.toFloat(), EDGE_LENGTH.toFloat())
		range = Rectangle2D.Float(p.x - RANGE / 2, p.y - RANGE / 2, RANGE.toFloat(), RANGE.toFloat())
		owner.buildingList.add(this)
	}
	
	override fun render(g2d: Graphics2D) {
		Screen.drawTile(g2d, 2, 0, p - EDGE_LENGTH / 2, EDGE_LENGTH, EDGE_LENGTH)
		Building.drawBar(g2d, p.x, p.y - 10, health, 500, Color.RED)
	}

	override fun update() {
		if (health <= 0) die()
		
		if (tick % 1100 == 0) {
			var z = Zombie(p, owner, teamNumber)
			owner.entityList.add(z)
		}
		tick++
	}

	fun die() {
		owner.buildingList.remove(this)
		//owner.graveList.remove(this);
	}

	override fun hurt(dmg: Int) {
		health -= dmg
	}
}
