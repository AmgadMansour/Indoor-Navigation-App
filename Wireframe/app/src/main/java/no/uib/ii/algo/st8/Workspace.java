package no.uib.ii.algo.st8;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import no.uib.ii.algo.st8.algorithms.GraphInformation;
import no.uib.ii.algo.st8.interval.IntervalGraph;
import no.uib.ii.algo.st8.interval.SimpleToBasicWrapper;
import no.uib.ii.algo.st8.model.DefaultEdge;
import no.uib.ii.algo.st8.model.DefaultVertex;
import no.uib.ii.algo.st8.util.Coordinate;
import no.uib.ii.algo.st8.util.FileAccess;
import no.uib.ii.algo.st8.util.GraphExporter;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.SimpleGraph;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.Utils;
import no.uib.ii.algo.st8.model.BeaconLocation;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.startupcompany.wireframe.R;
import com.startupcompany.wireframe.activity.base.BasePrivateActivity;
import com.startupcompany.wireframe.model.Destination;
import com.startupcompany.wireframe.model.Room;
import com.startupcompany.wireframe.model.Transition;
import com.startupcompany.wireframe.model.User;
import com.startupcompany.wireframe.util.ApiRouter;

/**
 * @author pgd
 */
public class Workspace extends BasePrivateActivity implements OnClickListener, SensorEventListener, BeaconManager.RangingListener {
//public class Workspace extends AppCompatActivity implements OnClickListener, SensorEventListener {

	Bundle bundle;
	private Region region;
    BeaconManager beaconManager;
	private static int scanningCounter = 0;
	private static double getYlocation = 0;
	private static double getXlocation = 0;
	private static double oldXlocation = 0, oldYlocation = 0, distance = -1;

	private static double lastX;
	private static double lastY;

	private DefaultVertex destination;
	private ArrayList<DefaultVertex> destinations = new ArrayList<DefaultVertex>();
	//private DefaultVertex theDestination;

	private ArrayList<Coordinate> locations = new ArrayList<Coordinate>();
	private ArrayList<DefaultVertex> passedNodes = new ArrayList<DefaultVertex>();

	private ArrayList<DefaultVertex> transitionNodes = new ArrayList<DefaultVertex>();
	private ArrayList<Transition> theTransitions = new ArrayList<Transition>();
	private ArrayList<DefaultVertex> path = new ArrayList<DefaultVertex>();
	private ArrayList<DefaultVertex> originalPath = new ArrayList<DefaultVertex>();

	private ArrayList<Room> rooms = new ArrayList<Room>();

	private ArrayList<Coordinate> listOfAverages = new ArrayList<Coordinate>();
	int averagesIndex = 0;

	private boolean initialLoc;

	private boolean navigate;

	private ArrayList<com.startupcompany.wireframe.model.Beacon> myBeacons = new ArrayList<com.startupcompany.wireframe.model.Beacon>();

	private boolean tour;
	private boolean selectDestination;
	private boolean categoryTour;

	private boolean updatedPath;

	private String graphJSON;

	private User userLocalization;
	private long userRoomId;

	private DefaultVertex connectedTNode;

