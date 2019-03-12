package entity.nature

import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Screen
import gametype.Game
import math.AABB
import math.vec2

class Tree(p: vec2) : Nature() {
	var resources = 127
	private var appearance: Int = 0
	
	companion object {
		const val EDGE_LENGTH = 16
		const val HALF_EDGE = EDGE_LENGTH / 2
	}

	init {
		super.p = p
		field = AABB(p, HALF_EDGE)
		appearance = random.nextInt(2)
	}

	override fun render(g2d: Graphics2D) {
		Screen.drawTile(g2d, 8, 3, p.x.toInt() - 16, p.y.toInt() - 16 + 7, 32, 32)
		Screen.drawTile(g2d, resources / 8, 15 - appearance, p.x.toInt() - 16, p.y.toInt() - 16, 32, 32)
	}

	override fun update() {
		if (resources <= 0) die()
	}

	override fun die() {
		remove()
	}

	override fun gatherResources(amount: Int) {
		resources -= amount
	}
	
	fun add(){
		Game.natureList.add(this)
		Game.treeList.add(this)
		Game.treeTree.add(this)
	}
	
	@Synchronized fun remove(){
		Game.natureList.remove(this)
		Game.treeList.remove(this)
		qt!!.remove()
	}
}
