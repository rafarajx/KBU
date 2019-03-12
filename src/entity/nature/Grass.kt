package entity.nature

import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Screen
import gametype.Game
import math.AABB
import math.vec2

class Grass(p: vec2) : Nature() {
	private var num = 0
	private var fieldSize = 16
	
	init {
		super.p = p
		field = AABB(p, 0.5f)
	}
	
	override fun render(g2d: Graphics2D) {
		Screen.drawTile(g2d, 8, 3, p.x.toInt() - fieldSize / 2, p.y.toInt() - fieldSize / 2 + 2, fieldSize, fieldSize)
		Screen.drawTile(g2d, 7 + num, 5, p.x.toInt() - fieldSize / 2, p.y.toInt() - fieldSize / 2, fieldSize, fieldSize)
	}
	
	override fun update() {
		if (tick % 1000 == 0 && num < 7) num++
		tick++
	}
	
	override fun gatherResources(amount: Int) {}
	
	fun add(){
		Game.natureList.add(this)
		Game.grassList.add(this)
	}
	
	@Synchronized fun remove(){
		Game.natureList.remove(this)
		Game.grassList.remove(this)
	}
}
