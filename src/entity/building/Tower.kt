package entity.building

import core.*
import fraction.Fraction
import gametype.Game
import math.AABB
import math.Circle
import math.vec2
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Line2D

class Tower(p: vec2, owner: Fraction, teamNumber: Int) : Building(owner) {

	companion object {
		private const val EDGE_LENGTH = 32
		private const val HALF_EDGE = EDGE_LENGTH / 2
		private const val RANGE = 150
		val COST = Resources(20, 30, 5, 0)
	}
	
	init {
		super.p = p
		this.owner = owner
		this.teamNumber = teamNumber
		target = null
		edgelength = EDGE_LENGTH
		halfedge = HALF_EDGE
		field = AABB(p, HALF_EDGE)
		range = Circle(p, RANGE)
		health = 400f
		damage = 10
	}
	
	var tower = Sprite()
	
	override fun renderGL() {



		//g2d.drawImage(Screen.tower, p.x.toInt() - HALF_EDGE, p.y.toInt() - HALF_EDGE, EDGE_LENGTH, EDGE_LENGTH, null)
		//g2d.color = Color.RED
		//if (target != null)
		//	g2d.draw(Line2D.Float(p.x, p.y, target!!.p.x, target!!.p.y))
		drawBarGL(p, health, 400f, Constants.RED)
	}
	
	override fun update() {
		if (health <= 0) die()
		if (tick % 2 == 0) getNearestEnemy()
		if (tick % 30 == 0) shot()
		tick++
	}
	
	private fun shot() {
		for (fraction in Game.fractionList) {
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
		for (fraction in Game.fractionList) {
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
		G.batch.add(tower)
		tower.updatePosition(vec2(p.x.toInt(), p.y.toInt()) - HALF_EDGE, vec2(edgelength))
		tower.updateTexCoords(vec2(0, 32), vec2(16))
		owner.buildingList.add(this)
		owner.towerList.add(this)
	}
	
	@Synchronized override fun remove(){
		G.batch.remove(tower)
		owner.buildingList.remove(this)
		owner.towerList.remove(this)
	}
	
	override fun hurt(dmg: Int) {
		health -= dmg
	}
}
