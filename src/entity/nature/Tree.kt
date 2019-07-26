package entity.nature

import core.G
import core.Screen
import core.Sprite
import fraction.Fraction
import gametype.Game
import math.AABB
import math.vec2
import java.awt.Graphics2D

class Tree(owner: Fraction, p: vec2) : Nature(owner) {
	var resources = 127
	private var appearance: Int = 0
	
	companion object {
		const val EDGE_LENGTH = 32
		const val HALF_EDGE = EDGE_LENGTH / 2
	}
	
	var tree = Sprite()
	var shadow = Sprite()

	init {
		super.p = p
		edgelength = EDGE_LENGTH
		halfedge = HALF_EDGE
		field = AABB(p, edgelength)
		appearance = random.nextInt(2)
	}

	override fun renderGL() {


		tree.updateTexCoords(vec2(resources / 8 * 16, 240 - 16 * appearance), vec2(16))

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
	
	override fun add(){
		G.batch.add(tree)
		tree.updatePosition(p - halfedge, vec2(edgelength))
		
		G.batch.add(shadow)
		shadow.updatePosition(p - vec2(0f, 7f) - halfedge, vec2(edgelength))
		shadow.updateTexCoords(vec2(8 * 16, 3 * 16), vec2(halfedge))
		shadow.updateDepth(-1.0f)
		
		Game.natureList.add(this)
		Game.treeList.add(this)
		Game.treeTree.add(this)
	}
	
	@Synchronized override fun remove(){
		G.batch.remove(tree)
		G.batch.remove(shadow)
		Game.natureList.remove(this)
		Game.treeList.remove(this)
		qt!!.remove()
	}
}
