package state

import java.awt.Graphics2D

import gametype.Game

object StateManager {

    private var state = State.Menu
    private var game: Game? = null

    enum class State {
        Menu, GameOptions, Help, Game
    }

    fun changeState(s: State) {
        state = s
    }

    fun setGame(game: Game) {
        StateManager.game = game
    }

    fun render(g2d: Graphics2D) {
        when (state) {
            StateManager.State.Game -> game!!.render(g2d)
            StateManager.State.GameOptions -> GameOptions.render(g2d)
            StateManager.State.Help -> Help.render(g2d)
            StateManager.State.Menu -> Menu.render(g2d)
        }
    }

    fun update() {
        when (state) {
            StateManager.State.Game -> game!!.update()
            StateManager.State.GameOptions -> GameOptions.update()
            StateManager.State.Help -> Help.update()
            StateManager.State.Menu -> Menu.update()
        }
    }
}
