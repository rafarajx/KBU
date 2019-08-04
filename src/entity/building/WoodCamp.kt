package entity.building

import core.*
import entity.nature.Cloud
import fraction.Fraction
import math.AABB
import math.Circle
import math.vec2

class WoodCamp(p: vec2, owner: Fraction, teamIndex: Int) : Building(owner) {

	companion object {
		private const val EDGE_LENGTH = 32
		private const val HALF_EDGE = EDGE_LENGTH / 2
		private const val RANGE = 200
		val COST = Resources(20, 5, 0, 0)
		private const val MAX_HEALTH = 200f
	}
	
	var woodcamp = Sprite()
	
	init {
		super.p = p
		this.owner = owner
		this.teamNumber = teamIndex
		edgelength = EDGE_LENGTH
		halfedge = HALF_EDGE
		field = AABB(p, HALF_EDGE)
		range = Circle(p, RANGE)
		health = MAX_HEALTH
		sprites = arrayOf(woodcamp)
	}
	

	override fun renderGL() {
		//g2d.drawImage(Screen.woodCamp, p.x.toInt() - HALF_EDGE, p.y.toInt() - HALF_EDGE, EDGE_LENGTH, EDGE_LENGTH, null)
		
		if(health != MAX_HEALTH) drawBarGL(p, health, MAX_HEALTH, Constants.RED)
		
	}

	override fun update() {
		if (health <= 0) die()
		
		tick++
	}

	override fun die() {
		remove()
	}
	
	override fun add(){
		Timer.addEvery(1.6f) {
			Cloud(owner, p - vec2(3.0f, 5.0f)).add()
		}
		
		Timer.addEvery(2.9f) {
			Cloud(owner, p - vec2(3.0f, 5.0f)).add()
		}
		
		woodcamp.updatePosition(vec2(p.x.toInt(), p.y.toInt()) - HALF_EDGE, vec2(edgelength))
		woodcamp.updateTexCoords(vec2(0, 48), vec2(16))
		owner.buildingList.add(this)
		owner.woodCampList.add(this)
		World.add(this)
		//owner!!.woodCampTree.add(this)
	}
	
	@Synchronized override fun remove() {
		owner.buildingList.remove(this)
		owner.woodCampList.remove(this)
		World.remove(this)
		//qt!!.remove()
	}

	override fun hurt(dmg: Int) {
		this.health -= dmg
	}
}
