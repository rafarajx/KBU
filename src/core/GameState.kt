package core

import java.awt.Graphics2D

open class GameState {
	
	open var id: Int = 0
	
	open fun onSet(){
		println("function onSet() from GameState class")
	}
	
	open fun render(g2d: Graphics2D){}

	open fun renderGL(){}

	open fun update(){}

	open fun keyCallback(key: Int, scancode: Int, action: Int, mods: Int) {}

	open fun mouseButtonCallback(button: Int, action: Int, mods: Int) {}

	open fun windowPosCallback(xpos: Int, ypos: Int) {}

	open fun cursorPosCallback(xpos: Float, ypos: Float) {}

}