package entity.building

import core.*
import entity.nature.Wheat
import entity.worker.Miller
import fraction.Fraction
import gametype.Game
import math.AABB
import math.Circle
import math.vec2
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.AffineTransform

class Windmill(p: vec2, owner: Fraction, teamIndex: Int) : Building(owner) {

	var at: AffineTransform? = null
	var theta = 0.0
	var miller : Array<Miller?> = arrayOf(null, null)

	companion object {
		val COST = Resources(30, 0, 0, 0)
		private const val EDGE_LENGTH = 32
		private const val HALF_EDGE = EDGE_LENGTH / 2
		private const val RANGE = 200
		private const val MAX_HEALTH = 200f
	}
	
	var windmill = Sprite()
	
	init {
		super.p = p
		super.owner = owner
		super.teamNumber = teamIndex
		edgelength = EDGE_LENGTH
		halfedge = HALF_EDGE
		field = AABB(p, HALF_EDGE)
		range = Circle(p, RANGE)
		health = MAX_HEALTH
		sprites = arrayOf(windmill)
	}
	

	override fun renderGL() {



		/*
		at = g2d.transform
		g2d.drawImage(Screen.windmill, p.x.toInt() - HALF_EDGE, p.y.toInt() - HALF_EDGE, EDGE_LENGTH, EDGE_LENGTH, null)
		g2d.rotate(theta, p.x.toDouble(), p.y.toDouble())
		Screen.drawTile(g2d, 7, 2, p - HALF_EDGE, EDGE_LENGTH, EDGE_LENGTH)
		g2d.transform = at
		*/
		if(health != MAX_HEALTH) drawBarGL(p, health, MAX_HEALTH, Constants.RED)
		
		
	}

	override fun update() {
		if (health <= 0) die()
		if (tick % 200 == 0) {
			val x1 = Building.random.nextInt(120) - 60 + p.x
			val y1 = Building.random.nextInt(120) - 60 + p.y
			val r = AABB(vec2(x1, y1), 0.5f)
			for (fraction in World.fractionList)
				for (building in fraction.buildingList)
					if (building.field!!.intersects(r)) return
			for (nature in World.natureList)
				if (nature.field!!.intersects(r)) return
			Wheat(World.nature, vec2(x1, y1)).add()
		}
		if (tick % 15L == 1L) {
			theta += 0.15
		}
		tick++
	}

	override fun die() {
		if(miller[0] != null) miller[0]!!.windmill = null
		if(miller[1] != null) miller[1]!!.windmill = null
		remove()
	}
	
	override fun add(){
		windmill.updatePosition(p - HALF_EDGE, vec2(edgelength))
		windmill.updateTexCoords(vec2(0, 16), vec2(16))
		owner.buildingList.add(this)
		owner.millList.add(this)
		World.add(this)
	}
	
	@Synchronized override fun remove() {
		owner.buildingList.remove(this)
		owner.millList.remove(this)
		World.remove(this)
	}

	override fun hurt(dmg: Int) {
		health -= dmg
	}
}
