package entity.nature

import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Screen
import gametype.Game
import math.AABB
import math.vec2

class Flowers(p: vec2) : Nature() {
	var appearance = 0
	
	companion object{
		private const val EDGE_LENGTH = 16
		private const val HALF_EDGE = 16
	}
	
	init {
		super.p = p
		appearance = random.nextInt(2)
		field = AABB(p, 0.5f)
	}
	
	override fun render(g2d: Graphics2D) {
		Screen.drawTile(g2d, 7 + appearance, 4, p - HALF_EDGE, EDGE_LENGTH, EDGE_LENGTH)
	}
	
	override fun update() {
	
	}
	
	override fun gatherResources(amount: Int) {}
	
	fun add(){
		Game.natureList.add(this)
		Game.flowersList.add(this)
	}
	
	@Synchronized fun remove(){
		Game.natureList.remove(this)
		Game.flowersList.remove(this)
	}
}
