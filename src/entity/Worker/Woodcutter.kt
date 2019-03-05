package entity.Worker

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import building.WoodCamp
import core.Input
import core.Resources
import core.Screen
import entity.Entity
import fraction.Fraction
import gametype.Game
import math.vec2
import nature.Tree
import kotlin.math.sqrt

class Woodcutter(p: vec2, owner: Fraction, teamIndex: Int) : Entity() {
	private var hasWood: Boolean = false

	init {
		super.p = p
		super.owner = owner
		super.teamNumber = teamIndex
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
		field = Rectangle2D.Float(p.x - edgeLength / 2, p.y - edgeLength / 2, edgeLength.toFloat(), edgeLength.toFloat())
		if (hasWood) {
			if (tick % 15 == 0) target = getNearestBuilding(owner!!.woodCampList)
			if (tick % 100 == 0) leaveWood()
		} else {
			if (tick % 15 == 0) target = getNearestNature(Game.treeList)
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
		for (tree in Game.treeList) {
			if (tree.field!!.intersects(field!!)) {
				tree.gatherResources(1)
				hasWood = true
				return
			}
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
}
