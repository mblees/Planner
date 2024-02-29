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
fun HygieneMenu(modifier: Modifier = Modifier) {
    // List of hygiene tasks
    val hygieneTasks = listOf(
        "Wash hands",
        "Brush teeth",
        "Take a shower",
        "Cut nails",
        "Clean ears",
        "Wash face"
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
                    modifier = Modifier.padding(16.dp)
                )

                // Display the task
                Text(
                    text = task,
                    modifier = Modifier.padding(end = 16.dp, top = 30.dp)
                )
            }
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
            onClick = { onButtonClicked("Uni") },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Uni")
        }
    }
}


@Composable
fun MainScreen() {
    var currentScreen by remember { mutableStateOf("Menu") }

    Column(modifier = Modifier.fillMaxSize()) {
        if (currentScreen != "Hygiene") {
            StartMenu { action ->
                currentScreen = action
            }
        }

        when (currentScreen) {
            "Hygiene" -> HygieneMenu(modifier = Modifier.fillMaxSize())
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