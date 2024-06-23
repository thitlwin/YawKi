import java.util.LinkedList
import java.util.Queue

class PathFindingInGrid {
    private val directions = arrayOf(
        Pair(-1, 0),
        Pair(0, 1),
        Pair(1, 0),
        Pair(0, -1)
    )

    fun shortestPath(grid: Array<IntArray>): Int {
        if (grid[0][0] == 0) {
            return -1
        }
        val n = grid.size
        val queue: Queue<Pair<Int, Int>> = LinkedList()

        // start from the top-left corner
        queue.add(Pair(0, 0))
        grid[0][0] = 0 // mark the starting cell as traversed by setting it to 0

        var steps = 1 // to keep track of the number of steps traversed

        while (queue.isNotEmpty()) {
            repeat(queue.size) { // process all cells in the current level
                // poll the current cell from the queue
                val (row, col) = queue.poll()!!

                if (row == n - 1 && col == n - 1) { // if we have reached to the bottom-right, return the steps
                    return steps
                }

                // explore all 4 directions from the current cell
                for (direction in directions) {
                    val (deltaRow, deltaCol) = direction
                    val newRow = row + deltaRow
                    val newCol = col + deltaCol

                    if (!grid.isIndexOutOfRange(newRow, newCol) && grid[newRow][newCol] == 1) {
                        queue.add(Pair(newRow, newCol))
                        grid[newRow][newCol] = 0
                    }
                }
            }
            steps++
        }
        return -1
    }
}

private fun <T> Array<T>.isIndexOutOfRange(newRow: Int, newCol: Int): Boolean {
    return newRow !in indices || newCol !in indices
}