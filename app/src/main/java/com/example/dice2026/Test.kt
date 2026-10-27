//package com.example.dice2026
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.material3.Button
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Slider
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableIntStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.dice2026.ui.theme.Dice2026Theme
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            Dice2026Theme {
//                DiceScreen()
//            }
//        }
//    }
//}
//
//@Composable
//fun DiceScreen(modifier: Modifier = Modifier) {
//    var numDice by remember { mutableIntStateOf(1) }
//    var diceValues by remember {
//        mutableStateOf(List(numDice) { (1..6).random() })
//    }
//
//    var userScore by remember { mutableIntStateOf(0) }
//    var botScore by remember { mutableIntStateOf(0) }
//    var lastUserRollSum by remember { mutableIntStateOf(0) }
//    var lastBotRoll by remember { mutableIntStateOf(0) }
//    var winner by remember { mutableStateOf<String?>(null) }
//    var isGameOver by remember { mutableStateOf(value = false) }
//
//    fun playTurn() {
//        if (isGameOver) return
//
//        // 1. User rolls
//        val newDice = List(numDice) { (1..6).random() }
//        diceValues = newDice
//        val userRoll = newDice.sum()
//        lastUserRollSum = userRoll
//
//        val potentialUserScore = userScore + userRoll
//        if (potentialUserScore == 50) {
//            userScore = 50
//            winner = "User"
//            isGameOver = true
//            return
//        } else if (potentialUserScore < 50) {
//            userScore = potentialUserScore
//        }
//
//        // 2. Bot rolls using calculatePlayer2()
//        val botRoll = calculatePlayer2()
//        lastBotRoll = botRoll
//
//        val potentialBotScore = botScore + botRoll
//        if (potentialBotScore == 50) {
//            botScore = 50
//            winner = "Bot"
//            isGameOver = true
//            return
//        } else if (potentialBotScore < 50) {
//            botScore = potentialBotScore
//        }
//    }
//
//    fun resetGame() {
//        userScore = 0
//        botScore = 0
//        lastUserRollSum = 0
//        lastBotRoll = 0
//        winner = null
//        isGameOver = false
//        diceValues = List(numDice) { (1..6).random() }
//    }
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(24.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//    ) {
//        Text(
//            text = "Dice Race to 50",
//            fontSize = 30.sp,
//            fontWeight = FontWeight.Bold,
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Scores Row
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceAround,
//        ) {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Text(text = "User Score", fontSize = 16.sp, fontWeight = FontWeight.Medium)
//                Text(
//                    text = "$userScore / 50",
//                    fontSize = 22.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = MaterialTheme.colorScheme.primary,
//                )
//            }
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Text(text = "Bot Score", fontSize = 16.sp, fontWeight = FontWeight.Medium)
//                Text(
//                    text = "$botScore / 50",
//                    fontSize = 22.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = MaterialTheme.colorScheme.error,
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        if (winner != null) {
//            Text(
//                text = "🎉 $winner Wins! 🎉",
//                fontSize = 24.sp,
//                fontWeight = FontWeight.Bold,
//                color = MaterialTheme.colorScheme.tertiary,
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//        } else {
//            Text(
//                text = "Last User Roll: $lastUserRollSum | Last Bot Roll: $lastBotRoll",
//                fontSize = 14.sp,
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//        }
//
//        Text(
//            text = "Number of Dice: $numDice",
//            fontSize = 16.sp,
//            fontWeight = FontWeight.Medium,
//        )
//
//        Slider(
//            value = numDice.toFloat(),
//            onValueChange = { newValue ->
//                val newNum = newValue.toInt().coerceIn(1, 6)
//                if (newNum != numDice) {
//                    numDice = newNum
//                    if (!isGameOver) {
//                        diceValues = List(newNum) { (1..6).random() }
//                    }
//                }
//            },
//            valueRange = 1f..6f,
//            steps = 4,
//            enabled = !isGameOver,
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Grid of dice on screen
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(3),
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp),
//            horizontalArrangement = Arrangement.spacedBy(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp),
//        ) {
//            items(diceValues.size) { index ->
//                val dieValue = diceValues[index]
//                DiceItem(
//                    value = dieValue,
//                    onClick = {
//                        playTurn()
//                    },
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        if (!isGameOver) {
//            Button(
//                onClick = { playTurn() },
//            ) {
//                Text("Roll Dice", fontSize = 18.sp)
//            }
//        } else {
//            Button(
//                onClick = { resetGame() },
//            ) {
//                Text("Play Again", fontSize = 18.sp)
//            }
//        }
//    }
//}
//
//@Composable
//fun DiceItem(
//    value: Int,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    val dieArray = remember {
//        intArrayOf(
//            R.drawable.dice1,
//            R.drawable.dice2,
//            R.drawable.dice3,
//            R.drawable.dice4,
//            R.drawable.dice5,
//            R.drawable.dice6,
//        )
//    }
//
//    val index = value - 1
//    val drawableRes = if (index in dieArray.indices) dieArray[index] else dieArray[0]
//
//    Column(
//        modifier = modifier
//            .size(80.dp)
//            .clickable { onClick() },
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally,
//    ) {
//        Image(
//            painter = painterResource(id = drawableRes),
//            contentDescription = "Die showing $value",
//            modifier = Modifier.size(70.dp),
//        )
//    }
//}
//
//fun calculatePlayer2(): Int {
//    val numDice = (1..4).random()
//    return rollDie(numDice)
//}
//
//fun rollDie(numDice: Int): Int {
//    return if (numDice == 1) {
//        (1..6).random()
//    } else {
//        val diceVal = intArrayOf(1, 1, 1, 1, 2, 3, 4, 5, 6)
//        diceVal[(0..7).random()]
//    }
//}
