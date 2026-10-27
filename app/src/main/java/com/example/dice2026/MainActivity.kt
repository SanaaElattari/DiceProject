package com.example.dice2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dice2026.ui.theme.Dice2026Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Dice2026Theme {
                DiceScreen()
            }
        }
    }
}

@Composable
fun DiceScreen(modifier: Modifier = Modifier) {
    var numDice by remember { mutableIntStateOf(1) }
    var diceValues by remember { mutableStateOf(List(numDice){1}) }

    var playerTotal by remember { mutableIntStateOf(0) }
    var botTotal by remember { mutableIntStateOf(0) }
    
    var prevUser by remember { mutableIntStateOf(0) }
    var prevBot by remember { mutableIntStateOf(0) }

    var winner by remember { mutableStateOf<String?>(null) }
    var isGameOver by remember { mutableStateOf(value = false) }

    fun playTurn() {
        if (isGameOver) return

        // 1. User rolls
        val newDice = List(numDice) { rollDie(numDice)}
        diceValues = newDice
        var playerRoundTotal = 0
        for(i in newDice) {
            if(i == 1) {
                playerRoundTotal = 0
                break;
            } else {
                playerRoundTotal+=i
            }
        }
        prevUser = playerRoundTotal

        if (playerTotal + playerRoundTotal == 50) {
            playerTotal = 50
            winner = "Player"
            isGameOver = true
            return
        } else if (playerTotal + playerRoundTotal < 50) {
            playerTotal += playerRoundTotal
        }

        // 2. Bot rolls using calculatePlayer2()
        val botRoundTotal = calculatePlayer2()
        prevBot = botRoundTotal

        if (botTotal + botRoundTotal == 50) {
            botTotal = 50
            winner = "Bot"
            isGameOver = true
            return
        } else if (botTotal + botRoundTotal < 50) {
            botTotal += botRoundTotal
        }
    }

    fun resetGame() {
        playerTotal = 0
        botTotal = 0
        prevUser = 0
        prevBot = 0
        winner = null
        isGameOver = false
        diceValues = List(numDice) { 1 }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    )
    {
        Text(
            text = "Dice Game! Who Can Reach 50 First?",
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Row for Scores
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Player's Score", fontSize = 16.sp)
                Text(
                    text = "$playerTotal / 50",
                    fontSize = 22.sp
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Bot's Score", fontSize = 16.sp)
                Text(
                    text = "$botTotal / 50",
                    fontSize = 22.sp,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (winner != null) {
            Text(
                text = "🎉 $winner Wins! 🎉",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            Text(
                text = "Last User Roll: $prevUser | Last Bot Roll: $prevBot",
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
            text = "Number of Dice: $numDice",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
        )

        Slider(
            value = numDice.toFloat(),
            onValueChange = { newValue ->
                val newNum = newValue.toInt().coerceIn(1, 4)
                if (newNum != numDice) {
                    numDice = newNum
                    if (!isGameOver) {
                        diceValues = List(newNum) { (1..6).random() }
                    }
                }
            },
            valueRange = 1f..4f,
            steps = 2,
            enabled = !isGameOver,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Grid of dice on screen
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(diceValues.size) { index ->
                val dieValue = diceValues[index]
                DiceItem(
                    value = dieValue,
                    onClick = {
                        playTurn()
                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!isGameOver) {
            Button(
                onClick = { playTurn() },
            ) {
                Text("Roll Dice", fontSize = 18.sp)
            }
        } else {
            Button(
                onClick = { resetGame() },
            ) {
                Text("Play Again", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun DiceItem(
    value: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dieArray = remember {
        intArrayOf(
            R.drawable.dice1,
            R.drawable.dice2,
            R.drawable.dice3,
            R.drawable.dice4,
            R.drawable.dice5,
            R.drawable.dice6,
        )
    }

    val index = value - 1
    val drawableRes = if (index in dieArray.indices) dieArray[index] else dieArray[0]

    Column(
        modifier = modifier
            .size(80.dp)
            .clickable { onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = drawableRes),
            contentDescription = "Die showing $value",
            modifier = Modifier.size(70.dp),
        )
    }
}

fun calculatePlayer2(): Int {
    val numDice = (1..4).random()
    return rollDie(numDice)
}

fun rollDie(numDice: Int): Int {
    if (numDice == 1) {
        return (1..6).random()
    } else if(numDice == 2){
        val diceVal = intArrayOf(1, 1, 1, 2, 3, 4, 5, 6)
        return diceVal[(0..7).random()]
    }
    else if(numDice == 3){
        val diceVal = intArrayOf(1, 1, 1, 1, 2, 3, 4, 5, 6)
        return diceVal[(0..8).random()]
    }
    else if(numDice == 4){
        val diceVal = intArrayOf(1, 1, 1, 1, 1, 2, 3, 4, 5, 6)
        return diceVal[(0..9).random()]
    }
    else {
        return 0;
    }

}
