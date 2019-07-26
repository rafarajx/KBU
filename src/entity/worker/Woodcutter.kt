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

class Woodcutter(p: vec2, owner: Fraction, teamIndex: Int) : Entity(owner) {
	private var hasWood: Boolean = false

	companion object{
		private const val FULL_HEALTH = 50f
	}
	
	init {
		super.p = p
		super.owner = owner
		super.teamNumber = teamIndex
		field = AABB(p, edgelength / 2)
		edgelength = 16
		health = FULL_HEALTH
		damage = 10
		speed = 0.75f
		owner.population++
	}
	
	var woodcutter = Sprite()

	override fun renderGL() {
		woodcutter.updatePosition(p - edgelength / 2, vec2(edgelength))
		if (target == null) {
			//Screen.drawTile(g2d, 1, 8, p - edgelength / 2, edgelength, edgelength)

			woodcutter.updateTexCoords(vec2(1 * 16, 8 * 16), vec2(16))

		} else {
			if (hasWood) {
				drawAnimatedEntityGL(woodcutter, 1, 9)
				//drawAnimatedEntity(g2d, 1, 9)
				//Screen.drawTile(g2d, 7, 0, p.x.toInt() - 8, p.y.toInt() - 14, edgelength, edgelength)
			} else {
				drawAnimatedEntityGL(woodcutter, 1, 8)
			}
		}
		if (Game.showHealth) {
			drawBarGL(p, health, FULL_HEALTH, Constants.RED)
		}
	}

	override fun update() {
		if (health < 0) die()
		field = AABB(p, edgelength / 2)
		if (hasWood) {
			if (tick % 15 == 0) target = getNearestEntity(owner.woodCampList)
			if (tick % 100 == 0) leaveWood()
		} else {
			if (tick % 15 == 0) target = getNearestEntity(Game.treeList)
			if (tick % 100 == 0) chopTree()
		}
		if (target != null) {
			val delta = target!!.p - p
			val d = delta.length()
			move = if (d == 0.0f) vec2(0.0f, 0.0f) else delta / d
			p += move * speed
		}
        if (tick % 1500 == 0) owner.resources.food--
		tick++
	}

	private fun chopTree() {
		for (tree in Game.treeList) {
			if (tree.field!!.intersects(field!!)) {
				tree.gatherResources(1)
				hasWood = true
				return
			}
		}
	}

	private fun leaveWood() {
		for (woodCamp in owner.woodCampList) {
			if (woodCamp.field!!.intersects(field!!)) {
				owner.resources.gain(Resources(1, 0, 0, 0))
				hasWood = false
				return
			}
		}
	}
	
	override fun die() {
		SimpleSound.die.play()
		owner.population--
		remove()
	}

	override fun add() {
		G.batch.add(woodcutter)
		owner.entityList.add(this)
	}

	@Synchronized
	override fun remove(){
		G.batch.remove(woodcutter)
		owner.entityList.remove(this)
	}
}
