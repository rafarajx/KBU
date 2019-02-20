package fraction

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import building.Grave
import gametype.Game

class ZombieAI(StartX: Int, StartY: Int, nation: String, fractionIndex: Int, c: Color, TeamIndex: Int) : Fraction() {
    var queueNum = 0
    var buildingQueue = IntArray(1)

    init {
        this.center.x = StartX.toFloat()
        this.center.y = StartY.toFloat()
        this.color = c
        this.teamIndex = TeamIndex
        this.AIBuildingTime = 30
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
        if (tick % AIBuildingTime == 0) {
            val x = random.nextInt(AIRange) - AIRange / 2 + center.x.toInt()
            val y = random.nextInt(AIRange) - AIRange / 2 + center.y.toInt()
            placeBuilding(this.buildingQueue[this.queueNum % this.buildingQueue.size], x + Game.cameraX,
                    y + Game.cameraY)
        }
        tick++
    }

    override fun placeBuilding(selectedBuilding: Int, positionX: Int, positionY: Int) {
        val cameraX = Game.cameraX.toDouble()
        val cameraY = Game.cameraY.toDouble()
        val r = Rectangle2D.Double(positionX.toDouble() - cameraX - 16.0, positionY.toDouble() - cameraY - 16.0, 32.0, 32.0)
        if (isColliding(r)) {
            return
        }
        when (selectedBuilding) {
            0 -> this.buildingList.add(Grave(positionX, positionY, this, teamIndex))
        }
    }

    fun pay(Cost: IntArray) {}
}
