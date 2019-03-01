package gametype

import java.awt.Color
import java.awt.Graphics2D

import core.Resources
import fraction.StandardAI
import math.vec2

object FFA : Game() {

	fun init(opponentsNumber: Int, difficulty: Int) {
		setupMenuBar()
		natureList.clear()
		fractionList.clear()
		setupNature(opponentsNumber + 1)
		fractionList.add(player)
		for (i in 0 until opponentsNumber) {
			val color = Color.getHSBColor(1.0f / opponentsNumber * i, 1.0f, 1.0f)
			val start = vec2(
				(-Math.sin(Math.PI * 2 / (opponentsNumber + 1) * (i + 1)) * 900).toInt() + 1000,
				(-Math.cos(Math.PI * 2 / (opponentsNumber + 1) * (i + 1)) * 900).toInt() + 1000)
			fractionList.add(
				StandardAI(
					start, color,
					Resources(50 * (difficulty + 1), 30 * (difficulty + 1), 5 * (difficulty + 1), 100 * (difficulty + 1)),
					difficulty * 50, i + 1
				)
			)
		}
	}

	override fun render(g2d: Graphics2D) {
		drawObjects(g2d)
		drawInterface(g2d)
	}
	
	override fun update() {
		updateObjects()
		updateInput()
		tick++
	}
}
