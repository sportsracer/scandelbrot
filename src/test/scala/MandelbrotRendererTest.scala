import org.scalatest.flatspec._
import org.scalatest.matchers.should.Matchers._

import DoubleHelper._

class MandelbrotRendererTest extends AnyFlatSpec:

  def fixture: ComplexViewport =
    ComplexViewport(256, 256, Complex(0, 0), 8)

  "ComplexViewport" should "convert screen-space coordinates to complex numbers" in {
    val viewport = fixture
    // check that top-left pixel corresponds to `topLeft` from viewport
    val topLeft = viewport.imgSpaceToComplex(0, 0)
    topLeft.re should equal(~(-4.0))
    topLeft.im should equal(~(-4.0))

    val center = viewport.imgSpaceToComplex(128, 128)
    center.re should equal(~0.0)
    center.im should equal(~0.0)
  }

  it should "zoom in on a clicked point" in {
    val viewport = fixture
    val zoomedViewport = viewport zoomBy 2

    zoomedViewport.topLeft.re should be > viewport.topLeft.re
    zoomedViewport.topLeft.im should be > viewport.topLeft.im
  }

  "MandelbrotRenderer" should "render a valid image" in {
    // Construct an image so that the top left pixel definitely is not in M, but the center is
    val viewport = fixture
    val img = MandelbrotRenderer.render(viewport)(using BlackWhiteColorizer)
    // Check that the two points are colored differently
    img.getRGB(128, 128) should not equal img.getRGB(0, 0)
  }
