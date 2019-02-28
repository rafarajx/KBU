package fraction

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import building.*
import core.Resources
import math.vec2
import java.util.*

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
        statusText[0] = "Friend"
        statusText[1] = "Enemy"
        Tower(start, this, teamNumber)
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
            Building.HOUSE -> {
                if (resources.enough(House.COST)) {
                    val x = random.nextInt(AIRange) - AIRange / 2 + center.x
                    val y = random.nextInt(AIRange) - AIRange / 2 + center.y
                    val r = Rectangle2D.Float(x - 16.0f, y - 16.0f, 32.0f, 32.0f)
                    if (isConstructionColliding(r)) {
                        this.AIRange += 50
                        return
                    }
                    resources.pay(House.COST)
                    House(vec2(x, y), this, teamNumber)
                }
            }
            Building.MILL -> {
                if (resources.enough(Mill.COST)) {
                    val x = random.nextInt(AIRange) - AIRange / 2 + center.x
                    val y = random.nextInt(AIRange) - AIRange / 2 + center.y
                    val r = Rectangle2D.Float(x - 16.0f, y - 16.0f, 32.0f, 32.0f)
                    if (isConstructionColliding(r)) {
                        this.AIRange += 50
                        return
                    }
                    resources.pay(Mill.COST)
                    Mill(vec2(x, y), this, teamNumber)
                }
            }
            Building.TOWER -> {
                if (resources.enough(Tower.COST)) {
                    val x = random.nextInt(AIRange) - AIRange / 2 + center.x
                    val y = random.nextInt(AIRange) - AIRange / 2 + center.y
                    val r = Rectangle2D.Float(x - 16.0f, y - 16.0f, 32.0f, 32.0f)
                    if (isConstructionColliding(r)) {
                        this.AIRange += 50
                        return
                    }
                    resources.pay(Tower.COST)
                    Tower(vec2(x, y), this, teamNumber)
                }
            }
            Building.WOODCAMP -> {
                if (resources.enough(WoodCamp.COST)) {

                    val p = getNearestTree(center)

                    p.x += (random.nextInt(100) - 50).toFloat()
                    p.y += (random.nextInt(100) - 50).toFloat()

                    val r = Rectangle2D.Float(p.x - 16.0f, p.y - 16.0f, 32.0f, 32.0f)
                    if (isConstructionColliding(r)) {
                        this.AIRange += 50
                        return
                    }
                    resources.pay(WoodCamp.COST)
                    WoodCamp(p, this, teamNumber)
                }
            }
            Building.QUARRY -> {
                if (resources.enough(Quarry.COST)) {

                    val p = getNearestRock(center)

                    p.x += (random.nextInt(100) - 50).toFloat()
                    p.y += (random.nextInt(100) - 50).toFloat()

                    val r = Rectangle2D.Float(p.x - 16.0f, p.y - 16.0f, 32.0f, 32.0f)
                    if (isConstructionColliding(r)) {
                        this.AIRange += 50
                        return
                    }
                    resources.pay(Quarry.COST)
                    Quarry(p, this, teamNumber)
                }
            }
            Building.BARRACK -> {
                if (resources.enough(Barrack.COST)) {
                    val x = random.nextInt(AIRange) - AIRange / 2 + center.x
                    val y = random.nextInt(AIRange) - AIRange / 2 + center.y
                    val r = Rectangle2D.Float(x - 16.0f, y - 16.0f, 32.0f, 32.0f)
                    if (isConstructionColliding(r)) {
                        this.AIRange += 50
                        return
                    }
                    resources.pay(Barrack.COST)
                    Barrack(vec2(x, y), this, teamNumber)
                }
            }
        }

        AIRange = baseAIRange
    }
}
