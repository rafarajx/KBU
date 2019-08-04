package entity.building

import core.*
import entity.nature.Cloud
import entity.worker.Miller
import entity.worker.Stonemason
import entity.worker.Woodcutter
import fraction.Fraction
import math.AABB
import math.Circle
import math.vec2

class House(p: vec2, owner: Fraction, teamNumber: Int) : Building(owner) {
	
	companion object {
		val COST = Resources(10, 5, 0, 0)
		private const val EDGE_LENGTH = 32
		private const val HALF_EDGE = EDGE_LENGTH / 2
		private const val RANGE = 200
		const val MAX_HEALTH = 200f
	}

	var house = Sprite()
	
	init {
		super.p = p
		super.owner = owner
		super.teamNumber = teamNumber
		edgelength = EDGE_LENGTH
		halfedge = HALF_EDGE
		field = AABB(p, HALF_EDGE)
		range = Circle(p, RANGE)
		health = 200f
		owner.maxPopulation += 4
		sprites = arrayOf(house)
	}
	

	private fun freeMill(): Windmill? {
		for (m in owner.millList)
			if (m.miller[0] == null || m.miller[1] == null) return m
		return null
	}
	
	val addcloud = {
		Cloud(owner, p - vec2(3.0f, 5.0f)).add()
	}
	
	override fun renderGL() {



		if(health != MAX_HEALTH) drawBarGL(p - vec2(0f, 10f), health, MAX_HEALTH, Constants.RED)
		if (owner.population < owner.maxPopulation && owner.resources.food > 4)
			drawBarGL(p, (tick % 1000).toFloat(), 1000f, Constants.ORANGE)
	}
	
	override fun update() {
		if (health <= 0) die()
		
		if (tick % 1000 == 0 && owner.population < owner.maxPopulation && owner.resources.food > 4) {
			val windmill = freeMill()
			if (windmill != null) {
				val miller = Miller(p, owner, teamNumber)
				miller.windmill = windmill
				if (windmill.miller[0] == null) {
					windmill.miller[0] = miller
				} else if (windmill.miller[1] == null) {
					windmill.miller[1] = miller
				}
				miller.add()
				owner.resources.food -= 5
			} else if (countEntities(Stonemason::class.simpleName) < owner.quarryList.size) {
				Stonemason(p, owner, teamNumber).add()
				owner.resources.food -= 5
			} else if (countEntities(Woodcutter::class.simpleName) < owner.woodCampList.size) {
				Woodcutter(p, owner, teamNumber).add()
				owner.resources.food -= 5
			}
		}
		tick++
	}
	
	private fun countEntities(name: String?): Int {
		var n = 0
		for (i in owner.entityList.indices)
			if (owner.entityList[i]::class.simpleName == name) n++
		return n
	}
	
	override fun die() {
		owner.maxPopulation -= 4
		remove()
	}
	
	override fun add() {
		
		Timer.addEvery(1.6f, addcloud)
		Timer.addEvery(2.9f, addcloud)
		
		house.updatePosition(vec2(p.x.toInt(), p.y.toInt()) - HALF_EDGE, vec2(edgelength))
		house.updateTexCoords(vec2(0), vec2(16))
		owner.buildingList.add(this)
		owner.houseList.add(this)
		World.add(this)
	}
	
	@Synchronized override fun remove(){
		owner.buildingList.remove(this)
		owner.houseList.remove(this)
		World.remove(this)
		Timer.removeEvery(addcloud)
	}
	
	override fun hurt(dmg: Int) {
		health -= dmg
	}
}
