package game;

import utility.KeyList;

public interface FrameUpdateListener {

	public void addFrame(KeyList e);

	public boolean isPlayable();

	public boolean isFinished();

	public boolean isDestroying();

	public void destroy(FreezePanel f);

}
