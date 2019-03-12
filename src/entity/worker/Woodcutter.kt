package entity.worker

import core.Resources
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

class Woodcutter(p: vec2, owner: Fraction, teamIndex: Int) : Entity() {
	private var hasWood: Boolean = false

	init {
		super.p = p
		super.owner = owner
		super.teamNumber = teamIndex
		field = AABB(p, edgeLength / 2)
		edgeLength = 16
		health = 50
		damage = 10
		speed = 0.75f
		owner.population++
	}

	override fun render(g2d: Graphics2D) {
		if (target == null) {
			Screen.drawTile(g2d, 1, 8, p - edgeLength / 2, edgeLength, edgeLength)
		} else {
			if (hasWood) {
				drawAnimatedEntity(g2d, 1, 9)
				Screen.drawTile(g2d, 7, 0, p.x.toInt() - 8, p.y.toInt() - 14, edgeLength, edgeLength)
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
		if (hasWood) {
			if (tick % 15 == 0) target = getNearestEntity(owner!!.woodCampList).p.copy()
			if (tick % 100 == 0) leaveWood()
		} else {
			if (tick % 15 == 0) target = Game.treeTree.nearest(p)!!.p
			if (tick % 100 == 0) chopTree()
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

	private fun chopTree() {
		
		val tree = Game.treeTree.nearest(p)!!
		
		if (tree.field!!.intersects(field!!)) {
			tree.gatherResources(1)
			hasWood = true
			return
		}
	
	}

	private fun leaveWood() {
		for (woodCamp in owner!!.woodCampList) {
			if (woodCamp.field!!.intersects(field!!)) {
				owner!!.resources.gain(Resources(1, 0, 0, 0))
				hasWood = false
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
