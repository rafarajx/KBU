package entity.building

import entity.Entity
import math.AABB
import math.Circle
import math.vec2
import java.awt.Color
import java.awt.Graphics2D
import java.util.*

open class Building: Entity(){
	
	override fun render(g2d: Graphics2D) {}
	
	override fun update() {}
	
	override fun hurt(dmg: Int) {}
	
	init {
		field = AABB(p, 32)
		range = Circle(p, 200)
	}
	
	companion object {
		var random = Random()
		
		const val HOUSE = 0
		const val MILL = 1
		const val TOWER = 2
		const val WOODCAMP = 3
		const val QUARRY = 4
		const val BARRACK = 5
		const val BARRICADE = 6
		
		
		fun drawBar(g2d: Graphics2D, x: Int, y: Int, current: Int, max: Int, c: Color) {
			if (current == max) return
			g2d.color = c
			g2d.fillRect(x - 13, y, (current.toFloat() / max * 26).toInt(), 4)
			g2d.color = Color.BLACK
			g2d.drawRect(x - 13, y, (current.toFloat() / max * 26).toInt(), 4)
		}
		
		fun drawBar(g2d: Graphics2D, p: vec2, current: Int, max: Int, c: Color) {
			drawBar(g2d, p.x.toInt(), p.y.toInt(), current, max, c)
		}
		
		fun drawBar(g2d: Graphics2D, x: Float, y: Float, current: Int, max: Int, c: Color) {
			drawBar(g2d, x.toInt(), y.toInt(), current, max, c)
		}
	}
}
