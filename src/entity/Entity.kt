package entity

import core.QuadTree
import core.Screen
import fraction.Fraction
import gametype.Game
import math.AABB
import math.Circle
import math.vec2
import sound.SimpleSound
import java.awt.Color
import java.awt.Graphics2D
import java.util.*
import kotlin.math.hypot
import kotlin.math.sqrt

open class Entity {
	var p = vec2(0.0f, 0.0f)
	var move = vec2(0.0f, 0.0f)
	var owner: Fraction? = null
	var field: AABB? = null
	var range: Circle? = null
	var teamNumber: Int = 0
	var health: Int = 100
	var damage: Int = 0
	var speed: Float = 0.0f
	var target: vec2? = null
	var edgeLength: Int = 0
	var tick = 1
	
	var qt: QuadTree<Entity>? = null
	
	companion object {
		var random = Random()
		
		fun drawBar(g2d: Graphics2D, x: Int, y: Int, current: Int, max: Int, c: Color) {
			g2d.color = c
			g2d.fillRect(x - 6, y, (current.toFloat() / max * 13).toInt(), 3)
			g2d.color = Color.BLACK
			g2d.drawRect(x - 6, y, (current.toFloat() / max * 13).toInt(), 3)
		}
		
		fun drawBar(g2d: Graphics2D, p: vec2, current: Int, max: Int, c: Color) {
			drawBar(g2d, p.x.toInt(), p.y.toInt(), current, max, c)
		}
		
		fun drawBar(g2d: Graphics2D, x: Float, y: Float, current: Int, max: Int, c: Color) {
			drawBar(g2d, x.toInt(), y.toInt(), current, max, c)
		}
	}
	
	open fun render(g2d: Graphics2D) { }
	
	fun drawAnimatedEntity(g2d: Graphics2D, tileX: Int, tileY: Int) {
		val sx = p.x.toInt() - edgeLength / 2
		val sy = p.y.toInt() - edgeLength / 2
		if (move.x == 0f && move.y == 0f) {
			Screen.drawTile(g2d, tileX, tileY, sx, sy, edgeLength, edgeLength)
			return
		}
		val frame = tick % 48 / 16
		
		val offset = if (move.x < 0 && move.x > Math.abs(move.y)) 9 //left
		else if (move.x > 0 && move.x > Math.abs(move.y)) 6 //right
		else if (move.y < 0 && move.y > Math.abs(move.x)) 3 //up
		else 0
		
		Screen.drawTile(g2d, tileX + offset + frame, tileY, sx, sy, edgeLength, edgeLength)
	}
	
	open fun update() {
	
	}
	
	open fun hurt(dmg: Int) {
		health -= dmg
	}
	
	fun getNearestEnemy(p : vec2, teamNumber: Int): vec2 {
		var max = Float.MAX_VALUE
		var targetPosition = p.copy()
		for (fraction in Game.fractionList) {
			if (fraction.teamNumber == teamNumber) continue
			for (entity in fraction.entityList) {
				val delta = entity.p - p
				val d = sqrt(delta.square().sum())
				if (max > d) {
					max = d
					targetPosition = entity.p.copy()
				}
			}
			for (building in fraction.buildingList) {
				val delta = building.p - p
				val d = sqrt(delta.square().sum())
				if (max > d) {
					max = d
					targetPosition = building.p.copy()
				}
			}
		}
		return targetPosition
	}
	
	fun <T: Entity> getNearestEntity(arrayList: ArrayList<T>): Entity {
		var max = Float.MAX_VALUE
		var target = this
		for (i in arrayList.indices) {
			val entity = arrayList[i]
			val delta = entity.p - p
			val d = hypot(delta.x, delta.y)
			if (max > d) {
				max = d
				target = entity
			}
		}
		return target
	}
	
	fun fight(damage: Int, TeamIndex: Int, field: AABB) {
		for (i in Game.fractionList.indices) {
			val fraction = Game.fractionList[i]
			for (j in fraction.entityList.indices) {
				val entity = fraction.entityList[j]
				if (entity.teamNumber == TeamIndex) break
				if (entity.field!!.intersects(field)) {
					entity.hurt(damage)
					return
				}
			}
			for (j in fraction.buildingList.indices) {
				val building = fraction.buildingList[j]
				if (building.teamNumber == TeamIndex) break
				if (building.field!!.intersects(field)) {
					building.hurt(damage)
					return
				}
			}
		}
	}
	
	open fun die() {
		SimpleSound.die.play()
		owner!!.population--
		owner!!.entityList.remove(this)
	}
}
