package entity.nature

import core.G
import core.Screen
import core.Sprite
import fraction.Fraction
import gametype.Game
import math.AABB
import math.vec2
import java.awt.Graphics2D

class Cloud(owner: Fraction, p: vec2) : Nature(owner) {
	private var spreadLevel = 0
	var fieldSize = 16
	
	companion object {
		
		const val EDGE_LENGTH = 16
		const val HALF_EDGE = EDGE_LENGTH / 2
	}
	
	init {
		super.p = p
		edgelength = EDGE_LENGTH
		halfedge = HALF_EDGE
		field = AABB(p, 0.5f)
	}
	
	var cloud = Sprite()
	
	override fun renderGL() {
		//Screen.drawTile(g2d, 7 + spreadLevel, 1, p, fieldSize, fieldSize)

		cloud.updatePosition(p, vec2(edgelength))
		cloud.updateTexCoords(vec2((7 + spreadLevel) * 16, 1 * 16), vec2(16))
	}
	
	override fun update() {
		if (tick % 30 == 0) spreadLevel++
		if (tick % 20 == 0) p += vec2((random.nextInt(4) - 1).toFloat(), (random.nextInt(1) + 3).toFloat())
		if (spreadLevel == 5) remove()
		field = AABB(p, 0.5f)
		
		tick++
	}
	
	override fun gatherResources(amount: Int) {}
	
	override fun add(){
		G.batch.add(cloud)
		Game.natureList.add(this)
		Game.cloudList.add(this)
	}
	
	@Synchronized override fun remove(){
		G.batch.remove(cloud)
		Game.natureList.remove(this)
		Game.cloudList.remove(this)
	}
}
