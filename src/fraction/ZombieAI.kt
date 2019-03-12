package fraction

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import entity.building.Grave
import gametype.Game
import math.AABB
import math.vec2

class ZombieAI(StartX: Int, StartY: Int, c: Color, TeamIndex: Int) : Fraction() {
	var queueNum = 0
	var buildingQueue = IntArray(1)
	
	init {
		this.center.x = StartX.toFloat()
		this.center.y = StartY.toFloat()
		this.color = c
		this.teamNumber = TeamIndex
		this.AIRange = 2000
		this.baseAIRange = 2000
		this.AIRange = this.baseAIRange
		maxPopulation = Integer.MAX_VALUE
	}
	
	override fun render(g2d: Graphics2D) {}
	
	override fun update() {
		if (tick < Game.toTicks(intArrayOf(10))) {
			tick++
			return
		}
		if (tick > Game.toTicks(intArrayOf(12)) && buildingList.size == 0) return
		for (i in entityList.indices) {
			val e = entityList[i]
			e.update()
		}
		for (i in buildingList.indices) {
			val b = buildingList[i]
			b.update()
		}
		if (tick % 60 == 0) {
			val x = random.nextInt(AIRange) - AIRange / 2 + center.x.toInt()
			val y = random.nextInt(AIRange) - AIRange / 2 + center.y.toInt()
			placeBuilding(this.buildingQueue[this.queueNum % this.buildingQueue.size], vec2(x, y) + Game.camera)
		}
		tick++
	}
	
	override fun placeBuilding(id: Int, p: vec2) {
		val camera = Game.camera
		val r = AABB(p - vec2(camera.x - 16.0f, camera.y - 16.0f), vec2(32.0f, 32.0f))
		if (isColliding(r)) return
		
		when (id) {
			0 -> this.buildingList.add(Grave(p, this, teamNumber))
		}
	}
	
}
