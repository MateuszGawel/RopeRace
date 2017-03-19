package com.apptogo.roperace.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;

public class ShadowedButton extends Group {

	private Image circle;
	private Image content;
	private Image shadow;

	private ColorSet colorSet;
	
	public enum ButtonSize{
		BIG, SMALL
	}
	
	public ShadowedButton(String content, ColorSet colorSet){
		setName(content);
		this.colorSet = colorSet;
		this.content = Image.getFromTexture(content);
		this.shadow = Image.getFromTexture(content + "_shadow");

		this.content.size(this.content.getRegion().getRegionWidth(), this.content.getRegion().getRegionHeight());
		shadow.size(shadow.getRegion().getRegionWidth(), shadow.getRegion().getRegionHeight());

		shadow.setColor(colorSet.getShadowColor());

		this.addActor(shadow);
		this.addActor(this.content);
		setSize(this.content.getWidth(), this.content.getHeight());
	}
	
	public ShadowedButton(String content, ColorSet colorSet, ButtonSize buttonSize) {
		this(content, colorSet);
		addCircleBackground(colorSet, buttonSize);
		setSize(this.circle.getWidth(), this.circle.getHeight());
	}
	
	public void applyColorToContent(ColorSet colorSet){
		content.setColor(colorSet.getMainColor());
		shadow.setColor(colorSet.getShadowColor());
		shadow.toFront();
	}
	
	private void addCircleBackground(ColorSet colorSet, ButtonSize buttonSize){
		if(buttonSize == ButtonSize.BIG){
			this.circle = Image.getFromTexture("circle");
		}
		else{
			this.circle = Image.getFromTexture("circle-small");
		}
		
		circle.size(circle.getRegion().getRegionWidth(), circle.getRegion().getRegionHeight());
		circle.setColor(colorSet.getMainColor());
		this.addActor(circle);
		
		this.setWidth(circle.getWidth());
		this.setHeight(circle.getHeight());
		
		circle.toBack();
	}

	public ColorSet getColorSet() {
		return colorSet;
	}

}
