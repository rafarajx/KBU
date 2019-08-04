package entity

import core.*
import fraction.Fraction
import gametype.Game
import math.AABB
import math.Circle
import math.vec2
import math.vec3
import sound.SimpleSound
import java.awt.Color
import java.awt.Graphics2D
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

open class Entity (var owner: Fraction){
	var p = vec2(0.0f, 0.0f)
	var move = vec2(0.0f, 0.0f)
	var field: AABB? = null
	var range: Circle? = null
	var teamNumber: Int = 0
	var health: Float = 100f
	var damage: Int = 0
	var speed: Float = 0.0f
	var target: Entity? = null
	var edgelength: Int = 0
	var halfedge: Int = 0
	var tick = 1
	var renderable = false
	var sprites: Array<Sprite> = arrayOf()
	var static = true
	
	var qt: QuadTree<Entity>? = null
	
	companion object {
		
		var random = Random()
		
		fun drawBarGL(pos: vec2, current: Float, max: Float, color: vec3) {
			RectRenderer.enableCamera()
			
			RectRenderer.depth = -0.03f
			RectRenderer.setColor(color)
			RectRenderer.fill(pos - vec2(6, 0), vec2(current / max * 13f, 3f))
			
			RectRenderer.depth = -0.04f
			RectRenderer.setColor(0.0f)
			RectRenderer.draw(pos - vec2(6, 0), vec2(current / max * 13f, 3f))
			
			RectRenderer.disableCamera()
		}
	}

	open fun render(g2d: Graphics2D) {}

	open fun renderGL() { }

	fun drawAnimatedEntityGL(sprite: Sprite, tileX: Int, tileY: Int) {
		
		sprite.updatePosition(vec2(p.x.toInt() - edgelength / 2, p.y.toInt() - edgelength / 2), vec2(edgelength))

		if (move.x == 0f && move.y == 0f) {
			sprite.updateTexCoords(vec2(tileX * 16, tileY * 16), vec2(16))
			return
		}
		val frame = tick % 48 / 16

		val offset = if (move.x < 0 && move.x > abs(move.y)) 9 //left
		else if (move.x > 0 && move.x > abs(move.y)) 6 //right
		else if (move.y < 0 && move.y > abs(move.x)) 3 //up
		else 0
		
		
		sprite.updateTexCoords(vec2((tileX + offset + frame) * 16, tileY * 16), vec2(16))
	}


	open fun update() {
	
	}
	
	open fun hurt(dmg: Int) {
		health -= dmg
	}
	
	fun getNearestEnemy(teamNumber: Int): Entity? {
		var max = Float.MAX_VALUE
		var target : Entity? = null
		for (fraction in World.fractionList) {
			if (fraction.teamNumber == teamNumber) continue
			for (entity in fraction.entityList) {
				val d = (entity.p - p).square().sum()
				if (max > d) {
					max = d
					target = entity
				}
			}
			for (building in fraction.buildingList) {
				val d = (building.p - p).square().sum()
				if (max > d) {
					max = d
					target = building
				}
			}
		}
		return target
	}
	
	fun <T: Entity> getNearestEntity(arrayList: ArrayList<T>): T? {
		var max = Float.MAX_VALUE
		var res : T? = null
		
		for (entity in arrayList) {
			if(entity == this) continue
			val d = (entity.p - p).square().sum()
			if (d < max) {
				max = d
				res = entity
			}
		}
		return res
	}
	
	fun fight(damage: Int, teamNumber: Int, field: AABB) {
		for (fraction in World.fractionList) {
			if (fraction.teamNumber == teamNumber) continue
			for (entity in fraction.entityList) {
				if (entity.field!!.intersects(field)) {
					entity.hurt(damage)
					return
				}
			}
			for (building in fraction.buildingList) {
				if (building.field!!.intersects(field)) {
					building.hurt(damage)
					return
				}
			}
		}
	}

	@Synchronized
	open fun add(){
	
	}

	@Synchronized
	open fun remove(){
	
	}

	open fun die() {
		SimpleSound.die.play()
		owner.population--
		owner.entityList.remove(this)
		remove()
	}
}
