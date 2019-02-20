package sound

import java.applet.Applet
import java.applet.AudioClip

class SimpleSound(name: String) {
    private val clip: AudioClip

    init {
        this.clip = Applet.newAudioClip(SimpleSound::class.java!!.getResource(name))
    }

    fun play() {
        this.clip.play()
    }

    fun loop() {
        this.clip.loop()
    }

    fun stop() {
        this.clip.stop()
    }

    companion object {
        val Die = SimpleSound("/playerhurt.wav")
        val playerDeath = SimpleSound("/death.wav")
        val monsterHurt = SimpleSound("/monsterhurt.wav")
        val test = SimpleSound("/test.wav")
        val pickup = SimpleSound("/pickup.wav")
        val bossdeath = SimpleSound("/bossdeath.wav")
        val craft = SimpleSound("/craft.wav")
        val explosion = SimpleSound("/explosion.wav")
    }
}
