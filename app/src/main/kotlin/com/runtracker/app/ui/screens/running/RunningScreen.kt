package com.runtracker.app.ui.screens.running

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.runtracker.app.ui.theme.*
import com.runtracker.app.util.LocationUtils
import com.runtracker.app.viewmodel.RunViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunningScreen(
    onBack: () -> Unit,
    viewModel: RunViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isTracking by viewModel.isTracking.collectAsState()
    val elapsedTime by viewModel.elapsedTime.collectAsState()
    val totalDistance by viewModel.totalDistance.collectAsState()
    val currentSpeed by viewModel.currentSpeed.collectAsState()
    val routePoints by viewModel.routePoints.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        if (hasLocationPermission && !isTracking) {
            viewModel.startTracking()
        }
    }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            )
        } else if (!isTracking) {
            viewModel.startTracking()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isTracking) "Sedang Berlari" else "Selesai",
                        fontWeight = FontWeight.Bold,
                        color = AccentGreen
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = AccentGreen
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (routePoints.isNotEmpty()) {
                    OsmMapView(routePoints, currentLocation)
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DarkSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Menunggu GPS...", color = LightGray, fontSize = 16.sp)
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    LocationUtils.formatDuration(elapsedTime),
                    color = AccentGreen,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    RealtimeStat(
                        label = "Jarak",
                        value = LocationUtils.formatDistance(totalDistance),
                        icon = Icons.Default.DirectionsRun
                    )
                    RealtimeStat(
                        label = "Kecepatan",
                        value = LocationUtils.formatSpeed(currentSpeed),
                        icon = Icons.Default.Speed
                    )
                    RealtimeStat(
                        label = "Kalori",
                        value = String.format("%.0f kk", LocationUtils.calculateCalories(totalDistance / 1000.0)),
                        icon = Icons.Default.LocalFireDepartment
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (isTracking) {
                    Button(
                        onClick = {
                            viewModel.stopTracking()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Icon(Icons.Default.Stop, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("BERHENTI", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                } else {
                    Button(
                        onClick = onBack,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Green600),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text("KEMBALI", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun RealtimeStat(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AccentGreen,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            value,
            color = AccentGreen,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            label,
            color = LightGray,
            fontSize = 12.sp
        )
    }
}

@Composable
fun OsmMapView(routePoints: List<Pair<Double, Double>>, currentLocation: Pair<Double, Double>?) {
    val context = LocalContext.current

    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))
    Configuration.getInstance().userAgentValue = context.packageName

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(16.0)
            }
        },
        update = { mapView ->
            mapView.overlays.clear()

            if (routePoints.isNotEmpty()) {
                val geoPoints = routePoints.map { GeoPoint(it.first, it.second) }

                val polyline = Polyline().apply {
                    setPoints(geoPoints)
                    outlinePaint.color = android.graphics.Color.GREEN
                    outlinePaint.strokeWidth = 8f
                }
                mapView.overlays.add(polyline)

                if (geoPoints.size == 1) {
                    mapView.controller.setCenter(geoPoints.first())
                    mapView.controller.setZoom(17.0)
                } else {
                    try {
                        val boundingBox = BoundingBox.fromGeoPoints(geoPoints)
                        mapView.zoomToBoundingBox(boundingBox.increaseByScale(1.3f), false)
                    } catch (_: Exception) {
                        mapView.controller.setCenter(geoPoints.last())
                        mapView.controller.setZoom(16.0)
                    }
                }
            }

            currentLocation?.let { loc ->
                val currentGeoPoint = GeoPoint(loc.first, loc.second)
                val marker = Marker(mapView).apply {
                    position = currentGeoPoint
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                    title = "Lokasi Anda"
                    setIcon(
                        androidx.core.content.ContextCompat.getDrawable(
                            mapView.context,
                            android.R.drawable.ic_menu_mylocation
                        )
                    )
                }
                mapView.overlays.add(marker)
            }
        }
    )
}
