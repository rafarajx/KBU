package entity.nature

import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Screen
import gametype.Game
import math.AABB
import math.vec2

class Cloud(p: vec2) : Nature() {
	private var spreadLevel = 0
	var fieldSize = 16
	
	init {
		super.p = p
		field = AABB(p, 0.5f)
	}
	
	override fun render(g2d: Graphics2D) {
		Screen.drawTile(g2d, 7 + spreadLevel, 1, p, fieldSize, fieldSize)
	}
	
	override fun update() {
		if (tick % 30 == 0) spreadLevel++
		if (tick % 20 == 0) p += vec2((random.nextInt(4) - 1).toFloat(), -(random.nextInt(1) + 3).toFloat())
		if (spreadLevel == 5) remove()
		field = AABB(p, 0.5f)
		
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
