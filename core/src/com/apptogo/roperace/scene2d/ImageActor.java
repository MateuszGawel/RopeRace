package com.apptogo.roperace.scene2d;

import com.apptogo.roperace.manager.ResourcesManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class ImageActor extends Image {

    com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> animation;

    private float width, height;
    protected float scaleBy = 1;
    private Vector2 customOffset = new Vector2();

    public ImageActor(String textureName)
    {
        super(ResourcesManager.getInstance().manager.get(textureName + ".png", Texture.class));
    }

    public void scaleFramesBy(float scale)
    {
        scaleBy = scale;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        width = getRegion().getRegionWidth();
        height = getRegion().getRegionHeight();

        setSize(width * scaleBy, height * scaleBy);
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        batch.setColor(this.getColor().r, this.getColor().g, this.getColor().b, this.getColor().a * parentAlpha);
        batch.draw(getRegion(),
                getX() + customOffset.x,
                getY() + customOffset.y,
                getOriginX(),
                getOriginY(),
                getWidth(),
                getHeight(),
                getScaleX(),
                getScaleY(),
                getRotation());
    }

    /** @return customOffset in box2d units */
    public Vector2 getCustomOffset() {
        return customOffset;
    }

    /** @param customOffset in box2d units */
    public void setCustomOffset(Vector2 customOffset) {
        this.customOffset = customOffset;
    }
}
