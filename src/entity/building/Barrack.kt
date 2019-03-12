package entity.building

import core.Resources
import core.Screen
import entity.Knight
import fraction.Fraction
import math.AABB
import math.Circle
import math.vec2
import java.awt.Color
import java.awt.Graphics2D

class Barrack(p: vec2, owner: Fraction, teamNumber: Int) : Building() {
	
	companion object {
		val COST = Resources(30, 80, 10, 0)
		private const val EDGE_LENGTH = 32
		private const val HALF_EDGE = EDGE_LENGTH / 2
		private const val RANGE = 200
	}
	
	init {
		super.p = p
		super.owner = owner
		super.teamNumber = teamNumber
		field = AABB(p, HALF_EDGE)
		range = Circle(p, RANGE)
		health = 400
	}
	
	override fun render(g2d: Graphics2D) {
		g2d.drawImage(Screen.barracks, p.x.toInt() - HALF_EDGE, p.y.toInt() - HALF_EDGE, EDGE_LENGTH, EDGE_LENGTH, null)
		Building.drawBar(g2d, p.x, p.y - 10.0f, health, 400, Color.RED)
		if (owner!!.resources.food > 20 && owner!!.population < owner!!.maxPopulation)
			Building.drawBar(g2d, p.x, p.y, tick % 600, 600, Color.BLUE)
	}
	
	override fun update() {
		if (health <= 0) die()
		if (tick % 600 == 0) {
			if (owner!!.resources.enough(0, 0, 1, 5) && owner!!.population < owner!!.maxPopulation) {
				Knight(p, owner!!, teamNumber).add()
				owner!!.resources.pay(0, 0, 1, 5)
			}
		}
		tick++
	}
	
	override fun die() {
		remove()
	}
	
	fun add(){
		owner!!.buildingList.add(this)
		owner!!.barrackList.add(this)
	}
	
	@Synchronized fun remove(){
		owner!!.buildingList.remove(this)
		owner!!.barrackList.remove(this)
	}
	
	override fun hurt(dmg: Int) {
		health -= dmg
	}
}
