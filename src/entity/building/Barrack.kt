package entity.building

import core.*
import entity.Knight
import fraction.Fraction
import math.AABB
import math.Circle
import math.vec2
import java.awt.Color
import java.awt.Graphics2D

class Barrack(p: vec2, owner: Fraction, teamNumber: Int) : Building(owner) {
	
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
		edgelength = EDGE_LENGTH
		halfedge = HALF_EDGE
		field = AABB(p, HALF_EDGE)
		range = Circle(p, RANGE)
		health = 400f
	}
	
	var barracks = Sprite()
	
	override fun renderGL() {

		
		drawBarGL(p - vec2(0f, 10.0f), health, 400f, Constants.RED)
		if (owner.resources.food > 20 && owner.population < owner.maxPopulation)
			drawBarGL(p, (tick % 600).toFloat(), 600f, Constants.BLUE)
	}
	
	override fun update() {
		if (health <= 0) die()
		if (tick % 600 == 0) {
			if (owner.resources.enough(0, 0, 1, 5) && owner.population < owner.maxPopulation) {
				Knight(p, owner, teamNumber).add()
				owner.resources.pay(0, 0, 1, 5)
			}
		}
		tick++
	}
	
	override fun die() {
		remove()
	}
	
	override fun add(){
		G.batch.add(barracks)
		barracks.updatePosition(vec2(p.x.toInt(), p.y.toInt()) - HALF_EDGE, vec2(edgelength))
		barracks.updateTexCoords(vec2(0, 80), vec2(16))
		owner.buildingList.add(this)
		owner.barrackList.add(this)
	}
	
	@Synchronized override fun remove(){
		G.batch.remove(barracks)
		owner.buildingList.remove(this)
		owner.barrackList.remove(this)
	}
	
	override fun hurt(dmg: Int) {
		health -= dmg
	}
}
