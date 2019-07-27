package entity

import core.Constants
import core.G
import core.Screen
import core.Sprite
import fraction.Fraction
import gametype.Game
import math.AABB
import math.vec2
import sound.SimpleSound
import java.awt.Color
import java.awt.Graphics2D

class Knight(p: vec2, owner: Fraction, teamNumber: Int) : Entity(owner) {
	
	var knight = Sprite()
	
	companion object{
		const val MAX_HEALTH = 100f
	}
	
	init {
		super.p = p
		super.owner = owner
		super.teamNumber = teamNumber
		edgelength = 16
		health = MAX_HEALTH
		damage = 20
		speed = 0.6f
		field = AABB(p, edgelength / 2)
		owner.population++
	}
	
	override fun renderGL() {
		knight.updatePosition(p - (edgelength / 2), vec2(edgelength))

		if (target == null){
			knight.updateTexCoords(vec2(1 * 16, 10 * 16), vec2(16))
		} else {
			drawAnimatedEntityGL(knight, 1, 10)
		}
		if (Game.showHealth) drawBarGL(p, health, MAX_HEALTH, Constants.RED)
	}
	
	override fun update() {
		if (health < 0) die()
		
		field = AABB(p, edgelength / 2)
		
		if (tick % 60 == 0) fight(20, teamNumber, field as AABB)
		if (tick % 1500 == 0) {
			if (owner.resources.food > 0) owner.resources.food--
			else health--
		}
		
		
		val n = getNearestEnemy(teamNumber)
		
		if (n != null) {
			var delta = n.p - p
			if (delta == vec2(0.0f))
				delta += vec2(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f)
			val d = delta.length()
			p += delta / d * speed
		}
		
		val e = getNearestEntity(Game.knightList)
		
		if(e != null && e != this){
			var delta = e.p - p
			val d = delta.length()
			if(d < 15){
				if (delta == vec2(0.0f)) delta += vec2(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f)
				p -= delta / d
			}
		}
		
		
		
		tick++
	}
	
	override fun die() {
		SimpleSound.die.play()
		owner.population--
		remove()
	}
	
	override fun add() {
		G.batch.add(knight)
		owner.knightList.add(this)
		owner.entityList.add(this)
		Game.knightList.add(this)
	}
	
	@Synchronized
	override fun remove() {
		G.batch.remove(knight)
		owner.knightList.remove(this)
		owner.entityList.remove(this)
		Game.knightList.remove(this)
	}
}
