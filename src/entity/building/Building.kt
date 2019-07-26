package entity.building

import entity.Entity
import fraction.Fraction
import math.AABB
import math.Circle
import math.vec2
import java.awt.Color
import java.awt.Graphics2D
import java.util.*

open class Building(owner: Fraction): Entity(owner){
	
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
	}
}
