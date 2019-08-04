package entity.nature

import core.Screen
import core.Sprite
import core.World
import fraction.Fraction
import gametype.Game
import math.AABB
import math.vec2
import java.awt.Graphics2D

class Grass(owner: Fraction, p: vec2) : Nature(owner) {
	private var num = 0
	private var fieldSize = 16
	
	companion object{
		const val EDGE_LENGTH = 32
		const val HALF_EDGE = EDGE_LENGTH / 2
	}
	var grass = Sprite()
	
	init {
		super.p = p
		edgelength = EDGE_LENGTH
		halfedge = HALF_EDGE
		field = AABB(p, 0.5f)
		sprites = arrayOf(grass)
	}
	
	
	override fun renderGL() {
		//Screen.drawTile(g2d, 8, 3, p.x.toInt() - fieldSize / 2, p.y.toInt() - fieldSize / 2 + 2, fieldSize, fieldSize)
		//Screen.drawTile(g2d, 7 + num, 5, p.x.toInt() - fieldSize / 2, p.y.toInt() - fieldSize / 2, fieldSize, fieldSize)
		grass.updateTexCoords(vec2((7 + num) * 16, 5 * 16), vec2(16))
	}
	
	override fun update() {
		if (tick % 1000 == 0 && num < 7) num++
		tick++
	}
	
	override fun gatherResources(amount: Int) {}
	
	override fun add(){
		grass.updatePosition(p - fieldSize / 2 + vec2(0, 2), vec2(halfedge))
		World.add(this)
	}
	
	@Synchronized override fun remove(){
		World.remove(this)
	}
}
