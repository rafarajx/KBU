package entity.building

import core.Resources
import core.Screen
import entity.Zombie
import fraction.Fraction
import math.AABB
import math.vec2
import java.awt.Color
import java.awt.Graphics2D

class Grave(p: vec2, owner: Fraction, teamIndex: Int) : Building() {

	companion object {
		private const val EDGE_LENGTH = 16
		private const val HALF_EDGE = EDGE_LENGTH / 2
		private const val RANGE = 150
		val COST = Resources(0)
	}
	
	init {
		super.p = p
		super.owner = owner
		super.teamNumber = teamIndex
		field = AABB(p, HALF_EDGE)
		health = 500
		
	}
	
	override fun render(g2d: Graphics2D) {
		Screen.drawTile(g2d, 2, 0, p - HALF_EDGE, EDGE_LENGTH, EDGE_LENGTH)
		Building.drawBar(g2d, p.x, p.y - 10, health, 500, Color.RED)
	}

	override fun update() {
		if (health <= 0) die()
		
		if (tick % 1100 == 0) {
			val z = Zombie(p, owner!!, teamNumber)
			owner!!.entityList.add(z)
		}
		tick++
	}

	override fun die() {
		remove()
	}
	
	fun add(){
		owner!!.buildingList.add(this)
	}
	
	@Synchronized fun remove(){
		owner!!.buildingList.remove(this)
		//owner.graveList.remove(this);
	}

	override fun hurt(dmg: Int) {
		health -= dmg
	}
}
