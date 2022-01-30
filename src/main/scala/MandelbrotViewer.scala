import swing.{Component, Dimension, Graphics2D}
import swing.event.{MouseClicked, UIElementResized}

class MandelbrotViewer(
    width: Int,
    height: Int,
    center: Complex = Complex(0, 0),
    scale: Double = 4
) extends Component:

  val LeftMouseButton = 1
  val RightMouseButton = 3

  preferredSize = new Dimension(width, height)
  var viewport = ComplexViewport(width, height, center, scale)

  // when clicked, re-center and zoom
  listenTo(mouse.clicks)
  reactions += { case e: MouseClicked =>
    val zoomFactor = e.peer.getButton match
      case LeftMouseButton  => 2 // zoom in
      case RightMouseButton => 0.5 // zoom out
      case _                => 1
    viewport = viewport centerOn (e.point.x, e.point.y) zoomBy zoomFactor
    repaint()
  }

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
    val img = ColorfulMandelbrotRenderer.render(viewport)
    g.drawImage(img, 0, 0, null)
