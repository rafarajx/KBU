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
import math.vec2
import sound.SimpleSound

class Player(start: vec2, c: Color, resources: Resources, teamNumber: Int) : Fraction() {

    init {
        this.center = start.copy()
        this.color = c
        this.resources = resources
        this.teamNumber = teamNumber;
        Game.camera.x = -start.x + Canvas.width / 2
        Game.camera.y = -start.y + Canvas.height / 2
        buildingList.add(Tower(start, this, teamNumber))
    }

    override fun render(g2d: Graphics2D) {
        if (!inRange(buildingList, Input.mouseX - Game.camera.x, Input.mouseY - Game.camera.y)) {
            Screen.drawTile(g2d, 8, 2, Input.mouseX - Game.camera.x.toInt() - 16, Input.mouseY - Game.camera.y.toInt() - 16, 32, 32)
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

    override fun placeBuilding(id: Int, p: vec2) {
        val camera = Game.camera
        val area: Rectangle2D
        if (id == 6) {
            area = Rectangle2D.Float(p.x - camera.x - 8.0f, p.y - camera.y - 8.0f, 16.0f, 16.0f)
        } else {
            area = Rectangle2D.Float(p.x - camera.x - 16.0f, p.y - camera.y - 16.0f, 32.0f, 32.0f)
        }
        if (!inRange(buildingList, area)) return
        if (isColliding(area)) return
        when (id) {
            0 -> if (resources.enough(House.COST)) {
                resources.pay(House.COST)
                SimpleSound.explosion.play()
                this.buildingList.add(House(p - camera, this, teamNumber))
            }
            1 -> if (resources.enough(Mill.COST)) {
                resources.pay(Mill.COST)
                SimpleSound.explosion.play()
                this.buildingList.add(Mill(p - camera, this, teamNumber))
            }
            2 -> if (resources.enough(Tower.COST)) {
                resources.pay(Tower.COST)
                SimpleSound.explosion.play()
                this.buildingList.add(Tower(p - camera, this, teamNumber))
            }
            3 -> if (resources.enough(WoodCamp.COST)) {
                resources.pay(WoodCamp.COST)
                SimpleSound.explosion.play()
                this.buildingList.add(WoodCamp(p - camera, this, teamNumber))
            }
            4 -> if (resources.enough(Quarry.COST)) {
                resources.pay(Quarry.COST)
                SimpleSound.explosion.play()
                this.buildingList.add(Quarry(p - camera, this, teamNumber))
            }
            5 -> if (resources.enough(Barrack.COST)) {
                resources.pay(Barrack.COST)
                SimpleSound.explosion.play()
                this.buildingList.add(Barrack(p - camera, this, teamNumber))
            }
            6 -> if (resources.enough(Barricade.COST)) {
                resources.pay(Barricade.COST)
                SimpleSound.explosion.play()
                this.buildingList.add(Barricade(p - camera, this, teamNumber))
            }
        }
    }
}
