package entity.nature

import core.G
import core.Screen
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
	
	override fun renderGL() {
		//Screen.drawTile(g2d, 7 + appearance, 4, p - HALF_EDGE, EDGE_LENGTH, EDGE_LENGTH)
		G.batch.updatePosition(flowersid, p - HALF_EDGE, vec2(edgelength))
	}
	
	override fun update() {
	
	}
	
	override fun gatherResources(amount: Int) {}
	
	override fun add(){
		flowersid = G.batch.getId()
		G.batch.updateTexCoords(flowersid, vec2((7 + appearance) * 16, 4 * 16), vec2(EDGE_LENGTH))
		Game.natureList.add(this)
		Game.flowersList.add(this)
	}
	
	@Synchronized override fun remove(){
		G.batch.removeSprite(flowersid)
		Game.natureList.remove(this)
		Game.flowersList.remove(this)
	}
}
