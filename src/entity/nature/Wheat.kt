package entity.nature

import core.Sprite
import core.Timer
import core.World
import fraction.Fraction
import math.AABB
import math.vec2

class Wheat(owner: Fraction, p: vec2) : Nature(owner) {
	var num = 1
	
	companion object {
		const val EDGE_LENGTH = 16
		const val HALF_EDGE = EDGE_LENGTH / 2
	}
	
	var wheat = Sprite()
	var shadow = Sprite()
	
	init {
		super.p = p
		edgelength = EDGE_LENGTH
		halfedge = HALF_EDGE
		field = AABB(p, 0.5f)
		sprites = arrayOf(wheat, shadow)
	}
	
	override fun renderGL() {
		//Screen.drawTile(g2d, 8, 3, p.x.toInt() - HALF_EDGE, p.y.toInt() - HALF_EDGE + 2, EDGE_LENGTH, EDGE_LENGTH)
		//Screen.drawTile(g2d, 7 + num, 6, p.x.toInt() - HALF_EDGE, p.y.toInt() - HALF_EDGE, EDGE_LENGTH, EDGE_LENGTH)


		wheat.updateTexCoords(vec2((7 + num) * 16, 6 * 16), vec2(16))

	}
	
	
	override fun update() {
		if (tick % 1200 == 0 && num < 7) num++
		tick++
	}
	
	fun gather(): Int {
		remove()
		return num + 1
	}
	
	override fun gatherResources(amount: Int) {
		num -= amount
		if (num < 0) remove()
	}
	
	override fun add(){
		Timer.addEvery(20f) {
			if(num < 7) num++
		}
		
		wheat.updatePosition(p - HALF_EDGE, vec2(16))
		
		shadow.updatePosition(p - HALF_EDGE, vec2(16))
		shadow.updateTexCoords(vec2(8 * 16, 3 * 16), vec2(16))
		shadow.updateDepth(2.0f)
		
		World.add(this)
	}
	
	@Synchronized override fun remove(){
		World.remove(this)
	}
}
