package entity.nature

import core.*
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
		appearance = Utils.nextInt(2)
		sprites = arrayOf(tree, shadow)
	}

	override fun renderGL() {



	}

	override fun update() {
	
	}
	
	override fun gatherResources(amount: Int) {
		tree.updateTexCoords(vec2(resources / 8 * 16, 240 - 16 * appearance), vec2(16))
		resources -= amount
		if (resources <= 0) die()
	}
	
	override fun die() {
		remove()
	}
	
	override fun add(){
		tree.updatePosition(p - halfedge, vec2(edgelength))
		tree.updateTexCoords(vec2(resources / 8 * 16, 240 - 16 * appearance), vec2(16))
		
		shadow.updatePosition(p - vec2(0f, 7f) - halfedge, vec2(edgelength))
		shadow.updateTexCoords(vec2(8 * 16, 3 * 16), vec2(halfedge))
		shadow.updateDepth(-1.0f)
		
		World.add(this)
	}
	
	@Synchronized override fun remove(){
		World.remove(this)
		//qt!!.remove()
	}
}
