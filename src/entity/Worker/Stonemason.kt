package entity.Worker

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import building.Quarry
import core.Input
import core.Resources
import core.Screen
import entity.Entity
import fraction.Fraction
import gametype.Game
import math.vec2
import nature.Rock
import kotlin.math.sqrt

class Stonemason(p: vec2, owner: Fraction, teamIndex: Int) : Entity() {
	private var hasStone: Boolean = false
	
	init {
		super.p = p
		this.owner = owner
		this.teamNumber = teamIndex
		edgeLength = 16
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
		field = Rectangle2D.Float(p.x - edgeLength / 2, p.y - edgeLength / 2, edgeLength.toFloat(), edgeLength.toFloat())
		if (hasStone) {
			if (tick % 15 == 0) target = getNearestBuilding(owner!!.quarryList)
			if (tick % 100 == 0) leaveStone()
		} else {
			if (tick % 15 == 0) target = getNearestNature(Game.rockList)
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
		for (rock in Game.rockList) {
			if (rock.field!!.intersects(field!!)) {
				rock.gatherResources(1)
				hasStone = true
				return
			}
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
}
