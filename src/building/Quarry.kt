package building

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Resources
import core.Screen
import fraction.Fraction
import math.vec2

class Quarry(p: vec2, owner: Fraction, teamIndex: Int) : Building() {
	internal var health = 200
	
	companion object {
		val COST = Resources(15, 20, 0, 0)
		private const val EDGE_LENGTH = 32
		private const val RANGE = 150
	}
	
	init {
		super.p = p
		this.owner = owner
		this.teamNumber = teamIndex
		this.field = Rectangle2D.Float(p.x - EDGE_LENGTH / 2, p.y - EDGE_LENGTH / 2, EDGE_LENGTH.toFloat(), EDGE_LENGTH.toFloat())
		this.range = Rectangle2D.Float(p.x - RANGE / 2, p.y - RANGE / 2, RANGE.toFloat(), RANGE.toFloat())
		owner.buildingList.add(this)
		owner.quarryList.add(this)
	}
	
	override fun render(g2d: Graphics2D) {
		Screen.drawTile(g2d, 0, 4, p - EDGE_LENGTH / 2, EDGE_LENGTH, EDGE_LENGTH)
		Building.drawBar(g2d, p, health, 200, Color.RED)
	}
	
	override fun update() {
		if (health <= 0) die()
	}
	
	fun die() {
		owner.buildingList.remove(this)
		owner.quarryList.remove(this)
	}
	
	override fun hurt(dmg: Int) {
		this.health -= dmg
	}
}
