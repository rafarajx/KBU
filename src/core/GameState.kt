package core

import java.awt.Graphics2D

open class GameState {
	
	open var id: Int = 0
	
	open fun onSet(){
		println("function onSet() from GameState class")
	}
	
	open fun render(g2d: Graphics2D){}
	
	open fun update(){}
	
}