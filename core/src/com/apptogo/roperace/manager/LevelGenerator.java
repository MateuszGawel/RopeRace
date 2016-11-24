package com.apptogo.roperace.manager;

import java.util.ArrayList;
import java.util.List;

import com.apptogo.roperace.actors.Hoop;
import com.apptogo.roperace.custom.MyShapeRenderer;
import com.apptogo.roperace.custom.MyShapeRenderer.ShapeType;
import com.apptogo.roperace.exception.LevelException;
import com.apptogo.roperace.physics.BodyBuilder;
import com.apptogo.roperace.physics.UserData;
import com.apptogo.roperace.physics.UserData.SegmentType;
import com.apptogo.roperace.screen.GameScreen;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class LevelGenerator{

	private TiledMap map;
	private OrthographicCamera  camera;
	private Vector2 startingPoint;
	private int levelNumber;
	private GameScreen screen;
	
	private MyShapeRenderer shapeRenderer;
	private List<Body> levelBodies = new ArrayList<Body>();
	
	public LevelGenerator(GameScreen screen) {
		this.camera = (OrthographicCamera)screen.getFrontStage().getCamera();
		shapeRenderer = new MyShapeRenderer();
		this.screen = screen;
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
				
				
				if("end".equals(mapObject.getName())){
					Float rotation = Float.valueOf((String)((RectangleMapObject) mapObject).getProperties().get("rotation"));
					Hoop hoop = new Hoop(screen, position, rotation);
					continue;
				}
				
				Body body = BodyBuilder.get()
					.type(BodyType.StaticBody)
					.position(position)
					.addFixture("level", "nonkilling").box(size)
					.create();
				
				levelBodies.add(body);
				UserData.get(body).segmentType = SegmentType.CATCHABLE;
				UserData.get(body).position = position;
				UserData.get(body).size = size;
			} else if (mapObject instanceof EllipseMapObject) {
				Ellipse ellipse = ((EllipseMapObject) mapObject).getEllipse();
				Vector2 position = new Vector2(UnitConverter.toBox2dUnits(ellipse.x + ellipse.width / 2), UnitConverter.toBox2dUnits(ellipse.y + ellipse.height / 2));

				if ("start".equals(mapObject.getName())) {
					startingPoint = position;
					continue;
				}

				float radius = UnitConverter.toBox2dUnits(Math.max(ellipse.height / 2, ellipse.width / 2));

				Body body = BodyBuilder.get().type(BodyType.StaticBody).position(position).addFixture("level", "nonkilling").circle(radius).create();

				levelBodies.add(body);
				UserData.get(body).segmentType = SegmentType.CATCHABLE;
				UserData.get(body).position = position;
				UserData.get(body).radius = radius;

			}
			else if(mapObject instanceof PolygonMapObject){
				Polygon polygon = ((PolygonMapObject)mapObject).getPolygon();
				Vector2 position = new Vector2(UnitConverter.toBox2dUnits(polygon.getX()), UnitConverter.toBox2dUnits(polygon.getY()));
				
				float[] vertices = ((PolygonMapObject)mapObject).getPolygon().getVertices();
				float[] transformedVertices = ((PolygonMapObject)mapObject).getPolygon().getTransformedVertices();
				float[] worldVertices = new float[vertices.length];
				float[] worldTransformedVertices = new float[transformedVertices.length];
				
				for (int i = 0; i < vertices.length; ++i) {
					worldVertices[i] = UnitConverter.toBox2dUnits(vertices[i]);
					worldTransformedVertices[i] = UnitConverter.toBox2dUnits(transformedVertices[i]);
				}
				
				Body body = BodyBuilder.get()
				.type(BodyType.StaticBody)
				.position(position)
				.addFixture("level", "nonkilling").polygon(worldVertices)
				.create();
				
				levelBodies.add(body);
				UserData.get(body).segmentType = SegmentType.CATCHABLE;
				UserData.get(body).position = position;
				UserData.get(body).vertices = worldTransformedVertices;

			}
			else if(mapObject instanceof PolylineMapObject){
				throw new LevelException("Polylines are not supported" + levelNumber);
			}
		}
		
		if(startingPoint == null){
			throw new LevelException("Starting point must be set on map: level" + levelNumber);
		}
	}
	
	/**
	 * renders all level bodies with color
	 */
	public void render(){
		shapeRenderer.setProjectionMatrix(camera.combined);
		
		shapeRenderer.begin(ShapeType.Filled);
		
		//render background
		shapeRenderer.setColor(1, 1, 1, 1);
		shapeRenderer.rect(0, 0, getMapSize().x, getMapSize().y);
			
		shapeRenderer.setColor(0, 0.7f, 1, 1);
		for(Body levelBody : levelBodies){
			UserData ud = UserData.get(levelBody);
			if(ud.segmentType == SegmentType.CATCHABLE && ud.position != null){
				if(ud.size != null){				
					shapeRenderer.rect(ud.position.x - ud.size.x/2, ud.position.y - ud.size.y/2, ud.size.x, ud.size.y);
				}
				else if(ud.vertices != null){
					shapeRenderer.polygon(ud.vertices);
				}
				else if(ud.radius != null){
					shapeRenderer.circle(ud.position.x, ud.position.y, ud.radius, (int) (40 * ud.radius));
				}
			}
		}
		shapeRenderer.end();
	}
	
	
	/**
	 * @return Vector2 with width and height of level in graphics units
	 */
	public Vector2 getMapSize()
	{
		MapProperties mapProperties = map.getProperties();
		
		int mapWidth = mapProperties.get("width", Integer.class);
		int mapHeight = mapProperties.get("height", Integer.class);
		int tilePixelWidth = mapProperties.get("tilewidth", Integer.class);
		int tilePixelHeight = mapProperties.get("tileheight", Integer.class);
		
		int mapPixelWidth = mapWidth * tilePixelWidth;
		int mapPixelHeight = mapHeight * tilePixelHeight;

		return new Vector2(mapPixelWidth, mapPixelHeight);
	}
}
