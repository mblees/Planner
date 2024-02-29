package com.example.planner

import com.example.planner.ui.theme.PlannerTheme
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField

import android.content.Context

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val influxDBConfig = getInfluxDBConfig(this)
        val serverUrl = influxDBConfig["serverUrl"]
        val databaseName = influxDBConfig["databaseName"]
        val username = influxDBConfig["username"]
        val password = influxDBConfig["password"]

        setContent {
            PlannerTheme {
                MainScreen()
            }
        }
    }
}

fun getInfluxDBConfig(context: Context): Map<String, String> {
    val influxDBConfig = mutableMapOf<String, String>()

    // Access resources
    val serverUrl = context.getString(R.string.serverURL)
    val databaseName = context.getString(R.string.databaseName)
    val username = context.getString(R.string.username)
    val password = context.getString(R.string.password)

    // Populate the map
    influxDBConfig["serverUrl"] = serverUrl
    influxDBConfig["databaseName"] = databaseName
    influxDBConfig["username"] = username
    influxDBConfig["password"] = password

    return influxDBConfig
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
                    checked = isChecked,
                    onCheckedChange = { checked ->
                        checkedTasks.value = checkedTasks.value.toMutableList().also {
                            it[index] = checked
                        }
                    },
                    modifier = Modifier.padding(8.dp)
                )

                // Display the task
                Text(
                    text = task,
                    modifier = Modifier.padding(end = 16.dp, top = 29.dp)
                )
            }
            if (index == gymChecklist.size - 1) {
                Button(
                    onClick = { onButtonClicked("Daten senden") },
                    modifier = Modifier
                        .fillMaxWidth()
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
                    checked = isChecked,
                    onCheckedChange = { checked ->
                        checkedTasks.value = checkedTasks.value.toMutableList().also {
                            it[index] = checked
                        }
                    },
                    modifier = Modifier.padding(8.dp)
                )

                // Display the task
                Text(
                    text = task,
                    modifier = Modifier.padding(end = 16.dp, top = 30.dp)
                )
            }
            if (index == hygieneTasks.size - 1) {
                Button(
                    onClick = { onButtonClicked("Daten senden") },
                    modifier = Modifier
                        .fillMaxWidth()
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

@OptIn(ExperimentalMaterial3Api::class)
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
        TextField(
            value = calories,
            onValueChange = { calories = it },
            label = { Text("Calories") }
        )
        TextField(
            value = protein,
            onValueChange = { protein = it },
            label = { Text("Protein") }
        )
        TextField(
            value = carbs,
            onValueChange = { carbs = it },
            label = { Text("Carbs") }
        )
        TextField(
            value = fat,
            onValueChange = { fat = it },
            label = { Text("Fat") }
        )
        Button(
            onClick = {onButtonClicked("Daten senden")},
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
        TextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Wofür hast du Geld ausgegeben?") }
        )
        TextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Preis in €") }
        )
        Button(
            onClick = {onButtonClicked("Daten senden")},
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymMenu(modifier: Modifier = Modifier, onButtonClicked: (String) -> Unit = {}) {
    var typeWorkout by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp) // Add padding to the column
    ) {
        TextField(
            value = typeWorkout,
            onValueChange = { typeWorkout = it },
            label = { Text("Was trainierst du heute?") }
        )
        Button(
            onClick = {onButtonClicked("Workout starten")},
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Workout starten")
        }

        Button(
            onClick = {onButtonClicked("Workout beenden")},
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Workout beenden")
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
            onClick = { onButtonClicked("Hygiene") },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Hygiene")
        }
        Button(
            onClick = { onButtonClicked("Essen") },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Essen")
        }
        Button(
            onClick = { onButtonClicked("Geld") },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Geld")
        }
        Button(
            onClick = { onButtonClicked("Gym") },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Gym")
        }
        Button(
            onClick = { onButtonClicked("Gym Checklist") },
            modifier = Modifier
                .fillMaxWidth()
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