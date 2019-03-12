package core

import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener

object Input : MouseListener, MouseMotionListener, KeyListener {
	
	interface KEvent{
		fun get(e: KeyEvent)
	}
	
	interface MEvent{
		fun get(e: MouseEvent)
	}
	
	var keyPressed: KEvent? = null
	
	var mousePressed: MEvent? = null
	var mouseMoved: MEvent? = null
	var mouseDragged: MEvent? = null
	var mouseClicked: MEvent? = null
	
	override fun keyPressed(e: KeyEvent) {
		if(keyPressed != null) keyPressed!!.get(e)
	}
	
	override fun keyReleased(e: KeyEvent) {}
	
	override fun keyTyped(e: KeyEvent) {}
	
	override fun mouseDragged(e: MouseEvent) {
		if(mouseDragged != null) mouseDragged!!.get(e)
	}
	
	override fun mouseMoved(e: MouseEvent) {
		if(mouseMoved != null) mouseMoved!!.get(e)
	}
	
	override fun mouseClicked(e: MouseEvent) {
		if(mouseClicked != null) mouseClicked!!.get(e)
	}
	
	override fun mouseEntered(e: MouseEvent) {}
	
	override fun mouseExited(e: MouseEvent) {}
	
	override fun mousePressed(e: MouseEvent) {
		if(mousePressed != null) mousePressed!!.get(e)
	}
	
	override fun mouseReleased(e: MouseEvent) {}
}
