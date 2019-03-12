package entity.building

import core.Resources
import core.Screen
import fraction.Fraction
import gametype.Game
import math.AABB
import math.Circle
import math.vec2
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Line2D

class Tower(p: vec2, owner: Fraction, teamNumber: Int) : Building() {

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
		this.target = p.copy()
		field = AABB(p, HALF_EDGE)
		range = Circle(p, RANGE)
		health = 400
		damage = 10
	}
	
	override fun render(g2d: Graphics2D) {
		g2d.drawImage(Screen.tower, p.x.toInt() - HALF_EDGE, p.y.toInt() - HALF_EDGE, EDGE_LENGTH, EDGE_LENGTH, null)
		g2d.color = Color.RED
		if (target!!.x != p.x && target!!.y != p.y)
			g2d.draw(Line2D.Float(p.x, p.y, target!!.x, target!!.y))
		Building.drawBar(g2d, p, health, 400, Color.RED)
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
					target = entity.p.copy()
					entity.hurt(damage)
					return
				}
			}
		}
		target = p.copy()
	}
	
	private fun getNearestEnemy() {
		for (fraction in Game.fractionList) {
			if (fraction.teamNumber == teamNumber) continue
			for (entity in fraction.entityList) {
				if (range!!.contains(entity.p)) {
					target = entity.p.copy()
					return
				}
			}
		}
		target = p.copy()
	}
	
	override fun die() {
		remove()
	}
	
	fun add(){
		owner!!.buildingList.add(this)
		owner!!.towerList.add(this)
	}
	
	@Synchronized fun remove(){
		owner!!.buildingList.remove(this)
		owner!!.towerList.remove(this)
	}
	
	override fun hurt(dmg: Int) {
		health -= dmg
	}
}
