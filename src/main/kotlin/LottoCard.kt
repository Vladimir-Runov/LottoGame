class LottoCard {
    // карточка, 3 строки по 9 ячеек, где =5 чисел в каждой строке, т.е. всего 15 уникальных чиселов 1...90
    private var arrLines = Array(3) { Array(9) { mutableMapOf<Int, Boolean>() } }
    private val matrix = Array(3) { IntArray(9) }
    private var nums = IntArray(15)


    init {
        nums = generateNumbers()
        do {
            for (x in nums.indices) {
                var xs: Int = nums[x] / 10
                if (xs == 9)
                    xs = 8

                for (i in (0..2).shuffled()) {
                    if (matrix[i].filter { it > 0 }.count() >= 5)
                        continue

                    if (matrix[i][xs] == 0) {
                        matrix[i][xs] = nums[x]
                        break
                    }
                }
            }
            if (matrix[0].filter { it > 0 }.count() == 5 &&
                matrix[1].filter { it > 0 }.count() == 5 &&
                matrix[2].filter { it > 0 }.count() == 5 )
                break
            // ошибка распределения
            for (i in matrix.indices)
                for (j in matrix.indices)
                    matrix[i][j] = 0
        } while (true)
    }
    var cardWin:Boolean = false
        get() = (nums.count() == 0)
    fun checkNum(num:Int) : Boolean {
        if (num in nums) {
            nums = nums.filter { it != num }.toIntArray()   // удаляе номер из пула для ускорения след. шага.
            return true     // он там был
        }
        return false
    }
    fun displString() : String = nums.joinToString(", ") { it.toString() }

    private fun generateNumbers(): IntArray {
        val numbers = mutableListOf<Int>()
        for (i in 1..9) {
            val range = (i * 10 - 9)..(i * 10)
            var x1 = 9  // граница диапазона для всех чисел кроме 80-х
            if(i == 9 )
                x1 = 10   // чтобы в сегменте 80-х появилось и 90
            val uniqueNumbers = (0..x1).shuffled().take(2)
            for (number in uniqueNumbers) {
                numbers.add(range.first + number - 1)
            }
        }
        return numbers.filter{ it in 1..90}.shuffled().take(15).toIntArray()
    }
    fun debugPrint() {
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if(matrix[i][j] == 0)
                    print(".. ")
                else if(matrix[i][j] < 10)
                    print(String.format("%1d  ", matrix[i][j]))
                else
                    print(String.format("%2d ", matrix[i][j]))
            }
            println("   ") //  s=${countVals(matrix[i])} ")
        }
    }
    private fun countVals(array: IntArray): Int {
        var count = 0
        for (i in array.indices) {
            if (array[i] > 0) {
                count++
            }
        }
        return count
    }

}




/*
*         nums = generateNumbers()
        for (x in nums.indices) {
            var xs: Int = nums[x] / 10
            if (xs == 9)
                xs = 8
            var v1 = nums[x]
            for (i in (0..2).shuffled()) {
                print( "row: $i V = ${nums[x]} xs: $xs ")
                if (matrix[i].filter { it > 0 }.count() != countVals(matrix[i]))
                    println( "error!")

                if (matrix[i].filter { it > 0 }.count() >= 5 ) {
                    println("cont")
                    continue
                }
                if (matrix[i][xs] == 0) {
                    matrix[i][xs] = nums[x]
                    v1 = 0
                    println( "break")
                    break
                } else {
                    println("...? $i ${matrix[i][xs]}")
                }
            }
            if (v1 != 0) {
                if (matrix[0].filter { it > 0 }.count() != 5) {}
                if (matrix[1].filter { it > 0 }.count() != 5) {}
                if (matrix[2].filter { it > 0 }.count() != 5) {}
            }
        }
        if (matrix[0].filter { it > 0 }.count() != 5 ||
            matrix[1].filter { it > 0 }.count() != 5 ||
            matrix[2].filter { it > 0 }.count() != 5 ) {
            println( "error2")
        }
    fun getStr(i:Int): String {
        var r : String = ""
        for (j in matrix[i].indices) {
            r = r + matrix[i][j].toString() + ", ";
        }
        return r
    }
*
* */