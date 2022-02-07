import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import swing.{Component, Dimension, Graphics2D}
import swing.event.{Key, KeyPressed, MousePressed, UIElementResized}

class MandelbrotViewer(
    width: Int,
    height: Int,
    center: Complex = Complex.origin,
    scale: Double = 4
) extends Component:

  final val ScreenshotScale = 5.0

  // Colorization methods to rotate between
  private var colorizers =
    EscapeTimeColorizer :: OriginOrbitTrapColorizer :: QuadrantOrbitTrapColorizer :: PickoverStalksColorizer :: Nil

  private def rotateColorizer(): Unit =
    colorizers = colorizers.tail :+ colorizers.head

  def currentColorizer: Colorizer = colorizers.head

  preferredSize = new Dimension(width, height)

  var viewport = ComplexViewport(width, height, center, scale)

  // when clicked, re-center and zoom
  listenTo(mouse.clicks)
  reactions += { case e: MousePressed =>
    val zoomFactor = e.peer.getButton match
      case MouseEvent.BUTTON1 => 2 // zoom in
      case MouseEvent.BUTTON3 => 0.5 // zoom out
      case _                  => 1
    viewport = viewport centerOn (e.point.x, e.point.y) zoomBy zoomFactor
    repaint()
  }

  // switch colorization mode
  listenTo(keys)
  reactions += {
    case KeyPressed(_, Key.Space, _, _) =>
      rotateColorizer()
      repaint()
    case KeyPressed(_, Key.Enter, _, _) =>
      saveImage()
    case KeyPressed(_, Key.Q, _, _) =>
      System.exit(0)
  }

  // necessary to capture key events
  focusable = true
  requestFocus()

  // handle resizing
  listenTo(this)
  reactions += { case e: UIElementResized =>
    viewport = ComplexViewport(
      this.size.width,
      this.size.height,
      viewport.center,
      viewport.scale
    )
    repaint()
  }

  private def render(viewport: ComplexViewport): BufferedImage =
    given Colorizer = currentColorizer
    val before = System.nanoTime
    val img = MandelbrotRenderer.render(viewport)
    val after = System.nanoTime
    println(
      s"Rendered using ${currentColorizer.getClass.getSimpleName} in ${(after - before) / 1e6} ms"
    )
    img

  override def paintComponent(g: Graphics2D): Unit =
    super.paintComponent(g)
    val img = render(viewport)
    g.drawImage(img, 0, 0, null)

  private def nextFreeFile(num: Int = 0): File =
    val file = File(f"img${num}%03d.png")
    if file.exists then nextFreeFile(num + 1) else file

  private def saveImage(): Unit =
    println("Rendering …")
    val img = render(viewport scaleBy ScreenshotScale)

    val file = nextFreeFile()
    file.createNewFile()
    ImageIO.write(img, "png", file)
    println(s"Saved screenshot to ${file.getPath}")
