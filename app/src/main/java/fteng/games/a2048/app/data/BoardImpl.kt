package board

import board.Direction.*

class CellImp(override val i: Int, override val j: Int) : Cell {

}

class SquareBoardImply(width: Int) : SquareBoard {
    override val width: Int = width
    val cells: Array<Array<CellImp>> = Array(width, { Array(width, { CellImp(0, 0) }) })

    init {
        for (i in 1..width) {
            for (j in 1..width) {
                var cell = CellImp(i, j)
                cells[i - 1][j - 1] = cell
            }
        }
    }

    fun validIndex(i: Int, j: Int) = !(i > width || j > width || i < 1 || j < 1)

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        if (!validIndex(i, j)) {
            return null
        }
        return cells[i - 1][j - 1]
    }

    override fun getCell(i: Int, j: Int): Cell {
        if (!validIndex(i, j)) {
            throw IllegalArgumentException()
        }
        return cells[i - 1][j - 1]
    }

    override fun getAllCells(): Collection<Cell> {
        var ret: MutableList<Cell> = mutableListOf()
        for (i in 1..width) {
            ret.addAll(getRow(i, 1..width))
        }
        return ret
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        var ret: MutableList<Cell> = mutableListOf()
        if (!validIndex(i, jRange.first) && validIndex(i, jRange.last)) {
            return ret
        }
        for (j in jRange) {
            if (getCellOrNull(i, j) != null) ret.add(getCell(i, j))
        }
        return ret
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        var ret: MutableList<Cell> = mutableListOf()
        if (!validIndex(iRange.first, j) && validIndex(iRange.last, j)) {
            return ret
        }
        for (i in iRange) {
            if (getCellOrNull(i, j) != null) ret.add(getCell(i, j))
        }
        return ret
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        var iNew = i
        var jNew = j
        when (direction) {
            UP -> iNew--
            DOWN -> iNew++
            LEFT -> jNew--
            RIGHT -> jNew++
        }

        return getCellOrNull(iNew, jNew)
    }

}

class GameBoardImpl<T>(val squareBoard: SquareBoard) : GameBoard<T>, SquareBoard by squareBoard {

    val map: HashMap<CellImp, T?> = HashMap()

    var CellImp.v: T?
        get() {
            return map.get(this)
        }
        set(value: T?) {
            map.set(this, value)
        }

    init {
        squareBoard.getAllCells().forEach { map.put(it as CellImp, null) }
    }

    override fun get(cell: Cell): T? {
        if (cell is CellImp) {
            return cell.v
        }
        return null
    }

    override fun set(cell: Cell, value: T?) {
        if (cell is CellImp) {
            cell.v = value
        }
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return map.filterValues(predicate).keys
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
       return map.filterValues(predicate).keys.first()
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return !map.filterValues(predicate).keys.isEmpty()
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return map.filterValues(predicate).size == map.size
    }

}

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImply(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(SquareBoardImply(width))

