object Main {

  type OptionMap = Map[Symbol, Any]

  val defaults: OptionMap = Map(
    Symbol("width") -> 800,
    Symbol("height") -> 600
  )

  val usage: String = "Usage: [--width INT] [--height INT]"

  def parseOptions(args: List[String], defaults: OptionMap = Map()): OptionMap = {
    args match {
      case Nil => defaults
      case "--width" :: value :: tail => parseOptions(tail, defaults) ++ Map(Symbol("width") -> value.toInt)
      case "--height" :: value :: tail => parseOptions(tail, defaults) ++ Map(Symbol("height") -> value.toInt)
      case option :: _ => throw new IllegalArgumentException("Unknown option " + option)
    }
  }

  def main(args: Array[String]): Unit = {
    var options: Option[OptionMap] = None
    try {
      options = Some(parseOptions(args.toList, defaults))
    } catch {
      case e: IllegalArgumentException => print(usage); System.exit(1)
    } finally {
      println(options)
    }
  }

}
