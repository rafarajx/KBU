package entity.building

import core.*
import fraction.Fraction
import math.AABB
import math.Circle
import math.vec2
import java.awt.Color
import java.awt.Graphics2D

class Quarry(p: vec2, owner: Fraction, teamIndex: Int) : Building(owner) {
	
	companion object {
		val COST = Resources(15, 20, 0, 0)
		private const val EDGE_LENGTH = 32
		private const val HALF_EDGE = EDGE_LENGTH / 2
		private const val RANGE = 150
		private const val FULL_HEALTH = 200f
	}
	
	var quarry = Sprite()
	
	init {
		super.p = p
		this.owner = owner
		this.teamNumber = teamIndex
		edgelength = EDGE_LENGTH
		halfedge = halfedge
		field = AABB(p, HALF_EDGE)
		range = Circle(p, RANGE)
		health = FULL_HEALTH
	}
	
	override fun renderGL() {
		//g2d.drawImage(Screen.quarry, p.x.toInt() - HALF_EDGE, p.y.toInt() - HALF_EDGE, EDGE_LENGTH, EDGE_LENGTH, null)
		
		drawBarGL(p, health, FULL_HEALTH, Constants.RED)


	}
	
	override fun update() {
		if (health <= 0) die()
	}
	
	override fun die() {
		remove()
	}
	
	override fun add() {
		G.batch.add(quarry)
		quarry.updatePosition(vec2(p.x.toInt(), p.y.toInt()) - HALF_EDGE, vec2(edgelength))
		quarry.updateTexCoords(vec2(0, 64), vec2(16))
		owner.buildingList.add(this)
		owner.quarryList.add(this)
	}
	
	@Synchronized override fun remove(){
		G.batch.remove(quarry)
		owner.buildingList.remove(this)
		owner.quarryList.remove(this)
	}
	
	override fun hurt(dmg: Int) {
		this.health -= dmg
	}
}
