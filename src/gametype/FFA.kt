package gametype

import core.Fog
import core.Resources
import core.World
import fraction.Player
import fraction.StandardAI
import math.vec2
import java.awt.Color
import java.awt.Graphics2D
import kotlin.math.cos
import kotlin.math.sin

object FFA : Game() {
	
	var opponentsNumber = 0
	var difficulty = 0

	override fun onSet() {
		setupMenuBar()
		
		World.natureList.clear()
		World.rockList.clear()
		World.treeList.clear()
		World.grassList.clear()
		World.wheatList.clear()
		World.cloudList.clear()
		World.flowersList.clear()
		
		World.fractionList.clear()
		
		World.setupNature(opponentsNumber + 1)
		
		player = Player(vec2(0.0f, -900.0f), Color.BLUE, Resources(100, 60, 10, 200), 0)
		World.fractionList.add(player)
		
		for (i in 0 until opponentsNumber) {
			val color = Color.getHSBColor(1.0f / opponentsNumber * i, 1.0f, 1.0f)
			val start = vec2(
				(-sin(Math.PI * 2 / (opponentsNumber + 1) * (i + 1)) * 900).toInt(),
				(-cos(Math.PI * 2 / (opponentsNumber + 1) * (i + 1)) * 900).toInt())
			World.fractionList.add(
				StandardAI(
					start, color,
					Resources(50 * (difficulty + 1), 30 * (difficulty + 1), 5 * (difficulty + 1), 100 * (difficulty + 1)),
					difficulty * 50, i + 1
				)
			)
		}
		
		
		/*
		val f1 = StandardAI(vec2(1000, 1000), Color.RED, Resources(200), 1, 0)
		val f2 = StandardAI(vec2(-1000, -1000), Color.BLUE, Resources(200), 1, 1)
		
		fractionList.add(f1)
		fractionList.add(f2)
		
		Barrack(vec2(120, 100), f1, 0).add()
		House(vec2(170, 100), f1, 0).add()
		
		Barrack(vec2(-120, -100), f2, 1).add()
		House(vec2(-190, -100), f2, 1).add()
		*/
		
	}

	override fun renderGL() {
		drawObjectsGL()
		if(fogEnabled)
			Fog.render(Game.player.buildingList)
		drawInterfaceGL()
	}
	
	override fun update() {
		World.update()
		tick++
	}
}
