package entity.worker

import core.Screen
import entity.Entity
import fraction.Fraction
import gametype.Game
import math.AABB
import math.vec2
import sound.SimpleSound
import java.awt.Color
import java.awt.Graphics2D
import kotlin.math.sqrt

class Stonemason(p: vec2, owner: Fraction, teamIndex: Int) : Entity() {
	private var hasStone: Boolean = false
	
	init {
		super.p = p
		this.owner = owner
		this.teamNumber = teamIndex
		edgeLength = 16
		field = AABB(p, edgeLength / 2)
		health = 50
		damage = 10
		speed = 0.75f
		owner.population++
		update()
	}
	
	override fun render(g2d: Graphics2D) {
		if (target == null) {
			Screen.drawTile(g2d, 1, 8, p - edgeLength / 2, edgeLength, edgeLength)
		} else {
			if (this.hasStone) {
				drawAnimatedEntity(g2d, 1, 9)
				Screen.drawTile(g2d, 8, 0, p.x.toInt() - 8, p.y.toInt() - 14, 16, 16)
			} else {
				drawAnimatedEntity(g2d, 1, 8)
			}
		}
		if (Game.showHealth) {
			Entity.drawBar(g2d, p, health, 50, Color.RED)
		}
	}
	
	override fun update() {
		if (health < 0) die()
		field = AABB(p, edgeLength / 2)
		if (hasStone) {
			if (tick % 15 == 0) target = getNearestEntity(owner!!.quarryList).p
			if (tick % 100 == 0) leaveStone()
		} else {
			if (tick % 15 == 0) target = Game.rockTree.nearest(p)!!.p
			if (tick % 100 == 0) gatherStone()
		}
		if (target != null) {
			val delta = target!! - p
			val d = sqrt(delta.square().sum())
			move = if (d == 0.0f) vec2(0.0f, 0.0f) else delta / d
			p += move * speed
		}
		if (tick % 1500 == 0) owner!!.resources.food--
		
		tick++
	}
	
	private fun gatherStone() {
		val rock = Game.rockTree.nearest(p)!!
		
		if (rock.field!!.intersects(field!!)) {
			rock.gatherResources(1)
			hasStone = true
			return
		}
	}
	
	private fun leaveStone() {
		for (quarry in owner!!.quarryList) {
			if (quarry.field!!.intersects(field!!)) {
				val n = Entity.random.nextInt(4)
				if (n == 0) owner!!.resources.iron++
				else owner!!.resources.stone++
				hasStone = false
				return
			}
		}
	}
	
	override fun die() {
		SimpleSound.die.play()
		owner!!.population--
		remove()
	}
	
	@Synchronized fun remove(){
		owner!!.entityList.remove(this)
	}
}
