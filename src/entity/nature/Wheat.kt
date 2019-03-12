package entity.nature

import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Screen
import gametype.Game
import math.AABB
import math.vec2

class Wheat(p: vec2) : Nature() {
	var num = 1
	
	companion object {
		const val EDGE_LENGTH = 16
		const val HALF_EDGE = EDGE_LENGTH / 2
	}
	
	init {
		super.p = p
		field = AABB(p, 0.5f)
	}
	
	override fun render(g2d: Graphics2D) {
		Screen.drawTile(g2d, 8, 3, p.x.toInt() - HALF_EDGE, p.y.toInt() - HALF_EDGE + 2, EDGE_LENGTH, EDGE_LENGTH)
		Screen.drawTile(g2d, 7 + num, 6, p.x.toInt() - HALF_EDGE, p.y.toInt() - HALF_EDGE, EDGE_LENGTH, EDGE_LENGTH)
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
	
	fun add(){
		Game.natureList.add(this)
		Game.wheatList.add(this)
	}
	
	@Synchronized fun remove(){
		Game.natureList.remove(this)
		Game.wheatList.remove(this)
	}
}
