package state

import java.awt.Color
import java.awt.Graphics2D

import core.Canvas
import core.Input
import core.Screen
import gametype.FFA
import gametype.SuppressUprising
import gametype.ZombieApocalypse

object GameOptions {
    private var gameMode = 0
    private val GAME_MODES = arrayOf("Free For All", "Suppress Uprising", "Zombie Apocalypse")
    private var optionNum = 0
    private var numberOfEnemies = 2
    private var difficulty = 1
    private val DIFFICULTIES = arrayOf("Easy", "Normal", "Hard", "Insane")
    private val S1 = "Press enter to continue..."

    private var tick = 1

    fun render(g2d: Graphics2D) {
        g2d.drawImage(Menu.BG, 0, 0, Canvas.WIDTH, Canvas.HEIGHT, null)
        g2d.color = Color(180, 180, 180, 180)
        g2d.fill3DRect(50, 50, 780, 660, true)
        Screen.drawString(g2d, "Game Options", 225, 100, 4.0)
        Screen.drawString(g2d, "Game Mode:", 130, 200, 3.0)
        if (optionNum == 0 && tick % 30 / 15 == 0) {
            Screen.drawString(g2d, "<" + GAME_MODES[gameMode] + ">", 520 - GAME_MODES[gameMode].length * 8, 200, 3.0)
        } else {
            Screen.drawString(g2d, "<" + GAME_MODES[gameMode] + ">", 520 - GAME_MODES[gameMode].length * 8, 202, 3.0)
        }
        if (gameMode == 0) {
            Screen.drawString(g2d, "Opponents Number:", 130, 250, 3.0)
        } else if (gameMode == 1) {
            Screen.drawString(g2d, "Friends Number:", 130, 250, 3.0)
        } else {
            Screen.drawString(g2d, "Friends Number:", 130, 250, 3.0)
        }
        if (optionNum == 1 && tick % 30 / 15 == 0) {
            Screen.drawString(g2d, "<$numberOfEnemies>", 570, 250, 3.0)
        } else {
            Screen.drawString(g2d, "<$numberOfEnemies>", 570, 252, 3.0)
        }
        Screen.drawString(g2d, "Difficulty:", 130, 300, 3.0)
        if (optionNum == 2 && tick % 30 / 15 == 0) {
            Screen.drawString(g2d, "<" + DIFFICULTIES[difficulty] + ">",
                    500 - DIFFICULTIES[difficulty].length * 8, 300, 3.0)
        } else {
            Screen.drawString(g2d, "<" + DIFFICULTIES[difficulty] + ">",
                    500 - DIFFICULTIES[difficulty].length * 8, 302, 3.0)
        }
        for (i in 0 until S1.length) {
            if (tick % (S1.length * 7) / 7 == i) {
                Screen.drawString(g2d, S1.substring(i, i + 1), 240 + i * 16, 664, 2.0)
            } else {
                Screen.drawString(g2d, S1.substring(i, i + 1), 240 + i * 16, 666, 2.0)
            }
        }
    }

    fun update() {
        if (tick % 5 == 0) {
            if (Input.isKeyDown(37)) {
                if (optionNum == 0) {
                    if (gameMode > 0) {
                        gameMode--
                    }
                } else if (optionNum == 1) {
                    if (numberOfEnemies > 0) {
                        numberOfEnemies--
                    }
                } else if (optionNum == 2 && difficulty > 0) {
                    difficulty--
                }
            }
            if (Input.isKeyDown(39)) {
                if (optionNum == 0) {
                    if (gameMode < GAME_MODES.size - 1) {
                        gameMode++
                    }
                } else if (optionNum == 1) {
                    if (numberOfEnemies < 7) {
                        numberOfEnemies++
                    }
                } else if (optionNum == 2 && difficulty < DIFFICULTIES.size - 1) {
                    difficulty++
                }
            }
            if (Input.isKeyDown(38) && optionNum > 0) {
                optionNum--
            }
            if (Input.isKeyDown(40) && optionNum < 2) {
                optionNum++
            }
        }
        if (Input.isKeyDown(10)) {
            when (gameMode) {
                0 -> StateManager.setGame(FFA(numberOfEnemies, difficulty + 1))
                1 -> StateManager.setGame(SuppressUprising(numberOfEnemies, difficulty + 1))
                2 -> StateManager.setGame(ZombieApocalypse(numberOfEnemies, difficulty + 1))
            }
            StateManager.changeState(StateManager.State.Game)
        }
        tick++
    }
}
