import swing._

object Main {

  type OptionMap = Map[Symbol, Any]

  val defaults: OptionMap = Map(
    Symbol("width") -> 800,
    Symbol("height") -> 800
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
      val opts = options.get
      val width = opts(Symbol("width")).asInstanceOf[Int]
      val height = opts(Symbol("height")).asInstanceOf[Int]

      val top = new MainFrame {
        title = "Scandelbrot"
        contents = new MandelbrotViewer(width, height)
      }
      top.pack()
      top.visible = true
    }
  }

}

class MandelbrotViewer(var width: Int, var height: Int, var topLeft: Complex=Complex(-2, -2), var scale: Double=4) extends Component {

  preferredSize = new Dimension(width, height)

  override def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g)
    val img = MandelbrotRenderer.render(width, height, topLeft, scale)
    g.drawImage(img, 0, 0, null)
  }
}