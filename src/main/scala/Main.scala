import swing._
import swing.event.MouseClicked

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
        contents = new MandelbrotViewer(ComplexViewport(width, height, Complex(-2, -2), 4))
      }
      top.pack()
      top.visible = true
    }
  }

}

class MandelbrotViewer(var viewport: ComplexViewport) extends Component {

  preferredSize = new Dimension(viewport.width, viewport.height)

  listenTo(mouse.clicks)
  reactions += {
    case e: MouseClicked => {
      viewport = viewport.zoomInOn(e.point.x, e.point.y, 0.5)
      repaint()
    }
  }

  override def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g)
    val img = ColorfulMandelbrotRenderer.render(viewport)
    g.drawImage(img, 0, 0, null)
  }
}