package building

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

import core.Resources
import core.Screen
import entity.Worker.Miller
import entity.Worker.Stonemason
import entity.Worker.Woodcutter
import fraction.Fraction
import gametype.Game
import nature.Cloud

class House(x: Int, y: Int, owner: Fraction, teamIndex: Int) : Building() {
    override var range: Rectangle2D? = null
    var health = 200

    private val freeMill: Mill?
        get() {
            for (i in owner!!.millList.indices) {
                val m = owner!!.millList[i]
                if (m.miller == null)
                    return m
            }
            return null
        }

    init {
        this.x = x
        this.y = y
        this.owner = owner
        this.teamNumber = teamIndex
        this.field = Rectangle2D.Double((x - EDGE_LENGTH / 2).toDouble(), (y - EDGE_LENGTH / 2).toDouble(), EDGE_LENGTH.toDouble(), EDGE_LENGTH.toDouble())
        this.range = Rectangle2D.Double((x - RANGE / 2).toDouble(), (y - RANGE / 2).toDouble(), RANGE.toDouble(), RANGE.toDouble())
        owner.maxPopulation += 4
        owner.buildingList.add(this)
        owner.houseList.add(this)
    }

    override fun render(g2d: Graphics2D) {
        Screen.drawTile(g2d, 0, 0, x - EDGE_LENGTH / 2, y - EDGE_LENGTH / 2, EDGE_LENGTH, EDGE_LENGTH)
        Building.drawBar(g2d, x, y - 10, health, 200, Color.RED)
        if (owner!!.population < owner!!.maxPopulation)
            Building.drawBar(g2d, x, y, tick % 1000, 1000, Color.ORANGE)
    }

    override fun update() {
        if (health <= 0) die()
        if (tick % 100 == 0) {
            Game.natureList.add(Cloud(x - 3, y - 15))
        }
        if (tick % 173 == 0) {
            Game.natureList.add(Cloud(x - 3, y - 15))
        }
        if (tick % 1000 == 0 && owner!!.population < owner!!.maxPopulation && owner!!.resources!!.enough(0, 0, 0, 5)) {
            val mill: Mill?
            if (freeMill != null) {
                mill = freeMill;
                owner!!.entityList.add(Miller(x, y, owner!!, teamNumber))
                val miller = Miller(x, y, owner!!, teamNumber)
                miller.mill = mill
                mill!!.miller = miller
                owner!!.resources!!.food -= 5
            } else if (countEntities(Stonemason::class.java!!.getName()) < owner!!.quarryList.size) {
                owner!!.entityList.add(Stonemason(x, y, owner!!, teamNumber))
                owner!!.resources!!.food -= 5
            } else if (countEntities(Woodcutter::class.java!!.getName()) < owner!!.woodCampList.size) {
                owner!!.entityList.add(Woodcutter(x, y, owner!!, teamNumber))
                owner!!.resources!!.food -= 5
            }
        }
        tick++
    }

    private fun countEntities(name: String): Int {
        var n = 0
        for (i in owner!!.entityList.indices)
            if (owner!!.entityList[i].javaClass.getName() == name) n++
        return n
    }

    fun die() {
        owner!!.maxPopulation -= 4
        owner!!.buildingList.remove(this)
        owner!!.houseList.remove(this)
    }

    override fun hurt(dmg: Int) {
        health -= dmg
    }

    companion object {
        val COST = Resources(10, 5, 0, 0)
        private val EDGE_LENGTH = 32
        private val RANGE = 150
    }
}
