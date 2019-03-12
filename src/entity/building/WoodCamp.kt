package entity.building

import core.Resources
import core.Screen
import entity.nature.Cloud
import fraction.Fraction
import gametype.Game
import math.AABB
import math.Circle
import math.vec2
import java.awt.Color
import java.awt.Graphics2D

class WoodCamp(p: vec2, owner: Fraction, teamIndex: Int) : Building() {

	companion object {
		private const val EDGE_LENGTH = 32
		private const val HALF_EDGE = EDGE_LENGTH / 2
		private const val RANGE = 200
		val COST = Resources(20, 5, 0, 0)
	}

	init {
		super.p = p
		this.owner = owner
		this.teamNumber = teamIndex
		field = AABB(p, HALF_EDGE)
		range = Circle(p, RANGE)
		health = 200
	}

	override fun render(g2d: Graphics2D) {
		g2d.drawImage(Screen.woodCamp, p.x.toInt() - HALF_EDGE, p.y.toInt() - HALF_EDGE, EDGE_LENGTH, EDGE_LENGTH, null)
		Building.drawBar(g2d, p, health, 200, Color.RED)
	}

	override fun update() {
		if (health <= 0) die()

		if (tick % 100 == 0 || tick % 173 == 0)
			Game.natureList.add(Cloud(p - vec2(3.0f, 5.0f)))
		tick++
	}

	override fun die() {
		remove()
	}
	
	fun add(){
		owner!!.buildingList.add(this)
		owner!!.woodCampList.add(this)
		//owner!!.woodCampTree.add(this)
	}
	
	@Synchronized fun remove() {
		owner!!.buildingList.remove(this)
		owner!!.woodCampList.remove(this)
		//qt!!.remove()
	}

	override fun hurt(dmg: Int) {
		this.health -= dmg
	}
}
