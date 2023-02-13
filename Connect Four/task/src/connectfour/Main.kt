package connectfour

fun getaddIdex(list: MutableList<MutableList<String>>, move: Int): Int {
    for (i in 0 until list.size) {
        if (list.get(i).get(move) == " ") {
            return i;
        }
    }
    return -1
}

val leftcorner = '\u255A'
val rightcorner = '\u255D'
val horizontal = '\u2550'
val vertical = '\u2551'
val corner = '\u2569'

var mutableList = mutableListOf<MutableList<String>>()
var turn = true
fun main() {


    val (firstPlayer, secondPlayer) = getPlayerNames()

    val (boardRow, boardColumn) = setBoardSize()

    val numberOfGames = setNumberOfGames()

    startInfo(firstPlayer.name, secondPlayer.name, boardRow, boardColumn, numberOfGames)

    for (gameNumber in 1..numberOfGames) {
        turn = gameNumber % 2 == 1
        if (numberOfGames > 1) {
            println("Game #$gameNumber")
        }
        printEmptyBoard(boardColumn, boardRow)

        createList(boardRow, boardColumn)

        gameLogic(firstPlayer, secondPlayer, boardColumn, boardRow)
        mutableList.clear()
        if (numberOfGames > 1) {
            println("Score")
            println("${firstPlayer.name}: ${firstPlayer.score} ${secondPlayer.name}: ${secondPlayer.score}")
        }
    }
    println("Game Over!")


}

private fun gameLogic(
    firstPlayer: Player,
    secondPlayer: Player,
    boardColumn: Int,
    boardRow: Int
) {
    do {
        if (turn) {
            println("${firstPlayer.name}'s turn:")

        } else {
            println("${secondPlayer.name}'s turn:")
        }
        val n = readln()
        if (n == "end") {
            continue
        }
        val move = n.toIntOrNull()
        if (move != null) {
            if (move > boardColumn || move < 1) {
                println("The column number is out of range (1 - $boardColumn)")

                continue
            }
        } else {
            println("Incorrect column number")

            continue
        }

        val index: Int = getaddIdex(mutableList, move - 1)

        if (index != -1) {
            if (turn) {

                mutableList[index][move - 1] = "o"
            } else {
                mutableList[index][move - 1] = "*"
            }

        } else {
            println("Column $move is full")
            continue
        }

        for (i in 1..boardColumn) {
            print(" $i")
        }
        println()



        for (i in boardRow downTo 1) {
            for (j in 1..boardColumn) {
                if (mutableList.get(i - 1).size > j - 1) {
                    print("$vertical${mutableList.get(i - 1).get(j - 1)}")
                } else {
                    print("$vertical ")
                }
            }
            println(vertical)
        }

        for (i in 1..boardColumn) {
            if (i == 1) {
                print("$leftcorner$horizontal")
            }
            if (i != 1) {
                print("$corner$horizontal")
            }
        }
        println(rightcorner)


        val winningPlayer = winningCondition(mutableList, firstPlayer, secondPlayer)

        if (winningPlayer != null) {
            println("Player ${winningPlayer.name} won")
            winningPlayer.score += 2
            break
        }

        if (checkDrawCondition(boardRow, boardColumn, mutableList)) {
            println("It is a draw")
            firstPlayer.score += 1
            secondPlayer.score += 1
            break
        }

        turn = !turn
    } while (n != "end")
}

private fun startInfo(
    firstPlayerName: String,
    secondPlayerName: String,
    boardRow: Int,
    boardColumn: Int,
    numberOfGames: Int
) {
    println(
        "$firstPlayerName VS $secondPlayerName\n" +
                "$boardRow X $boardColumn board"
    )
    if (numberOfGames > 1) {
        println("Total $numberOfGames games")
    } else {
        println("Single game")
    }
}

fun setNumberOfGames(): Int {
    var isSet = false
    var numberOfGames = 1
    while (!isSet) {
        println("Do you want to play single or multiple games?")
        println("For a single game, input 1 or press Enter")
        println("Input a number of games:")
        val line = readln()
        if (line == "") {
            numberOfGames = 1
            isSet = true
        } else {
            val number = line.toIntOrNull()
            if (number != null) {
                if (number > 0) {
                    isSet = true
                    numberOfGames = number
                } else {
                    println("Invalid input")
                }
            } else {
                println("Invalid input")
            }
        }
    }
    return numberOfGames
}

