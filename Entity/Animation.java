package Entity;

import java.awt.image.BufferedImage;

public class Animation {

	private BufferedImage[] frames;
	private int currentFrame;
	private long startTime;
	private long delay;
	private boolean playedOnce;
	public Animation() {
		playedOnce = false;
	}

	public void setFrames(BufferedImage[] frames) {
		this.frames = frames;
		currentFrame = 0;
		startTime = System.nanoTime();
		playedOnce = false;
	}

	public void setDelay(long d) { delay = d; }

	public void update() {
		if(delay == -1) return;
		long elapsed = (System.nanoTime() - startTime) / 1000000;
		if(elapsed > delay) {
			currentFrame++;
			startTime = System.nanoTime();
		}
		if(currentFrame == frames.length) {
			currentFrame = frames.length-1;
			playedOnce = true;
		}
	}

	public void update2() {
		if(delay == -1) return;
		long elapsed = (System.nanoTime() - startTime) / 1000000;
		if(elapsed > delay) {
			currentFrame++;
			startTime = System.nanoTime();
		}
		if(currentFrame == frames.length) {
			currentFrame = 0;
			playedOnce = true;
		}
	}

	public BufferedImage getImage() { return frames[currentFrame]; }
	public void playedOnce(boolean po){ playedOnce = po; }
	public boolean hasPlayedOnce() { return playedOnce; }
}
















