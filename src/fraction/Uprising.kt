package fraction

import core.Resources
import core.Window
import core.World
import entity.building.*
import gametype.Game
import math.AABB
import math.vec2
import java.awt.Color

class Uprising(start: vec2, color: Color, baseAIRange: Int, teamNumber: Int) : Fraction() {
	var queueNum = 0
	var buildingQueue = intArrayOf(2, 2, 2, 5, 6, 6, 6, 6)

	init {
        super.center = start.copy()
        super.teamNumber = teamNumber
        super.AIRange = baseAIRange
        super.baseAIRange = baseAIRange
		super.color = color
        super.resources = Resources(Window.BILLION)
        maxPopulation = Window.BILLION
        statusText[0] = "Allied Rebels"
        statusText[1] = "Rebels"
        Tower(start, this, teamNumber).add()
    }

	override fun update() {
		if (buildingList.size == 0) isDefeated = true
  
		if (isDefeated) return
		if (tick % 1000 == 0) {
			val x = random.nextInt(AIRange) - AIRange / 2 + center.x.toInt()
			val y = random.nextInt(AIRange) - AIRange / 2 + center.y.toInt()
			placeBuilding(buildingQueue[queueNum % buildingQueue.size], vec2(x, y) + Game.camera)
		}
		tick++
	}

	override fun placeBuilding(id: Int, p: vec2) {
		
		val r = AABB(p, 16)
		if (World.isColliding(r)) {
			this.AIRange += 100
			return
		}
		when (id) {
			Building.HOUSE -> if (resources.enough(House.COST)) {
				resources.pay(House.COST)
				House(p, this, teamNumber).add()
			}
			Building.MILL -> if (resources.enough(Windmill.COST)) {
				resources.pay(Windmill.COST)
				Windmill(p, this, teamNumber).add()
			}
			Building.TOWER -> if (resources.enough(Tower.COST)) {
				resources.pay(Tower.COST)
				Tower(p, this, teamNumber).add()
			}
			Building.WOODCAMP -> if (resources.enough(WoodCamp.COST)) {
				resources.pay(WoodCamp.COST)
				WoodCamp(p, this, teamNumber).add()
			}
			Building.QUARRY -> if (resources.enough(Quarry.COST)) {
				resources.pay(Quarry.COST)
				Quarry(p, this, teamNumber).add()
			}
			Building.BARRACK -> if (resources.enough(Barrack.COST)) {
				resources.pay(Barrack.COST)
				Barrack(p, this, teamNumber).add()
			}
			Building.BARRICADE -> if (resources.enough(Barricade.COST)) {
				resources.pay(Barricade.COST)
				Barricade(p, this, teamNumber).add()
			}
		}
		AIRange = baseAIRange
		queueNum++
	}
}
