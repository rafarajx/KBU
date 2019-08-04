package core

import entity.Entity
import math.AABB
import math.Circle
import math.vec2
import java.awt.Color
import java.awt.Graphics2D
import kotlin.math.sqrt

class QuadTree<T: Entity>(var parent: QuadTree<T>?, var aabb: AABB) {
	
	constructor(parent: QuadTree<T>?, ul: vec2, dr: vec2): this(parent, AABB(ul, dr))
	
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
	
	fun clear(){
		a = null
		b = null
		c = null
		d = null
	}
	
	fun add(obj: T) {
		
		//println("${obj.p} ${value == null} $isSubdivided");
		
		if (value == null){
			value = obj
			obj.qt = this as QuadTree<Entity>
			isSubdivided = true
			return
		}
		
		if (aabb.center.y >= obj.p.y) {
			if (aabb.center.x >= obj.p.x) {
				//if(a == null) a = QuadTree(this, aabb.ul, aabb.center)
				a!!.add(obj)
			} else {
				//if(b == null) b = QuadTree(this, vec2(aabb.center.x, aabb.ul.y), vec2(aabb.dr.x, aabb.center.y))
				b!!.add(obj)
			}
		} else {
			if (aabb.center.x >= obj.p.x) {
				//if(c == null) c = QuadTree(this, vec2(aabb.ul.x, aabb.center.y), vec2(aabb.center.x, aabb.dr.y))
				c!!.add(obj)
			} else {
				//if(d == null) d = QuadTree(this, aabb.center, aabb.dr)
				d!!.add(obj)
			}
		}
	
		
		
	}
	
	fun split(){
		//if(a == null) a = QuadTree(this, aabb.ul, aabb.center)
		//if(b == null) b = QuadTree(this, vec2(aabb.center.x, aabb.ul.y), vec2(aabb.dr.x, aabb.center.y))
		//if(c == null) c = QuadTree(this, vec2(aabb.ul.x, aabb.center.y), vec2(aabb.center.x, aabb.dr.y))
		//if(d == null) d = QuadTree(this, aabb.center, aabb.dr)
		isSubdivided = true
	}
	
	fun reinsert(){
		val t = value
		value = null
		add(t!!)
	}
	
	fun draw(g2d: Graphics2D) {
		
		g2d.color = Color.BLACK
		
		//g2d.drawRect(aabb.ul.x.toInt(), aabb.ul.y.toInt(), (aabb.dr.x - aabb.ul.x).toInt(), (aabb.dr.y - aabb.ul.y).toInt())
		
		if (a != null) a!!.draw(g2d)
		if (b != null) b!!.draw(g2d)
		if (c != null) c!!.draw(g2d)
		if (d != null) d!!.draw(g2d)
	}
	
	fun search(p: vec2): QuadTree<T> {
		if (aabb.center.x >= p.x) {
			if (aabb.center.y >= p.y) {
				if (a == null)
					return this
				return a!!.search(p)
			} else {
				if (c == null)
					return this
				return c!!.search(p)
			}
		} else {
			if (aabb.center.y >= p.y) {
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
			if(t.p == p) continue
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
			
			
			if (a != null) a!!.getNodesInRange(circle, temp)
			if (b != null) b!!.getNodesInRange(circle, temp)
			if (c != null) c!!.getNodesInRange(circle, temp)
			if (d != null) d!!.getNodesInRange(circle, temp)
			
		}
	}
	
	fun listAllInner(temp: ArrayList<T>) {
		
		if(value != null) temp.add(value!!)
		
		if (a != null) a!!.listAllInner(temp)
		if (b != null) b!!.listAllInner(temp)
		if (c != null) c!!.listAllInner(temp)
		if (d != null) d!!.listAllInner(temp)
	}
	
	fun remove() {
		value = null
		if(parent != null) {
			if (parent!!.a == this) parent!!.a = null
			if (parent!!.b == this) parent!!.b = null
			if (parent!!.c == this) parent!!.c = null
			if (parent!!.d == this) parent!!.d = null
		}
	}
}