package entity.building

import core.Resources
import core.Screen
import fraction.Fraction
import math.AABB
import math.Circle
import math.vec2
import java.awt.Color
import java.awt.Graphics2D

class Barricade(p: vec2, owner: Fraction, teamNumber: Int) : Building() {
	
	companion object {
		val COST = Resources(10, 20, 1, 0)
		private const val EDGE_LENGTH = 16
		private const val HALF_EDGE = EDGE_LENGTH / 2
		private const val RANGE = 200
	}
	
	init {
		super.p = p
		super.owner = owner
		super.teamNumber = teamNumber
		field = AABB(p, HALF_EDGE)
		range = Circle(p, RANGE)
		health = 500
	}
	
	override fun render(g2d: Graphics2D) {
		g2d.drawImage(Screen.barricade, p.x.toInt() - HALF_EDGE, p.y.toInt() - HALF_EDGE, EDGE_LENGTH, EDGE_LENGTH, null)
		Building.drawBar(g2d, p, health, 500, Color.RED)
	}
	
	override fun update() {
		if (health <= 0) die()
	}
	
	override fun die() {
		remove()
	}
	
	fun add(){
		owner!!.buildingList.add(this)
		owner!!.barricadeList.add(this)
	}
	
	@Synchronized fun remove(){
		owner!!.buildingList.remove(this)
		owner!!.barricadeList.remove(this)
	}
	
	override fun hurt(dmg: Int) {
		health -= dmg
	}
}
