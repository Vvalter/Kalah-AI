package info.kwarc.teaching.AI.Kalah

import scala.util.Random
import collection.JavaConverters._

/**
  * The abstract class for agents. Needs to implement four things:
  *
  * name     : What you call your agent (just for fun). Please make this one fixed (unlike in the example
  *                   below)
  * students : Please put your full names in this list.
  */
abstract class Agent {
  val name : String
  val students : java.lang.Iterable[String]
  implicit val me = this
  /**
    * is called once at the start of a game.
    * @param board The [[Board]] used (states in particular the parameters)
    * @param playerOne is true iff this agent is playerOne in the current game
    */
  def init(board : Board, playerOne : Boolean)

  /**
    * This method is called by [[Game]] to request an action. Note that you have <b>at most</b> 5sec to
    * return an action; otherwise the thread is aborted and the timeoutMove variable (by default=1) is chosen.
    * So you can use the full 5 seconds to constantly revise that variable.
    */
  def move : Int
  @volatile var timeoutMove : Int = 1
}

/**
  * A human player that reads actions (as field numbers) from stdin. Can be used to test your agents.
  * @param name     : What you call your agent (just for fun).
  */
class HumanPlayer(val name : String) extends Agent {
  val students = List("Dennis").asJava
  private var currentboard : Board = null
  private var playerone = None.asInstanceOf[Option[Boolean]]

  def init(board : Board, playerOne : Boolean): Unit = {
    currentboard = board
    playerone = Some(playerOne)
    println("Initializing for " + name)
    println("Playing Kalah(" + board.houses + "," + board.initSeeds + ")")
  }

  def move : Int = {
    println("Your move, " + name)
    println(currentboard.asString(this))
    readLine("Enter house: ").toInt
  }
}

class RandomPlayer(val name : String) extends Agent {
  val students = List("Dennis").asJava
  private var currentboard : Board = null

  def init(board : Board, playerOne : Boolean): Unit = {
    currentboard = board
  }
  def move : Int = {
    val ls = currentboard.getHouses
    val rnd = new Random
    var i = rnd.nextInt(ls.asScala.size /* + 1*/)
    while (/* i > 0 && */ ls.asScala.toList(i /* -1 */ ) == 0) {
      i = rnd.nextInt(ls.asScala.size /* + 1 */)
    }
    /*
    if (i == 0) {
      Thread.sleep(10000)
      0
    } else */
    timeoutMove = i + 1
    i + 1
  }
}

class TimeOut extends Agent/*("Timeouter",List("Dennis"))*/ {
  val name = "Timeouter"
  val students = List("Dennis").asJava
  private var currentboard : Board = null

  def init(board : Board, playerOne : Boolean): Unit = {
    currentboard = board
    Thread.sleep(11000)
    println("Woke up!")
  }
  def move : Int = {
    Thread.sleep(8000)
    println("Woke up!")
    0
  }
}