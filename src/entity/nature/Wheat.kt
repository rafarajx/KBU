package entity.nature

import core.G
import core.Screen
import fraction.Fraction
import gametype.Game
import math.AABB
import math.vec2
import java.awt.Graphics2D

class Wheat(owner: Fraction, p: vec2) : Nature(owner) {
	var num = 1
	
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
	
	var wheatid = 0
	var shadowid = 0
	
	override fun renderGL() {
		//Screen.drawTile(g2d, 8, 3, p.x.toInt() - HALF_EDGE, p.y.toInt() - HALF_EDGE + 2, EDGE_LENGTH, EDGE_LENGTH)
		//Screen.drawTile(g2d, 7 + num, 6, p.x.toInt() - HALF_EDGE, p.y.toInt() - HALF_EDGE, EDGE_LENGTH, EDGE_LENGTH)


		G.batch.updateTexCoords(wheatid, vec2((7 + num) * 16, 6 * 16), vec2(16))

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
		wheatid = G.batch.getId()
		G.batch.updatePosition(wheatid, p - HALF_EDGE, vec2(16))
		
		shadowid = G.batch.getId()
		G.batch.updatePosition(shadowid, p - HALF_EDGE, vec2(16))
		G.batch.updateTexCoords(shadowid, vec2(8 * 16, 3 * 16), vec2(16))
		G.batch.updateDepth(shadowid, 2.0f)
		
		Game.natureList.add(this)
		Game.wheatList.add(this)
	}
	
	@Synchronized override fun remove(){
		G.batch.removeSprite(wheatid)
		G.batch.removeSprite(shadowid)
		Game.natureList.remove(this)
		Game.wheatList.remove(this)
	}
}
