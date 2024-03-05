package com.example.planner

import com.example.planner.ui.theme.PlannerTheme

import android.os.Bundle
import android.util.Log

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.kotlin.InfluxDBClientKotlin
import com.influxdb.client.kotlin.InfluxDBClientKotlinFactory
import com.influxdb.client.write.Point
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("onCreate", "Started the App")
        dailyTask()
        setContent {
            PlannerTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun GymPreparationMenu(modifier: Modifier = Modifier, onButtonClicked: (String) -> Unit = {}) {
    // List of thigs that need to be brought
    val gymChecklist = listOf(
        "Kopfhörer",
        "Unterwäsche",
        "Trainingskleider",
        "frische Kleider",
        "Duschgel",
        "Handtuch",
        "Wasserflasche",
        "Chipkarte"
        // Add more tasks as needed
    )

    // State to hold the checked status of each task
    val checkedTasks = remember { mutableStateOf(List(gymChecklist.size) { false }) }

    LazyColumn(modifier = modifier) {
        items(gymChecklist) { task ->
            val index = gymChecklist.indexOf(task)
            val isChecked = checkedTasks.value[index]

            // Row to align checkbox and text
            Row(
                modifier = Modifier.padding(horizontal = 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Checkbox to mark task as done
                Checkbox(
                    checked = isChecked, onCheckedChange = { checked ->
                        checkedTasks.value = checkedTasks.value.toMutableList().also {
                            it[index] = checked
                        }
                    }, modifier = Modifier.padding(8.dp)
                )

                // Display the task
                Text(
                    text = task, modifier = Modifier.padding(end = 16.dp, top = 29.dp)
                )
            }
            if (index == gymChecklist.size - 1) {
                Button(
                    onClick = { onButtonClicked("Daten senden") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Daten senden")
                }
                Button(
                    onClick = { onButtonClicked("Start Menu") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp)
                ) {
                    Text("Start Menu")
                }
            }
        }
    }
}


@Composable
fun HygieneMenu(modifier: Modifier = Modifier, onButtonClicked: (String) -> Unit = {}) {
    // List of hygiene tasks
    val hygieneTasks = listOf(
        "Skincare",
        "Rasieren",
        "Duschen",
        "Zähneputzen Morgens",
        "Zähneputzen Abends",
        "Nägel schneiden"
        // Add more tasks as needed
    )

    // State to hold the checked status of each task
    val checkedTasks = remember { mutableStateOf(List(hygieneTasks.size) { false }) }

    LazyColumn(modifier = modifier) {
        items(hygieneTasks) { task ->
            val index = hygieneTasks.indexOf(task)
            val isChecked = checkedTasks.value[index]

            // Row to align checkbox and text
            Row(
                modifier = Modifier.padding(horizontal = 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Checkbox to mark task as done
                Checkbox(
                    checked = isChecked, onCheckedChange = { checked ->
                        checkedTasks.value = checkedTasks.value.toMutableList().also {
                            it[index] = checked
                        }
                    }, modifier = Modifier.padding(8.dp)
                )

                // Display the task
                Text(
                    text = task, modifier = Modifier.padding(end = 16.dp, top = 30.dp)
                )
            }
            if (index == hygieneTasks.size - 1) {
                Button(
                    onClick = { onButtonClicked("Daten senden") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Daten senden")
                }
                Button(
                    onClick = { onButtonClicked("Start Menu") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp)
                ) {
                    Text("Start Menu")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
fun FoodMenu(modifier: Modifier = Modifier, onButtonClicked: (String) -> Unit = {}) {
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp) // Add padding to the column
    ) {
        TextField(value = calories, onValueChange = { calories = it }, label = { Text("Calories") })
        TextField(value = protein, onValueChange = { protein = it }, label = { Text("Protein") })
        TextField(value = carbs, onValueChange = { carbs = it }, label = { Text("Carbs") })
        TextField(value = fat, onValueChange = { fat = it }, label = { Text("Fat") })
        Button(
            onClick = {
                GlobalScope.launch {
                    sendDataPoint("food", "calories", calories.safeToLong())
                    sendDataPoint("food", "protein", protein.safeToLong())
                    sendDataPoint("food", "carbs", carbs.safeToLong())
                    sendDataPoint("food", "fat", fat.safeToLong())
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Daten senden")
        }
        Button(
            onClick = { onButtonClicked("Start Menu") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Start Menu")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoneyMenu(modifier: Modifier = Modifier, onButtonClicked: (String) -> Unit = {}) {
    var category by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp) // Add padding to the column
    ) {
        TextField(value = category,
            onValueChange = { category = it },
            label = { Text("Wofür hast du Geld ausgegeben?") })
        TextField(value = amount, onValueChange = { amount = it }, label = { Text("Preis in €") })
        Button(
            onClick = { onButtonClicked("Daten senden") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Daten senden")
        }
        Button(
            onClick = { onButtonClicked("Start Menu") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Start Menu")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
fun GymMenu(modifier: Modifier = Modifier, onButtonClicked: (String) -> Unit = {}) {
    val chestDay = listOf(
        "Bankdrücken",
        "Seitheben",
        "Trizeps Pulldown",
        "Flys Machine",
        "Trizeps hinter Kopf",
        "Flys Kabelturm",
        "Schulterpresse"
    )

    val backDay = listOf(
        "Latzug",
        "Bizeps Kabelturm",
        "Rudern Machine",
        "Reverse Flys Machine",
        "Bizepscurls SZ-Stange",
        "Lat Pulls Kabelturm"
    )

    val legDay = listOf(
        "Beinpresse",
        "Beinbeuger",
        "Beinstrecker",
        "Wadenheben",
        "Situps",
        "Plank",
        "Plank seitlich"
    )

    var selectedTrainingDay by remember { mutableStateOf<String?>(null) }

    val exercises = when (selectedTrainingDay) {
        "Chest" -> chestDay
        "Back" -> backDay
        "Legs" -> legDay
        else -> emptyList()
    }

    // Map to store exercise values
    var exerciseValues by remember { mutableStateOf(mapOf<String, String>()) }

    var isDropdownExpanded by remember { mutableStateOf(false) }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp) // Add padding to the column
    ) {
        Box {
            Button(
                onClick = { isDropdownExpanded = !isDropdownExpanded },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(selectedTrainingDay ?: "Choose Training Day")
            }

            if (isDropdownExpanded) {
                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false },
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    DropdownMenuItem(text = { Text("Chest Day") }, onClick = {
                        selectedTrainingDay = "Chest"
                        isDropdownExpanded = false
                    })
                    DropdownMenuItem(text = { Text("Back Day") }, onClick = {
                        selectedTrainingDay = "Back"
                        isDropdownExpanded = false
                    })
                    DropdownMenuItem(text = { Text("Leg Day") }, onClick = {
                        selectedTrainingDay = "Legs"
                        isDropdownExpanded = false
                    })
                }
            }
        }

        // Display text fields for each exercise
        exercises.forEach { exercise ->
            TextField(
                value = exerciseValues[exercise] ?: "",
                onValueChange = { newValue ->
                    exerciseValues = exerciseValues + (exercise to newValue)
                },
                label = { Text(exercise) }
            )
        }

        Button(
            onClick = {
                // Sending data points for all exercises
                GlobalScope.launch {
                    exercises.forEach { exercise ->
                        val value = exerciseValues[exercise] ?: ""
                        // Send data point for each exercise
                        sendDataPoint(selectedTrainingDay.toString(), exercise, value.safeToLong())
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Daten senden")
        }

        Button(
            onClick = { onButtonClicked("Start Menu") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Start Menu")
        }
    }
}


@Composable
fun StartMenu(modifier: Modifier = Modifier, onButtonClicked: (String) -> Unit = {}) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp) // Add padding to the column
    ) {
        Button(
            onClick = { onButtonClicked("Hygiene") }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Hygiene")
        }
        Button(
            onClick = { onButtonClicked("Essen") }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Essen")
        }
        Button(
            onClick = { onButtonClicked("Geld") }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Geld")
        }
        Button(
            onClick = { onButtonClicked("Gym") }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gym")
        }
        Button(
            onClick = { onButtonClicked("Gym Checklist") }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gym Checklist")
        }
    }
}


@Composable
fun MainScreen() {
    var currentScreen by remember { mutableStateOf("Start Menu") }

    Column(modifier = Modifier.fillMaxSize()) {
        when (currentScreen) {
            "Start Menu" -> StartMenu { action ->
                currentScreen = action
            }

            "Hygiene" -> HygieneMenu { action ->
                currentScreen = action
            }

            "Gym Checklist" -> GymPreparationMenu { action ->
                currentScreen = action
            }

            "Essen" -> FoodMenu { action ->
                currentScreen = action
            }

            "Geld" -> MoneyMenu { action ->
                currentScreen = action
            }

            "Gym" -> GymMenu { action ->
                currentScreen = action
            }
        }
    }
}

fun connectInfluxDB(): InfluxDBClientKotlin {
    val token =
        "E297d7E9DQ8HtitYgfhJyZ0pRcNUUuHETnHWWGta_rXLfST1De_aTt6L4FgORkVRo1u5tpohutP_rHW-lrduWg=="
    val org = "Planer"
    val bucket = "KotlinApp"

    return InfluxDBClientKotlinFactory.create(
        "https://eu-central-1-1.aws.cloud2.influxdata.com", token.toCharArray(), org, bucket
    )
}

suspend fun sendDataPoint(measurement: String, field: String, value: Long?) {
    val client = connectInfluxDB()

    if (value != null) {
        client.use { influxDBClient ->
            val writeApi = influxDBClient.getWriteKotlinApi()

            val point = Point.measurement(measurement).addField(field, value)
                .time(Instant.now(), WritePrecision.NS)

            writeApi.writePoint(point)
        }
    }

    client.close()
}

fun String.safeToLong(): Long? {
    return if (isEmpty() || all { it.isDigit() }) {
        if (isEmpty()) null else toLong()
    } else {
        null
    }
}

fun dailyTask() {
    // Create a scheduled executor service with a single thread
    val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    // Define the time of day when you want your task to execute
    val desiredHour = 13 // Change this to your desired hour (24-hour format)
    val desiredMinute = 21 // Change this to your desired minute

    // Get the current time
    val currentTime = System.currentTimeMillis() + 3600000 //+1 hour to adjust to German time

    // Calculate the delay until the next execution time
    val currentHour = ((currentTime / 3600000) % 24).toInt() // milliseconds to hours
    val currentMinute = ((currentTime / 60000) % 60).toInt() // milliseconds to minutes

    println("current hour $currentHour")
    println("current minute $currentMinute")

    var delayHours: Int
    val delayMinutes: Int

    if (currentHour > desiredHour || (currentHour == desiredHour && currentMinute >= desiredMinute)) {
        // If the current time has already passed the desired time today,
        // schedule the task for the next day
        delayHours = 24 - currentHour + desiredHour
        delayMinutes = 60 - currentMinute + desiredMinute
        println("Task Scheduled for next Day")
    } else {
        delayHours = desiredHour - currentHour
        if (currentMinute > desiredMinute) {
            delayHours -= 1
            delayMinutes = 60 - currentMinute + desiredMinute
        } else {
            delayMinutes = desiredMinute - currentMinute
        }
        println("Task Scheduled for Today")
    }

    // Calculate the total delay in milliseconds until the next execution
    val initialDelay =
        delayHours * 3600000 + delayMinutes * 60000 // hours to milliseconds, minutes to milliseconds

    println("Initialized Scheduled Executor")
    println("First execution in $delayHours Hours")
    println("First execution in $delayMinutes Minutes")
    println("First execution in $initialDelay Milliseconds")

    // Schedule the task to execute daily at the desired time
    executor.scheduleAtFixedRate({
        // Define the task you want to execute
        println("Task executed at ${System.currentTimeMillis()}")
    }, initialDelay.toLong(), 24 * 3600000, TimeUnit.MILLISECONDS) // 24 hours in milliseconds

    // Add a shutdown hook to gracefully shutdown the executor service when the application terminates
    Runtime.getRuntime().addShutdownHook(Thread {
        executor.shutdown()
        println("Executor service shutdown")
    })
}
