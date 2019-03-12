package entity.building

import core.Resources
import core.Screen
import entity.nature.Cloud
import entity.worker.Miller
import entity.worker.Stonemason
import entity.worker.Woodcutter
import fraction.Fraction
import gametype.Game
import math.AABB
import math.Circle
import math.vec2
import java.awt.Color
import java.awt.Graphics2D

class House(p: vec2, owner: Fraction, teamNumber: Int) : Building() {
	
	companion object {
		val COST = Resources(10, 5, 0, 0)
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
		health = 200
		owner.maxPopulation += 4
	}
	
	private fun freeMill(): Windmill? {
		for (m in owner!!.millList)
			if (m.miller[0] == null || m.miller[1] == null) return m
		return null
	}
	
	override fun render(g2d: Graphics2D) {
		g2d.drawImage(Screen.house, p.x.toInt() - HALF_EDGE, p.y.toInt() - HALF_EDGE, EDGE_LENGTH, EDGE_LENGTH, null)
		Building.drawBar(g2d, p.x, p.y - 10, health, 200, Color.RED)
		if (owner!!.population < owner!!.maxPopulation && owner!!.resources.food > 4)
			Building.drawBar(g2d, p, tick % 1000, 1000, Color.ORANGE)
	}
	
	override fun update() {
		if (health <= 0) die()
		if (tick % 100 == 0 || tick % 173 == 0) {
			Game.natureList.add(Cloud(p - vec2(3.0f, 5.0f)))
		}
		if (tick % 1000 == 0 && owner!!.population < owner!!.maxPopulation && owner!!.resources.food > 4) {
			val windmill = freeMill()
			if (windmill != null) {
				val miller = Miller(p, owner!!, teamNumber)
				miller.windmill = windmill
				if (windmill.miller[0] == null) {
					windmill.miller[0] = miller
				} else if (windmill.miller[1] == null) {
					windmill.miller[1] = miller
				}
				owner!!.entityList.add(miller)
				owner!!.resources.food -= 5
			} else if (countEntities(Stonemason::class.simpleName) < owner!!.quarryList.size) {
				owner!!.entityList.add(Stonemason(p, owner!!, teamNumber))
				owner!!.resources.food -= 5
			} else if (countEntities(Woodcutter::class.simpleName) < owner!!.woodCampList.size) {
				owner!!.entityList.add(Woodcutter(p, owner!!, teamNumber))
				owner!!.resources.food -= 5
			}
		}
		tick++
	}
	
	private fun countEntities(name: String?): Int {
		var n = 0
		for (i in owner!!.entityList.indices)
			if (owner!!.entityList[i]::class.simpleName == name) n++
		return n
	}
	
	override fun die() {
		owner!!.maxPopulation -= 4
		remove()
	}
	
	fun add(){
		owner!!.buildingList.add(this)
		owner!!.houseList.add(this)
	}
	
	@Synchronized fun remove(){
		owner!!.buildingList.remove(this)
		owner!!.houseList.remove(this)
	}
	
	override fun hurt(dmg: Int) {
		health -= dmg
	}
}
