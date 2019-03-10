package gametype

import java.awt.Color
import java.awt.Graphics2D

import core.Resources
import fraction.StandardAI
import math.vec2

object FFA : Game() {
	
	var opponentsNumber = 0
	var difficulty = 0

	override fun onSet() {
		setupMenuBar()
		
		natureList.clear()
		rockList.clear()
		treeList.clear()
		grassList.clear()
		wheatList.clear()
		cloudList.clear()
		flowersList.clear()
		
		fractionList.clear()
		
		setupNature(opponentsNumber + 1)
		
		fractionList.add(player)
		
		for (i in 0 until opponentsNumber) {
			val color = Color.getHSBColor(1.0f / opponentsNumber * i, 1.0f, 1.0f)
			val start = vec2(
				(-Math.sin(Math.PI * 2 / (opponentsNumber + 1) * (i + 1)) * 900).toInt(),
				(-Math.cos(Math.PI * 2 / (opponentsNumber + 1) * (i + 1)) * 900).toInt())
			fractionList.add(
				StandardAI(
					start, color,
					Resources(50 * (difficulty + 1), 30 * (difficulty + 1), 5 * (difficulty + 1), 100 * (difficulty + 1)),
					difficulty * 50, i + 1
				)
			)
		}
		setBasicInput()
	}

	override fun render(g2d: Graphics2D) {
		drawObjects(g2d)
		drawInterface(g2d)
	}
	
	override fun update() {
		updateObjects()
		tick++
	}
}
