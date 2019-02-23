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
import math.vec2
import nature.Cloud

class House(p: vec2, owner: Fraction, teamIndex: Int) : Building() {
    override var range: Rectangle2D? = null
    var health = 200

    private fun freeMill(): Mill? {
        for (i in owner!!.millList.indices) {
            val m = owner!!.millList[i]
            if (m.miller == null)
                return m
        }
        return null
    }

    init {
        super.p = p
        this.owner = owner
        this.teamNumber = teamIndex
        this.field = Rectangle2D.Float(p.x - EDGE_LENGTH / 2, p.y - EDGE_LENGTH / 2, EDGE_LENGTH.toFloat(), EDGE_LENGTH.toFloat())
        this.range = Rectangle2D.Float(p.x - RANGE / 2, p.y - RANGE / 2, RANGE.toFloat(), RANGE.toFloat())
        owner.maxPopulation += 4
        owner.buildingList.add(this)
        owner.houseList.add(this)
    }

    override fun render(g2d: Graphics2D) {
        Screen.drawTile(g2d, 0, 0, p - EDGE_LENGTH / 2, EDGE_LENGTH, EDGE_LENGTH)
        Building.drawBar(g2d, p.x, p.y - 10, health, 200, Color.RED)
        if (owner!!.population < owner!!.maxPopulation)
            Building.drawBar(g2d, p, tick % 1000, 1000, Color.ORANGE)
    }

    override fun update() {
        if (health <= 0) die()
        if (tick % 100 == 0 || tick % 173 == 0) {
            Game.natureList.add(Cloud(p - vec2(3.0f, 5.0f)))
        }
        if (tick % 1000 == 0 && owner!!.population < owner!!.maxPopulation && owner!!.resources.enough(0, 0, 0, 5)) {
            val mill: Mill?
            if (freeMill() != null) {
                mill = freeMill()
                owner!!.entityList.add(Miller(p, owner!!, teamNumber))
                val miller = Miller(p, owner!!, teamNumber)
                miller.mill = mill
                mill!!.miller[0] = miller
                owner!!.resources.food -= 5
            } else if (countEntities(Stonemason::class.simpleName) < owner!!.quarryList.size) {
                owner!!.entityList.add(Stonemason(p, owner!!, teamNumber))
                owner!!.resources.food -= 5
            } else if (countEntities(Woodcutter::class.simpleName) < owner!!.woodCampList.size) {
                owner!!.entityList.add(Woodcutter(p, owner!!, teamNumber))
                owner!!.resources.food -= 5
            }
        }
        tick++
    }

    private fun countEntities(name: String?): Int {
        var n = 0
        for (i in owner!!.entityList.indices)
            if (owner!!.entityList[i]::class.simpleName == name) n++
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
