package nature

import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Screen
import gametype.Game
import math.vec2

class Cloud(p: vec2) : Nature() {
	var spreadLevel = 0
	var fieldSize = 16
	
	init {
		super.p = p
		field = Rectangle2D.Float(p.x, p.y, 1.0f, 1.0f)
	}
	
	override fun render(g2d: Graphics2D) {
		Screen.drawTile(g2d, 7 + spreadLevel, 1, p, fieldSize, fieldSize)
	}
	
	override fun update() {
		if (tick % 30 == 0) {
			spreadLevel++
		}
		if (tick % 20 == 0) {
			p += vec2((Nature.r.nextInt(4) - 1).toFloat(), -(Nature.r.nextInt(1) + 3).toFloat())
		}
		if (spreadLevel == 5) {
			remove()
		}
		tick++
	}
	
	override fun gatherResources(amount: Int) {}
	
	fun add(){
		Game.natureList.add(this)
		Game.cloudList.add(this)
	}
	
	@Synchronized fun remove(){
		Game.natureList.remove(this)
		Game.cloudList.remove(this)
	}
}
