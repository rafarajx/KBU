package entity.worker

import core.*
import entity.Entity
import fraction.Fraction
import gametype.Game
import math.AABB
import math.vec2
import sound.SimpleSound
import java.awt.Color
import java.awt.Graphics2D

class Stonemason(p: vec2, owner: Fraction, teamIndex: Int) : Entity(owner) {
	private var hasStone: Boolean = false
	
	companion object{
		private const val FULL_HEALTH = 50f
	}
	
	var stonemason = Sprite()
	val stone = Sprite()
	
	
	init {
		super.p = p
		this.owner = owner
		this.teamNumber = teamIndex
		edgelength = 16
		field = AABB(p, edgelength / 2)
		health = FULL_HEALTH
		damage = 10
		speed = 0.75f
		owner.population++
		update()
		sprites = arrayOf(stonemason, stone)
		static = false
	}
	
	override fun renderGL() {
		stonemason.updatePosition(p - edgelength / 2, vec2(edgelength))

		if (target == null) {
			stonemason.updateTexCoords(vec2(1 * 16, 8 * 16), vec2(16))
		} else {
			if (hasStone) {
				drawAnimatedEntityGL(stonemason, 1, 9)
				//Screen.drawTile(g2d, 8, 0, p.x.toInt() - 8, p.y.toInt() - 14, 16, 16)
				stone.updatePosition(p - vec2(8, 2), vec2(16))
			} else {
				drawAnimatedEntityGL(stonemason, 1, 8)
			}
		}
		if (Game.showHealth) {
			drawBarGL(p, health, FULL_HEALTH, Constants.RED)
		}
	}
	
	override fun update() {
		if (health < 0) die()
		field = AABB(p, edgelength / 2)
		if (hasStone) {
			if (tick % 15 == 0){
				target = getNearestEntity(owner.quarryList)
			}
			if (tick % 100 == 0){
				leaveStone()
				stone.updateTexCoords(vec2(0), vec2(0))
			}
		} else {
			if (tick % 15 == 0) target = getNearestEntity(World.rockList)
			if (tick % 100 == 0){
				gatherStone()
				stone.updateTexCoords(vec2(8 * 16, 0), vec2(16))
			}
		}
		if (target != null) {
			val delta = (target!!.p - p)
			val d = delta.length()
			move = if (d == 0.0f) vec2(0.0f, 0.0f) else delta / d
			p += move * speed
		}
		if (tick % 1500 == 0) owner.resources.food--
		
		tick++
	}
	
	private fun gatherStone() {
		for (rock in World.rockList) {
			if (rock.field!!.intersects(field!!)) {
				rock.gatherResources(1)
				hasStone = true
				return
			}
		}
	}
	
	private fun leaveStone() {
		for (quarry in owner.quarryList) {
			if (quarry.field!!.intersects(field!!)) {
				val n = Entity.random.nextInt(4)
				if (n == 0) owner.resources.iron++
				else owner.resources.stone++
				hasStone = false
				return
			}
		}
	}
	
	override fun die() {
		owner.population--
		remove()
	}

	override fun add() {
		owner.entityList.add(this)
		World.add(this)
	}
	
	@Synchronized override fun remove(){
		owner.entityList.remove(this)
		World.remove(this)
	}
}
