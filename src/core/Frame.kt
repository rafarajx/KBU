package core

import java.awt.image.BufferedImage

import javax.swing.JFrame

class Frame : JFrame() {
    init {
        title = Main.GAME_TITLE
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        iconImage = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)
        isResizable = false

        val canvas = Canvas()
        add(canvas)

        pack()
        setLocationRelativeTo(null)
        isVisible = true

        Thread { canvas.loop() }.start()
    }

    companion object {
        private val serialVersionUID = 1L
        var input : Input = Input()
    }
}
