package com.apptogo.roperace.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;

public class ShadowedButton extends Group {

	private Image circle;
	private Image content;
	private Image shadow;

	public enum ButtonSize{
		BIG, SMALL
	}
	
	public ShadowedButton(String content, ColorSet colorSet, ButtonSize buttonSize) {
		super();
		if(buttonSize == ButtonSize.BIG){
			this.circle = Image.getFromTexture("circle");
		}
		else{
			this.circle = Image.getFromTexture("circle-small");
		}
		this.content = Image.getFromTexture(content);
		this.shadow = Image.getFromTexture(content + "_shadow");

		circle.size(circle.getRegion().getRegionWidth(), circle.getRegion().getRegionHeight());
		this.content.size(this.content.getRegion().getRegionWidth(), this.content.getRegion().getRegionHeight());
		shadow.size(shadow.getRegion().getRegionWidth(), shadow.getRegion().getRegionHeight());

		circle.setColor(colorSet.getMainColor());
		shadow.setColor(colorSet.getShadowColor());

		this.addActor(circle);
		this.addActor(shadow);
		this.addActor(this.content);

		this.setWidth(circle.getWidth());
		this.setHeight(circle.getHeight());
	}

}
