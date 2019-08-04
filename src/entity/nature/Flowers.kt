package entity.nature

import core.Screen
import core.Sprite
import core.World
import fraction.Fraction
import gametype.Game
import math.AABB
import math.vec2
import java.awt.Graphics2D

class Flowers(owner: Fraction, p: vec2) : Nature(owner) {
	var appearance = 0
	
	companion object{
		private const val EDGE_LENGTH = 16
		private const val HALF_EDGE = EDGE_LENGTH / 2
	}
	
	var flowersid = 0
	
	init {
		super.p = p
		appearance = random.nextInt(2)
		edgelength = EDGE_LENGTH
		halfedge = HALF_EDGE
		field = AABB(p, 0.5f)
	}
	
	val flower = Sprite()
	
	override fun renderGL() {
		//Screen.drawTile(g2d, 7 + appearance, 4, p - HALF_EDGE, EDGE_LENGTH, EDGE_LENGTH)
		flower.updatePosition(p - HALF_EDGE, vec2(edgelength))
		sprites = arrayOf(flower)
	}
	
	override fun update() {
	
	}
	
	override fun gatherResources(amount: Int) {}
	
	override fun add(){
		flower.updateTexCoords(vec2((7 + appearance) * 16, 4 * 16), vec2(EDGE_LENGTH))
		World.add(this)
	}
	
	@Synchronized override fun remove(){
		World.remove(this)
	}
}
