package core

import math.AABB
import math.Circle
import math.vec2
import nature.Nature
import java.awt.Color
import java.awt.Graphics2D
import kotlin.math.sqrt

class QuadTree<T : Nature>(var aabb: AABB) {
	
	constructor(ul: vec2, dr: vec2): this(AABB(ul, dr))
	
	var value: T? = null
	
	/*
		A | B
		--+--
		C | D
	*/
	
	var a: QuadTree<T>? = null
	var b: QuadTree<T>? = null
	var c: QuadTree<T>? = null
	var d: QuadTree<T>? = null
	
	var isSubdivided = false
	
	fun add(obj: T) {
	
		if (this.value == null && !isSubdivided){
			this.value = obj
			return
		}
		
		if (aabb.mid.y >= obj.p.y) {
			if (aabb.mid.x >= obj.p.x) {
				subdivide()
				a!!.add(obj)
				reinsert()
			} else {
				subdivide()
				b!!.add(obj)
				reinsert()
			}
		} else {
			if (aabb.mid.x >= obj.p.x) {
				subdivide()
				c!!.add(obj)
				reinsert()
			} else {
				subdivide()
				d!!.add(obj)
				reinsert()
			}
		}
	}
	
	fun subdivide(){
		if(a == null) a = QuadTree(aabb.ul, aabb.mid)
		if(b == null) b = QuadTree(vec2(aabb.mid.x, aabb.ul.y), vec2(aabb.dr.x, aabb.mid.y))
		if(c == null) c = QuadTree(vec2(aabb.ul.x, aabb.mid.y), vec2(aabb.mid.x, aabb.dr.y))
		if(d == null) d = QuadTree(aabb.mid, aabb.dr)
		isSubdivided = true
	}
	
	fun reinsert(){
		if(this.value != null) {
			val t = this.value
			this.value = null
			add(t!!)
		}
	}
	
	fun draw(g2d: Graphics2D) {
		
		g2d.color = Color.BLACK
		
		g2d.drawRect(aabb.ul.x.toInt(), aabb.ul.y.toInt(), (aabb.dr.x - aabb.ul.x).toInt(), (aabb.dr.y - aabb.ul.y).toInt())
		
		if (a != null) a!!.draw(g2d)
		if (b != null) b!!.draw(g2d)
		if (c != null) c!!.draw(g2d)
		if (d != null) d!!.draw(g2d)
	}
	
	fun search(p: vec2): QuadTree<T> {
		if (aabb.mid.x >= p.x) {
			if (aabb.mid.y >= p.y) {
				if (a == null)
					return this
				return a!!.search(p)
			} else {
				if (c == null)
					return this
				return c!!.search(p)
			}
		} else {
			if (aabb.mid.y >= p.y) {
				if (b == null)
					return this
				return b!!.search(p)
			} else {
				if (d == null)
					return this
				return d!!.search(p)
			}
		}
	}
	
	fun nearest(p: vec2): T? {
		val qt = search(p)
		val temp = ArrayList<T>()
		qt.listAllInner(temp)
		
		var max = Float.MAX_VALUE
		var best: T? = null
		for(t in temp){
			val d = (t.p - p).square().sum()
			if(d < max){
				max = d
				best = t
			}
		}
		
		temp.clear()
		getNodesInRange(Circle(p, sqrt(max)), temp)
		
		for(t in temp){
			val d = (t.p - p).square().sum()
			if(d < max){
				max = d
				best = t
			}
		}
		
		return best
	}
	
	fun getNodesInRange(circle: Circle, temp: ArrayList<T>){
	
		if(circle.intersects(aabb)){
		
			if(value != null) temp.add(value!!)
			if(isSubdivided) {
				a!!.getNodesInRange(circle, temp)
				b!!.getNodesInRange(circle, temp)
				c!!.getNodesInRange(circle, temp)
				d!!.getNodesInRange(circle, temp)
			}
		}
	}
	
	fun listAllInner(temp: ArrayList<T>) {
		
		if(value != null) temp.add(value!!)
		
		if(isSubdivided) {
			if (a != null) a!!.listAllInner(temp)
			if (b != null) b!!.listAllInner(temp)
			if (c != null) c!!.listAllInner(temp)
			if (d != null) d!!.listAllInner(temp)
		}
	}
	
	fun remove(obj: T) {
	
	}
}