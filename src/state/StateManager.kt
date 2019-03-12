package state

import core.GameState
import java.awt.Graphics2D

object StateManager {

	var state: GameState = Menu
	set(value) {
		value.onSet()
		field = value
	}
	
	init {
		state.onSet()
	}
	
	fun render(g2d: Graphics2D) {
		state.render(g2d)
	}
	
	fun update() {
		state.update()
	}
}
