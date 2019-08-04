package entity.nature

import core.*
import fraction.Fraction
import math.AABB
import math.vec2

class Cloud(owner: Fraction, p: vec2) : Nature(owner) {
	private var spreadLevel = 0
	var fieldSize = 16
	
	companion object {
		
		const val EDGE_LENGTH = 16
		const val HALF_EDGE = EDGE_LENGTH / 2
	}
	var cloud = Sprite()
	
	init {
		super.p = p
		edgelength = EDGE_LENGTH
		halfedge = HALF_EDGE
		field = AABB(p, 0.5f)
		sprites = arrayOf(cloud)
		static = false
	}
	
	val spread = {
		spreadLevel++
		if (spreadLevel >= 5) remove()
		cloud.updateTexCoords(vec2((7 + spreadLevel) * 16, 1 * 16), vec2(16))
	}
	
	val moveposition = {
		super.p += vec2((random.nextInt(4) - 1).toFloat(), (random.nextInt(1) + 3).toFloat())
		cloud.updatePosition(super.p, vec2(edgelength))
	}
	
	override fun renderGL() {
		//Screen.drawTile(g2d, 7 + spreadLevel, 1, p, fieldSize, fieldSize)
		
		
	}
	
	override fun update() {
		
		field = AABB(p, 0.5f)
		
		tick++
	}
	
	override fun gatherResources(amount: Int) {}
	
	@Synchronized
	override fun add(){
		
		
		Timer.addEvery(0.5f, spread)
		
		Timer.addEvery(0.33f, moveposition)
		
		World.add(this)
	}
	
	@Synchronized
	override fun remove(){
		
		Timer.removeEvery(spread)
		Timer.removeEvery(moveposition)
		
		World.remove(this)
	}
}
