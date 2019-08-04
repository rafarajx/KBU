package fraction

import core.*
import entity.Entity
import entity.Knight
import entity.building.*
import gametype.Game
import math.AABB
import math.vec2
import org.w3c.dom.Text
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
	
	var knightList = ArrayList<Knight>()
	
	var resources: Resources = Resources(0)
	var population = 0
	var maxPopulation = 0
	var isDefeated: Boolean = false
	var teamNumber: Int = 0
	var color: Color = Color(0, 0, 0)
	var statusText = arrayOf("", "")
	var tick = 1
	
	open fun update(){
	
	}
	
	fun renderBuildingsGL() {
		for (i in buildingList.indices) {
			if(buildingList.indices.contains(i)) {
				val b = buildingList[i]
				//Screen.draw(g2d, b.field!!)
				b.renderGL()
			}
		}
	}
	
	fun renderEntitiesGL() {
		for (i in entityList.indices) {
			if(entityList.indices.contains(i)) {
				val e = entityList[i]
				/*
				g2d.color = color
				g2d.drawRect(
					e.field!!.x.toInt(),
					e.field!!.y.toInt(),
					e.field!!.width.toInt(),
					e.field!!.height.toInt()
				)
				*/
				e.renderGL()
			}
		}
	}
	
	fun drawStatusGL() {

		val p = vec2(
			status.x.toInt() - statusText[1].length / 2 * Screen.LETTER_WIDTH * 2 + Game.camera.x.toInt(),
			status.y.toInt() - 30 + Game.camera.y.toInt()
		)

		if (isDefeated) return
		
		TextRenderer.depth = -0.08f
		if (teamNumber == 0)
			TextRenderer.draw(statusText[0], p, 2.0f)
		else
			TextRenderer.draw(statusText[1], p, 2.0f)
	}
	
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
	
	fun <T: Building> inRange(arrayList: ArrayList<T>, r: AABB): Boolean {
		for (i in arrayList.indices) {
			val b = arrayList[i]
			if (b.range!!.intersects(r)) return true
		}
		return false
	}
	
	fun <T: Entity> getNearestEntity(p: vec2, arrayList: ArrayList<T>) : T? {
		var max = Float.MAX_VALUE
		var target: T? = null
		for (entity in arrayList) {
			if(p == entity.p) continue
			val delta = entity.p - p
			val d = hypot(delta.x, delta.y)
			if (max > d) {
				max = d
				target = entity
			}
		}
		return target
	}
	
	fun <T: Entity> getNearestEnemy(e: T, arrayList: ArrayList<T>) : T {
		var max = Float.MAX_VALUE
		var target = e
		for (i in arrayList.indices) {
			val entity = arrayList[i]
			if(e == entity) continue
			val delta = entity.p - e.p
			val d = hypot(delta.x, delta.y)
			if (max > d) {
				max = d
				target = entity
			}
		}
		return target
	}
}
