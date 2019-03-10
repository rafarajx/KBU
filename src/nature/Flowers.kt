package nature

import java.awt.Graphics2D
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D

import core.Screen
import gametype.Game
import math.vec2

class Flowers(p: vec2) : Nature() {
	var appearance = 0
	
	companion object{
		private const val EDGE_LENGTH = 16
	}
	
	init {
		super.p = p
		this.appearance = Nature.r.nextInt(2)
		this.field = Rectangle2D.Float(p.x - EDGE_LENGTH / 2, p.y - EDGE_LENGTH / 2, EDGE_LENGTH.toFloat(), EDGE_LENGTH.toFloat())
	}
	
	override fun render(g2d: Graphics2D) {
		Screen.drawTile(g2d, 7 + appearance, 4, p - EDGE_LENGTH / 2, EDGE_LENGTH, EDGE_LENGTH)
	}
	
	override fun update() {
		field = Rectangle2D.Float(p.x - 8.0f, p.y - 8.0f, 16.0f, 16.0f)
	}
	
	override fun gatherResources(amount: Int) {}
	
	fun add(){
		Game.natureList.add(this)
		Game.flowersList.add(this)
	}
	
	@Synchronized fun remove(){
		Game.natureList.remove(this)
		Game.flowersList.remove(this)
	}
}
