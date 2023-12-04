// мешок бочонков
class BarrelBag(val size:Int = 90) {
    private val stack = mutableListOf<Int>()

    init {
        val nums = IntArray(size) { it + 1 }
//        nums.forEach { print( "$it ") }
//        println("")
        nums.shuffle()

        nums.forEach { stack.add(it) }
    }
    // достать бочонок из мешка
    fun pop(): Int  {
        return when (stack.isEmpty()) {
            true -> 0
            false -> stack.removeLast()
        }
    }
    fun isEmpty(): Boolean {
        return stack.isEmpty()
    }
    fun displString() : String = stack.joinToString(", ") { it.toString() }

}