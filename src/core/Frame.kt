package core

import java.awt.Dimension
import java.awt.image.BufferedImage

import javax.swing.JFrame

object Frame : JFrame() {
    
    var minSize: Dimension
    
    init {
        title = Main.GAME_TITLE
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        iconImage = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)
        minimumSize = Dimension(1000, 800)
    
        add(Canvas)
    
        pack()
        setLocationRelativeTo(null)
        isVisible = true
    
        minSize = size
        
        Thread { Canvas.loop() }.start()
    }
}
