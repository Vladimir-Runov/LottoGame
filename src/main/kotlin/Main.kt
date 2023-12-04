import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.launch


//@OptIn(ObsoleteCoroutinesApi::class)
@OptIn(ObsoleteCoroutinesApi::class, DelicateCoroutinesApi::class)
fun main(): Unit = runBlocking {
    val numPlayers = 114
    val numCardsPerPlayer = 22
    var winnedId  = -1
    var playerChannels = mutableListOf(Channel<GameProcessingEvent>())
    val bag = BarrelBag(90)

    // крупье или ведущий игы... тот, кто достает бочонки
    val arbitrator = actor<GameProcessingEvent> {
        var counter = 0
        var round = 0
        var gameFinish = false
        for (message in channel) {
            //println(">> A Msg:" + message.name)
            when (message) {
                is GameProcessingEvent.Ready -> {
                    counter += 1
                    if (counter >= numPlayers) {
                        counter = 0
                        println("A: All players are ready ")
                        println("A: Мешок { " + bag.displString() + "}")
                        playerChannels.forEach {
                            delay(100)
                            it.trySend(GameProcessingEvent.LetsStart)
                        }
                    }
                }
                is GameProcessingEvent.LetsStart -> {
                    counter += 1
                    if (counter >= numPlayers) {
                        counter = 0
                        round += 1;
                        val numBarrel = bag.pop()
                        println("A: *** Раунд ${round}. номер: [$numBarrel] в мешке {" + bag.displString() + "}")
                        playerChannels.forEach {
                            delay(10)
                            it.trySend(GameProcessingEvent.Check(numBarrel, round))
                        }
                    }
                }

                is GameProcessingEvent.CheckResult -> {
                    counter += 1
                    if (message.win) {
                        gameFinish = true
                        println("A: -------------------------------------------------------")
                        println("A: *****    на раунде №${round} выиграл игрок # ${message.id}   (вся карточка заполнена) на бочонке с номером # ${message.num} ")
                        println("A: -------------------------------------------------------")
                        winnedId = message.id;
                    }
                    if (bag.isEmpty()) {
                        if (!gameFinish) {
                            println("A: Мешок пуст!! ...")
                            gameFinish = true
                        }
                    }

                    if (counter >= numPlayers) {
                        counter = 0
                        if (gameFinish) {
                            playerChannels.forEach {
                                delay(10)
                                it.trySend(GameProcessingEvent.GameOver)      /// ????  не работает если send!
                            }
                        } else if (! bag.isEmpty()) {
                            round += 1;
                            val numBarrel = bag.pop()
                            println("A: *** Раунд ${round}. НОМЕР: [$numBarrel] в мешке: {" + bag.displString() + "}")
                            playerChannels.forEach {
                                delay(10)
                                it.trySend(GameProcessingEvent.Check(numBarrel, round))
                            }
                        }
                    }
                }
                is GameProcessingEvent.Check -> TODO()
                is GameProcessingEvent.GameOver -> {
                    counter += 1
                   // println("... игрок № $counter вышел из игры." )
                    if (counter >= numPlayers) {
                        //println("Actor...  closing")
                        channel.close()
                        cancel()
                    }
                }
                else -> { }
            }  // mes
            //println("Actor for ...")
        }
        //println("Actor exit...")
    }

    val jobList = mutableListOf<Job>()
        repeat(numPlayers) { playerId ->
            val job = launch {
                println("   === Игрок $playerId и его $numCardsPerPlayer карточки:")
                val lottoCards = mutableListOf<LottoCard>()
                repeat(numCardsPerPlayer) {
                    lottoCards.add(LottoCard())
                }
                lottoCards.forEach{
                    it.debugPrint()
                    println("")
                }

                val channel = Channel<GameProcessingEvent>()
                playerChannels.add(channel)
                //println("Thread $playerId init")
                arbitrator.send(GameProcessingEvent.Ready(playerId))

                for (message in channel) {
                    //println(">> L $playerId Msg:" + message.name)
                    when (message) {
                        is GameProcessingEvent.Ready -> {
                            //println(">>Thread $playerId Ready ...")
                            delay(100L) // non
                        }

                        is GameProcessingEvent.LetsStart -> {
                            delay(100)
                            arbitrator.send(GameProcessingEvent.LetsStart)
                        }

                        is GameProcessingEvent.Check -> {
                            //println("   in Round ${message.round} Player ($playerId) Checking ${message.num} ")
                            var marked = false // игрок что то закрыл в своиз карточках..... номе у него есть (среди карт)
                            var playerWin = false
                            for (card in lottoCards) {
                                val numsStr = card.displString()
                                if (card.checkNum(message.num)) {
                                    marked = true
                                   // println("     номер [${message.num}] закрылся на карт. : {" + numsStr + "}")
                                }
                                if(card.cardWin) {
                                    playerWin = true
                                    println("     карточка выиграла (все номера закрыты)")
                                    card.debugPrint()
                                }
                            }
                            if (marked)
                                arbitrator.send(GameProcessingEvent.CheckResult(playerId, message.num, playerWin))
                            else
                                arbitrator.send(GameProcessingEvent.CheckResult(playerId, message.num, playerWin))

                        }

                        is GameProcessingEvent.CheckResult -> {}

                        is GameProcessingEvent.GameOver -> {
                            arbitrator.send(GameProcessingEvent.GameOver)  //  попрощастья с раздающим
                            playerChannels[playerId].close()
                            cancel()
                        }
                    }
                }
            }
            jobList.add(job)
    }

    jobList.forEach{ it.join() }
    //
    //jobList.forEach{ println(" isActive -${it.isActive} isCancelled- ${it.isCancelled} isCompleted-${it.isCompleted}") }
    //println("main bye..")
}
