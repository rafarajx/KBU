package gametype

import java.awt.Color
import java.awt.Graphics2D

import core.Resources
import core.Screen
import fraction.Player
import fraction.StandardAI
import fraction.ZombieAI
import math.vec2
import sound.SimpleSound

object ZombieApocalypse : Game() {

	private var showTime = true

	fun init(FriendsNumber: Int, difficulty: Int) {
		var difficulty = difficulty
		setupMenuBar()
		natureList.clear()
		fractionList.clear()
		setupNature(FriendsNumber + 1)
		var start = vec2(1000.0f, 1000.0f)
		fractionList.add(Player(start, Color.BLUE, Resources(100, 60, 8, 30), 0))
		difficulty++
		for (i in 0 until FriendsNumber) {
			val red = random.nextInt(255)
			val green = random.nextInt(255)
			val blue = random.nextInt(255)
			val c = Color(red, green, blue)
			start = vec2(random.nextInt(2000), random.nextInt(2000))
			fractionList.add(
				StandardAI(
					start, c,
					Resources(200 / difficulty, 120 / difficulty, 20 / difficulty, 60 / difficulty),
					(5 - difficulty) * 50, 0
				)
			)
		}
		fractionList.add(ZombieAI(1000, 1000, Color.RED, 1))
	}

	override fun render(g2d: Graphics2D) {
		drawObjects(g2d)
		drawInterface(g2d)
		if (!this.showTime) {
			return
		}
		val ticksLeft = tick - toTicks(intArrayOf(10))
		val timeLeft = toTime(ticksLeft)
		if (ticksLeft > 0) {
			SimpleSound.bossdeath.play()
			this.showTime = false
		}
		Screen.drawString(g2d, "Zombies will attack in: " + timeLeft[0] + ":" + timeLeft[1] + timeLeft[2], 200, 100, 2.0)
	}

	override fun update() {
		updateObjects()
		updateInput()
		tick++
	}
}
