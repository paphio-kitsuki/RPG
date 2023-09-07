package game;

import java.util.Random;

public class Stopper {

	private double borntime;

	public Stopper() {
		borntime = System.currentTimeMillis() + new Random().nextDouble();
	}

	public boolean equals(Stopper s) {
		return this.borntime == s.borntime;
	}
}
