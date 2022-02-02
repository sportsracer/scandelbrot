import scala.collection.parallel.CollectionConverters._

import java.awt.image.BufferedImage

/** Helper class to represent, and modify, a viewport that determines which part
  * of the complex pane gets rendered to screen
  * @param width
  *   Width of the image to be rendered
  * @param height
  *   Height of the image to be rendered
  * @param center
  *   Complex number at center of image
  * @param scale
  *   Top-right point in image is (topLeft.re + scale, topLeft.im)
  */
case class ComplexViewport(
    width: Int,
    height: Int,
    center: Complex,
    scale: Double
):

  /** What magnitude on the imaginary axis does `height` scale to?
    */
  def verticalScale: Double =
    scale * height / width

  def topLeft: Complex =
    Complex(center.re - scale / 2f, center.im - verticalScale / 2f)

  /** Convert from image space to represented complex number */
  def imgSpaceToComplex(x: Int, y: Int): Complex =
    Complex(
      topLeft.re + scale * x / width,
      topLeft.im + verticalScale * y / height
    )

  /** Create a new viewport zoomed in by `factor` */
  def zoomBy(factor: Double): ComplexViewport =
    val newScale = scale / factor
    ComplexViewport(width, height, center, newScale)

  /** Create a new viewport centered around (x, y) */
  def centerOn(x: Int, y: Int): ComplexViewport =
    val newCenter = imgSpaceToComplex(x, y)
    this centerOn newCenter

  /** Create a new viewport centered around `newCenter` */
  def centerOn(newCenter: Complex): ComplexViewport =
    ComplexViewport(width, height, newCenter, scale)

object MandelbrotRenderer:

  /** Render a Mandelbrot set into a `BufferedImage`
    * @param viewport
    *   Determines how to map complex plane coordinates to the rendered image
    *   space
    * @param colorizer
    *   Determines how to color pixels based on their escape time
    * @return
    *   Hopefully a very pretty image of a Mandelbrot set
    */
  def render(viewport: ComplexViewport)(using
      colorizer: Colorizer
  ): BufferedImage =
    val img =
      BufferedImage(viewport.width, viewport.height, BufferedImage.TYPE_INT_RGB)

    // Put y-values of rows to be rendered in parallel collection
    val ys = (
      for y <- 0 until viewport.height
      yield y
    ).par

    // Compute each row as array of colors in parallel
    val rows = ys.map(y =>
      (
        for x <- 0 until viewport.width yield
          val c = viewport.imgSpaceToComplex(x, y)
          val orbit = MandelbrotSet.orbit(c)
          val color = colorizer.getColor(orbit)
          color.getRGB
      ).toArray
    )

    // Update the rendered image row by row
    (ys zip rows).foreach({ case (y, row) =>
      img.setRGB(0, y, viewport.width - 1, 1, row, 0, viewport.width)
    })

    img
