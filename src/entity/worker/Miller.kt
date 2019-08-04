package entity.worker

import core.*
import entity.Entity
import entity.building.Windmill
import fraction.Fraction
import gametype.Game
import math.AABB
import math.vec2
import kotlin.math.sqrt

class Miller(p: vec2, owner: Fraction, teamNumber: Int) : Entity(owner) {
	private var wheatNum = 0
	var windmill: Windmill? = null
	
	companion object{
		private const val FULL_HEALTH = 50f
	}
	
	val miller = Sprite()
	val wheat = Sprite()
	
	init {
		super.p = p
		super.owner = owner
		super.teamNumber = teamNumber
		edgelength = 16
		health = FULL_HEALTH
		damage = 10
		speed = 0.75f
		field = AABB(p, edgelength / 2)
		owner.population++
		sprites = arrayOf(miller, wheat)
		static = false
	}
	
	
	override fun renderGL() {
		miller.updatePosition(p - edgelength / 2, vec2(edgelength))
		if (target == null) {
			miller.updateTexCoords(vec2(1 * 16, 8 * 16), vec2(16))
		} else {
			if (wheatNum == 0) {
				drawAnimatedEntityGL(miller, 1, 8)
			} else {
				drawAnimatedEntityGL(miller, 1, 9)
				wheat.updatePosition(p - vec2(8, 2), vec2(edgelength))
			}
		}
		if (Game.showHealth) {
			drawBarGL(p, health, FULL_HEALTH, Constants.RED)
		}
	}
	
	override fun update() {
		if (health < 0) die()
		field = AABB(p, edgelength / 2)
		if (windmill == null) {
			for (m in owner.millList) {
				if (m.miller[0] == null) {
					windmill = m
					windmill!!.miller[0] = this
					break
				} else if (m.miller[1] == null) {
					windmill = m
					windmill!!.miller[1] = this
					break
				}
			}
		} else {
			if (target != null) {
				val delta = (target!!.p - p)
				val d = sqrt(delta.square().sum())
				move = if (d == 0.0f) vec2(0.0f, 0.0f) else delta / d
				p += move * speed
			}
		}
		
		tick++
	}
	
	private fun leaveWheat() {
		if (windmill!!.field!!.intersects(field!!)) {
			owner.resources.food += wheatNum
			wheatNum = 0
			return
		}
	}
	
	private fun gatherWheat() {
		for (wheat in World.wheatList) {
			if (wheat.field!!.intersects(field!!)) {
				wheatNum = wheat.gather()
				return
			}
		}
	}
	
	override fun die() {
		owner.population--
		if(windmill != null) {
			if (windmill!!.miller[0] != null) {
				windmill!!.miller[0] = null
			} else if (windmill!!.miller[1] != null) {
				windmill!!.miller[1] = null
			}
		}
		remove()
	}

	override fun add() {
		Timer.addEvery(25.0f) {
			owner.resources.food--
		}
		
		Timer.addEvery(0.25f){
			if (windmill != null && wheatNum == 0) {
				target = getNearestEntity(World.wheatList)
			}
		}
		Timer.addEvery(6f){
			if (windmill != null && wheatNum == 0){
				gatherWheat()
				wheat.updateTexCoords(vec2(9 * 16, 0), vec2(16))
			}
		}
		Timer.addEvery(0.25f){
			if (windmill != null && wheatNum != 0){
				target = windmill
			}
		}
		Timer.addEvery(1f){
			if (windmill != null && wheatNum != 0){
				leaveWheat()
				wheat.updateTexCoords(vec2(0, 0), vec2(0))
			}
		}
		
		owner.entityList.add(this)
		World.add(this)
	}

	@Synchronized
	override fun remove() {
		owner.entityList.remove(this)
		World.remove(this)
	}
}
