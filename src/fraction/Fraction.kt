package fraction

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D
import java.util.ArrayList
import java.util.Random

import math.vec2
import building.Building
import core.Resources
import core.Screen
import entity.Entity
import gametype.Game
import building.House
import building.Mill
import building.Tower
import building.WoodCamp
import building.Quarry
import building.Barrack
import building.Barricade
import nature.Rock
import nature.Tree

open class Fraction {
    var random = Random()
    var center = vec2(0.0f)
    var status = vec2(0.0f)
    var AIBuildingTime : Int = 0
    var AIRange : Int = 0
    var baseAIRange : Int = 0
    var fractionIndex : Int = 0
    var nation = ""

    var buildingList = ArrayList<Building>()

    var houseList = ArrayList<House>()
    var millList = ArrayList<Mill>()
    var towerList = ArrayList<Tower>()
    var woodCampList = ArrayList<WoodCamp>()
    var quarryList = ArrayList<Quarry>()
    var barrackList = ArrayList<Barrack>()
    var barricadeList = ArrayList<Barricade>()

    var entityList = ArrayList<Entity>()
    var resources: Resources = Resources(0)
    var population = 0
    var maxPopulation = 0
    var isDefeated: Boolean = false
    internal var teamIndex: Int = 0
    var color: Color = Color(0, 0, 0)
    internal var statusText = arrayOf("", "")
    protected var tick = 1

    open fun render(g2d: Graphics2D) {}

    fun renderBuildings(g2d: Graphics2D) {
        for (i in buildingList.indices) {
            val b = buildingList[i]
            g2d.color = color
            g2d.draw(b.field)
            b.render(g2d)
        }
    }

    fun renderEntities(g2d: Graphics2D) {
        for (i in entityList.indices) {
            val e = entityList[i]
            g2d.color = color
            g2d.drawRect(e.field!!.x.toInt(), e.field!!.y.toInt(), e.field!!.width.toInt(), e.field!!.height.toInt())
            e.render(g2d)
        }
    }

    fun drawStatus(g2d: Graphics2D) {
        if (isDefeated) return
        if (teamIndex == 0)
            Screen.drawString(g2d, statusText[0], status.x.toInt() - statusText[1].length / 2 * Screen.LETTER_WIDTH * 2, status.y.toInt() - 30, 2.0)
        else
            Screen.drawString(g2d, statusText[1], status.x.toInt() - statusText[1].length / 2 * Screen.LETTER_WIDTH * 2, status.y.toInt() - 30, 2.0)
    }

    open fun update() {}

    fun updateCenter() {
        var centerX = 0.0f
        var centerY = 0.0f
        for (i in buildingList.indices) {
            val building = buildingList[i]
            centerX += building.x
            centerY += building.y
        }
        centerX /= buildingList.size.toFloat()
        centerY /= buildingList.size.toFloat()
        this.center.x = centerX
        this.center.y = centerY
    }

    fun moveStatus() {
        val dx = center.x - status.x
        val dy = center.y - status.y
        //double d = math.sqrt(dx * dx + dy * dy);
        //double mx = dx / d;
        //double my = dy / d;
        status.x += dx / 100.0f
        status.y += dy / 100.0f
    }

    open fun placeBuilding(paramInt: Int, paramDouble1: Int, paramDouble2: Int) {}


    fun isColliding(r: Rectangle2D): Boolean {
        for (i in Game.fractionList.indices) {
            val fraction = Game.fractionList[i]
            for (j in fraction.buildingList.indices) {
                val building = fraction.buildingList[j]
                if (building.field!!.intersects(r)) return true
            }
            for (j in fraction.entityList.indices) {
                val entity = fraction.entityList[j]
                if (entity.field!!.intersects(r)) return true
            }
        }
        val it = Game.natureList.iterator();
        for (n in it) {
            if (n.field!!.intersects(r)) it.remove()
        }
        return false
    }

    fun inRange(BuildingList: ArrayList<Building>, r: Rectangle2D): Boolean {
        for (i in BuildingList.indices) {
            val b = BuildingList[i]
            if (b.range!!.intersects(r)) {
                return true
            }
        }
        return false
    }

    fun inRange(BuildingList: ArrayList<Building>, xpos: Double, ypos: Double): Boolean {
        for (i in BuildingList.indices) {
            val b = BuildingList[i]
            if (b.range!!.contains(xpos, ypos)) {
                return true
            }
        }
        return false
    }

    fun getNearestNature(from: vec2, name: String?): vec2 {
        var D = Integer.MAX_VALUE.toFloat()
        val target = vec2(from.x, from.y)
        for (i in Game.natureList.indices) {
            val nature = Game.natureList[i]
            if (nature::class.simpleName == name) {
                val dx = nature.x - from.x
                val dy = nature.y - from.y
                val d = dx * dx + dy * dy
                if (D > d) {
                    D = d
                    target.x = nature.x
                    target.y = nature.y
                }
            }
        }
        return target
    }

    fun getNearestTree(from: vec2): vec2 {
        return getNearestNature(from, Tree::class.simpleName)
    }

    fun getNearestRock(from: vec2): vec2 {
        return getNearestNature(from, Rock::class.simpleName)
    }

}