private fun createList(boardRow: Int, boardColumn: Int) {
    repeat(boardRow) {
        // `row` is a new row in the array
        mutableList.add(mutableListOf<String>())
    }



    for (i in 0 until boardRow) {
        for (j in 0 until boardColumn) {
            mutableList.get(i).add(" ")
        }
    }
}

private fun printEmptyBoard(boardColumn: Int, boardRow: Int) {
    for (i in 1..boardColumn) {
        print(" $i")
    }
    println()

    for (j in 1..boardRow) {
        for (i in 1..boardColumn) {
            print("$vertical ")
        }
        println(vertical)
    }

    for (i in 1..boardColumn) {
        if (i == 1) {
            print("$leftcorner$horizontal")
        }
        if (i != 1) {
            print("$corner$horizontal")
        }
    }
    println(rightcorner)
}

private fun setBoardSize(): Pair<Int, Int> {
    var boardRow = 6
    var boardColumn = 7

    var isBoardSizeSet = false
    val regex = Regex("\\d+[xX]\\d+")

    var boardSizeVal: String

    while (!isBoardSizeSet) {
        println("Set the board dimensions (Rows x Columns)")
        println("Press Enter for default (6 x 7)")
        boardSizeVal = readln().filter { !it.isWhitespace() }
        if (regex.matches(boardSizeVal) || boardSizeVal.isEmpty()) {
            if (boardSizeVal.isNotEmpty()) {
                boardRow = boardSizeVal.first().toString().toInt()
                boardColumn = boardSizeVal.last().toString().toInt()
            }
            if (boardRow < 5 || boardRow > 9) {
                println("Board rows should be from 5 to 9")
                continue
            }
            if (boardColumn < 5 || boardColumn > 9) {
                println("Board columns should be from 5 to 9")
                continue
            }
            isBoardSizeSet = true
        } else {
            println("Invalid Input")
        }


    }

    return Pair(boardRow, boardColumn)
}

private fun getPlayerNames(): Pair<Player, Player> {
    println("Connect Four")
    println("First player's name:")
    val firstPlayerName = readln().toString()
    val firstPlayer = Player(firstPlayerName)
    println("Second player's name:")
    val secondPlayerName = readln().toString()
    val secondPlayer = Player(secondPlayerName)
    return Pair(firstPlayer, secondPlayer)
}


private fun winningCondition(
    mutableList: MutableList<MutableList<String>>,
    firstPlayerName: Player,
    secondPlayerName: Player
): Player? {

    val row = mutableList.size
    val column = mutableList.get(0).size

    for (i in 0 until row) {
        for (j in 0 until column) {
            val look = mutableList.get(i).get(j)
            if (look == " ") {
                continue
            }
            if (j + 3 < column && mutableList.get(i)
                    .get(j + 1) == look && mutableList.get(i).get(j + 2) == look && mutableList.get(i)
                    .get(j + 3) == look
            ) {
                return getWinningPlayerName(look, firstPlayerName, secondPlayerName)
            }
            if (i + 3 < row) {
                if (mutableList.get(i + 1).get(j) == look && mutableList.get(i + 2)
                        .get(j) == look && mutableList.get(i + 3).get(j) == look
                ) {
                    return getWinningPlayerName(look, firstPlayerName, secondPlayerName)
                }
                if (j + 3 < column && mutableList.get(i + 1).get(j + 1) == look && mutableList.get(i + 2)
                        .get(j + 2) == look && mutableList.get(i + 3).get(j + 3) == look
                ) {
                    return getWinningPlayerName(look, firstPlayerName, secondPlayerName)
                }
                if (j - 3 >= 0 && mutableList.get(i + 1).get(j - 1) == look && mutableList.get(i + 2)
                        .get(j - 2) == look && mutableList.get(i + 3).get(j - 3) == look
                ) {
                    return getWinningPlayerName(look, firstPlayerName, secondPlayerName)
                }
            }
        }
    }
    return null
}


private fun getWinningPlayerName(lookValue: String, firstPlayer: Player, secondPlayer: Player): Player {
    if (lookValue == "o") {
        return firstPlayer
    } else {
        return secondPlayer
    }
}

private fun checkDrawCondition(
    boardRow: Int,
    boardColumn: Int,
    mutableList: MutableList<MutableList<String>>
): Boolean {
    var fullColumn = 0
    for (i in 1..boardColumn) {
        if (mutableList.get(boardRow - 1).get(i - 1) != " ") {
            fullColumn++
        }
    }

    if (fullColumn == boardColumn) {
        return true
    }
    return false
}

class Player(var name: String) {
    var score: Int = 0
}