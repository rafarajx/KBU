package building

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Line2D
import java.awt.geom.Rectangle2D

import core.Resources
import core.Screen
import fraction.Fraction
import gametype.Game
import math.vec2

class Tower(p: vec2, owner: Fraction, teamNumber: Int) : Building() {

	companion object {
		private const val EDGE_LENGTH = 32
		private const val RANGE = 300
		const val damage = 10
		val COST = Resources(20, 30, 5, 0)
	}
	
	var target: vec2
	internal var health = 200
	
	init {
		super.p = p
		this.owner = owner
		this.teamNumber = teamNumber
		this.target = p.copy()
		field = Rectangle2D.Float(p.x - EDGE_LENGTH / 2,    p.y - EDGE_LENGTH / 2,  EDGE_LENGTH.toFloat(),  EDGE_LENGTH.toFloat())
		range = Rectangle2D.Float(p.x - RANGE / 2,          p.y - RANGE / 2,        RANGE.toFloat(),        RANGE.toFloat())
		owner.buildingList.add(this)
		owner.towerList.add(this)
	}
	
	override fun render(g2d: Graphics2D) {
		g2d.drawImage(Screen.tower, p.x.toInt() - EDGE_LENGTH / 2, p.y.toInt() - EDGE_LENGTH / 2, EDGE_LENGTH, EDGE_LENGTH, null)
		g2d.color = Color.RED
		if (target.x != p.x && target.y != p.y)
			g2d.draw(Line2D.Float(p.x, p.y, target.x, target.y))
		Building.drawBar(g2d, p, health, 200, Color.RED)
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
				if (range!!.contains(entity.field!!)) {
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
				if (range!!.contains(entity.field!!)) {
					target = entity.p.copy()
					return
				}
			}
		}
		target = p.copy()
	}
	
	fun die() {
		remove()
	}
	
	@Synchronized fun remove(){
		owner.buildingList.remove(this)
		owner.towerList.remove(this)
	}
	
	override fun hurt(dmg: Int) {
		health -= dmg
	}
}
