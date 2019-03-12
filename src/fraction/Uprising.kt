package fraction

import core.Main
import core.Resources
import entity.building.*
import gametype.Game
import math.AABB
import math.vec2
import java.awt.Color
import java.awt.Graphics2D

class Uprising(start: vec2, color: Color, baseAIRange: Int, teamNumber: Int) : Fraction() {
	var queueNum = 0
	var buildingQueue = intArrayOf(2, 2, 2, 5, 6, 6, 6, 6)

	init {
        super.center = start.copy()
        super.teamNumber = teamNumber
        super.AIRange = baseAIRange
        super.baseAIRange = baseAIRange
		super.color = color
        super.resources = Resources(Main.BILLION)
        maxPopulation = Main.BILLION
        statusText[0] = "Allied Rebels"
        statusText[1] = "Rebels"
        Tower(start, this, teamNumber).add()
    }

	override fun render(g2d: Graphics2D) {}

	override fun update() {
		if (buildingList.size == 0) isDefeated = true
        
        for (i in entityList.indices) {
            if(entityList.indices.contains(i))
                entityList[i].update()
        }
        for (i in buildingList.indices) {
            if(buildingList.indices.contains(i))
                buildingList[i].update()
        }
        
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
		if (isColliding(r)) {
			this.AIRange += 100
			return
		}
		when (id) {
			Building.HOUSE -> if (resources.enough(House.COST)) {
				resources.pay(House.COST)
				buildingList.add(House(p, this, teamNumber))
			}
			Building.MILL -> if (resources.enough(Windmill.COST)) {
				resources.pay(Windmill.COST)
				buildingList.add(Windmill(p, this, teamNumber))
			}
			Building.TOWER -> if (resources.enough(Tower.COST)) {
				resources.pay(Tower.COST)
				buildingList.add(Tower(p, this, teamNumber))
			}
			Building.WOODCAMP -> if (resources.enough(WoodCamp.COST)) {
				resources.pay(WoodCamp.COST)
				buildingList.add(WoodCamp(p, this, teamNumber))
			}
			Building.QUARRY -> if (resources.enough(Quarry.COST)) {
				resources.pay(Quarry.COST)
				buildingList.add(Quarry(p, this, teamNumber))
			}
			Building.BARRACK -> if (resources.enough(Barrack.COST)) {
				resources.pay(Barrack.COST)
				buildingList.add(Barrack(p, this, teamNumber))
			}
			Building.BARRICADE -> if (resources.enough(Barricade.COST)) {
				resources.pay(Barricade.COST)
				buildingList.add(Barricade(p, this, teamNumber))
			}
		}
		AIRange = baseAIRange
		queueNum++
	}
}
