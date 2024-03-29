package fraction

import core.Resources
import core.World
import entity.building.*
import gametype.Game
import math.AABB
import math.vec2
import java.awt.Color
import java.awt.Graphics2D

class StandardAI(start: vec2, color: Color, resources: Resources, baseAIRange: Int, teamNumber: Int) : Fraction() {
	
	init {
		this.center = start.copy()
		this.color = color
		this.resources = resources
		this.teamNumber = teamNumber
		this.baseAIRange = baseAIRange
		this.AIRange = baseAIRange
		status.x = center.x
		status.y = center.y
		statusText[0] = "Ally"
		statusText[1] = "Enemy"
		Tower(start, this, teamNumber).add()
	}
	
	override fun update() {
		
		if (buildingList.size == 0) isDefeated = true
		
		if (isDefeated) return
		if (tick % 60 == 0) updateCenter()
		moveStatus()
		if (tick % 60 == 0) build()
		tick++
	}
	
	private fun build() {
		if (houseList.size < 1)
			placeBuilding(Building.HOUSE)
		else if (millList.size < 1)
			placeBuilding(Building.MILL)
		else if (quarryList.size < 1)
			placeBuilding(Building.QUARRY)
		else if (woodCampList.size < 1)
			placeBuilding(Building.WOODCAMP)
		else if (population >= maxPopulation || entityList.size >= maxPopulation || maxPopulation <= buildingList.size)
			placeBuilding(Building.HOUSE)
		else if (resources.wood > 40 && resources.stone > 80 && resources.iron > 10 && resources.food > 100) {
			if (random.nextBoolean()) {
				placeBuilding(Building.TOWER)
			} else {
				placeBuilding(Building.BARRACK)
			}
		} else
			placeBuilding(smallest())
	}
	
	private fun smallest(): Int {
		val map = hashMapOf(
			Building.WOODCAMP to resources.wood * woodCampList.size,
			Building.QUARRY to resources.stone * quarryList.size,
			Building.MILL to resources.food * millList.size,
			Building.QUARRY to resources.iron * millList.size * 4
		)
		return map.minBy { it.value }!!.key
	}
	
	private fun placeBuilding(bnum: Int) {
		
		when (bnum) {
			Building.HOUSE -> if (resources.enough(House.COST)) {
				val x = random.nextInt(AIRange) - AIRange / 2 + center.x
				val y = random.nextInt(AIRange) - AIRange / 2 + center.y
				val r = AABB(vec2(x, y), 16)
				if (World.isConstructionColliding(r)) {
					this.AIRange += 50
					return
				}
				resources.pay(House.COST)
				House(vec2(x, y), this, teamNumber).add()
			}
			Building.MILL -> if (resources.enough(Windmill.COST)) {
				val x = random.nextInt(AIRange) - AIRange / 2 + center.x
				val y = random.nextInt(AIRange) - AIRange / 2 + center.y
				val r = AABB(vec2(x, y), 16)
				if (World.isConstructionColliding(r)) {
					this.AIRange += 50
					return
				}
				resources.pay(Windmill.COST)
				Windmill(vec2(x, y), this, teamNumber).add()
			}
			Building.TOWER -> if (resources.enough(Tower.COST)) {
				val x = random.nextInt(AIRange) - AIRange / 2 + center.x
				val y = random.nextInt(AIRange) - AIRange / 2 + center.y
				val r = AABB(vec2(x, y), 16)
				if (World.isConstructionColliding(r)) {
					this.AIRange += 50
					return
				}
				resources.pay(Tower.COST)
				Tower(vec2(x, y), this, teamNumber).add()
			}
			Building.WOODCAMP -> if (resources.enough(WoodCamp.COST)) {
				val p = (getNearestEntity(center, World.treeList) ?: return).p.copy()
				
				p.x += (random.nextInt(100) - 50).toFloat()
				p.y += (random.nextInt(100) - 50).toFloat()
				
				val r = AABB(p, 16)
				if (World.isConstructionColliding(r)) {
					this.AIRange += 50
					return
				}
				resources.pay(WoodCamp.COST)
				WoodCamp(p, this, teamNumber).add()
			}
			Building.QUARRY -> if (resources.enough(Quarry.COST)) {
				val p = (getNearestEntity(center, World.rockList) ?: return).p.copy()
				
				p.x += (random.nextInt(100) - 50).toFloat()
				p.y += (random.nextInt(100) - 50).toFloat()
				
				val r = AABB(p, 16)
				if (World.isConstructionColliding(r)) {
					this.AIRange += 50
					return
				}
				resources.pay(Quarry.COST)
				Quarry(p, this, teamNumber).add()
			
			}
			Building.BARRACK -> if (resources.enough(Barrack.COST)) {
				val x = random.nextInt(AIRange) - AIRange / 2 + center.x
				val y = random.nextInt(AIRange) - AIRange / 2 + center.y
				val r = AABB(vec2(x, y), 16)
				if (World.isConstructionColliding(r)) {
					this.AIRange += 50
					return
				}
				resources.pay(Barrack.COST)
				Barrack(vec2(x, y), this, teamNumber).add()
			}
		}
		
		AIRange = baseAIRange
	}
}
