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
	
	fun renderGL() {
		state.renderGL()
	}
	
	fun update() {
		state.update()
	}

	fun keyCallback(key: Int, scancode: Int, action: Int, mods: Int){
		state.keyCallback(key, scancode, action, mods)
	}

	fun windowPosCallback(xpos: Int, ypos: Int){
		state.windowPosCallback(xpos, ypos)
	}

	fun mouseButtonCallback(button: Int, action: Int, mods: Int){
		state.mouseButtonCallback(button, action, mods)
	}

	fun cursorPosCallback(xpos: Float, ypos: Float){
		state.cursorPosCallback(xpos, ypos)
	}
}
