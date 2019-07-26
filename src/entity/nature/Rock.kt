package entity.nature

import core.G
import core.Screen
import core.Sprite
import fraction.Fraction
import gametype.Game
import math.AABB
import math.vec2
import java.awt.Graphics2D

class Rock(owner: Fraction, p: vec2) : Nature(owner) {
	internal var resources = 200
	private val appearance: Int
	
	companion object{
		const val EDGE_LENGTH = 16
		const val HALF_EDGE = EDGE_LENGTH / 2
	}
	
	var rock = Sprite()
	var shadow = Sprite()
	
	init {
		super.p = p
		this.appearance = random.nextInt(4)
		edgelength = EDGE_LENGTH
		halfedge = HALF_EDGE
		field = AABB(p, halfedge)
	}
	
	override fun renderGL() {
	
		
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
		G.batch.add(rock)
		rock.updatePosition(p - halfedge, vec2(edgelength))
		rock.updateTexCoords(vec2((7 + appearance) * 16, 7 * 16), vec2(16))
		
		G.batch.add(shadow)
		shadow.updatePosition(p + vec2(0f, 2f) - halfedge, vec2(edgelength))
		shadow.updateTexCoords(vec2(8 * 16, 3 * 16), vec2(16))
		shadow.updateDepth(2.0f)
		
		Game.natureList.add(this)
		Game.rockList.add(this)
		Game.rockTree.add(this)
	}
	
	@Synchronized override fun remove(){
		G.batch.remove(rock)
		G.batch.remove(shadow)
		Game.natureList.remove(this)
		Game.rockList.remove(this)
		qt!!.remove()
	}
}
