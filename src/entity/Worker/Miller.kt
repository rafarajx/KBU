package entity.Worker

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import building.Windmill
import core.Screen
import entity.Entity
import fraction.Fraction
import gametype.Game
import math.vec2
import sound.SimpleSound
import kotlin.math.sqrt

class Miller(p: vec2, owner: Fraction, teamNumber: Int) : Entity() {
	private var wheatNum = 0
	var windmill: Windmill? = null
	
	init {
		super.p = p
		super.owner = owner
		super.teamNumber = teamNumber
		edgeLength = 16
		health = 50
		damage = 10
		speed = 0.75f
		owner.population++
		field = Rectangle2D.Float(p.x - edgeLength / 2, p.y - edgeLength / 2, edgeLength.toFloat(), edgeLength.toFloat())
	}
	
	override fun render(g2d: Graphics2D) {
		if (target == null) {
			Screen.drawTile(g2d, 1, 8, p - edgeLength / 2, edgeLength, edgeLength)
		} else {
			if (wheatNum == 0) {
				drawAnimatedEntity(g2d, 1, 8)
			} else {
				drawAnimatedEntity(g2d, 1, 9)
				Screen.drawTile(g2d, 9, 0, p.x.toInt() - edgeLength / 2, p.y.toInt() - edgeLength / 2 - 6, edgeLength, edgeLength)
			}
		}
		if (Game.showHealth) {
			Entity.drawBar(g2d, p, health, 50, Color.RED)
		}
	}
	
	override fun update() {
		if (health < 0) die()
		field = Rectangle2D.Float(p.x - edgeLength / 2, p.y - edgeLength / 2, edgeLength.toFloat(), edgeLength.toFloat())
		if (windmill == null) {
			for (m in owner!!.millList) {
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
				if (tick % 15 == 0) target = getNearestNature(Game.wheatList)
				if (tick % 400 == 0) gatherWheat()
			} else {
				if (tick % 15 == 0) target = vec2(windmill!!.p.x, windmill!!.p.y)
				if (tick % 100 == 0) leaveWheat()
			}
			if (target != null) {
				val delta = (target!! - p)
				val d = sqrt(delta.square().sum())
				move = if (d == 0.0f) vec2(0.0f, 0.0f) else delta / d
				p += move * speed
			}
		}
		if (tick % 1500 == 0) {
			if (tick % 1500 == 0) owner!!.resources.food--
		}
		tick++
	}
	
	private fun leaveWheat() {
		if (windmill!!.field!!.intersects(field!!)) {
			owner!!.resources.food += wheatNum
			wheatNum = 0
			return
		}
	}
	
	private fun gatherWheat() {
		for (i in Game.wheatList.indices) {
			val wheat = Game.wheatList[i]
			if (wheat.field!!.intersects(field!!)) {
				wheatNum = wheat.gather()
				return
			}
		}
	}
	
	override fun die() {
		SimpleSound.die.play()
		owner!!.population--
		owner!!.entityList.remove(this)
		if(windmill != null) {
			if (windmill!!.miller[0] != null) {
				windmill!!.miller[0] = null
			} else if (windmill!!.miller[1] != null) {
				windmill!!.miller[1] = null
			}
		}
	}
}
