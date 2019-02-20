package fraction

import building.*
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Main
import core.Resources
import gametype.Game

class Uprising(startX: Int, startY: Int, Nation: String, fractionIndex: Int, teamIndex: Int) : Fraction() {
    var queueNum = 0
    var buildingQueue = intArrayOf(2, 2, 2, 5, 6, 6, 6, 6)

    init {
        this.center.x = startX.toFloat()
        this.center.y = startY.toFloat()
        this.teamIndex = teamIndex
        this.AIRange = baseAIRange
        this.baseAIRange = baseAIRange
        this.buildingList.add(Tower(startX, startY, this, teamIndex))
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
            placeBuilding(buildingQueue[queueNum % buildingQueue.size], x + Game.cameraX, y + Game.cameraY)
        }
        tick++
    }

    override fun placeBuilding(selectedBuilding: Int, positionX: Int, positionY: Int) {

        val cameraX = Game.cameraX
        val cameraY = Game.cameraY
        val r = Rectangle2D.Float(positionX.toFloat() - cameraX.toFloat() - 16.0f, positionY.toFloat() - cameraY.toFloat() - 16.0f, 32.0f, 32.0f)
        if (isColliding(r)) {
            this.AIRange += 100
            return
        }
        when (selectedBuilding) {
            Building.HOUSE -> if (resources.enough(House.COST)) {
                resources.pay(House.COST)
                buildingList.add(House(positionX - cameraX, positionY - cameraY, this, teamIndex))
            }
            Building.MILL -> if (resources.enough(Mill.COST)) {
                resources.pay(Mill.COST)
                buildingList.add(Mill(positionX - cameraX, positionY - cameraY, this, teamIndex))
            }
            Building.TOWER -> if (resources.enough(Tower.COST)) {
                resources.pay(Tower.COST)
                buildingList.add(Tower(positionX - cameraX, positionY - cameraY, this, teamIndex))
            }
            Building.WOODCAMP -> if (resources.enough(WoodCamp.COST)) {
                resources.pay(WoodCamp.COST)
                buildingList.add(WoodCamp(positionX - cameraX, positionY - cameraY, this, teamIndex))
            }
            Building.QUARRY -> if (resources.enough(Quarry.COST)) {
                resources.pay(Quarry.COST)
                buildingList.add(Quarry(positionX - cameraX, positionY - cameraY, this, teamIndex))
            }
            Building.BARRACK -> if (resources.enough(Barrack.COST)) {
                resources.pay(Barrack.COST)
                buildingList.add(Barrack(positionX - cameraX, positionY - cameraY, this, teamIndex))
            }
            Building.BARRICADE -> if (resources.enough(Barricade.COST)) {
                resources.pay(Barricade.COST)
                buildingList.add(Barricade(positionX - cameraX, positionY - cameraY, this, teamIndex))
            }
        }
        AIRange = baseAIRange
        queueNum++
    }
}
