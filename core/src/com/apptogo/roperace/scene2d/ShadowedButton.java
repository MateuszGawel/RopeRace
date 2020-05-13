package com.apptogo.roperace.scene2d;

import com.apptogo.roperace.enums.ColorSet;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Group;

public class ShadowedButton extends Group {

	protected Image circle;
	protected Image content;
	protected Image shadow;

	protected ColorSet colorSet;
	
	public enum ButtonSize{
		BIG, SMALL
	}
	
	public ShadowedButton() {
	}
	
	public ShadowedButton(String content, ColorSet colorSet){
		setName(content);
		this.colorSet = colorSet;
		
		this.shadow = Image.get(content + "_shadow");
		AtlasRegion shadowRegion = ((AtlasRegion)this.shadow.getRegion());
		this.shadow.setColor(colorSet.getSecondaryColor());
		this.shadow.position(shadowRegion.offsetX, shadowRegion.offsetY);
		this.addActor(shadow);
		
		this.content = Image.get(content);
		AtlasRegion contentRegion = ((AtlasRegion)this.content.getRegion());
		this.content.position(contentRegion.offsetX, contentRegion.offsetY);
		this.addActor(this.content);
		
		setSize(contentRegion.originalWidth, contentRegion.originalHeight);
	}
	
	public ShadowedButton(String content, ColorSet colorSet, ButtonSize buttonSize) {
		this(content, colorSet);
		addCircleBackground(colorSet, buttonSize);
		setSize(this.circle.getWidth(), this.circle.getHeight());
	}

	public void applyColorToContent(ColorSet colorSet){
		content.setColor(colorSet.getMainColor());
		shadow.setColor(colorSet.getSecondaryColor());
		shadow.toFront();
	}
	
	protected void addCircleBackground(ColorSet colorSet, ButtonSize buttonSize){
		if(buttonSize == ButtonSize.BIG){
			this.circle = Image.get("circle");
		}
		else{
			this.circle = Image.get("circle-small");
		}
		
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
