import java.awt.Color
import java.awt.image.BufferedImage

/**
 * Helper class to represent, and modify, a viewport that determines which part of the complex pane gets rendered to
 * screen
 * @param width Width of the image to be rendered
 * @param height Height of the image to be rendered
 * @param topLeft Complex number at (0, 0) top-left point in image
 * @param scale Top-right point in image is (topLeft.re + scale, topLeft.im)
 */
case class ComplexViewport(width: Int, height: Int, topLeft: Complex, scale: Double) {

  /** Convert from image space to represented complex number */
  def imgSpaceToComplex(x: Int, y: Int): Complex = {
    Complex(topLeft.re + scale * x / width, topLeft.im + scale * y / height * (width / height))
  }

  /** Create a new viewport centered around (x, y), and zoomed in by `zoomFactor` */
  def zoomInOn(x: Int, y: Int, zoomFactor: Double): ComplexViewport = {
    val newCenter = imgSpaceToComplex(x, y)
    val newScale = scale * zoomFactor
    val newTopLeft = Complex(newCenter.re - newScale / 2f, newCenter.im - newScale / 2f * (height / width))
    ComplexViewport(width, height, newTopLeft, newScale)
  }
}

object MandelbrotRenderer {

  /**
   * Compute the color that a complex number should be visualized with.
   * @param steps `None` if number is in Mandelbrot set, number of steps it took to exit sphere with radius 2 otherwise
   * @return Color to visualize this number with
   */
  def getColor(steps: Option[Int]): Color = {
    steps match {
      case Some(_) => Color.BLACK
      case None => Color.WHITE
    }
  }

  /**
   * Render a Mandelbrot set into a `BufferedImage`
   * @return Hopefully a very pretty image of a Mandelbrot set
   */
  def render(viewport: ComplexViewport): BufferedImage = {
    val img = new BufferedImage(viewport.width, viewport.height, BufferedImage.TYPE_INT_RGB)

    for (x <- 0 until img.getWidth()) {
      for (y <- 0 until img.getHeight()) {
        val c = viewport.imgSpaceToComplex(x, y)
        val steps = MandelbrotSet.iterate(c)
        val color = getColor(steps)
        img.setRGB(x, y, color.getRGB)
      }
    }

    img
  }
}
