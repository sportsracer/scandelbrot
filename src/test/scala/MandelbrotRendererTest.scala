import org.scalatest._
import org.scalatest.matchers.should.Matchers._

class MandelbrotRendererTest extends FlatSpec {

  "MandelbrotRenderer" should "render a valid image" in {
    // Construct an image so that the top left pixel definitely is not in M, but the center is
    val img = MandelbrotRenderer.render(255, 255, Complex(-4, -4), 8)
    // Check that the two points are colored differently
    img.getRGB(128, 128) should not equal img.getRGB(0, 0)
  }

}
