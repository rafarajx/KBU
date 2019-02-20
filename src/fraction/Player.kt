package fraction

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import building.Barrack
import building.Barricade
import building.House
import building.Mill
import building.Quarry
import building.Tower
import building.WoodCamp
import core.Canvas
import core.Input
import core.Resources
import core.Screen
import gametype.Game
import sound.SimpleSound

class Player(startX: Int, startY: Int, nation: String, fractionIndex: Int, c: Color, resources: Resources, teamIndex: Int) : Fraction() {

    init {
        this.center.x = startX.toFloat()
        this.center.y = startY.toFloat()
        this.color = c
        this.resources = resources
        this.teamIndex = teamIndex
        Game.cameraX = -startX + Canvas.WIDTH / 2
        Game.cameraY = -startY + Canvas.HEIGHT / 2
        buildingList.add(Tower(startX, startY, this, teamIndex))
    }

    override fun render(g2d: Graphics2D) {
        if (!inRange(buildingList, (Input.mouseX - Game.cameraX).toDouble(), (Input.mouseY - Game.cameraY).toDouble())) {
            Screen.drawTile(g2d, 8, 2, Input.mouseX - Game.cameraX - 16, Input.mouseY - Game.cameraY - 16, 32, 32)
        }
    }

    override fun update() {
        if (tick % 100 == 0 && buildingList.size == 0 && entityList.size == 0) isDefeated = true
        for (i in entityList.indices) {
            if(entityList.indices.contains(i))
                entityList[i].update()
        }
        if (isDefeated) return
        for (i in buildingList.indices) {
            if(buildingList.indices.contains(i))
                buildingList[i].update()
        }
        tick++
    }

    override fun placeBuilding(selectedBuilding: Int, positionX: Int, positionY: Int) {
        val cameraX = Game.cameraX
        val cameraY = Game.cameraY
        val area: Rectangle2D
        if (selectedBuilding == 6) {
            area = Rectangle2D.Float(positionX.toFloat() - cameraX.toFloat() - 8.0f, positionY.toFloat() - cameraY.toFloat() - 8.0f, 16.0f, 16.0f)
        } else {
            area = Rectangle2D.Float(positionX.toFloat() - cameraX.toFloat() - 16.0f, positionY.toFloat() - cameraY.toFloat() - 16.0f, 32.0f, 32.0f)
        }
        if (!inRange(buildingList, area)) return
        if (isColliding(area)) return
        when (selectedBuilding) {
            0 -> if (resources!!.enough(House.COST)) {
                resources!!.pay(House.COST)
                SimpleSound.explosion.play()
                this.buildingList.add(House(positionX - cameraX, positionY - cameraY, this, teamIndex))
            }
            1 -> if (resources!!.enough(Mill.COST)) {
                resources!!.pay(Mill.COST)
                SimpleSound.explosion.play()
                this.buildingList.add(Mill(positionX - cameraX, positionY - cameraY, this, teamIndex))
            }
            2 -> if (resources!!.enough(Tower.COST)) {
                resources!!.pay(Tower.COST)
                SimpleSound.explosion.play()
                this.buildingList.add(Tower(positionX - cameraX, positionY - cameraY, this, teamIndex))
            }
            3 -> if (resources!!.enough(WoodCamp.COST)) {
                resources!!.pay(WoodCamp.COST)
                SimpleSound.explosion.play()
                this.buildingList.add(WoodCamp(positionX - cameraX, positionY - cameraY, this, teamIndex))
            }
            4 -> if (resources!!.enough(Quarry.COST)) {
                resources!!.pay(Quarry.COST)
                SimpleSound.explosion.play()
                this.buildingList.add(Quarry(positionX - cameraX, positionY - cameraY, this, teamIndex))
            }
            5 -> if (resources!!.enough(Barrack.COST)) {
                resources!!.pay(Barrack.COST)
                SimpleSound.explosion.play()
                this.buildingList.add(Barrack(positionX - cameraX, positionY - cameraY, this, teamIndex))
            }
            6 -> if (resources!!.enough(Barricade.COST)) {
                resources!!.pay(Barricade.COST)
                SimpleSound.explosion.play()
                this.buildingList.add(Barricade(positionX - cameraX, positionY - cameraY, this, teamIndex))
            }
        }
    }
}
