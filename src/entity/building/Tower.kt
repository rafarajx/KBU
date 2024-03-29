package entity.building

import core.*
import fraction.Fraction
import math.AABB
import math.Circle
import math.vec2

class Tower(p: vec2, owner: Fraction, teamNumber: Int) : Building(owner) {

	companion object {
		private const val EDGE_LENGTH = 32
		private const val HALF_EDGE = EDGE_LENGTH / 2
		private const val RANGE = 150
		val COST = Resources(20, 30, 5, 0)
		const val MAX_HEALTH = 400f
	}
	
	var tower = Sprite()
	
	init {
		super.p = p
		this.owner = owner
		this.teamNumber = teamNumber
		target = null
		edgelength = EDGE_LENGTH
		halfedge = HALF_EDGE
		field = AABB(p, HALF_EDGE)
		range = Circle(p, RANGE)
		health = MAX_HEALTH
		damage = 10
		
		sprites = arrayOf(tower)
	}
	
	
	override fun renderGL() {



		//g2d.drawImage(Screen.tower, p.x.toInt() - HALF_EDGE, p.y.toInt() - HALF_EDGE, EDGE_LENGTH, EDGE_LENGTH, null)
		//g2d.color = Color.RED
		//if (target != null)
		//	g2d.draw(Line2D.Float(p.x, p.y, target!!.p.x, target!!.p.y))
		if(health != MAX_HEALTH) drawBarGL(p, health, MAX_HEALTH, Constants.RED)
		if(target != null){
			
		}
	}
	
	override fun update() {
		if (health <= 0) die()
		
		tick++
	}
	
	private fun shot() {
		for (fraction in World.fractionList) {
			if (fraction.teamNumber == teamNumber) continue
			for (entity in fraction.entityList) {
				if (range!!.contains(entity.p)) {
					target = entity
					entity.hurt(damage)
					return
				}
			}
		}
		target = null
	}
	
	private fun getNearestEnemy() {
		for (fraction in World.fractionList) {
			if (fraction.teamNumber == teamNumber) continue
			for (entity in fraction.entityList) {
				if (range!!.contains(entity.p)) {
					target = entity
					return
				}
			}
		}
		target = null
	}
	
	override fun die() {
		remove()
	}
	
	override fun add(){
		Timer.addEvery(0.5f) {
			shot()
		}
		
		Timer.addEvery(0.03f){
			getNearestEnemy()
		}
		
		tower.updatePosition(vec2(p.x.toInt(), p.y.toInt()) - HALF_EDGE, vec2(edgelength))
		tower.updateTexCoords(vec2(0, 32), vec2(16))
		owner.buildingList.add(this)
		owner.towerList.add(this)
		World.add(this)
	}
	
	@Synchronized override fun remove(){
		owner.buildingList.remove(this)
		owner.towerList.remove(this)
		World.remove(this)
	}
	
	override fun hurt(dmg: Int) {
		health -= dmg
	}
}
