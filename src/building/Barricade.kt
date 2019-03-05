package building

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Resources
import core.Screen
import fraction.Fraction
import math.vec2

class Barricade(p: vec2, owner: Fraction, teamNumber: Int) : Building() {
	
	internal var health = 250
	
	companion object {
		val COST = Resources(10, 20, 1, 0)
		private const val EDGE_LENGTH = 16
		private const val RANGE = 100
	}
	
	init {
		super.p = p
		super.owner = owner
		super.teamNumber = teamNumber
		field = Rectangle2D.Float(p.x - EDGE_LENGTH / 2, p.y - EDGE_LENGTH / 2, EDGE_LENGTH.toFloat(), EDGE_LENGTH.toFloat())
		range = Rectangle2D.Float(p.x - RANGE / 2, p.y - RANGE / 2, RANGE.toFloat(), RANGE.toFloat())
		owner.buildingList.add(this)
		owner.barricadeList.add(this)
	}
	
	override fun render(g2d: Graphics2D) {
		Screen.drawTile(g2d, 0, 6, p - EDGE_LENGTH / 2, EDGE_LENGTH, EDGE_LENGTH)
		Building.drawBar(g2d, p, health, 250, Color.RED)
	}
	
	override fun update() {
		if (health <= 0) die()
	}
	
	fun die() {
		owner.buildingList.remove(this)
		owner.barricadeList.remove(this)
	}
	
	override fun hurt(dmg: Int) {
		health -= dmg
	}
}
