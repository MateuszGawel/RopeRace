package com.apptogo.roperace.level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.apptogo.roperace.actors.Hoop;
import com.apptogo.roperace.actors.Star;
import com.apptogo.roperace.custom.MyShapeRenderer;
import com.apptogo.roperace.custom.MyShapeRenderer.ShapeType;
import com.apptogo.roperace.enums.ColorSet;
import com.apptogo.roperace.exception.LevelException;
import com.apptogo.roperace.level.LevelData.LevelType;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.manager.ResourcesManager;
import com.apptogo.roperace.physics.BodyBuilder;
import com.apptogo.roperace.physics.UserData;
import com.apptogo.roperace.physics.UserData.SegmentType;
import com.apptogo.roperace.scene2d.Label;
import com.apptogo.roperace.screen.GameScreen;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

public class LevelGenerator{

	private TiledMap map;
	private OrthographicCamera  camera;
	private Vector2 startingPoint;
	private int levelNo;
	private GameScreen screen;
	
	private MyShapeRenderer shapeRenderer;
	private List<Body> levelBodies = new ArrayList<Body>();
	private Map<Integer, MapObject> polylinesTop = new HashMap<Integer, MapObject>();
	private Map<Integer, MapObject> polylinesBottom = new HashMap<Integer, MapObject>();
	private Map<Float[], Float[]> polylineVertices = new HashMap<Float[], Float[]>();
	private int starCounter;
	private int currentLineId;
	
	public LevelGenerator(GameScreen screen) {
		this.camera = (OrthographicCamera)screen.getFrontStage().getCamera();
		shapeRenderer = new MyShapeRenderer();
		this.screen = screen;
	}

	public void loadLevel(int levelno, int worldNo){
		this.levelNo = levelno;
		map = ResourcesManager.getInstance().loadAndGetTiledMap(levelno, worldNo);
		fillLevelData();
		createObjects();
	}
	
	private void fillLevelData() {
		String type = map.getProperties().get("type").toString();
		Float bronzeReq = Float.valueOf(map.getProperties().get("bronzeReq").toString());
		Float silverReq = Float.valueOf(map.getProperties().get("silverReq").toString());
		Float goldReq = Float.valueOf(map.getProperties().get("goldReq").toString());
		
		LevelData levelData = new LevelData(LevelType.valueOf(type.toUpperCase()), goldReq, silverReq, bronzeReq);
		screen.setLevelData(levelData);
	}

	public Vector2 getStartingPoint(){
		return startingPoint;
	}
	
