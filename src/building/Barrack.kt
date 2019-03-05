package building

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Resources
import core.Screen
import entity.Knight
import fraction.Fraction
import math.vec2

class Barrack(p: vec2, owner: Fraction, teamNumber: Int) : Building() {
	
	internal var health = 200
	
	companion object {
		val COST = Resources(30, 80, 10, 0)
		private const val EDGE_LENGTH = 32
		private const val RANGE = 150
	}
	
	init {
		super.p = p
		super.owner = owner
		super.teamNumber = teamNumber
		field = Rectangle2D.Float(p.x - EDGE_LENGTH / 2, p.y - EDGE_LENGTH / 2, EDGE_LENGTH.toFloat(), EDGE_LENGTH.toFloat())
		range = Rectangle2D.Float(p.x - RANGE / 2, p.y - RANGE / 2, RANGE.toFloat(), RANGE.toFloat())
		owner.buildingList.add(this)
		owner.barrackList.add(this)
	}
	
	override fun render(g2d: Graphics2D) {
		Screen.drawTile(g2d, 0, 5, p - 16.0f, EDGE_LENGTH, EDGE_LENGTH)
		Building.drawBar(g2d, p.x, p.y - 10.0f, health, 200, Color.RED)
		if (owner.resources.food > 20 && owner.population < owner.maxPopulation)
			Building.drawBar(g2d, p.x, p.y, tick % 600, 600, Color.BLUE)
	}
	
	override fun update() {
		if (health <= 0) die()
		if (tick % 600 == 0) {
			if (owner.resources.enough(0, 0, 1, 5) && owner.population < owner.maxPopulation) {
				owner.entityList.add(Knight(p.copy(), owner, teamNumber))
				owner.resources.pay(0, 0, 1, 5)
			}
		}
		tick++
	}
	
	fun die() {
		owner.buildingList.remove(this)
		owner.barrackList.remove(this)
	}
	
	override fun hurt(dmg: Int) {
		health -= dmg
	}
}
