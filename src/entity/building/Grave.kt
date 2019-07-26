package entity.building

import core.Resources
import core.Screen
import entity.Zombie
import fraction.Fraction
import math.AABB
import math.vec2
import java.awt.Color
import java.awt.Graphics2D

class Grave(p: vec2, owner: Fraction, teamIndex: Int) : Building(owner) {

	companion object {
		private const val EDGE_LENGTH = 16
		private const val HALF_EDGE = EDGE_LENGTH / 2
		private const val RANGE = 150
		val COST = Resources(0)
		private const val FULL_HEALTH = 500f
	}
	
	init {
		super.p = p
		super.owner = owner
		super.teamNumber = teamIndex
		field = AABB(p, HALF_EDGE)
		health = FULL_HEALTH
		
	}
	
	override fun update() {
		if (health <= 0) die()
		
		if (tick % 1100 == 0) {
			val z = Zombie(p, owner, teamNumber)
			owner.entityList.add(z)
		}
		tick++
	}

	override fun die() {
		remove()
	}
	
	override fun add(){
		owner.buildingList.add(this)
	}
	
	@Synchronized override fun remove(){
		owner.buildingList.remove(this)
		//owner.graveList.remove(this);
	}

	override fun hurt(dmg: Int) {
		health -= dmg
	}
}
