package fraction

import building.*
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Main
import core.Resources
import gametype.Game
import math.vec2

class Uprising(start: vec2, teamIndex: Int) : Fraction() {
    var queueNum = 0
    var buildingQueue = intArrayOf(2, 2, 2, 5, 6, 6, 6, 6)

    init {
        this.center = start.copy()
        this.teamNumber = teamIndex
        this.AIRange = baseAIRange
        this.baseAIRange = baseAIRange
        this.buildingList.add(Tower(start, this, teamIndex))
        color = Color.RED
        maxPopulation = Main.BILLION
        resources = Resources(Main.BILLION)
        statusText[0] = "Friendly Rebels"
        statusText[1] = "Rebels"
    }

    override fun render(g2d: Graphics2D) {}

    override fun update() {
        if (buildingList.size == 0) isDefeated = true
        for (i in entityList.indices) {
            val e = entityList[i]
            e.update()
        }
        for (i in buildingList.indices) {
            val b = buildingList[i]
            b.update()
        }
        if (isDefeated) return
        if (tick % 1200 == 0) {
            val x = random.nextInt(AIRange) - AIRange / 2 + center.x.toInt()
            val y = random.nextInt(AIRange) - AIRange / 2 + center.y.toInt()
            placeBuilding(buildingQueue[queueNum % buildingQueue.size], vec2(x, y) + Game.camera)
        }
        tick++
    }

    override fun placeBuilding(selectedBuilding: Int, p: vec2) {
        val camera = Game.camera
        val r = Rectangle2D.Float(p.x - camera.x - 16.0f, p.y - camera.y - 16.0f, 32.0f, 32.0f)
        if (isColliding(r)) {
            this.AIRange += 100
            return
        }
        when (selectedBuilding) {
            Building.HOUSE -> if (resources.enough(House.COST)) {
                resources.pay(House.COST)
                buildingList.add(House(p - camera, this, teamNumber))
            }
            Building.MILL -> if (resources.enough(Mill.COST)) {
                resources.pay(Mill.COST)
                buildingList.add(Mill(p - camera, this, teamNumber))
            }
            Building.TOWER -> if (resources.enough(Tower.COST)) {
                resources.pay(Tower.COST)
                buildingList.add(Tower(p - camera, this, teamNumber))
            }
            Building.WOODCAMP -> if (resources.enough(WoodCamp.COST)) {
                resources.pay(WoodCamp.COST)
                buildingList.add(WoodCamp(p - camera, this, teamNumber))
            }
            Building.QUARRY -> if (resources.enough(Quarry.COST)) {
                resources.pay(Quarry.COST)
                buildingList.add(Quarry(p - camera, this, teamNumber))
            }
            Building.BARRACK -> if (resources.enough(Barrack.COST)) {
                resources.pay(Barrack.COST)
                buildingList.add(Barrack(p - camera, this, teamNumber))
            }
            Building.BARRICADE -> if (resources.enough(Barricade.COST)) {
                resources.pay(Barricade.COST)
                buildingList.add(Barricade(p - camera, this, teamNumber))
            }
        }
        AIRange = baseAIRange
        queueNum++
    }
}
