import java.awt.Color
import java.awt.image.BufferedImage

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
   * @param width Width of the image
   * @param height Height of the image
   * @param topLeft Which complex number corresponds to the top-left corner?
   * @param scale Top-right corner corresponds to `(topLeft.re + scale, topLeft.im)`
   * @return Hopefully a very pretty image of a Mandelbrot set
   */
  def render(width: Int, height: Int, topLeft: Complex, scale: Double): BufferedImage = {
    val img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

    for (x <- 0 until img.getWidth()) {
      for (y <- 0 until img.getHeight()) {
        val re = topLeft.re + (x.toFloat / width) * scale
        val im = topLeft.im + (y.toFloat / height) * (scale * height / width)
        val c = Complex(re, im)
        val steps = MandelbrotSet.iterate(c)
        val color = getColor(steps)
        img.setRGB(x, y, color.getRGB)
      }
    }

    img
  }
}
