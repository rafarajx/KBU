package entity.nature

import java.awt.Graphics2D
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D

import core.Screen
import gametype.Game
import math.AABB
import math.vec2

class Rock(p: vec2) : Nature() {
	internal var resources = 200
	private val appearance: Int
	
	companion object {
		private const val EDGE_LENGTH = 16
		private const val HALF_EDGE = EDGE_LENGTH / 2
	}
	
	init {
		super.p = p
		this.appearance = random.nextInt(4)
		field = AABB(p, HALF_EDGE)
	}
	
	override fun render(g2d: Graphics2D) {
		Screen.drawTile(g2d, 8, 3, p.x.toInt() - HALF_EDGE, p.y.toInt() - HALF_EDGE + 2, EDGE_LENGTH, EDGE_LENGTH)
		Screen.drawTile(g2d, 7 + appearance, 7, p - HALF_EDGE, EDGE_LENGTH, EDGE_LENGTH)
	}
	
	override fun update() {
		if (resources <= 0) die()
	}
	
	override fun die() {
		remove()
	}
	
	override fun gatherResources(amount: Int) {
		resources -= amount
	}
	
	fun add(){
		Game.natureList.add(this)
		Game.rockList.add(this)
		Game.rockTree.add(this)
	}
	
	@Synchronized fun remove(){
		Game.natureList.remove(this)
		Game.rockList.remove(this)
		qt!!.remove()
	}
}
