sealed class GameProcessingEvent(val id:Int, val name:String ) {
    class Ready(var idx: Int) : GameProcessingEvent(idx,"ready") {}
    object LetsStart: GameProcessingEvent(0, "let's -start") {  }
    object GameOver : GameProcessingEvent(0, "game is over") {  }
    class Check(val num:Int, val round:Int) : GameProcessingEvent(0, "check number") {  }
    class CheckResult(idx: Int, val num:Int, val win: Boolean) : GameProcessingEvent(idx, "answer of checking") {  }
}
