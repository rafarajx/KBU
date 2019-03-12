package entity

import core.Screen
import fraction.Fraction
import gametype.Game
import math.AABB
import math.vec2
import sound.SimpleSound
import java.awt.Color
import java.awt.Graphics2D
import kotlin.math.sqrt

class Knight(p: vec2, owner: Fraction, teamNumber: Int) : Entity() {
	
	init {
		super.p = p
		super.owner = owner
		super.teamNumber = teamNumber
		edgeLength = 16
		health = 100
		damage = 20
		speed = 0.6f
		field = AABB(p, edgeLength / 2)
		owner.population++
	}
	
	override fun render(g2d: Graphics2D) {
		if (target == null) Screen.drawTile(g2d, 1, 10, p - (edgeLength / 2), edgeLength, edgeLength)
		else {
			drawAnimatedEntity(g2d, 1, 10)
			if (Game.showHealth) Entity.drawBar(g2d, p, health, 100, Color.RED)
		}
	}
	
	override fun update() {
		if (health < 0) die()
		
		field = AABB(p, edgeLength / 2)
		if (tick % 20 == 0) target = getNearestEnemy(p, teamNumber)
		
		if (target != null) {
			val delta = target!! - p
			val d = sqrt(delta.square().sum())
			move = if (d == 0.0f) vec2(0.0f, 0.0f) else delta / d
			p += move * speed
		}
		if (tick % 60 == 0) fight(20, teamNumber, field as AABB)
		if (tick % 1500 == 0) {
			if (owner!!.resources.food > 0) owner!!.resources.food--
			else health--
		}
		
		/*
		val n = Game.knightTree.nearest(p)
		
		if(n != null) {
			println("close")
			val dir = n.p - p
			if(dir.square().sum() < 20){
				p += 1.0f
			}
		}
		*/
		
		tick++
	}
	
	override fun die() {
		SimpleSound.die.play()
		owner!!.population--
		remove()
	}
	
	fun add() {
		owner!!.entityList.add(this)
		Game.knightList.add(this)
	}
	
	@Synchronized fun remove(){
		owner!!.entityList.remove(this)
		Game.knightList.remove(this)
	}
}
