package nature

import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Screen
import gametype.Game
import math.vec2

class Tree(p: vec2) : Nature() {
	var resources = 127
	private var appearance: Int = 0
	
	companion object {
		const val EDGE_LENGTH = 32
	}

	init {
		super.p = p
		this.appearance = Nature.r.nextInt(2)
		this.field = Rectangle2D.Float(p.x - 16.0f, p.y - 16.0f, 32.0f, 32.0f)
	}

	override fun render(g2d: Graphics2D) {
		Screen.drawTile(g2d, 8, 3, p.x.toInt() - 16, p.y.toInt() - 16 + 7, 32, 32)
		Screen.drawTile(g2d, resources / 8, 15 - appearance, p.x.toInt() - 16, p.y.toInt() - 16, 32, 32)
	}

	override fun update() {
		if (resources <= 0) die()
	}

	fun die() {
		remove()
	}

	override fun gatherResources(amount: Int) {
		resources -= amount
	}
	
	fun add(){
		Game.natureList.add(this)
		Game.treeList.add(this)
	}
	
	fun remove(){
		Game.natureList.remove(this)
		Game.treeList.remove(this)
	}
}
