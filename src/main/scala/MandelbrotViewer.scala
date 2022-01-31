import java.awt.event.MouseEvent
import swing.{Component, Dimension, Graphics2D}
import swing.event.{Key, KeyPressed, MouseClicked, UIElementResized}

class MandelbrotViewer(
    width: Int,
    height: Int,
    center: Complex = Complex(0, 0),
    scale: Double = 4
) extends Component:

  // Colorization methods to rotate between
  private var colorizers = RainbowColorizer :: BlackWhiteSteppedColorizer :: Nil

  private def rotateColorizer(): Unit =
    colorizers = colorizers.tail :+ colorizers.head

  def currentColorizer: Colorizer = colorizers.head

  preferredSize = new Dimension(width, height)

  var viewport = ComplexViewport(width, height, center, scale)

  // when clicked, re-center and zoom
  listenTo(mouse.clicks)
  reactions += { case e: MouseClicked =>
    val zoomFactor = e.peer.getButton match
      case MouseEvent.BUTTON1 => 2 // zoom in
      case MouseEvent.BUTTON3 => 0.5 // zoom out
      case _                  => 1
    viewport = viewport centerOn (e.point.x, e.point.y) zoomBy zoomFactor
    repaint()
  }

  // switch colorization mode
  listenTo(keys)
  reactions += { case KeyPressed(_, Key.Space, _, _) =>
    rotateColorizer()
    repaint()
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

  override def paintComponent(g: Graphics2D): Unit =
    super.paintComponent(g)
    given Colorizer = currentColorizer
    val img = MandelbrotRenderer.render(viewport)
    g.drawImage(img, 0, 0, null)
