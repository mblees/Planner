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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlannerTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun GymPreparationMenu(modifier: Modifier = Modifier, onButtonClicked : (String) -> Unit = {}) {
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
            if(index == gymChecklist.size - 1){
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
fun HygieneMenu(modifier: Modifier = Modifier, onButtonClicked : (String) -> Unit = {}) {
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
            if(index == hygieneTasks.size - 1){
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
fun FoodMenu(modifier: Modifier = Modifier, onButtonClicked : (String) -> Unit = {}) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp) // Add padding to the column
    ){
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



@Composable
fun StartMenu(modifier: Modifier = Modifier, onButtonClicked : (String) -> Unit = {}) {
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
            "Essen" -> FoodMenu() { action ->
                currentScreen = action
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PlannerTheme {
        StartMenu{}
    }
}

@Preview(showBackground = true)
@Composable
fun HygieneMenuPreview() {
    PlannerTheme {
        HygieneMenu()
    }
}