import FileIO.FileIO
import model.GameField

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class FileIOStub extends FileIO:
  var saveCalls: List[(GameField, String)] = List()
  var loadCalls: List[String] = List()
  var getTargetsResult: List[String] = List()

  def save(gameField: GameField, target: String): Unit =
    saveCalls = (gameField, target) :: saveCalls

  def load(source: String): GameField =
    loadCalls = source :: loadCalls
    GameField.init()
    

  def getTargets: List[String] =
    getTargetsResult