	private void createObjects(){
		for(MapObject mapObject : map.getLayers().get("terrain").getObjects()){
			if(mapObject instanceof RectangleMapObject){
				Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();


				if("immaterial".equals(mapObject.getName()) && "label".equals((mapObject.getProperties().get("type", String.class)))){
					handleLabelCreation(mapObject);
					continue;
				}

				Vector2 size = new Vector2(UnitConverter.toBox2dUnits(rectangle.width), UnitConverter.toBox2dUnits(rectangle.height));
				Vector2 position = new Vector2(UnitConverter.toBox2dUnits(rectangle.x + rectangle.width/2), UnitConverter.toBox2dUnits(rectangle.y + rectangle.height/2));
				
				
				if("end".equals(mapObject.getName())){
					float rotation = 0;
					if (((RectangleMapObject) mapObject).getProperties().get("rotation", Float.class) != null)
						rotation = ((RectangleMapObject) mapObject).getProperties().get("rotation", Float.class);
					
					screen.setHoop(new Hoop(screen, position, rotation));
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
				
				if("star".equals(mapObject.getName())){
					starCounter++;
					String name = "star" + starCounter;
					Body body = BodyBuilder
							.get()
							.type(BodyType.StaticBody)
							.position(position)
							.addFixture(name, "").circle(radius)
							.sensor(true)
							.create();
					new Star(screen, body, name);
					continue;
				}
				

				Body body = BodyBuilder
						.get()
						.type(BodyType.StaticBody)
						.position(position)
						.addFixture("level", "nonkilling").circle(radius)
						.create();

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
				
				Body body = null;
				if("immaterial".equals(mapObject.getName())){
					body = BodyBuilder.get()
							.type(BodyType.StaticBody)
							.position(position)
							.addFixture("immaterial", "immaterial").polygon(worldVertices)
							.sensor(true)
							.create();
				}
				else{
					body = BodyBuilder.get()
							.type(BodyType.StaticBody)
							.position(position)
							.addFixture("level", "nonkilling").polygon(worldVertices)
							.create();
				}	
				
				levelBodies.add(body);
				UserData.get(body).segmentType = SegmentType.CATCHABLE;
				UserData.get(body).position = position;
				UserData.get(body).vertices = worldTransformedVertices;

			}
			else if(mapObject instanceof PolylineMapObject){
				if (mapObject.getProperties().get("type") != null) {
					String type = mapObject.getProperties().get("type").toString();
					//first add all polylines to hashmaps with lineId
					if ("top".equals(type)) {
						polylinesTop.put(Integer.valueOf(mapObject.getProperties().get("lineId").toString()), mapObject);
					} else {
						polylinesBottom.put(Integer.valueOf(mapObject.getProperties().get("lineId").toString()), mapObject);
					}
				}
				else{
					createPolyline(mapObject);
				}
			}
		}
		
		createPolylines();
		
		if(startingPoint == null){
			throw new LevelException("Starting point must be set on map: level" + levelNo);
		}
	}

	private void handleLabelCreation(MapObject mapObject) {
		if(mapObject instanceof RectangleMapObject) {
			Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

			Label label = Label.get(mapObject.getProperties().get("text").toString(), mapObject.getProperties().get("size").toString());
			label.position(rectangle.getX(), -Main.SCREEN_HEIGHT / 2 + rectangle.getY());
			label.setSize(rectangle.width, rectangle.height);
			label.setColor(ColorSet.LIGHT_GRAY.getMainColor());
			screen.getLabelStage().addActor(label);
		}
	}

	private void createPolylines() {
		
		//iterate through all top polylines
		for (MapObject mapObject : polylinesTop.values()) {
			System.out.println("-------------------------------");
			currentLineId = Integer.parseInt(mapObject.getProperties().get("lineId").toString());
			System.out.println("LINE: " + currentLineId);
			Float[] verticesTop = createPolyline(mapObject);
			//find proper bottom polyline
			MapObject mapObjectBottom = polylinesBottom.get(currentLineId);
			Float[] verticesBottomBeforeRaycast = createPolyline(mapObjectBottom);
			UserData lastPolylineUserData = UserData.get(levelBodies.get(levelBodies.size() - 1));
			lastPolylineUserData.polyLineBottom = true;
			lastPolylineUserData.lineId = currentLineId;
			
			//create new array of vertices to make polyline with same amount of vertices as top one (raycasting)
			Float[] verticesBottom = new Float[verticesTop.length];
			for(int i=0; i<verticesTop.length-1; i+=2){
				calculateVertex(new Vector2(verticesTop[i], verticesTop[i+1]), verticesBottomBeforeRaycast);
				
				//read temp vertex
				verticesBottom[i] = tempIntersectionVertex.x;
				verticesBottom[i+1] = tempIntersectionVertex.y;
			}
			
			//we have two lines with the same amount of vertices
			System.out.println("___________");
			System.out.println("TOP: " + Arrays.toString(verticesTop));
			System.out.println("BEFORE RAY: " + Arrays.toString(verticesBottomBeforeRaycast));
			System.out.println("BOTTOM: " + Arrays.toString(verticesBottom));
			polylineVertices.put(verticesTop, verticesBottom);
		}
	}
	
	private Float[] sortVertices(Float[] vertices){
		Float[] sortedVertices = new Float[vertices.length];
		if(vertices[0] < vertices[2]){
			return vertices;
		}
			
		for(int i=vertices.length-1, n=0; i>=0; i-=2, n+=2){
			sortedVertices[n] = vertices[i-1];
			sortedVertices[n+1] = vertices[i];
		}
		return sortedVertices;
	}
	
	private Float[] createPolyline(MapObject mapObject){
		if(mapObject == null){
			return null;
		}
		Object interpolatedProperty = mapObject.getProperties().get("interpolated");
		boolean interpolated = (interpolatedProperty != null) ? Boolean.parseBoolean(interpolatedProperty.toString()) : false;

		Polyline polyline = ((PolylineMapObject) mapObject).getPolyline();
		Vector2 position = new Vector2(UnitConverter.toBox2dUnits(polyline.getX()), UnitConverter.toBox2dUnits(polyline.getY()));
		
		float[] vertices = polyline.getVertices();
		float[] transformedVertices = polyline.getTransformedVertices();

		if(interpolated){
			vertices = interpolate(vertices);
			transformedVertices = interpolate(transformedVertices);
		}

		float[] worldVertices = new float[vertices.length];
		Float[] worldTransformedVertices = new Float[transformedVertices.length];

		for (int i = 0; i < vertices.length; ++i) {
			worldVertices[i] = UnitConverter.toBox2dUnits(vertices[i]);
			worldTransformedVertices[i] = UnitConverter.toBox2dUnits(transformedVertices[i]);
		}

		Body body = BodyBuilder.get().type(BodyType.StaticBody).position(position).addFixture("level", "nonkilling").polyline(worldVertices).create();

		levelBodies.add(body);
		UserData.get(body).segmentType = SegmentType.CATCHABLE;
		UserData.get(body).position = position;
		return sortVertices(worldTransformedVertices);
	}

	private float[] interpolate(float[] vertices) {
		Vector2 helper = new Vector2(0, 0);
		Vector2 out = new Vector2(0, 0);
		int interpolatedPoints = 20;
		float step = 1/(float)interpolatedPoints;

		float[] interpolatedVertices = new float[interpolatedPoints*2+2]; //+2 to include last point

		float i = 0;
		int j = 0;

		while(j <= interpolatedPoints*2) {
			//interpolated between 3 points for steps between 0..1. Polyline must have 3 points to work
			Vector2 interpolatedVector = Bezier.quadratic(out, i, new Vector2(vertices[0], vertices[1]), new Vector2(vertices[2], vertices[3]), new Vector2(vertices[4], vertices[5]), helper);
			interpolatedVertices[j] = (float) (Math.round(interpolatedVector.x * 100.0) / 100.0);
			interpolatedVertices[j+1] = (float) (Math.round(interpolatedVector.y * 100.0) / 100.0);
			i += step;
			j += 2;
		}

		return interpolatedVertices;
	}

	private Vector2 tempIntersectionVertex;
	private void calculateVertex(Vector2 startPoint, final Float[] verticesBottom){
		Vector2 endPoint = new Vector2(startPoint.x, -getMapSize().y);
		System.out.println("START POINT: " + startPoint);
		System.out.println("END POINT: " + endPoint);
		System.out.println("VERTICES: " + Arrays.toString(verticesBottom));
		//raycast from top vertex to bottom line and return intersection point to temp field
		screen.getWorld().rayCast(new RayCastCallback() {
			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

				UserData userData = UserData.get(fixture.getBody());
				if(verticesBottom == null){
					tempIntersectionVertex = new Vector2(point.x, 0.5f);
				}
				else if(currentLineId != userData.lineId){
					return -1; //it's not the line we are looking for
				}
				else if(point.x < verticesBottom[0]){
					tempIntersectionVertex = new Vector2(verticesBottom[0], verticesBottom[1]);
				}
				else if(point.x > verticesBottom[verticesBottom.length-2]){
					tempIntersectionVertex = new Vector2(verticesBottom[verticesBottom.length-2], verticesBottom[verticesBottom.length-1]);
				} 
				else if (!userData.polyLineBottom) {
					//it went on the bottom
					if (point.y <= 0.5) {
						tempIntersectionVertex = new Vector2(verticesBottom[0], verticesBottom[1]);
					}
					return -1;
				} 
				else {
					tempIntersectionVertex = point;
				}
				return 0;
			}
		}, startPoint, endPoint);
	}
	
	/**
	 * renders all level bodies with color
	 */
	public void render(){
		shapeRenderer.setProjectionMatrix(camera.combined);
		
		shapeRenderer.begin(ShapeType.Filled);
		
		//render background
		shapeRenderer.setColor(1, 1, 1, 1);
		shapeRenderer.rect(0, 0, UnitConverter.toBox2dUnits(getMapSize().x), UnitConverter.toBox2dUnits(getMapSize().y));
		for(Body levelBody : levelBodies){
			UserData ud = UserData.get(levelBody);
			shapeRenderer.setColor(decideColor(ud));
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
		
		//render polylines
	    Iterator<Map.Entry<Float[],Float[]>> it = polylineVertices.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry<Float[],Float[]> pair = it.next();
	        shapeRenderer.polyline(pair.getKey(),  pair.getValue());
	    }
		
		shapeRenderer.end();
	}

	private Color decideColor(UserData ud) {
		if("immaterial".equals(ud.type)){
			return ColorSet.LIGHT_GRAY.getMainColor();
		}
		return Main.getInstance().getCurrentScreen().getCurrentColorSet().getMainColor();
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
