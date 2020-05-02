import java.awt.image.BufferedImage
import scala.util.Random

import swing._

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
      val opts = options.get
      val width = opts(Symbol("width")).asInstanceOf[Int]
      val height = opts(Symbol("height")).asInstanceOf[Int]

      val top = new MainFrame {
        title = "Scandelbrot"
        contents = new Mandelbrot(width, height)
      }
      top.pack()
      top.visible = true
    }
  }

}

class Mandelbrot(width: Int, height: Int) extends Component {

  preferredSize = new Dimension(width, height)

  override def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g)
    val img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

    val rand = new Random()
    def randomColor(): Int = {
      val r = rand.nextInt(256)
      val g = rand.nextInt(256)
      val b = rand.nextInt(256)
      r * 256 * 256 + g * 256 + b
    }

    for (x <- 0 until img.getWidth()) {
      for (y <- 0 until img.getHeight()) {
        img.setRGB(x, y, randomColor())
      }
    }

    g.drawImage(img, 0, 0, null)
  }
}