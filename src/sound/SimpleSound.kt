package sound

import core.Main
import java.applet.Applet
import java.applet.AudioClip
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.FloatControl

class SimpleSound(name: String) {
   
	private var clip = AudioSystem.getClip()
	var gain: FloatControl
	
	init {
		clip.open(AudioSystem.getAudioInputStream(Main::class.java.getResource(name)))
		gain = clip.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
	}

	fun play() {
		clip.start()
	}

	fun loop(times: Int) {
		clip.loop(times)
	}

	fun stop() {
		clip.stop()
	}

	companion object {
		val die = SimpleSound("/playerhurt.wav")
		val playerDeath = SimpleSound("/death.wav")
		val monsterHurt = SimpleSound("/monsterhurt.wav")
		val test = SimpleSound("/test.wav")
		val pickup = SimpleSound("/pickup.wav")
		val bossdeath = SimpleSound("/bossdeath.wav")
		val craft = SimpleSound("/craft.wav")
		val explosion = SimpleSound("/explosion.wav")
		val soundtrack = SimpleSound("/soundtrack.wav")
	}
}
