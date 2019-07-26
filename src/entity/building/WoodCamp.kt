package entity.building

import core.*
import entity.nature.Cloud
import fraction.Fraction
import gametype.Game
import math.AABB
import math.Circle
import math.vec2
import java.awt.Color
import java.awt.Graphics2D

class WoodCamp(p: vec2, owner: Fraction, teamIndex: Int) : Building(owner) {

	companion object {
		private const val EDGE_LENGTH = 32
		private const val HALF_EDGE = EDGE_LENGTH / 2
		private const val RANGE = 200
		val COST = Resources(20, 5, 0, 0)
		private const val FULL_HEALTH = 200f
	}

	init {
		super.p = p
		this.owner = owner
		this.teamNumber = teamIndex
		edgelength = EDGE_LENGTH
		halfedge = HALF_EDGE
		field = AABB(p, HALF_EDGE)
		range = Circle(p, RANGE)
		health = FULL_HEALTH
	}
	
	var woodcamp = Sprite()

	override fun renderGL() {
		//g2d.drawImage(Screen.woodCamp, p.x.toInt() - HALF_EDGE, p.y.toInt() - HALF_EDGE, EDGE_LENGTH, EDGE_LENGTH, null)
		
		drawBarGL(p, health, FULL_HEALTH, Constants.RED)
		
	}

	override fun update() {
		if (health <= 0) die()

		if (tick % 100 == 0 || tick % 173 == 0)
			Cloud(Game.nature, p - vec2(3.0f, 5.0f)).add()
		tick++
	}

	override fun die() {
		remove()
	}
	
	override fun add(){
		G.batch.add(woodcamp)
		woodcamp.updatePosition(vec2(p.x.toInt(), p.y.toInt()) - HALF_EDGE, vec2(edgelength))
		woodcamp.updateTexCoords(vec2(0, 48), vec2(16))
		owner.buildingList.add(this)
		owner.woodCampList.add(this)
		//owner!!.woodCampTree.add(this)
	}
	
	@Synchronized override fun remove() {
		G.batch.remove(woodcamp)
		owner.buildingList.remove(this)
		owner.woodCampList.remove(this)
		//qt!!.remove()
	}

	override fun hurt(dmg: Int) {
		this.health -= dmg
	}
}
