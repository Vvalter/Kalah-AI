package info.kwarc.teaching.AI.Kalah

import scala.collection.mutable
import info.kwarc.teaching.AI.Kalah.WS1617.agents.SuperAgent;

/**
  * Created by jazzpirate on 08.11.16.
  */
object Test {
  def main(args: Array[String]): Unit = {
//    val game = new Game(new SuperAgent(), new SuperAgent())(3, 1)
//    val (sc1, sc2) = game.play(true)
//    println("Score Player 1: " + sc1)
    /*
    val tn = new Tournament
    val ret = tn.run(6,6)
    ret.indices.foreach(i => println(i + ": " + ret(i)._1 + "(" + ret(i)._2 + ")"))
    */
  }
}

class Tournament {

  def players: List[Agent] = List(
    new RandomPlayer("R1"),
    new RandomPlayer("R2"),
    new RandomPlayer("R3"),
    new TimeOut
  )

  val scores = mutable.HashMap(players.map(p => (p.name, 0)): _*)

  def run(houses: Int, seeds : Int, showboard : Boolean = false) = {
    players foreach (p => {
      players foreach (q => if (p.name!=q.name) {
        println(p.name + " vs. " + q.name)
        val result = (new Game(p,q)(houses,seeds)).play(showboard)
        if (result._1 > result._2) {
          println(p.name + " wins!")
          scores(p.name) += houses
        }
        else if (result._2 > result._1) {
          println(q.name + " wins!")
          scores(q.name) += houses
        }
      })
    })
    scores.toList.sortBy(_._2).reverse
  }
}
