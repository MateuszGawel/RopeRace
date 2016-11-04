package com.apptogo.roperace.screen;

import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.scene2d.Image;
import com.apptogo.roperace.scene2d.Listener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class LevelSelectionScreen extends BasicScreen {

	private static final int NUMBER_OF_LEVELS=10;
	
	public LevelSelectionScreen(Main game) {
		super(game);
	}
	
	private Group levelGroup;
	private Table table;
	private ScrollPane scrollPane;
	
	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ---------------------------------------------- PREPARATION ----------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/
	
	@Override
	protected void prepare() {

		prepareBackStage();
		prepareFrontStage();
		prepareScrollPane();
	}

	

	protected void prepareBackStage() {
		//		Image background = Image.get("background").width(Main.SCREEN_WIDTH).position(0, -Main.SCREEN_HEIGHT / 2f).centerX();
		//		backStage.addActor(background);
	}

	protected void prepareFrontStage() {
		Image backButton = Image.getFromTexture("back-button");
		backButton.size(backButton.getRegion().getRegionWidth(), backButton.getRegion().getRegionHeight())
			.position(Main.SCREEN_WIDTH/2 - backButton.getRegion().getRegionWidth() - 20, -Main.SCREEN_HEIGHT/2 + 20)
			.addListener(Listener.click(game, new MenuScreen(game)));
		frontStage.addActor(backButton);
		
	}
	
	private void prepareScrollPane()
	{		
		float padding = 50;
		
		TextureRegion dummyImage = Image.getFromTexture("level-button").getRegion();
		
        table = new Table();
		table.row().pad(0, padding, 0, padding);

    	for(int i=0; i<=NUMBER_OF_LEVELS; i++){
	    	Image image = Image.getFromTexture("level-button");
	    	image.size(image.getRegion().getRegionWidth(), image.getRegion().getRegionHeight());
	    	image.addListener(Listener.click(game, new GameScreen(game, i+1)));
	    	
	    	Cell<Image> cell = table.add(image);
	    	if(i==0){
	    		cell.pad(0, Main.SCREEN_WIDTH/2 - image.getRegion().getRegionWidth()/2, 0, padding);
	    	}
    	}
                
        scrollPane = new ScrollPane(table);
        scrollPane.setScrollingDisabled(false, true);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setSize(Main.SCREEN_WIDTH, dummyImage.getRegionHeight());
        scrollPane.setPosition(-Main.SCREEN_WIDTH/2, -dummyImage.getRegionWidth()/2);
        frontStage.addActor(scrollPane);
	}
	
	/** ---------------------------------------------------------------------------------------------------------- **/
	/** -------------------------------------------------- STEP -------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/
	
	@Override
	protected void step(float delta) {
		// --- backstage render first --- //

		//simulate physics and handle body contacts


		// --- frontstage render last --- //
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.backViewport.apply();
		this.backStage.act(delta);
		this.backStage.draw();

		step(delta);

		this.frontViewport.apply();
		this.frontStage.act(delta);
		this.frontStage.draw();

		handleInput();
	}
	
	private void handleSwipe(){
		
	}
	
	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ------------------------------------------------ DISPOSE ------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void dispose() {
		super.dispose();
		frontStage.dispose();
	}
}
