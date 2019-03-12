package fraction

import core.Resources
import core.Screen
import entity.Entity
import entity.building.*
import gametype.Game
import math.AABB
import math.vec2
import java.awt.Color
import java.awt.Graphics2D
import java.util.*
import kotlin.math.hypot

open class Fraction {
	var random = Random()
	var center = vec2(0.0f)
	var status = vec2(0.0f)
	var AIRange : Int = 0
	var baseAIRange : Int = 0
	
	var buildingList = ArrayList<Building>()
	
	var houseList = ArrayList<House>()
	var millList = ArrayList<Windmill>()
	var towerList = ArrayList<Tower>()
	var woodCampList = ArrayList<WoodCamp>()
	var quarryList = ArrayList<Quarry>()
	var barrackList = ArrayList<Barrack>()
	var barricadeList = ArrayList<Barricade>()
	
	//var houseTree = QuadTree<House>(null, vec2(-3000, -3000), vec2(3000, 3000))
	//var woodCampTree = QuadTree<WoodCamp>(null, vec2(-3000, -3000), vec2(3000, 3000))
	
	var entityList = ArrayList<Entity>()
	
	var resources: Resources = Resources(0)
	var population = 0
	var maxPopulation = 0
	var isDefeated: Boolean = false
	var teamNumber: Int = 0
	var color: Color = Color(0, 0, 0)
	var statusText = arrayOf("", "")
	var tick = 1
	
	open fun render(g2d: Graphics2D) {}
	
	fun renderBuildings(g2d: Graphics2D) {
		for (i in buildingList.indices) {
			if(buildingList.indices.contains(i)) {
				val b = buildingList[i]
				g2d.color = color
				Screen.draw(g2d, b.field!!)
				b.render(g2d)
			}
		}
	}
	
	fun renderEntities(g2d: Graphics2D) {
		for (i in entityList.indices) {
			if(entityList.indices.contains(i)) {
				val e = entityList[i]
				g2d.color = color
				g2d.drawRect(
					e.field!!.x.toInt(),
					e.field!!.y.toInt(),
					e.field!!.width.toInt(),
					e.field!!.height.toInt()
				)
				e.render(g2d)
			}
		}
	}
	
	fun drawStatus(g2d: Graphics2D) {
		
		if (isDefeated) return
		if (teamNumber == 0)
			Screen.drawString(g2d, statusText[0], status.x.toInt() - statusText[1].length / 2 * Screen.LETTER_WIDTH * 2, status.y.toInt() - 30, 2.0)
		else
			Screen.drawString(g2d, statusText[1], status.x.toInt() - statusText[1].length / 2 * Screen.LETTER_WIDTH * 2, status.y.toInt() - 30, 2.0)
	}
	
	open fun update() {}
	
	fun updateCenter() {
		var center = vec2(0.0f, 0.0f)
		for (i in buildingList.indices) {
			if(buildingList.indices.contains(i)) {
				val building = buildingList[i]
				center += building.p
			}
		}
		center /= buildingList.size.toFloat()
		this.center = center
	}
	
	fun moveStatus() {
		val dx = center.x - status.x
		val dy = center.y - status.y
		//double d = math.sqrt(dx * dx + dy * dy);
		//double mx = dx / d;
		//double my = dy / d;
		status.x += dx / 100.0f
		status.y += dy / 100.0f
	}
	
	open fun placeBuilding(id: Int, p: vec2) {}
	
	fun isConstructionColliding(r: AABB): Boolean {
		for (i in Game.fractionList.indices) {
			val fraction = Game.fractionList[i]
			for (j in fraction.buildingList.indices) {
				val building = fraction.buildingList[j]
				if (building.field!!.intersects(r)) return true
			}
			for (j in fraction.entityList.indices) {
				val entity = fraction.entityList[j]
				if (entity.field!!.intersects(r)) return true
			}
		}
		return false
	}
	
	fun isColliding(r: AABB): Boolean {
		for (i in Game.fractionList.indices) {
			val fraction = Game.fractionList[i]
			for (j in fraction.buildingList.indices) {
				val building = fraction.buildingList[j]
				if (building.field!!.intersects(r)) return true
			}
			for (j in fraction.entityList.indices) {
				val entity = fraction.entityList[j]
				if (entity.field!!.intersects(r)) return true
			}
		}
		for (i in Game.natureList.indices) {
			if(Game.natureList.indices.contains(i)){
				if(Game.natureList[i].field == null) continue
				if (Game.natureList[i].field!!.intersects(r)) return true
			}
		}
		return false
	}
	
	fun <T: Building> inRange(arrayList: ArrayList<T>, r: AABB): Boolean {
		for (i in arrayList.indices) {
			val b = arrayList[i]
			if (b.range!!.intersects(r)) return true
		}
		return false
	}
	
	fun <T: Entity> getNearestEntity(p: vec2, arrayList: ArrayList<T>): vec2 {
		var max = Float.MAX_VALUE
		var target = p.copy()
		for (i in arrayList.indices) {
			val entity = arrayList[i]
			val delta = entity.p - p
			val d = hypot(delta.x, delta.y)
			if (max > d) {
				max = d
				target = entity.p.copy()
			}
		}
		return target
	}
}
