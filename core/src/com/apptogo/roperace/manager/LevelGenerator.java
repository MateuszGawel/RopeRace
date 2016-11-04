package com.apptogo.roperace.manager;

import com.apptogo.roperace.exception.LevelException;
import com.apptogo.roperace.physics.BodyBuilder;
import com.apptogo.roperace.screen.GameScreen;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class LevelGenerator{

	private TiledMap map;
	private OrthographicCamera  camera;
	private GameScreen screen;
	private Vector2 startingPoint;
	private int levelNumber;
	
	public LevelGenerator(GameScreen screen) {
		this.screen = screen;
		this.camera = (OrthographicCamera)screen.getFrontStage().getCamera();
	}

	public void loadLevel(int levelNumber){
		this.levelNumber = levelNumber;
		map = ResourcesManager.getInstance().loadAndGetTiledMap(levelNumber);
		createObjects();
	}
	
	public Vector2 getStartingPoint(){
		return startingPoint;
	}

	private void createObjects(){
		for(MapObject mapObject : map.getLayers().get("terrain").getObjects()){
			if(mapObject instanceof RectangleMapObject){
				Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
				
				Vector2 size = new Vector2(UnitConverter.toBox2dUnits(rectangle.width), UnitConverter.toBox2dUnits(rectangle.height));
				Vector2 position = new Vector2(UnitConverter.toBox2dUnits(rectangle.x + rectangle.width/2), UnitConverter.toBox2dUnits(rectangle.y + rectangle.height/2));
				
				BodyBuilder.get()
					.type(BodyType.StaticBody)
					.position(position)
					.addFixture("level", "nonkilling").box(size)
					.create();
			}
			else if(mapObject instanceof EllipseMapObject){
				Ellipse ellipse = ((EllipseMapObject)mapObject).getEllipse();
				Vector2 position = new Vector2(UnitConverter.toBox2dUnits(ellipse.x + ellipse.width/2), UnitConverter.toBox2dUnits(ellipse.y + ellipse.height/2));
				
				if("start".equals(mapObject.getName())){
					startingPoint = position;
				}
				else{
					float radius = UnitConverter.toBox2dUnits(ellipse.width/2);
					
					BodyBuilder.get()
						.type(BodyType.StaticBody)
						.position(position)
						.addFixture("level", "nonkilling").circle(radius)
						.create();
				}
			}
			else if(mapObject instanceof PolygonMapObject){
				
			}
			else if(mapObject instanceof PolylineMapObject){
				
			}
		}
		
		if(startingPoint == null){
			throw new LevelException("Starting point must be set on map: level" + levelNumber);
		}
	}
}