	private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);
	@Override
	public void onBackPressed() {
		if (!controller.undo())
			super.onBackPressed();
	}

	private GraphViewController controller;

	private volatile boolean saveOnExit = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//frameLayout = (FrameLayout) findViewById(R.id.root);
		//System.out.println("FRAME: " + frameLayout.toString());
		//Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		//setSupportActionBar(toolbar);

		System.out.println("done markus log hei stupedamen");
		beaconManager = new BeaconManager(this);
		beaconManager.setRangingListener(this);
		/*Point size = new Point();
		this.getWindowManager().getDefaultDisplay().getRealSize(size);
		int width = size.x;
		int height = size.y;*/

		// EDITED
		Display display = this.getWindowManager().getDefaultDisplay();
		DisplayMetrics realMetrics = new DisplayMetrics();
		display.getRealMetrics(realMetrics);
		int width = realMetrics.widthPixels;
		int height = realMetrics.heightPixels;

		System.out.println("HEIGHT: " + height);

		// EDITED
		/*DisplayMetrics displaymetrics = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		int width = displaymetrics.widthPixels;

		System.out.println("HEIGHT: " + height);*/

		// scaleGestureDetector = new ScaleGestureDetector(this,
		// new SimpleScaleGestureDetector());

		// Bitmapbmp=BitmapFactory.decodeResource(getResources(),R.drawable.bg_image);

		// Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bg_image_larger);
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.map_colored);

		BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);

		// EDITED

		// bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
		controller = new GraphViewController(this, width, height);
		controller.getView().setBackgroundDrawable(bitmapDrawable);

		setContentView(controller.getView());

		// shake
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		this.sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if (sensors.size() > 0) {
			sensor = sensors.get(0);
			sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
			System.out.println("PAAL REGISTERED SENSOR");
		}
		GraphViewController.EDGE_DRAW_MODE = false;

		String title = "Grapher";
		String message = "Tap to create vertices." + "\nHold to toggle between vertex creation and edge drawing mode.";

		// EDITED
		/*AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message).setTitle(title);

		builder.setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				shortToast("Go ahead and graph!");
			}
		});
		builder.setNeutralButton("Load", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				load();
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();*/

		load();

		loadTransitionNodes();

		loadRooms();

		loadBeacons();

		navigate = true;

		for (int i = 0; i < transitionNodes.size(); i++) {
			System.out.println("TX: " + transitionNodes.get(i).getCoordinate().getX());
		}

		bundle = getIntent().getExtras();
		String mode = bundle.getString("MODE");

		if (mode.equals("DEST")) {
			selectDestinationMode();
		}
		else if (mode.equals("TOUR")) {
			tourMode();
		}
		else if (mode.equals("CAT")) {
			categoryMode();
		}

		// EDITED
		/*frameLayout.post(new Runnable() {
			@Override
			public void run() {
				System.out.print("CANVASFRAME: " + frameLayout.getWidth() + ", " + frameLayout.getHeight());
			}
		});*/
	}

	public void loadBeacons() {
		ApiRouter.withToken(getCurrentUser().getToken()).getBeacons(1,
				new Callback<List<com.startupcompany.wireframe.model.Beacon>>() {
					@Override
					public void success(List<com.startupcompany.wireframe.model.Beacon> mapBeacons, Response rawResponse) {
						//stopProgress();
						myBeacons = (ArrayList<com.startupcompany.wireframe.model.Beacon>) mapBeacons;
					}

					@Override
					public void failure(RetrofitError e) {
						displayError(e);
					}
				});
	}

	public void loadRooms() {
		ApiRouter.withToken(getCurrentUser().getToken()).getMapRooms(1,
				new Callback<List<Room>>() {
					@Override
					public void success(List<Room> mapRooms, Response rawResponse) {
						//stopProgress();
						rooms = (ArrayList<Room>) mapRooms;
					}

					@Override
					public void failure(RetrofitError e) {
						displayError(e);
					}
				});
	}

	public void loadTransitionNodes() {
		ApiRouter.withToken(getCurrentUser().getToken()).getTransitionNodes(1,
				new Callback<List<Transition>>() {
					@Override
					public void success(List<Transition> transitions, Response rawResponse) {
						//stopProgress();
						for (int i = 0; i < transitions.size(); i++) {
							Transition t = transitions.get(i);

							float x = t.getxCoordinate();
							float y = t.getyCoordinate();

							Coordinate c = new Coordinate(x, y);

							DefaultVertex v = new DefaultVertex(c);

							transitionNodes.add(v);
						}

						theTransitions = (ArrayList<Transition>) transitions;
					}

					@Override
					public void failure(RetrofitError e) {
						displayError(e);
					}
				});

//		Coordinate c1 = new Coordinate(123.24651f, 1372.0f);
//		DefaultVertex v1 = new DefaultVertex(c1);
//		transitionNodes.add(v1);
//
//		Coordinate c2 = new Coordinate(127.0f, 1259.7137f);
//		DefaultVertex v2 = new DefaultVertex(c2);
//		transitionNodes.add(v2);
//
//		Coordinate c3 = new Coordinate(123.490364f, 1068.0f);
//		DefaultVertex v3 = new DefaultVertex(c3);
//		transitionNodes.add(v3);
//
//		Coordinate c4 = new Coordinate(267.0f, 1064.0454f);
//		DefaultVertex v4 = new DefaultVertex(c4);
//		transitionNodes.add(v4);
//
//		Coordinate c5 = new Coordinate(259.0f, 856.0f);
//		DefaultVertex v5 = new DefaultVertex(c5);
//		transitionNodes.add(v5);
//
//		Coordinate c6 = new Coordinate(267.0f, 640.0f);
//		DefaultVertex v6 = new DefaultVertex(c6);
//		transitionNodes.add(v6);
//
//		Coordinate c7 = new Coordinate(187.0f, 608.0f);
//		DefaultVertex v7 = new DefaultVertex(c7);
//		transitionNodes.add(v7);
//
//		Coordinate c8 = new Coordinate(315.0f, 612.0f);
//		DefaultVertex v8 = new DefaultVertex(c8);
//		transitionNodes.add(v8);
//
//		Coordinate c9 = new Coordinate(315.0f, 672.0f);
//		DefaultVertex v9 = new DefaultVertex(c9);
//		transitionNodes.add(v9);
//
//		Coordinate c10 = new Coordinate(427.0f, 636.6154f);
//		DefaultVertex v10 = new DefaultVertex(c10);
//		transitionNodes.add(v10);
//
//		Coordinate c11 = new Coordinate(407.0f, 627.0f);
//		DefaultVertex v11 = new DefaultVertex(c11);
//		transitionNodes.add(v11);
//
//		Coordinate c12 = new Coordinate(451.0f, 676.0f);
//		DefaultVertex v12 = new DefaultVertex(c12);
//		transitionNodes.add(v12);
//
//		Coordinate c13 = new Coordinate(455.0f, 604.0f);
//		DefaultVertex v13 = new DefaultVertex(c13);
//		transitionNodes.add(v13);
//
//		Coordinate c14 = new Coordinate(563.0f, 632.0f);
//		DefaultVertex v14 = new DefaultVertex(c14);
//		transitionNodes.add(v14);
//
//		Coordinate c15 = new Coordinate(535.0f, 672.0f);
//		DefaultVertex v15 = new DefaultVertex(c15);
//		transitionNodes.add(v15);
//
//		Coordinate c16 = new Coordinate(591.0f, 672.0f);
//		DefaultVertex v16 = new DefaultVertex(c16);
//		transitionNodes.add(v16);
//
//		Coordinate c17 = new Coordinate(647.0f, 632.0f);
//		DefaultVertex v17 = new DefaultVertex(c17);
//		transitionNodes.add(v17);
//
//		Coordinate c18 = new Coordinate(623.0f, 581.0f);
//		DefaultVertex v18 = new DefaultVertex(c18);
//		transitionNodes.add(v18);
//
//		Coordinate c19 = new Coordinate(710.0f, 672.0f);
//		DefaultVertex v19 = new DefaultVertex(c19);
//		transitionNodes.add(v19);
//
//		Coordinate c20 = new Coordinate(770.0f, 632.0f);
//		DefaultVertex v20 = new DefaultVertex(c20);
//		transitionNodes.add(v20);
//
//		Coordinate c21 = new Coordinate(750.01483f, 604.0f);
//		DefaultVertex v21 = new DefaultVertex(c21);
//		transitionNodes.add(v21);
//
//		Coordinate c22 = new Coordinate(766.0f, 672.0f);
//		DefaultVertex v22 = new DefaultVertex(c22);
//		transitionNodes.add(v22);
//
//		Coordinate c23 = new Coordinate(866.0f, 631.7191f);
//		DefaultVertex v23 = new DefaultVertex(c23);
//		transitionNodes.add(v23);
//
//		Coordinate c24 = new Coordinate(846.0f, 672.0f);
//		DefaultVertex v24 = new DefaultVertex(c24);
//		transitionNodes.add(v24);
//
//		Coordinate c25 = new Coordinate(894.0f, 672.0f);
//		DefaultVertex v25 = new DefaultVertex(c25);
//		transitionNodes.add(v25);
//
//		Coordinate c26 = new Coordinate(894.0f, 597.0f);
//		DefaultVertex v26 = new DefaultVertex(c26);
//		transitionNodes.add(v26);
//
//		Coordinate c27 = new Coordinate(1002.0f, 630.9849f);
//		DefaultVertex v27 = new DefaultVertex(c27);
//		transitionNodes.add(v27);
//
//		Coordinate c28 = new Coordinate(986.0f, 668.0f);
//		DefaultVertex v28 = new DefaultVertex(c28);
//		transitionNodes.add(v28);
//
//		Coordinate c29 = new Coordinate(1034.0f, 672.0f);
//		DefaultVertex v29 = new DefaultVertex(c29);
//		transitionNodes.add(v29);
//
//		Coordinate c30 = new Coordinate(1034.0f, 600.97833f);
//		DefaultVertex v30 = new DefaultVertex(c30);
//		transitionNodes.add(v30);
	}

	public void selectDestinationMode() {
		// EDITED FOR LOCALIZATION

		// SOURCE
		//Coordinate s = new Coordinate(getTrueWidth(126.612976f), getTrueHeight(1451.0f));

		//controller.redraw();

		// Bundle bundle = getIntent().getExtras();
		final long destinationId = bundle.getLong("BOOTH");

		ApiRouter.withToken(getCurrentUser().getToken()).getDestination(destinationId,
				new Callback<Destination>() {
					@Override
					public void success(Destination destination, Response rawResponse) {
						//stopProgress();
						float x = getTrueWidth(destination.getxCoordinate());
						float y = getTrueHeight(destination.getyCoordinate());

						Coordinate c = new Coordinate(x, y);
						DefaultVertex d = controller.getClosestVertex(c, controller.USER_MISS_RADIUS);
						d.setType("DESTINATION");

						destinations.add(d);
						//theDestination = controller.getClosestVertex(c, controller.USER_MISS_RADIUS);

						selectDestination = true;
					}

					@Override
					public void failure(RetrofitError e) {
						displayError(e);
					}
				});

		/*Coordinate d;

		switch (boothNo) {
			case "1": d = new Coordinate(47.0f, 1268.1721f); break;
			case "2": d = new Coordinate(227.0f, 521.0f); break;
			case "3": d = new Coordinate(387.0f, 760.0f); break;
			case "4": d = new Coordinate(647.0f, 505.0f); break;
			case "5": d = new Coordinate(930.0f, 485.0f); break;
			case "6": d = new Coordinate(974.0f, 736.0f); break;
			default: d = new Coordinate(126.612976f, 1451.0f);
		}

		d.setX(getTrueWidth(d.getX()));
		d.setY(getTrueHeight(d.getY()));

		//DefaultVertex source = controller.getClosestVertex(s, controller.USER_MISS_RADIUS);
		//controller.getUserSelectedVertices().add(source);

		// DefaultVertex destination = controller.getClosestVertex(d, controller.USER_MISS_RADIUS);
		destination = controller.getClosestVertex(d, controller.USER_MISS_RADIUS);
		destinations.add(destination);*/
		//controller.getUserSelectedVertices().add(destination);

		//controller.redraw();

		//controller.showPath();
	}

	public void tourMode() {
		// EDITED FOR LOCALIZATION

		tour = true;

		//controller.showSpanningTree();
	}

	public void categoryMode() {
		// EDITED FOR LOCALIZATION

		long catId = bundle.getLong("CATEGORY");

		ApiRouter.withToken(getCurrentUser().getToken()).getDestinationsByCategory(1, catId,
				new Callback<List<Destination>>() {
					@Override
					public void success(List<Destination> destinationsByCategory, Response rawResponse) {
						//stopProgress();
						for (int i = 0; i < destinationsByCategory.size(); i++) {
							Destination d = destinationsByCategory.get(i);

							float x = getTrueWidth(d.getxCoordinate());
							float y = getTrueHeight(d.getyCoordinate());

							Coordinate c = new Coordinate(x, y);
							DefaultVertex v = controller.getClosestVertex(c, controller.USER_MISS_RADIUS);
							v.setType("DESTINATION");

							destinations.add(v);
						}
					}

					@Override
					public void failure(RetrofitError e) {
						displayError(e);
					}
				});

		categoryTour = true;

		/*Coordinate s = new Coordinate(getTrueWidth(126.612976f), getTrueHeight(1451.0f));

		ArrayList<DefaultVertex> cats = new ArrayList<DefaultVertex>();
		cats.add(controller.getClosestVertex(s, controller.USER_MISS_RADIUS));

		if (category.equals("MOB")) {
			Coordinate d1 = new Coordinate(getTrueWidth(227.0f), getTrueHeight(521.0f));
			Coordinate d2 = new Coordinate(getTrueWidth(319.0f), getTrueHeight(760.0f));
			Coordinate d3 = new Coordinate(getTrueWidth(387.0f), getTrueHeight(760.0f));

			destinations.add(controller.getClosestVertex(d1, controller.USER_MISS_RADIUS));
			destinations.add(controller.getClosestVertex(d2, controller.USER_MISS_RADIUS));
			destinations.add(controller.getClosestVertex(d3, controller.USER_MISS_RADIUS));
		}
		else if (category.equals("TST")) {
			Coordinate d1 = new Coordinate(getTrueWidth(647.0f), getTrueHeight(505.0f));
			Coordinate d2 = new Coordinate(getTrueWidth(798.0f), getTrueHeight(493.0f));

			destinations.add(controller.getClosestVertex(d1, controller.USER_MISS_RADIUS));
			destinations.add(controller.getClosestVertex(d2, controller.USER_MISS_RADIUS));
		}
		else if (category.equals("UNI")) {
			Coordinate d1 = new Coordinate(getTrueWidth(355.0f), getTrueHeight(505.0f));
			Coordinate d2 = new Coordinate(getTrueWidth(495.0f), getTrueHeight(501.0f));
			Coordinate d3 = new Coordinate(getTrueWidth(930.0f), getTrueHeight(485.0f));

			destinations.add(controller.getClosestVertex(d1, controller.USER_MISS_RADIUS));
			destinations.add(controller.getClosestVertex(d2, controller.USER_MISS_RADIUS));
			destinations.add(controller.getClosestVertex(d3, controller.USER_MISS_RADIUS));
		}
		else if (category.equals("CLD")) {
			Coordinate d1 = new Coordinate(getTrueWidth(595.0f), getTrueHeight(752.0f));
			Coordinate d2 = new Coordinate(getTrueWidth(694.0f), getTrueHeight(760.1247f));
			Coordinate d3 = new Coordinate(getTrueWidth(766.0f), getTrueHeight(748.0f));

			destinations.add(controller.getClosestVertex(d1, controller.USER_MISS_RADIUS));
			destinations.add(controller.getClosestVertex(d2, controller.USER_MISS_RADIUS));
			destinations.add(controller.getClosestVertex(d3, controller.USER_MISS_RADIUS));
		}*/

		//showCategoryTour(cats);
	}

	public void showCategoryTour(ArrayList<DefaultVertex> arr) {
		for (int i = 0; i < arr.size()-1; i++) {
			controller.getUserSelectedVertices().add(arr.get(i));
			controller.getUserSelectedVertices().add(arr.get(i+1));

			controller.redraw();

			controller.showPath();
		}
	}

	private boolean copyTikzToClipboard() {
		String text = GraphExporter.getTikz(controller.getGraph(), controller.getTransformMatrix());
		try {
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			clipboard.setText(text);
			return true;
		} catch (Exception e) {
			System.err.println("Error while copying TiKZ to clipboard: " + e.getMessage());
			e.printStackTrace();
			e.printStackTrace();
			return false;
		}
	}

	private boolean copyMetapostToClipboard() {
		String text = GraphExporter.getMetapost(controller.getGraph());
		try {
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			clipboard.setText(text);
			return true;
		} catch (Exception e) {
			System.err.println("Error while copying metapost to clipboard: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	private SensorManager sensorManager;
	private List<Sensor> sensors;
	private Sensor sensor;
	private long lastUpdate = -1;
	private long currentTime = -1;

	private float last_x, last_y, last_z;
	private float current_x, current_y, current_z, currenForce;
	private static final int FORCE_THRESHOLD = 800; // used to be 900
	private final int DATA_X = SensorManager.DATA_X;
	private final int DATA_Y = SensorManager.DATA_Y;
	private final int DATA_Z = SensorManager.DATA_Z;

	// //// shake
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER || event.values.length < 3)
			return;

		currentTime = System.currentTimeMillis();

		if ((currentTime - lastUpdate) > 100) {
			long diffTime = (currentTime - lastUpdate);
			lastUpdate = currentTime;

			current_x = event.values[DATA_X];
			current_y = event.values[DATA_Y];
			current_z = event.values[DATA_Z];

			currenForce = Math.abs(current_x + current_y + current_z - last_x - last_y - last_z) / diffTime * 10000;

			if (currenForce > FORCE_THRESHOLD) {
				//controller.shake();
			}

			last_x = current_x;
			last_y = current_y;
			last_z = current_z;

		}
	}

	private void shareTikz() {

		String shareBody = GraphExporter.getTikz(controller.getGraph(), controller.getTransformMatrix());

		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, controller.graphInfo());
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "Share graph with"));

	}

	private boolean shareInterval() {
		String shareBody = "\\documentclass{article}\n\\usepackage{tikz}\n\\begin{document}\n\n";

		SimpleToBasicWrapper<DefaultVertex, DefaultEdge<DefaultVertex>> wrap = new SimpleToBasicWrapper<DefaultVertex, DefaultEdge<DefaultVertex>>(
				controller.getGraph());

		IntervalGraph ig = wrap.getIntervalGraph();
		if (ig == null)
			return false;

		shareBody += "\\begin{figure}[htp]";
		shareBody += "\\centering";

		shareBody += GraphExporter.getTikzIntervalRepresentation(wrap.getIntervalGraph());

		shareBody += "\n\t\\caption{" + GraphInformation.graphInfo(controller.getGraph()) + " (Grapher)}";
		shareBody += "\n\t\\label{fig:interval}";
		shareBody += "\n\t\\end{figure}";

		shareBody += "\n\n\\end{document}\n";

		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, controller.graphInfo());
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "Share graph with"));

		return true;
	}

	private void shareMetapost() {
		String shareBody = GraphExporter.getMetapost(controller.getGraph());

		shareBody += "\n\n% Sent to you by Grapher";

		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, controller.graphInfo());
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "Share graph with"));

	}

	// //// shake

	public void onClick(View v) {
		// ignoring clicks, listens to gesture stuff anyway
	}

	// Initiating Menu XML file (menu.xml)
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.drawer_context, menu);
		return true;
	}

	/**
	 * returns a string containing date and graph info
	 */
	private String createFileName() {
		return new Date().toGMTString() + " " + controller.graphInfo();
	}

	@Override
	@SuppressLint("WorldReadableFiles")
	protected void onDestroy() {
		beaconManager.disconnect();

		super.onDestroy();

		if (controller.getGraph().vertexSet().size() > 0 && saveOnExit) {
			try {
				String json = new FileAccess().save(controller.getGraph());
				FileOutputStream fOut = openFileOutput(createFileName() + ".json", MODE_WORLD_READABLE);
				OutputStreamWriter osw = new OutputStreamWriter(fOut);

				// Write the string to the file
				osw.write(json);

				/*
				 * ensure that everything is really written out and close
				 */
				osw.flush();
				osw.close();

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// Unregister from SensorManager.
		sensorManager.unregisterListener(this);
		sensorManager.unregisterListener(this, sensor);
		for (Sensor s : sensors) {
			sensorManager.unregisterListener(this, s);
		}
	}

	public void longToast(String toast) {
		Toast.makeText(Workspace.this, toast, Toast.LENGTH_LONG).show();
	}

	public void shortToast(String toast) {
		Toast.makeText(Workspace.this, toast, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Event Handling for Individual menu item selected Identify single menu item
	 * by it's id
	 * */
	public boolean onOptionsItemSelected(MenuItem item) {
		// System.out.println("MenuItem      \t" + item.getTitle());
		// System.out.println(" > Condensed  \t" + item.getTitleCondensed());
		// System.out.println(" > numeric id \t" + item.getItemId());
		// System.out.println();

		switch (item.getItemId()) {

		case R.id.finish:
			saveOnExit = false;
			finish();
			return true;

		case R.id.finish_and_save:
			saveOnExit = true;
			finish();
			return true;

		case R.id.new_graph:
			controller.newGraph();
			return true;

		case R.id.snap:
			controller.snapToGrid();
			return true;

			// case R.id.chordalization:
			// controller.showChordalization();
			// return true;

		case R.id.threshold:
			controller.showThreshold();
			return true;

		case R.id.compute_maximum_independent_set:
			controller.showMaximumIndependentSet();
			return true;

		case R.id.compute_chromatic_number:
			controller.chromaticNumber();
			return true;

		case R.id.compute_colouring:
			controller.showColouring();
			return true;

		case R.id.compute_maximum_clique:
			controller.showMaximumClique();
			return true;

		case R.id.vertex_integrity:
			controller.showVertexIntegrity();
			return true;

		case R.id.compute_minimal_triangulation:
			controller.minimalTriangulation();
			return true;

		case R.id.compute_steiner_tree:
			controller.showSteinerTree();
			return true;

		case R.id.clear_colours:
			controller.clearAll();
			return true;

		case R.id.compute_treewidth:
			controller.treewidth();
			return true;

		case R.id.compute_simplicial_vertices:
			int simplicials = controller.showSimplicialVertices();
			if (simplicials > 1)
				shortToast(simplicials + " simplicial vertices");
			else if (simplicials == 1)
				shortToast(simplicials + " simplicial vertex");
			else
				shortToast("No simplicial vertices.");
			return true;

		case R.id.compute_chordality:
			boolean isChordal = controller.isChordal();
			if (isChordal)
				shortToast("Graph is chordal.");
			else
				shortToast("Graph is not chordal.");
			return true;

		case R.id.compute_claw_deletion:
			int deletionSize = controller.showClawDeletion();
			if (deletionSize == 0)
				shortToast("Graph is claw-free");
			else
				shortToast("Claw-free deletion size " + deletionSize);
			return true;

		case R.id.compute_perfect_code:
			controller.showPerfectCode();
			return true;

		case R.id.compute_claws:
			boolean hasclaw = controller.showAllClaws();
			if (hasclaw)
				shortToast("Found claw");
			else
				shortToast("Graph is claw-free");
			return true;

		case R.id.compute_cycle_4:
			controller.showAllCycle4();
			return true;

		case R.id.compute_regularity_deletion_set:
			controller.showRegularityDeletionSet();
			return true;

		case R.id.compute_odd_cycle_transversal:
			controller.showOddCycleTransversal();
			return true;

		case R.id.compute_feedback_vertex_set:
			controller.showFeedbackVertexSet();
			return true;

		case R.id.compute_connected_feedback_vertex_set:
			controller.showConnectedFeedbackVertexSet();
			return true;

		case R.id.compute_vertex_cover:
			controller.showVertexCover();
			return true;

		case R.id.compute_connected_vertex_cover:
			controller.showConnectedVertexCover();
			return true;

		case R.id.compute_minimum_dominating_set:
			controller.showDominatingSet();
			return true;

			// case R.id.compute_minimum_red_blue_dominating_set:
			// controller.showRedBlueDominatingSet();
			// return true;

		case R.id.spring:
			controller.longShake(300);
			shortToast("Shaken, not stirred");
			return true;

		case R.id.hamiltonian_path:
			controller.showHamiltonianPath();
			return true;

		case R.id.hamiltonian_cycle:
			controller.showHamiltonianCycle();
			return true;

		case R.id.flow:
			int flow = controller.showFlow();
			if (flow < 0)
				shortToast("Please select two vertices (hold to select)");
			else if (flow == 0)
				shortToast("Not connected");
			else
				shortToast("Max flow " + flow);
			return true;

		case R.id.path:
			//int res = controller.showPath();
			int res = 0;
			if (res < 0)
				shortToast("Please select two vertices (hold to select)");
			else if (res == 0)
				shortToast("No path!");
			else
				shortToast("Path length " + res);
			return true;

		case R.id.power:
			controller.constructPower();
			shortToast("Power graph has been constructed");
			return true;

		case R.id.compute_mst:
			controller.showSpanningTree();
			return true;

		case R.id.compute_balanced_separator:
			controller.showSeparator();
			return true;

		case R.id.compute_diameter:
			int diam = controller.diameter();
			if (diam < 0)
				shortToast("Diameter is infinite");
			else
				shortToast("Diameter " + diam);

			return true;

		case R.id.compute_girth:
			int girth = controller.girth();
			if (girth < 0)
				shortToast("Acyclic");
			else
				shortToast("Girth " + girth);

			return true;

		case R.id.bipartition:
			boolean bipartite = controller.showBipartition();
			if (bipartite)
				shortToast("Is bipartite");
			else
				shortToast("Is not bipartite");
			return true;

		case R.id.compute_all_cuts:
			int cuts = controller.showAllCutVertices();
			if (cuts == 0)
				shortToast("No cut vertices");

			else if (cuts == 1)
				shortToast("1 cut vertex");
			else
				shortToast(cuts + " cut vertices");
			return true;

		case R.id.compute_all_bridges:
			int bridges = controller.showAllBridges();
			if (bridges == 0)
				shortToast("No bridges");
			else if (bridges == 1)
				shortToast("1 bridge");
			else
				shortToast(bridges + " bridges");
			return true;

		case R.id.test_eulerian:
			boolean eulerian = controller.isEulerian();
			if (eulerian)
				shortToast("Graph is eulerian");
			else
				shortToast("Graph is not eulerian, odd degree vertices highlighted.");
			return true;

		case R.id.show_center:
			boolean conn = controller.showCenterVertex();
			if (!conn)
				shortToast("No center vertex in disconnected graph");
			return true;

		case R.id.centralize:
			controller.centralize();
			return true;

		case R.id.add_universal_vertex:
			int degree = controller.addUniversalVertex();
			if (degree == 0)
				shortToast("Added singleton");
			else if (degree == 1)
				shortToast("Added vertex adjacent to 1 vertex");
			else
				shortToast("Added vertex adjacent to " + degree + " vertices");
			return true;

		case R.id.compute_bandwidth:
			controller.computeBandwidth();
			return true;

		case R.id.metapost_to_clipboard:
			if (copyMetapostToClipboard()) {
				shortToast("Copied info on " + controller.graphInfo());
			} else {
				shortToast("An error occured copying to clipboard!");
			}
			return true;

		case R.id.tikz_to_clipboard:
			if (copyTikzToClipboard()) {
				shortToast("Copied info on " + controller.graphInfo());
			} else {
				shortToast("An error occured copying to clipboard!");
			}
			return true;

		case R.id.share_tikz:
			shareTikz();
			return true;

		case R.id.share_interval:
			if (shareInterval()) {
				return true;
			}

			// DO NOT PUT ANYTHING HERE!

			// THE CASE ABOVE DOES NOT BREAK IF GRAPH NOT INTERVAL

		case R.id.interval:
			if(controller.getGraph() ==null || controller.getGraph().vertexSet().size() ==1){
				Intent nextScreen = new Intent(getApplicationContext(), IntervalActivity.class);
				DefaultVertex.resetCounter();
				startActivityForResult(nextScreen, 0);
				finish();
			}
			int interval = controller.showInterval();
			switch (interval) {
			case 0:
				shortToast("Graph is interval");
				Intent nextScreen = new Intent(getApplicationContext(), IntervalActivity.class);
				DefaultVertex.resetCounter();
				startActivityForResult(nextScreen, 0);
				finish();
				break;
			case 1:
				shortToast("Not interval, AT is highlighted");
				break;
			case 2:
				shortToast("Not chordal");
			}
			return true;

			// TODO SCREENSHOT SAVING MUST SAVE IN EXTERNAL
			// case R.id.screenshot:
			// if (!writeScreenshot()) {
			// longToast("Writing failed.");
			// }
			// return true;

		case R.id.share_metapost:
			shareMetapost();
			return true;

		case R.id.select_all:
			controller.selectAll();
			return true;

		case R.id.deselect_all:
			controller.deselectAll();
			return true;

		case R.id.select_all_highlighted_vertices:
			controller.selectAllHighlightedVertices();
			return true;

		case R.id.invert_selected:
			controller.invertSelectedVertices();
			return true;

		case R.id.select_reachable:
			controller.selectAllReachableVertices();
			return true;

		case R.id.graph_complement:
			controller.complement();
			return true;

		case R.id.local_complement:
			if (!controller.localComplement()) {
				shortToast("Select at least one vertex to perform local complement");
			}
			return true;

		case R.id.contract:
			if (!controller.contract()) {
				shortToast("Select two adjacent vertices");
			}
			return true;

		case R.id.complete_selected:
			controller.completeSelectedVertices();
			return true;

		case R.id.complement_selected:
			controller.complementSelected();
			return true;

		case R.id.delete_selected:
			int deleted = controller.deleteSelectedVertices();
			if (deleted == 0) {
				shortToast("No vertices selected");
			} else {
				shortToast("Deleted " + deleted + " vertices");
			}
			return true;

		case R.id.induce_subgraph:
			int removed = controller.induceSubgraph();
			if (removed == 0) {
				shortToast("All vertices selected, none deleted");
			} else {
				shortToast("Removed " + removed + " vertices");
			}
			return true;

		case R.id.toggle_edge_edit:
			boolean edgedraw = controller.toggleEdgeDraw();
			shortToast(edgedraw ? "Edge draw mode" : "Vertex move mode");
			return true;

		case R.id.save:
			save();
			return true;

		case R.id.load:
			load();
			return true;

		case R.id.delete:
			delete();
			return true;

		case R.id.toggle_label_drawing:
			boolean doShow = !GraphViewController.DO_SHOW_LABELS;
			GraphViewController.DO_SHOW_LABELS = doShow;
			if (doShow)
				shortToast("Showing labels");
			else
				shortToast("Not showing labels");
			controller.redraw();
			return true;

		default:
			System.out.println("Option item selected, " + item.getTitle());
			return super.onOptionsItemSelected(item);
		}
	}

	public void save() {

		System.out.println("save");
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Title");
		alert.setMessage("Message");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@SuppressLint("WorldReadableFiles")
			public void onClick(DialogInterface dialog, int whichButton) {
				Editable value = input.getText();
				try {
					String json = new FileAccess().save(controller.getGraph());

					System.out.println("GRAPH: " + json);

					FileOutputStream fOut = openFileOutput(value + ".json", MODE_WORLD_READABLE);
					OutputStreamWriter osw = new OutputStreamWriter(fOut);

					// Write the string to the file
					osw.write(json);

					/*
					 * ensure that everything is really written out and close
					 */
					osw.flush();
					osw.close();

				} catch (JSONException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		alert.show();
	}

	/**
	 * Write a screenshot to file, calls first controller.screenshot for obtaining
	 * bitmap
	 *
	 * @return
	 */
	private boolean writeScreenshot() {

		// TODO this must write to external location

		FileOutputStream out = null;
		try {
			out = openFileOutput(createFileName() + ".png", MODE_WORLD_WRITEABLE);

			// bmp is your Bitmap instance
			Bitmap bmp = controller.screenShot();
			System.out.println("got bibimbap from controller");
			bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
			System.out.println("done compressing bitmap and writing to file " + out.getFD());
			System.out.println("done writing to " + out);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (out != null) {
					out.close();
					System.out.println("File closed: " + out.getFD());
					System.out.println("File closed: " + out.toString());
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public void delete() {
		final String[] files = fileList();

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Delete file");
		builder.setItems(files, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				System.out.println("DELETE REQUEST " + item + " -- " + files[item]);

				if (deleteFile(files[item]))
					shortToast("Deleted file " + files[item]);
				else
					shortToast("Unable to delete file!");
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void load() {
		System.out.println("load");
		final String[] files = fileList();

		//AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//builder.setTitle("Pick a file");
		/*builder.setItems(files, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				Toast.makeText(getApplicationContext(), files[item], Toast.LENGTH_SHORT).show();
				try {
					StringBuffer stringBuffer = new StringBuffer();
					String inputLine = "";
					FileInputStream input = openFileInput(files[item].toString());
					InputStreamReader isr = new InputStreamReader(input);
					BufferedReader bufferedReader = new BufferedReader(isr);

					while ((inputLine = bufferedReader.readLine()) != null) {
						stringBuffer.append(inputLine);
						stringBuffer.append("\n");
					}

					bufferedReader.close();
					String json = stringBuffer.toString();
					System.out.println(json);

					new FileAccess().load(controller.getGraph(), json);

					controller.clearMemory();

					controller.makeInfo();
					controller.redraw();

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
		*/

		// EDITED
		try {
			StringBuffer stringBuffer = new StringBuffer();
			String inputLine = "";
			FileInputStream input = openFileInput(files[0].toString());

			System.out.println("GRAPH: " + files[0].toString());

			InputStreamReader isr = new InputStreamReader(input);
			BufferedReader bufferedReader = new BufferedReader(isr);

			while ((inputLine = bufferedReader.readLine()) != null) {
				stringBuffer.append(inputLine);
				stringBuffer.append("\n");
			}

			bufferedReader.close();
			String json = stringBuffer.toString();
			System.out.println(json);

			loadGraph(controller.getGraph(), json);

			controller.clearMemory();

			controller.makeInfo();
			controller.redraw();

		} catch (Exception e) {
			try {
				FileOutputStream fOut = openFileOutput("c2.json", MODE_WORLD_READABLE);
				OutputStreamWriter osw = new OutputStreamWriter(fOut);

//				ApiRouter.withToken(getCurrentUser().getToken()).getMapGraph(1,
//						new Callback<com.startupcompany.wireframe.model.Graph>() {
//							@Override
//							public void success(com.startupcompany.wireframe.model.Graph mapGraph, Response rawResponse) {
//								//stopProgress();
//								graphJSON = mapGraph.getJson();
//							}
//
//							@Override
//							public void failure(RetrofitError e) {
//								displayError(e);
//							}
//						});

				// Write the string to the file
				String json = "{\"vertices\":{\"n1\":{\"x\":126.61296081542969,\"y\":1451},\"n2\":{\"x\":123.24651336669922,\"y\":1372},\"n3\":{\"x\":127,\"y\":1259.7137451171875},\"n4\":{\"x\":123.49036407470703,\"y\":1068},\"n5\":{\"x\":267,\"y\":1064.04541015625},\"n6\":{\"x\":219,\"y\":675.19904},\"n7\":{\"x\":47,\"y\":1368},\"n8\":{\"x\":47,\"y\":1268.172119140625},\"n9\":{\"x\":80,\"y\":660},\"n10\":{\"x\":267,\"y\":640},\"n11\":{\"x\":227,\"y\":521},\"n12\":{\"x\":355,\"y\":505},\"n13\":{\"x\":495,\"y\":501},\"n14\":{\"x\":319,\"y\":760},\"n15\":{\"x\":387,\"y\":760},\"n16\":{\"x\":455,\"y\":744},\"n17\":{\"x\":527,\"y\":748},\"n18\":{\"x\":595,\"y\":752},\"n19\":{\"x\":694,\"y\":760.1246948242188},\"n20\":{\"x\":766,\"y\":748},\"n21\":{\"x\":830,\"y\":748},\"n22\":{\"x\":898,\"y\":748},\"n23\":{\"x\":974,\"y\":736},\"n24\":{\"x\":1042,\"y\":732},\"n25\":{\"x\":647,\"y\":505},\"n26\":{\"x\":798,\"y\":493},\"n27\":{\"x\":930,\"y\":485},\"n28\":{\"x\":1030,\"y\":489},\"n29\":{\"x\":427,\"y\":636.6154174804688},\"n30\":{\"x\":563,\"y\":632},\"n31\":{\"x\":647,\"y\":632},\"n32\":{\"x\":770,\"y\":632},\"n33\":{\"x\":866,\"y\":631.7191162109375},\"n34\":{\"x\":1002,\"y\":630.9849243164063},\"n35\":{\"x\":187,\"y\":608},\"n36\":{\"x\":315,\"y\":672},\"n37\":{\"x\":315,\"y\":612},\"n38\":{\"x\":407,\"y\":672},\"n39\":{\"x\":451,\"y\":676},\"n40\":{\"x\":455,\"y\":604},\"n41\":{\"x\":535,\"y\":672},\"n42\":{\"x\":591,\"y\":672},\"n43\":{\"x\":710,\"y\":672},\"n44\":{\"x\":766,\"y\":672},\"n45\":{\"x\":846,\"y\":672},\"n46\":{\"x\":894,\"y\":672},\"n47\":{\"x\":986,\"y\":668},\"n48\":{\"x\":1034,\"y\":672},\"n49\":{\"x\":1034,\"y\":600.9783325195313},\"n50\":{\"x\":894,\"y\":597},\"n51\":{\"x\":750.0148315429688,\"y\":604},\"n52\":{\"x\":623,\"y\":581}},\"edges\":[{\"source\":\"n2\",\"target\":\"n1\"},{\"source\":\"n3\",\"target\":\"n2\"},{\"source\":\"n4\",\"target\":\"n3\"},{\"source\":\"n5\",\"target\":\"n4\"},{\"source\":\"n6\",\"target\":\"n5\"},{\"source\":\"n10\",\"target\":\"n6\"},{\"source\":\"n29\",\"target\":\"n10\"},{\"source\":\"n30\",\"target\":\"n29\"},{\"source\":\"n31\",\"target\":\"n30\"},{\"source\":\"n32\",\"target\":\"n31\"},{\"source\":\"n33\",\"target\":\"n32\"},{\"source\":\"n34\",\"target\":\"n33\"},{\"source\":\"n7\",\"target\":\"n2\"},{\"source\":\"n8\",\"target\":\"n3\"},{\"source\":\"n9\",\"target\":\"n6\"},{\"source\":\"n35\",\"target\":\"n10\"},{\"source\":\"n11\",\"target\":\"n35\"},{\"source\":\"n37\",\"target\":\"n10\"},{\"source\":\"n12\",\"target\":\"n37\"},{\"source\":\"n36\",\"target\":\"n10\"},{\"source\":\"n14\",\"target\":\"n36\"},{\"source\":\"n38\",\"target\":\"n29\"},{\"source\":\"n15\",\"target\":\"n38\"},{\"source\":\"n39\",\"target\":\"n29\"},{\"source\":\"n16\",\"target\":\"n39\"},{\"source\":\"n40\",\"target\":\"n29\"},{\"source\":\"n13\",\"target\":\"n40\"},{\"source\":\"n41\",\"target\":\"n30\"},{\"source\":\"n42\",\"target\":\"n30\"},{\"source\":\"n17\",\"target\":\"n41\"},{\"source\":\"n18\",\"target\":\"n42\"},{\"source\":\"n52\",\"target\":\"n31\"},{\"source\":\"n25\",\"target\":\"n52\"},{\"source\":\"n43\",\"target\":\"n31\"},{\"source\":\"n19\",\"target\":\"n43\"},{\"source\":\"n44\",\"target\":\"n32\"},{\"source\":\"n20\",\"target\":\"n44\"},{\"source\":\"n51\",\"target\":\"n32\"},{\"source\":\"n26\",\"target\":\"n51\"},{\"source\":\"n45\",\"target\":\"n33\"},{\"source\":\"n21\",\"target\":\"n45\"},{\"source\":\"n46\",\"target\":\"n33\"},{\"source\":\"n22\",\"target\":\"n46\"},{\"source\":\"n50\",\"target\":\"n33\"},{\"source\":\"n27\",\"target\":\"n50\"},{\"source\":\"n49\",\"target\":\"n34\"},{\"source\":\"n28\",\"target\":\"n49\"},{\"source\":\"n47\",\"target\":\"n34\"},{\"source\":\"n23\",\"target\":\"n47\"},{\"source\":\"n48\",\"target\":\"n34\"},{\"source\":\"n24\",\"target\":\"n48\"}]}";

				osw.write(json);
				//osw.write(graphJSON);

				/*
				 * ensure that everything is really written out and close
				 */
				osw.flush();
				osw.close();

				load();
			} catch (Exception exc) {
				e.printStackTrace();
			}
		} /*catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public void loadGraph(
			SimpleGraph<DefaultVertex, DefaultEdge<DefaultVertex>> graph,
			String json) throws JSONException {

		JSONObject graphJson = new JSONObject(json);
		HashSet<DefaultVertex> verticesSet = new HashSet<DefaultVertex>(
				graph.vertexSet());
		HashSet<DefaultEdge<DefaultVertex>> edgesSet = new HashSet<DefaultEdge<DefaultVertex>>(
				graph.edgeSet());
		graph.removeAllVertices(verticesSet);
		graph.removeAllEdges(edgesSet);

		JSONObject vertices = graphJson.getJSONObject("vertices");
		HashMap<String, DefaultVertex> verticesMap = new HashMap<String, DefaultVertex>();
		for (@SuppressWarnings("rawtypes")
			 Iterator i = vertices.keys(); i.hasNext();) {
			String key = (String) i.next();
			JSONObject vertexJson = vertices.getJSONObject(key);

			// EDITED
			DefaultVertex vertex = new DefaultVertex(new Coordinate(
					getTrueWidth((float) vertexJson.getDouble("x")), getTrueHeight((float) vertexJson.getDouble("y"))));
			vertex.setLabel(key);
			graph.addVertex(vertex);
			verticesMap.put(key, vertex);
		}

		JSONArray edges = graphJson.getJSONArray("edges");

		for (int i = 0; i < edges.length(); i++) {
			JSONObject edge = edges.getJSONObject(i);
			String source = edge.getString("source");
			String target = edge.getString("target");
			graph.addEdge(verticesMap.get(source), verticesMap.get(target));
		}

	}

	public float getTrueWidth(float c) {
		//DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

		Display display = this.getWindowManager().getDefaultDisplay();
		DisplayMetrics realMetrics = new DisplayMetrics();
		display.getRealMetrics(realMetrics);

		if (realMetrics.widthPixels >= 1080) {
			return (c * realMetrics.widthPixels) / 1080;
		}
		else {
			return (c * 1080) / realMetrics.widthPixels;
		}

		// return c * (metrics.scaledDensity / 3.0f);
	}

	public float getTrueHeight(float c) {
		//DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

		Display display = this.getWindowManager().getDefaultDisplay();
		DisplayMetrics realMetrics = new DisplayMetrics();
		display.getRealMetrics(realMetrics);

		if (realMetrics.heightPixels >= 1920) {
			return (c * realMetrics.heightPixels) / 1920;
		}
		else {
			return (c * 1920) / realMetrics.heightPixels;
		}

		// return c * (metrics.scaledDensity / 3.0f);
	}

	@Override
	protected void onStart() {
		super.onStart();

		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				if (region != null) {
					beaconManager.startRanging(region);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (SystemRequirementsChecker.checkWithDefaultDialogs(this)) {
			startScanning();
		}
	}


	private void startScanning() {
		Toast.makeText(this, "Scanning..", Toast.LENGTH_LONG);
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
			}
		});
	}

	@Override
	protected void onStop() {
		beaconManager.disconnect();

		super.onStop();
	}

	int timeOut = 0;
	ArrayList<Double> averageRSSI = new ArrayList<>();

	@Override
	public void onBeaconsDiscovered(Region region,final List<Beacon> beacons) { // --- commented "3" snippets for sorting

				//	final View view = findViewById(R.id.sonar);

				// Note that beacons reported here are already sorted by estimated
				// distance between device and beacon.
				ArrayList<Float> xLocations = new ArrayList<Float>();// x
				ArrayList<Float> yLocations = new ArrayList<Float>(); //y
				ArrayList<Double> zLocations = new ArrayList<Double>();// z
				ArrayList<Double> distances = new ArrayList<Double>(); // distances
				ArrayList<Double> realRSSI = new ArrayList<Double>();// RSSI in real values
				ArrayList<Integer> beaconsID = new ArrayList<Integer>();// beaconsID

//				LinkedList<Beacon> sortingBeacon = new LinkedList<>();
//				for (int i = 0; i < beacons.size(); i++) {
//					if (BeaconLocation(beacons.get(i)).getId() != 0) {
//						sortingBeacon.addLast(beacons.get(i));
//					}
//				}

				int count = 0;
				// clear arraylists

				if (scanningCounter != 0) {
					scanningCounter = 0;
					xLocations.clear();
					yLocations.clear();
					distances.clear();
					count++;
				}

				if (beacons.size() == 0) {
					Toast.makeText(getApplicationContext(), " No Beacons Found", Toast.LENGTH_LONG).show();

					// EDITED -- closes the activity if no beacons are found
					//finish();
				} else {


					// 0 ID!
					for (int i = 0; i < beacons.size(); i++) {
						if(BeaconLocation(beacons.get(i)).getId() != 0)
						beaconsID.add(BeaconLocation(beacons.get(i)).getId());

					}

//					Beacon temp2;
//					int temp;
//					for (int i = 1; i < beaconsID.size(); i++) {
//						for (int j = i; j > 0; j--) {
//							if (beaconsID.get(j) > beaconsID.get(j - 1)) {
//								temp = beaconsID.get(j);
//								Log.i("sorting", "" + temp);
//								beaconsID.set(j, beaconsID.get(j - 1));
//								beaconsID.set(j - 1, temp);
//								temp2 = sortingBeacon.get(j);
//								sortingBeacon.set(j, sortingBeacon.get(j - 1));
//								sortingBeacon.set(j - 1, temp2);
//							}
//						}
//					}


//					for (int i = 0; i < sortingBeacon.size(); i++) {
//
//						xLocations.add((float) BeaconLocation(sortingBeacon.get(i)).getX());
//						yLocations.add((float) BeaconLocation(sortingBeacon.get(i)).getY());
//						distances.add(Utils.computeAccuracy(sortingBeacon.get(i)));
//
//						double getRssi = sortingBeacon.get(i).getRssi();
//
//						double accuracy = (1.0 / Math.pow(10, (getRssi / 10))) - (1.0 / Math.pow(10, (-34 / 10)));
//						realRSSI.add(1 / accuracy);
//
//						if (averageRSSI.size() == sortingBeacon.size()) {
//							double tempSUMRSSI = averageRSSI.get(i);
//							averageRSSI.set(i, tempSUMRSSI + realRSSI.get(i));
//						} else {
//							averageRSSI.add(realRSSI.get(i));
//						}
//
//					}

					//////// IF NO SORTING
					for (int i = 0; i < beacons.size(); i++) {

						xLocations.add((float) BeaconLocation(beacons.get(i)).getX());
						yLocations.add((float) BeaconLocation(beacons.get(i)).getY());
						distances.add(Utils.computeAccuracy(beacons.get(i)));

						double getRssi = beacons.get(i).getRssi();

						double accuracy = (1.0 / Math.pow(10, (getRssi / 10))) - (1.0 / Math.pow(10, (-34 / 10)));
						realRSSI.add(1 / accuracy);

						if (averageRSSI.size() == beacons.size()) {
							double tempSUMRSSI = averageRSSI.get(i);
							averageRSSI.set(i, tempSUMRSSI + realRSSI.get(i));
						} else {
							averageRSSI.add(realRSSI.get(i));
						}

					}

					timeOut++;


					if (beacons.size() >= 2 && timeOut == 5) {
						double sumTotalRealRssi = 0.0;
						for (int i = 0; i < averageRSSI.size(); i++) {
							sumTotalRealRssi = sumTotalRealRssi + averageRSSI.get(i);
							double tempSUMRSSI = averageRSSI.get(i);
							averageRSSI.set(i, (tempSUMRSSI / 5));
						}
						sumTotalRealRssi = sumTotalRealRssi / 5;

						//	toolbar.setSubtitle("Found beacons: " + beacons.size());
						ArrayList<Double> ratioRealRSSI = new ArrayList<Double>();
						double sumXRatio = 0, sumYRatio = 0;
						for (int i = 0; i < realRSSI.size(); i++) {
							ratioRealRSSI.add((averageRSSI.get(i) / sumTotalRealRssi));
							double tempX = ratioRealRSSI.get(i) * getTrueWidth(xLocations.get(i));
							double tempY = ratioRealRSSI.get(i) * getTrueHeight(yLocations.get(i));
							sumXRatio = sumXRatio + tempX;
							sumYRatio = sumYRatio + tempY;
						}

//						getXlocation = sumXRatio;
//						if (getXlocation < 0) {
//							getXlocation = 0;
//						}
//						getYlocation = sumYRatio;
//						if (getYlocation < 0) {
//							getYlocation = 0;
//						}

						if (sumXRatio > 0)
							getXlocation = sumXRatio;

						if (sumYRatio > 0)
							getYlocation = sumYRatio;

						if (oldXlocation == -1 || oldYlocation == -1) {
							oldXlocation = getXlocation;
							oldYlocation = getXlocation;
						} else {
							distance = Math.sqrt(Math.pow(getXlocation - oldXlocation, 2) + Math.pow(getYlocation - oldYlocation, 2));
							oldXlocation = getXlocation;
							oldYlocation = getXlocation;
						}

/*
						frameLayout.post(new Runnable() {
							@Override
							public void run() {
								int canvasWidth = LocationBackgroundView.screenWidth;
								int canvasHeight = LocationBackgroundView.screenHeight;
								double width = ((float) canvasWidth * (((51.2 * getXlocation)) / 413.0));
								double height = ((float) frameLayout.getHeight() - canvasHeight + (canvasHeight * (((60.466666667 * getYlocation)) / 914.0)));
								dotView.animate().x((float) width).y((float) height);
							}
						});

						scanningCounter = scanningCounter + 1;
						translateXY(view, getXlocation, getYlocation);*/

//

						// localizationCoordinates(getXlocation, getYlocation);

						timeOut = 0;
						averageRSSI.clear();
						//Toast.makeText(getApplicationContext(), "x " + getXlocation + "," + "y " + getYlocation, Toast.LENGTH_SHORT).show();
						System.out.println("XLOC: " + getXlocation + " YLOC: " + getYlocation);

						// TEMP ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
						// lastX = getXlocation;
						// lastY = getYlocation;

						userLocalization = getCurrentUser();

						lastX = userLocalization.getxCoordinate();
						lastY = userLocalization.getyCoordinate();

						System.out.println("LASTXY: " + lastX + ", " + lastY);

						System.out.println("GETXY: " + (float) getXlocation + ", " + (float) getYlocation);
						//System.out.println("GETXY: " + getXlocation + ", " + getYlocation);

						System.out.println("ROOMSIZE: " + rooms.size());

						// BOOLEAN?
//						deleteSourceNode();

						if (getYlocation > getTrueHeight(330.0f)) {
							/*if (listOfAverages.size() < 3) {
								listOfAverages.add(null);
								listOfAverages.set(averagesIndex, new Coordinate(getXlocation, getYlocation));

								if (averagesIndex == 0) {
									locations.add(new Coordinate(getXlocation, getYlocation));

									drawSourceNode();
//
									drawEdgeToNearestTransitionNode();
//
									showShortestPathToDestination();
								}
							}

							else {
								///////////////////////////////////////////
								for (int i = 0; i < rooms.size(); i++) {
									Room r = rooms.get(i);
									float xmax = getTrueWidth(r.getXmax());
									float xmin = getTrueWidth(r.getXmin());
									float ymax = getTrueHeight(r.getYmax());
									float ymin = getTrueHeight(r.getYmin());

									System.out.println("ROOMID: " + r.getId());

									System.out.println("ROOMXMAX: " + xmax);
									System.out.println("ROOMXMIN: " + xmin);
									System.out.println("ROOMYMAX: " + ymax);
									System.out.println("ROOMYMIN: " + ymin);


									userRoomId = 0;
									if (getXlocation < xmax && getXlocation > xmin && getYlocation < ymax && getYlocation > ymin) {
										userRoomId = r.getId();
										break;
									}
								}

								ApiRouter.withToken(getCurrentUser().getToken()).updateLocation(userLocalization.getId(), (float) getXlocation,
										(float) getYlocation, userRoomId,
										new Callback<User>() {
											@Override
											public void success(User user, Response rawResponse) {
												//stopProgress();
												System.out.println("USERLOC: X=" + user.getxCoordinate() + " Y=" + user.getyCoordinate());

												System.out.println("USERROOMID: " + userRoomId);

												userLocalization.setxCoordinate((float) getXlocation);
												userLocalization.setyCoordinate((float) getYlocation);
												userLocalization.setRoomId(userRoomId);
											}

											@Override
											public void failure(RetrofitError e) {
												displayError(e);
											}
										});
								///////////////////////////////////////////

								Coordinate c = averageHelper(listOfAverages.get(0), listOfAverages.get(1), 2.0, 3.0);

								Coordinate coordinate = averageHelper(c, listOfAverages.get(2), 3.0, 5.0);

								Coordinate loc = new Coordinate(getXlocation, getYlocation);

								double distance = Math.sqrt(Math.pow((coordinate.getX() - loc.getX()), 2) + Math.pow((coordinate.getY() - loc.getY()), 2));

								if (distance < 270.0) {
//									Coordinate loc = new Coordinate(getXlocation, getYlocation);
									locations.add(loc);

									drawSourceNode();
//
									drawEdgeToNearestTransitionNode();
//
									showShortestPathToDestination();

									listOfAverages.set(averagesIndex, loc);
								}
								else {
									Coordinate loc2 = averageHelper(coordinate, loc, 7.0, 1.0);
									listOfAverages.set(averagesIndex, loc2);
								}

								//listOfAverages.set(averagesIndex, loc);
							}

							averagesIndex++;
							if (averagesIndex == 3)
								averagesIndex = 0;*/


							///////////////////

							///////////////////////////////////////////
							for (int i = 0; i < rooms.size(); i++) {
								Room r = rooms.get(i);
								float xmax = getTrueWidth(r.getXmax());
								float xmin = getTrueWidth(r.getXmin());
								float ymax = getTrueHeight(r.getYmax());
								float ymin = getTrueHeight(r.getYmin());

								System.out.println("ROOMID: " + r.getId());

								System.out.println("ROOMXMAX: " + xmax);
								System.out.println("ROOMXMIN: " + xmin);
								System.out.println("ROOMYMAX: " + ymax);
								System.out.println("ROOMYMIN: " + ymin);


								userRoomId = 0;
								if (getXlocation < xmax && getXlocation > xmin && getYlocation < ymax && getYlocation > ymin) {
									userRoomId = r.getId();
									break;
								}
							}

							ApiRouter.withToken(getCurrentUser().getToken()).updateLocation(userLocalization.getId(), (float) getXlocation,
									(float) getYlocation, userRoomId,
									new Callback<User>() {
										@Override
										public void success(User user, Response rawResponse) {
											//stopProgress();
											System.out.println("USERLOC: X=" + user.getxCoordinate() + " Y=" + user.getyCoordinate());

											System.out.println("USERROOMID: " + userRoomId);

											userLocalization.setxCoordinate((float) getXlocation);
											userLocalization.setyCoordinate((float) getYlocation);
											userLocalization.setRoomId(userRoomId);
										}

										@Override
										public void failure(RetrofitError e) {
											displayError(e);
										}
									});
							///////////////////////////////////////////

							Coordinate loc = new Coordinate(getXlocation, getYlocation);

							if (!initialLoc) {
								locations.add(loc);

								drawSourceNode();
//
//								drawEdgeToNearestTransitionNode();
//
//								showShortestPathToDestination();

								initialLoc = true;
							}
							else {
								double distance = Math.sqrt(Math.pow((locations.get(0).getX() - loc.getX()), 2) + Math.pow((locations.get(0).getY() - loc.getY()), 2));

								if (distance < 270.0) {
//								Coordinate loc = new Coordinate(getXlocation, getYlocation);
									locations.add(loc);

									drawSourceNode();
//
//									drawEdgeToNearestTransitionNode();
//
//									showShortestPathToDestination();

									//listOfAverages.set(averagesIndex, loc);
								}
								else {
									float trueX = getTrueWidth(connectedTNode.getCoordinate().getX());
									float trueY = getTrueHeight(connectedTNode.getCoordinate().getY());

									float x = ((2 / 5) * locations.get(0).getX()) + ((3 / 5) * trueX);
									float y = ((2 / 5) * locations.get(0).getY()) + ((3 / 5) * trueY);

									Coordinate c = new Coordinate(x, y);
									locations.add(c);

									drawSourceNode();
								}
							}

							//////////////////
						}

//						drawSourceNode();
////
//						drawEdgeToNearestTransitionNode();
////
//						showShortestPathToDestination();

						//deleteSourceNode();

					} else {
//						if (initialLoc) {
//							float trueX = getTrueWidth(connectedTNode.getCoordinate().getX());
//							float trueY = getTrueHeight(connectedTNode.getCoordinate().getY());
//
//							float x = ((2 / 5) * locations.get(0).getX()) + ((3 / 5) * trueX);
//							float y = ((2 / 5) * locations.get(0).getY()) + ((3 / 5) * trueY);
//
//							Coordinate c = new Coordinate(x, y);
//							locations.add(c);
//
//							drawSourceNode();
//						}
					}
					//    scanningCounter = scanningCounter + 1;
				}


	}

	private Coordinate averageHelper(Coordinate c1, Coordinate c2, double w1, double w2) {
		//double distance = Math.sqrt(Math.pow((c1.getX() - c2.getX()), 2) + Math.pow((c1.getY() - c2.getY()), 2));

		//double x = (c1.getX() * ((w1 / (w1 + w2)) * distance)) + (c2.getX() * ((w2 / (w1 + w2)) * distance));
		//double y = (c1.getY() * ((w1 / (w1 + w2)) * distance)) + (c2.getY() * ((w2 / (w1 + w2)) * distance));

		double x = ((w1 / (w1 + w2)) * c1.getX()) + ((w2 / (w1 + w2)) * c2.getX());
		double y = ((w1 / (w1 + w2)) * c1.getY()) + ((w2 / (w1 + w2)) * c2.getY());

		return new Coordinate(x, y);
	}

	public void deleteSourceNode() { // GET TRUE WIDTH AND HEIGHT???
		// Can delete other nodes by mistake
		// TODO add labels to vertices?
		// Coordinate cLast = new Coordinate(getTrueWidth((float) lastX), getTrueHeight((float) lastY));
		//Coordinate cLast = new Coordinate(lastX, lastY);

		Coordinate cLast = locations.get(0);
		DefaultVertex vLast = controller.getClosestVertex(cLast, controller.USER_MISS_RADIUS);

		if (vLast != null && vLast.getType() != null && vLast.getType().equals("USER")) { // TODO: Later --> vLast.getType().equals("USER")
			controller.getUserSelectedVertices().add(vLast);
			controller.deleteSelectedVertices();
			locations.remove(0);
		}
	}

	public void drawSourceNode() { // GET TRUE WIDTH AND HEIGHT???
		deleteSourceNode();

		// Coordinate c = new Coordinate(getTrueWidth((float) getXlocation), getTrueHeight((float) getYlocation));
		//Coordinate c = new Coordinate(getXlocation, getYlocation);

		Coordinate c = locations.get(0);
		DefaultVertex v = new DefaultVertex(c);
		v.setType("USER");

		controller.getGraphWithMemory().addVertex(v);
		controller.redraw();

		for (int i = 0; i < destinations.size(); i++) {
			DefaultVertex d = destinations.get(i);

			float x = d.getCoordinate().getX();
			float y = d.getCoordinate().getY();

			double distanceSquared = Math.pow((x - getXlocation), 2) + Math.pow((y - getYlocation), 2);

			System.out.println("DISTANCE DEST: " + distanceSquared);

			if (distanceSquared <= Math.pow(170, 2)) {
				destinations.remove(d);
				d.setType(null);
			}

			if (destinations.isEmpty()) {
				Toast.makeText(Workspace.this, "You have reached your destination!", Toast.LENGTH_SHORT).show();

				// TODO: stop showing paths
				navigate = false;
			}
		}

		if (navigate) {
			drawEdgeToNearestTransitionNode();
			showShortestPathToDestination();
		}

		/**
		 * MIGHT USE {originalPath} IN CASE OF RE-ROUTING
		 */

/*		if (!(originalPath == null || originalPath.size() == 0)) {
			for (int i = 0; i < path.size(); i++) {
				float nodeX = path.get(i).getCoordinate().getX();
				float nodeY = path.get(i).getCoordinate().getY();

				final double range = Math.pow(10, 2);

				float dx = (float) getXlocation - nodeX;
				float dy = (float) getYlocation - nodeY;

				double diffX = Math.pow(dx, 2);
				double diffY = Math.pow(dy, 2);

				if (diffX <= range && diffY <= range) {
					path.remove(i);
					break;
				}
			}
		}*/
	}

	public void drawEdgeToNearestTransitionNode() { // GET TRUE WIDTH AND HEIGHT???
		// See how you can handle different cases in redraw(), because now it can display more than one path at once

		//System.out.println("NODES: " + transitionNodes.size());

/*		if (originalPath == null || originalPath.size() == 0) {
			drawNearestEdgeHelper(transitionNodes);
		}
		else {
			drawNearestEdgeHelper(path);
		}*/

		double shortestDistanceSquared = Double.MAX_VALUE;
		int index = 0;

		//System.out.println("NODES: " + transitionNodes.size());

		for (int i = 0; i < transitionNodes.size(); i++) {
			DefaultVertex t = transitionNodes.get(i);

			float x = getTrueWidth(t.getCoordinate().getX());
			float y = getTrueHeight(t.getCoordinate().getY());

			System.out.println("FLOATX: " + x + " FLOATY: " + y);

			// double distanceSquared = Math.pow((x - getTrueWidth((float) getXlocation)), 2) + Math.pow((y - getTrueHeight((float) getYlocation)), 2);
			double distanceSquared = Math.pow((x - getXlocation), 2) + Math.pow((y - getYlocation), 2);

//			if (distanceSquared <= Math.pow(10, 2) && !(passedNodes.contains(t))) {
//				passedNodes.add(t);
//			}

//			else if (distanceSquared <= Math.pow(10, 2) && passedNodes.contains(t)) {
//				passedNodes.remove(t);
//			}

			System.out.println("DISTANCE: " + distanceSquared);

//			if (distanceSquared < shortestDistanceSquared && !(passedNodes.contains(t)) && userRoomId == theTransitions.get(i).getRoomId()) {
//				shortestDistanceSquared = distanceSquared;
//				index = i;
//			}

			if (distanceSquared < shortestDistanceSquared && userRoomId == theTransitions.get(i).getRoomId()) {
				shortestDistanceSquared = distanceSquared;
				index = i;
				connectedTNode = t;
			}

			System.out.println("INDEX: " + index);
		}

		DefaultVertex t = transitionNodes.get(index);

		float x = getTrueWidth(t.getCoordinate().getX());
		float y = getTrueHeight(t.getCoordinate().getY());

		Coordinate c = new Coordinate(x, y);
		DefaultVertex nearestTransitionNode = controller.getClosestVertex(c, controller.USER_MISS_RADIUS);

		// DefaultVertex user = new DefaultVertex(new Coordinate(getTrueWidth((float) getXlocation), getTrueHeight((float) getYlocation)));
		// DefaultVertex user = new DefaultVertex(new Coordinate(getXlocation, getYlocation));
		//DefaultVertex user = controller.getClosestVertex(new Coordinate(getXlocation, getYlocation), controller.USER_MISS_RADIUS);
		DefaultVertex user = controller.getClosestVertex(locations.get(0), controller.USER_MISS_RADIUS);

		//System.out.println("USER: " + user.toString());

		controller.getGraphWithMemory().addEdge(user, nearestTransitionNode);

		controller.redraw();
	}

	/*private void drawNearestEdgeHelper(ArrayList<DefaultVertex> arr) {
		*//** TODO:
		 * Iterate through array of transition nodes
		 * Search for/Apply good algorithm to get nearest coordinate
		 * Draw edge between source and resulting node [controller.getGraphWithMemory().addEdge(source, destination); controller.redraw();]
		 *//*

		userLocalization = getCurrentUser();

		double shortestDistanceSquared = Double.MAX_VALUE;
		int index = 0;

		for (int i = 0; i < arr.size(); i++) {
			DefaultVertex t = arr.get(i);

			float x = getTrueWidth(t.getCoordinate().getX());
			float y = getTrueHeight(t.getCoordinate().getY());

			System.out.println("FLOATX: " + x + " FLOATY: " + y);

			// double distanceSquared = Math.pow((x - getTrueWidth((float) getXlocation)), 2) + Math.pow((y - getTrueHeight((float) getYlocation)), 2);
			double distanceSquared = Math.pow((x - getXlocation), 2) + Math.pow((y - getYlocation), 2);

			System.out.println("DISTANCE: " + distanceSquared);

			if (distanceSquared < shortestDistanceSquared && userLocalization.getRoomId() == theTransitions.get(i).getRoomId()) {
				shortestDistanceSquared = distanceSquared;
				index = i;
			}
			System.out.println("INDEX: " + index);
		}

		DefaultVertex t = arr.get(index);

		float x = getTrueWidth(t.getCoordinate().getX());
		float y = getTrueHeight(t.getCoordinate().getY());

		Coordinate c = new Coordinate(x, y);
		DefaultVertex nearestTransitionNode = controller.getClosestVertex(c, controller.USER_MISS_RADIUS);

		// DefaultVertex user = new DefaultVertex(new Coordinate(getTrueWidth((float) getXlocation), getTrueHeight((float) getYlocation)));
		// DefaultVertex user = new DefaultVertex(new Coordinate(getXlocation, getYlocation));
		DefaultVertex user = controller.getClosestVertex(new Coordinate(getXlocation, getYlocation), controller.USER_MISS_RADIUS);

		//System.out.println("USER: " + user.toString());

		controller.getGraphWithMemory().addEdge(user, nearestTransitionNode);

		controller.redraw();
	}*/

	public void showShortestPathToDestination() { // GET TRUE WIDTH AND HEIGHT???
		/** TODO:
		 * Highlight user's source node and destination (save destination in static variable? tours? is it already automatically handled?) [controller.getUserSelectedVertices().add(source); controller.getUserSelectedVertices().add(destination); controller.redraw();]
		 * Show shortest path [controller.showPath();]
		 */

		Coordinate c = locations.get(0);

		DefaultVertex user = controller.getClosestVertex(c, controller.USER_MISS_RADIUS);

		/*controller.getUserSelectedVertices().add(user);
		controller.getUserSelectedVertices().add(destination);

		controller.redraw();

		controller.showPath();*/

		if (tour) {
			controller.showSpanningTree();
		}

//		if (destinations.contains(user)) {
//			destinations.remove(user);
//		}

		//ArrayList<DefaultVertex> arr = (ArrayList<DefaultVertex>) destinations.clone();
		ArrayList<DefaultVertex> arr = new ArrayList<DefaultVertex>();
		arr.add(user);
		arr.addAll(1, destinations);

		System.out.println("ARRSIZE: " + arr.size());

		//destinations.clear();
		//destinations.add(user);
		//destinations.addAll(1, arr);

		System.out.println("DESTINATIONSSIZE: " + destinations.size());

		for (int i = 0; i < arr.size()-1; i++) {
			controller.getUserSelectedVertices().add(arr.get(i));
			controller.getUserSelectedVertices().add(arr.get(i+1));

			controller.redraw();

			controller.showPath();

//			controller.getUserSelectedVertices().remove(arr.get(i));
//			controller.getUserSelectedVertices().remove(arr.get(i+1));
//
//			controller.redraw();
		}

		arr.clear();

		// Coordinate c = new Coordinate(getTrueWidth((float) getXlocation), getTrueHeight((float) getYlocation));
		/*Coordinate c = new Coordinate(getXlocation, getYlocation);

		DefaultVertex user = controller.getClosestVertex(c, controller.USER_MISS_RADIUS);

		*//*controller.getUserSelectedVertices().add(user);
		controller.getUserSelectedVertices().add(destination);

		controller.redraw();

		controller.showPath();*//*

		if (tour) {
			controller.showSpanningTree();

			if (!updatedPath) {
				originalPath = transitionNodes;
				path = originalPath;

				updatedPath = true;
			}
		}

		if (selectDestination) {
			controller.getUserSelectedVertices().add(user);
			controller.getUserSelectedVertices().add(theDestination);

			controller.redraw();

			GraphPath<DefaultVertex, DefaultEdge<DefaultVertex>> gp = controller.showPath();

			if (!updatedPath) {
				for (int i = 1; i < gp.getEdgeList().size(); i++) {
					DefaultEdge<DefaultVertex> e = gp.getEdgeList().get(i);
				*//*if (transitionNodes.contains(e.getSource())) {
					originalPath.add(e.getSource());
				}*//*
					originalPath.add(e.getSource());
				}
				path = originalPath;

				updatedPath = true;
			}
		}

		if (categoryTour) {
			ArrayList<DefaultVertex> arr = (ArrayList<DefaultVertex>) destinations.clone();
			destinations.clear();
			destinations.add(user);
			destinations.addAll(1, arr);

			for (int i = 0; i < destinations.size()-1; i++) {
				controller.getUserSelectedVertices().add(destinations.get(i));
				controller.getUserSelectedVertices().add(destinations.get(i + 1));

				controller.redraw();

				GraphPath<DefaultVertex, DefaultEdge<DefaultVertex>> gp = controller.showPath();

				if (!updatedPath) {
					for (int j = 1; j < gp.getEdgeList().size(); j++) {
						DefaultEdge<DefaultVertex> e = gp.getEdgeList().get(j);
				*//*if (transitionNodes.contains(e.getSource())) {
					originalPath.add(e.getSource());
				}*//*
						originalPath.add(e.getSource());
					}
				}
			}
			if (!updatedPath) {
				path = originalPath;

				updatedPath = true;
			}
		}*/
	}

	private BeaconLocation BeaconLocation(Beacon beacon) {
		/*final Beacon theBeacon = beacon;
		final BeaconLocation beaconLocation = new BeaconLocation();

		ApiRouter.withToken(getCurrentUser().getToken()).getBeacons(1,
				new Callback<List<com.startupcompany.wireframe.model.Beacon>>() {
					@Override
					public void success(List<com.startupcompany.wireframe.model.Beacon> mapBeacons, Response rawResponse) {
						//stopProgress();
						myBeacons = (ArrayList<com.startupcompany.wireframe.model.Beacon>) mapBeacons;

						for (int i = 0; i < myBeacons.size(); i++) {
							com.startupcompany.wireframe.model.Beacon b = myBeacons.get(i);

							if (theBeacon.getMacAddress().toStandardString().equals(b.getMacAddress())) {
								beaconLocation.setX(b.getxCoordinate());
								beaconLocation.setY(b.getyCoordinate());
								beaconLocation.setId((int) b.getId());

								//System.out.println("BEACONIN");

								break;
							}
						}
					}

					@Override
					public void failure(RetrofitError e) {
						displayError(e);
					}
				});*/

		// NEW!!

//		for (int i = 0; i < myBeacons.size(); i++) {
//			com.startupcompany.wireframe.model.Beacon b = myBeacons.get(i);
//
//			if (beacon.getMacAddress().toStandardString().equals(b.getMacAddress())) {
//				beaconLocation.setX(b.getxCoordinate());
//				beaconLocation.setY(b.getyCoordinate());
//				beaconLocation.setId((int) b.getId());
//
//				//System.out.println("BEACONIN");
//
//				break;
//			}
//		}

		//////////////////// NEW TEST ///////////////////////

		BeaconLocation beaconLocation = new BeaconLocation();

//		if (beacon.getMacAddress().toStandardString().equals("20:CD:39:B0:64:48")) {
//			beaconLocation.setX(0.0);
//			beaconLocation.setY(0.0);
//			beaconLocation.setId(1);
//		}
//
//		if (beacon.getMacAddress().toStandardString().equals("20:CD:39:B0:71:65")) {
//			beaconLocation.setX(5.0);
//			beaconLocation.setY(0.0);
//			beaconLocation.setId(2);
//		}

		if (beacon.getMacAddress().toStandardString().equals("20:CD:39:B0:64:48")) { // foo2 el servers - emc
//			beaconLocation.setX(14.835903f);
//			beaconLocation.setY(377.0f);

			beaconLocation.setX(3.0f);
			beaconLocation.setY(333.0f);

			beaconLocation.setId(1);
		}

		if (beacon.getMacAddress().toStandardString().equals("20:CD:39:B0:71:65")) { // odam eli foo2 el servers - emc
//			beaconLocation.setX(151.0f);
//			beaconLocation.setY(373.0f);

			beaconLocation.setX(79.0f);
			beaconLocation.setY(337.94016f);

			beaconLocation.setId(2);
		}

		if (beacon.getMacAddress().toStandardString().equals("20:CD:39:B1:15:AA")) { // ganb bab el emc eli gowa - emc
//			beaconLocation.setX(155.0f);
//			beaconLocation.setY(668.0f);

			beaconLocation.setX(75.0f);
			beaconLocation.setY(521.0f);

			beaconLocation.setId(3);
		}

		if (beacon.getMacAddress().toStandardString().equals("20:CD:39:B1:14:14")) { // 3and el shebak - emc
//			beaconLocation.setX(15.0f);
//			beaconLocation.setY(668.0f);

			beaconLocation.setX(6.9402676f);
			beaconLocation.setY(521.0f);

			beaconLocation.setId(4);
		}

		if (beacon.getMacAddress().toStandardString().equals("20:CD:39:B0:74:77")) { // odam bab el emc - emc
//			beaconLocation.setX(14.292226f);
//			beaconLocation.setY(884.0f);

			beaconLocation.setX(6.9273686f);
			beaconLocation.setY(660.0f);

			beaconLocation.setId(5);
		}

		if (beacon.getMacAddress().toStandardString().equals("20:CD:39:B0:7C:38")) { // foo2 bab el emc - emc
//			beaconLocation.setX(159.0f);
//			beaconLocation.setY(888.0f);

			beaconLocation.setX(75.0f);
			beaconLocation.setY(660.4758f);

			beaconLocation.setId(6);
		}

		if (beacon.getMacAddress().toStandardString().equals("20:91:48:42:03:24")) { // odam bab emc
//			beaconLocation.setX(215.0f);
//			beaconLocation.setY(803.97156f);

			beaconLocation.setX(171.0f);
			beaconLocation.setY(660.0f);

			beaconLocation.setId(7);
		}

		if (beacon.getMacAddress().toStandardString().equals("20:91:48:42:02:E3")) { // ganb eli odam bab emc
//			beaconLocation.setX(219.0f);
//			beaconLocation.setY(892.0f);

			beaconLocation.setX(171.0f);
			beaconLocation.setY(776.0f);

			beaconLocation.setId(8);
		}

		if (beacon.getMacAddress().toStandardString().equals("20:91:48:42:84:00")) { // foo2 bab el ooda
//			beaconLocation.setX(79.0f);
//			beaconLocation.setY(976.0f);

			beaconLocation.setX(75.0f);
			beaconLocation.setY(776.0f);

			beaconLocation.setId(9);
		}

		if (beacon.getMacAddress().toStandardString().equals("20:91:48:42:82:E2")) { // ashour
			beaconLocation.setX(391.0f);
			beaconLocation.setY(672.0474f);
			beaconLocation.setId(10);
		}

		if (beacon.getMacAddress().toStandardString().equals("20:91:48:4C:5A:40")) { // 3and bab awel lab
			beaconLocation.setX(271.0f);
			beaconLocation.setY(608.4713f);
			beaconLocation.setId(11);
		}

		if (beacon.getMacAddress().toStandardString().equals("20:91:48:4C:5B:46")) { // gowa awel lab
			beaconLocation.setX(267.0f);
			beaconLocation.setY(337.94016f);
			beaconLocation.setId(12);
		}

		if (beacon.getMacAddress().toStandardString().equals("20:91:48:4C:5A:6E")) { // 3and el bawaba eli abl el sellem mesh ganb el asasanseir
//			beaconLocation.setX(287.18268f);
//			beaconLocation.setY(892.0f);

			beaconLocation.setX(287.0f);
			beaconLocation.setY(800.0f);

			beaconLocation.setId(13);
		}

		if (beacon.getMacAddress().toStandardString().equals("20:91:48:42:84:2F")) { // foo2 bab tany lab
			beaconLocation.setX(399.0f);
			beaconLocation.setY(607.9762f);
			beaconLocation.setId(14);
		}

		if (beacon.getMacAddress().toStandardString().equals("20:91:48:42:86:D5")) { // odam foo2 bab el ooda
//			beaconLocation.setX(11.014456f);
//			beaconLocation.setY(976.0f);

			beaconLocation.setX(6.997014f);
			beaconLocation.setY(776.0f);

			beaconLocation.setId(15);
		}

		/////////////////////////////////////////////////////

/*		if (beacon.getMacAddress().toStandardString().equals("20:CD:39:B0:76:E9")) {
			beaconLocation.setX(14.835903f);
			beaconLocation.setY(377.0f);
			beaconLocation.setId(1);
		}
		if (beacon.getMacAddress().toStandardString().equals("20:CD:39:AE:FB:AA")) {
			beaconLocation.setX(151.0f);
			beaconLocation.setY(373.0f);
			beaconLocation.setId(2);
		}*/

//		for (int i = 0; i < myBeacons.size(); i++) {
//			if (beacon.getMacAddress().toStandardString().equals(myBeacons.get(i).getMacAddress())) {
//				System.out.println("IAMIN");
//				beaconLocation.setX(myBeacons.get(i).getxCoordinate());
//				beaconLocation.setY(myBeacons.get(i).getyCoordinate());
//
//				System.out.println("BEACONLCATION: " + beaconLocation.getX() + ", " + beaconLocation.getY());
//			}
//		}

		////////////////////////////


//
////		 EMC LAB
//		if (beacon.getMacAddress().toStandardString().equals("20:CD:39:B1:15:AA")) {
//			beaconLocation.setX(155.0f);
//			beaconLocation.setY(668.0f);
//			beaconLocation.setId(3);
//		}
//		if (beacon.getMacAddress().toStandardString().equals("20:CD:39:B1:14:14")) {
//			beaconLocation.setX(15.0f);
//			beaconLocation.setY(668.0f);
//			beaconLocation.setId(4);
//		}
//		if (beacon.getMacAddress().toStandardString().equals("20:CD:39:B0:74:77")) {
//			beaconLocation.setX(14.292226f);
//			beaconLocation.setY(884.0f);
//			beaconLocation.setId(5);
//		}
//
//		if (beacon.getMacAddress().toStandardString().equals("20:CD:39:B0:7C:38")) {
//			beaconLocation.setX(159.0f);
//			beaconLocation.setY(888.0f);
//			beaconLocation.setId(6);
//		}

		////////////////////////////

//		if (beacon.getMacAddress().toStandardString().equals("20:CD:39:B0:64:48")) {
//			beaconLocation.setX(0.0);
//			beaconLocation.setY(0.0);
//			beaconLocation.setId(1);
//		}
//		if (beacon.getMacAddress().toStandardString().equals("20:CD:39:B0:71:65")) {
//			beaconLocation.setX(5.0);
//			beaconLocation.setY(0.0);
//			beaconLocation.setId(2);
//		}

		// EMC LAB
//		if (beacon.getMacAddress().toStandardString().equals("20:CD:39:B1:15:AA")) {
//			beaconLocation.setX(163.0f);
//			beaconLocation.setY(684.0f);
//			beaconLocation.setId(1);
//		}
//		if (beacon.getMacAddress().toStandardString().equals("20:CD:39:B1:14:14")) {
//			beaconLocation.setX(19.0f);
//			beaconLocation.setY(684.0f);
//			beaconLocation.setId(2);
//		}
//		if (beacon.getMacAddress().toStandardString().equals("20:CD:39:B0:74:77")) {
//			beaconLocation.setX(19.0f);
//			beaconLocation.setY(895.8223f);
//			beaconLocation.setId(3);
//		}
//
//		if (beacon.getMacAddress().toStandardString().equals("20:CD:39:B0:7C:38")) {
//			beaconLocation.setX(159.0f);
//			beaconLocation.setY(896.0f);
//			beaconLocation.setId(4);
//		}

//		if (beacon.getMacAddress().toStandardString().equals("20:91:48:42:03:24")) {
//			beaconLocation.setX(8.0);
//			beaconLocation.setY(10.0);
//			beaconLocation.setId(7);
//		}
//		if (beacon.getMacAddress().toStandardString().equals("20:91:48:42:02:E3")) {
//			beaconLocation.setX(8.0);
//			beaconLocation.setY(15.0);
//			beaconLocation.setId(8);
//		}
//		if (beacon.getMacAddress().toStandardString().equals("20:91:48:42:84:00")) {
//			beaconLocation.setX(5.0);
//			beaconLocation.setY(15.0);
//			beaconLocation.setId(9);
//		}
//		if (beacon.getMacAddress().toStandardString().equals("20:91:48:42:82:E2")) {
//			beaconLocation.setX(0.0);
//			beaconLocation.setY(15.0);
//			beaconLocation.setId(10);
//		}

//		if (beacon.getMacAddress().toStandardString().equals("20:CD:39:B0:76:E9")) { // white
//			beaconLocation.setX(500.0);
//			beaconLocation.setY(1000.0);
//			beaconLocation.setId(1);
//		}
//
//		else if (beacon.getMacAddress().toStandardString().equals("20:CD:39:AE:FB:AA")) { // blue
//			beaconLocation.setX(520.0);
//			beaconLocation.setY(1030.0);
//			beaconLocation.setId(2);
//		}

		return beaconLocation;
	}

	public ArrayList<Double> localizationCoordinates(double x ,double y){
		ArrayList<Double> coordinates = new ArrayList<>();
		coordinates.add(x);
		coordinates.add(y);
		return coordinates;
	}
}
