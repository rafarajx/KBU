package core

import java.awt.image.BufferedImage

import javax.swing.JFrame

object Frame : JFrame() {
    
    init {
        title = Main.GAME_TITLE
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        iconImage = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)
    
        add(Canvas)
    
        pack()
        setLocationRelativeTo(null)
        isVisible = true
    
        Thread { Canvas.loop() }.start()
    }
}
