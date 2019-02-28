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
    var AIRange : Int = 0
    var baseAIRange : Int = 0

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
    var teamNumber: Int = 0
    var color: Color = Color(0, 0, 0)
    var statusText = arrayOf("", "")
    var tick = 1

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
        if (teamNumber == 0)
            Screen.drawString(g2d, statusText[0], status.x.toInt() - statusText[1].length / 2 * Screen.LETTER_WIDTH * 2, status.y.toInt() - 30, 2.0)
        else
            Screen.drawString(g2d, statusText[1], status.x.toInt() - statusText[1].length / 2 * Screen.LETTER_WIDTH * 2, status.y.toInt() - 30, 2.0)
    }

    open fun update() {}

    fun updateCenter() {
        var center = vec2(0.0f, 0.0f)
        for (i in buildingList.indices) {
            val building = buildingList[i]
            center += building.p
        }
        center /= buildingList.size.toFloat()
        this.center = center
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

    open fun placeBuilding(id: Int, p: vec2) {}

    fun isConstructionColliding(r: Rectangle2D): Boolean {
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
        return false
    }

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
        val it = Game.natureList.iterator()
        for (n in it) {
            if (n.field!!.intersects(r)) return true
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

    fun inRange(BuildingList: ArrayList<Building>, xpos: Float, ypos: Float): Boolean {
        for (i in BuildingList.indices) {
            val b = BuildingList[i]
            if (b.range!!.contains(xpos.toDouble(), ypos.toDouble())) {
                return true
            }
        }
        return false
    }

    private fun getNearestNature(from: vec2, name: String?): vec2 {
        var max = Float.MAX_VALUE
        var target = from.copy()
        for (i in Game.natureList.indices) {
            val nature = Game.natureList[i]
            if (nature::class.simpleName == name) {
                val delta = nature.p - from
                val d = delta.square().sum()
                if (max > d) {
                    max = d
                    target = nature.p.copy()
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
