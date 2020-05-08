import java.awt.Color
import java.awt.image.BufferedImage

/**
 * Helper class to represent, and modify, a viewport that determines which part of the complex pane gets rendered to
 * screen
 * @param width Width of the image to be rendered
 * @param height Height of the image to be rendered
 * @param center Complex number at center of image
 * @param scale Top-right point in image is (topLeft.re + scale, topLeft.im)
 */
case class ComplexViewport(width: Int, height: Int, center: Complex, scale: Double) {

  /**
   * What magnitude on the imaginary axis does `height` scale to?
   */
  def verticalScale: Double = {
    scale * height / width
  }

  /** Convert from image space to represented complex number */
  def imgSpaceToComplex(x: Int, y: Int): Complex = {
    val topLeft = Complex(center.re - scale / 2f, center.im - verticalScale / 2f)
    Complex(topLeft.re + scale * x / width, topLeft.im + verticalScale * y / height)
  }

  /** Create a new viewport zoomed in by `factor` */
  def zoomBy(factor: Double): ComplexViewport = {
    val newScale = scale / factor
    ComplexViewport(width, height, center, newScale)
  }

  /** Create a new viewport centered around (x, y) */
  def centerOn(x: Int, y: Int): ComplexViewport = {
    val newCenter = imgSpaceToComplex(x, y)
    this centerOn newCenter
  }

  /** Create a new viewport centered around `newCenter` */
  def centerOn(newCenter: Complex): ComplexViewport = {
    ComplexViewport(width, height, newCenter, scale)
  }

}

abstract class MandelbrotRenderer {

  /**
   * Compute the color that a complex number should be visualized with.
   * @param steps `None` if number is in Mandelbrot set, number of steps it took to exit sphere with radius 2 otherwise
   * @return Color to visualize this number with
   */
  def getColor(steps: Option[Int]): Color

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

trait ColorfulRendering {

  /**
   * Return 0 if value is smaller than lower bound, 1 if larger than upper bound, or a linearly scaled value in [0, 1]
   * if in between.
   */
  def boundBy(lower: Float, upper: Float, value: Float): Float = {
    if (value < lower) 0f else {
      if (value > upper) 1f else {
        (value - lower) / (upper - lower)
      }
    }
  }

  def getColor(steps: Option[Int]): Color = {
    steps match {
      case Some(s) => {
        // these values were hand-tweaked to look aesthetically pleasing; no meaning behind the magic numbers
        val hue = boundBy(10, 200, s) + .8f
        val sat = (1f - boundBy(1, 256, s))
        val bri = boundBy(1, 50, s) * .8f
        new Color(Color.HSBtoRGB(hue, sat, bri))
      }
      case None => Color.WHITE
    }

  }
}

object ColorfulMandelbrotRenderer extends MandelbrotRenderer with ColorfulRendering