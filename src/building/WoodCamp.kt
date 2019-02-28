package building

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Resources
import core.Screen
import fraction.Fraction
import gametype.Game
import math.vec2
import nature.Cloud

class WoodCamp(p: vec2, owner: Fraction, teamIndex: Int) : Building() {
	internal var health = 200
 
	companion object {
		private const val EDGE_LENGTH = 32
		private const val RANGE = 150
		val COST = Resources(20, 5, 0, 0)
	}

	init {
		super.p = p
		this.owner = owner
		this.teamNumber = teamIndex
		this.field = Rectangle2D.Float(p.x - EDGE_LENGTH / 2, p.y - EDGE_LENGTH / 2, EDGE_LENGTH.toFloat(), EDGE_LENGTH.toFloat())
		this.range = Rectangle2D.Float(p.x - RANGE / 2, p.y - RANGE / 2, RANGE.toFloat(), RANGE.toFloat())
		owner.buildingList.add(this)
		owner.woodCampList.add(this)
	}

	override fun render(g2d: Graphics2D) {
		Screen.drawTile(g2d, 0, 3, p - EDGE_LENGTH / 2, EDGE_LENGTH, EDGE_LENGTH)
		Building.drawBar(g2d, p, health, 200, Color.RED)
	}

	override fun update() {
		if (health <= 0) die()

		if (tick % 100 == 0 || tick % 173 == 0) {
			Game.natureList.add(Cloud(p - vec2(3.0f, 5.0f)))
		}
		tick++
	}

	fun die() {
		owner.buildingList.remove(this)
		owner.woodCampList.remove(this)
	}

	override fun hurt(dmg: Int) {
		this.health -= dmg
	}
}
