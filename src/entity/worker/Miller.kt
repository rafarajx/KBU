package entity.worker

import core.Constants
import core.G
import core.Screen
import core.Sprite
import entity.Entity
import entity.building.Windmill
import fraction.Fraction
import gametype.Game
import math.AABB
import math.vec2
import sound.SimpleSound
import java.awt.Color
import java.awt.Graphics2D
import kotlin.math.sqrt

class Miller(p: vec2, owner: Fraction, teamNumber: Int) : Entity(owner) {
	private var wheatNum = 0
	var windmill: Windmill? = null
	
	companion object{
		private const val FULL_HEALTH = 50f
	}
	
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
	}
	
	var miller = Sprite()
	
	override fun renderGL() {
		miller.updatePosition(p - edgelength / 2, vec2(edgelength))
		if (target == null) {
			//Screen.drawTile(g2d, 1, 8, p - edgelength / 2, edgelength, edgelength)
			miller.updateTexCoords(vec2(1 * 16, 8 * 16), vec2(16))
		} else {
			if (wheatNum == 0) {
				drawAnimatedEntityGL(miller, 1, 8)
			} else {
				drawAnimatedEntityGL(miller, 1, 9)
				//Screen.drawTile(g2d, 9, 0, p.x.toInt() - edgelength / 2, p.y.toInt() - edgelength / 2 - 6, edgelength, edgelength)
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
			if (wheatNum == 0) {
				if (tick % 15 == 0) {
					target = getNearestEntity(Game.wheatList)
				}
				if (tick % 400 == 0) gatherWheat()
			} else {
				if (tick % 15 == 0) target = windmill
				if (tick % 100 == 0) leaveWheat()
			}
			if (target != null) {
				val delta = (target!!.p - p)
				val d = sqrt(delta.square().sum())
				move = if (d == 0.0f) vec2(0.0f, 0.0f) else delta / d
				p += move * speed
			}
		}
		if (tick % 1500 == 0) {
			if (tick % 1500 == 0) owner.resources.food--
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
		for (wheat in Game.wheatList) {
			if (wheat.field!!.intersects(field!!)) {
				wheatNum = wheat.gather()
				return
			}
		}
	}
	
	override fun die() {
		SimpleSound.die.play()
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
		G.batch.add(miller)
		owner.entityList.add(this)
	}

	@Synchronized
	override fun remove() {
		G.batch.remove(miller)
		owner.entityList.remove(this)
	}
}
