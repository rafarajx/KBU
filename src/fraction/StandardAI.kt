package fraction

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import building.*
import core.Resources

class StandardAI(startX: Int, startY: Int, nation: String, fractionIndex: Int, color: Color, resources: Resources, AIBuildingTime: Int, baseAIRange: Int, TeamIndex: Int) : Fraction() {

    init {
        this.center.x = startX.toFloat()
        this.center.y = startY.toFloat()
        this.color = color
        this.resources = resources
        this.fractionIndex = fractionIndex
        this.nation = nation
        this.teamIndex = TeamIndex
        this.AIBuildingTime = AIBuildingTime
        this.baseAIRange = baseAIRange
        this.AIRange = baseAIRange
        this.buildingList.add(Tower(startX, startY, this, TeamIndex))
        status.x = center.x
        status.y = center.y
        statusText[0] = "Friend"
        statusText[1] = "Enemy"
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
        if (tick % AIBuildingTime == 0) build()
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
        else if (entityList.size > maxPopulation || maxPopulation < buildingList.size)
            placeBuilding(Building.HOUSE)
        else if (resources.wood > 40 && resources.stone > 80 && resources.food > 100) {
            if (random.nextBoolean()) {
                placeBuilding(Building.TOWER)
            } else {
                placeBuilding(Building.BARRACK)
            }
        } else
            placeBuilding(smallest())
    }

    private fun smallest(): Int {
        val wc = resources.wood * woodCampList.size
        val sc = resources.stone * quarryList.size
        val fc = resources.food * millList.size
        return if (wc < fc && wc < sc)
            Building.WOODCAMP
        else if (sc < fc && sc < wc)
            Building.QUARRY
        else
            Building.MILL
    }

    private fun placeBuilding(bnum: Int) {

        when (bnum) {
            Building.HOUSE -> {
                if (resources.enough(House.COST)) {
                    val x = random.nextInt(AIRange) - AIRange / 2 + center.x.toInt()
                    val y = random.nextInt(AIRange) - AIRange / 2 + center.y.toInt()
                    val r = Rectangle2D.Float(x - 16.0f, y - 16.0f, 32.0f, 32.0f)
                    if (isColliding(r)) {
                        this.AIRange += 50
                        return
                    }
                    resources.pay(House.COST)
                    House(x, y, this, teamIndex)
                }
            }
            Building.MILL -> {
                if (resources.enough(Mill.COST)) {
                    val x = random.nextInt(AIRange) - AIRange / 2 + center.x.toInt()
                    val y = random.nextInt(AIRange) - AIRange / 2 + center.y.toInt()
                    val r = Rectangle2D.Float(x - 16.0f, y - 16.0f, 32.0f, 32.0f)
                    if (isColliding(r)) {
                        this.AIRange += 50
                        return
                    }
                    resources.pay(Mill.COST)
                    Mill(x, y, this, teamIndex)
                }
            }
            Building.TOWER -> {
                if (resources.enough(Tower.COST)) {
                    val x = random.nextInt(AIRange) - AIRange / 2 + center.x.toInt()
                    val y = random.nextInt(AIRange) - AIRange / 2 + center.y.toInt()
                    val r = Rectangle2D.Float(x - 16.0f, y - 16.0f, 32.0f, 32.0f)
                    if (isColliding(r)) {
                        this.AIRange += 50
                        return
                    }
                    resources.pay(Tower.COST)
                    Tower(x, y, this, teamIndex)
                }
            }
            Building.WOODCAMP -> {
                if (resources.enough(WoodCamp.COST)) {

                    val p = getNearestTree(center)

                    p.x += (random.nextInt(100) - 50).toFloat()
                    p.y += (random.nextInt(100) - 50).toFloat()

                    val r = Rectangle2D.Float(p.x - 16.0f, p.y - 16.0f, 32.0f, 32.0f)
                    if (isColliding(r)) {
                        this.AIRange += 50
                        return
                    }
                    resources.pay(WoodCamp.COST)
                    WoodCamp(p.x.toInt(), p.y.toInt(), this, teamIndex)
                }
            }
            Building.QUARRY -> {
                if (resources.enough(Quarry.COST)) {

                    val p = getNearestRock(center)

                    p.x += (random.nextInt(100) - 50).toFloat()
                    p.y += (random.nextInt(100) - 50).toFloat()

                    val r = Rectangle2D.Float(p.x - 16.0f, p.y - 16.0f, 32.0f, 32.0f)
                    if (isColliding(r)) {
                        this.AIRange += 50
                        return
                    }
                    resources.pay(Quarry.COST)
                    Quarry(p.x.toInt(), p.y.toInt(), this, teamIndex)
                }
            }
            Building.BARRACK -> {
                if (resources.enough(Barrack.COST)) {
                    val x = random.nextInt(AIRange) - AIRange / 2 + center.x.toInt()
                    val y = random.nextInt(AIRange) - AIRange / 2 + center.y.toInt()
                    val r = Rectangle2D.Float(x - 16.0f, y - 16.0f, 32.0f, 32.0f)
                    if (isColliding(r)) {
                        this.AIRange += 50
                        return
                    }
                    resources.pay(Barrack.COST)
                    Barrack(x, y, this, teamIndex)
                }
            }
        }

        AIRange = baseAIRange
    }
}
