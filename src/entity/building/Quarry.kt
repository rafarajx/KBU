package entity.building

import core.Resources
import core.Screen
import fraction.Fraction
import math.AABB
import math.Circle
import math.vec2
import java.awt.Color
import java.awt.Graphics2D

class Quarry(p: vec2, owner: Fraction, teamIndex: Int) : Building() {
	
	companion object {
		val COST = Resources(15, 20, 0, 0)
		private const val EDGE_LENGTH = 32
		private const val HALF_EDGE = EDGE_LENGTH / 2
		private const val RANGE = 150
		private const val FULL_HEALTH = 200
	}
	
	init {
		super.p = p
		this.owner = owner
		this.teamNumber = teamIndex
		field = AABB(p, HALF_EDGE)
		range = Circle(p, RANGE)
		health = FULL_HEALTH
	}
	
	override fun render(g2d: Graphics2D) {
		g2d.drawImage(Screen.quarry, p.x.toInt() - HALF_EDGE, p.y.toInt() - HALF_EDGE, EDGE_LENGTH, EDGE_LENGTH, null)
		Building.drawBar(g2d, p, health, FULL_HEALTH, Color.RED)
	}
	
	override fun update() {
		if (health <= 0) die()
	}
	
	override fun die() {
		remove()
	}
	
	fun add(){
		owner!!.buildingList.add(this)
		owner!!.quarryList.add(this)
	}
	
	@Synchronized fun remove(){
		owner!!.buildingList.remove(this)
		owner!!.quarryList.remove(this)
	}
	
	override fun hurt(dmg: Int) {
		this.health -= dmg
	}
}
