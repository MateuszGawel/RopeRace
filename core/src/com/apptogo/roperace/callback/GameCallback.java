package com.apptogo.roperace.callback;

public interface GameCallback {
	public void showLeaderboard();
	public void showAchievements();
	public void shareOnGooglePlus();
	public void submitScore(int score);
	public void setBannerVisible(boolean visible);
	public void showFullscreenAd();
	public void vibrate();
	public void incrementAchievement(String achievementId);
	public void incrementAchievement(String achievementId, int step);
	public void unlockAchievement(String achievementId);
}
