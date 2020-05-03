import org.scalatest._
import org.scalatest.matchers.should.Matchers._

class MainTest extends FlatSpec {

  "Main" should "parse command line options" in {
    val noOptions = Main.parseOptions(Nil, Main.defaults)
    noOptions(Symbol("width")) should equal (Main.defaults(Symbol("width")))
    noOptions(Symbol("height")) should equal (Main.defaults(Symbol("height")))

    val options = Main.parseOptions("--width" :: "1024" :: "--height" :: "768" :: Nil, Main.defaults)
    options(Symbol("width")) should equal (1024)
    options(Symbol("height")) should equal (768)

    an [IllegalArgumentException] should be thrownBy Main.parseOptions("--nonsense" :: "--options" :: Nil)
  }

}
