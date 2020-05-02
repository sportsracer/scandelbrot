import org.scalatest.funsuite.AnyFunSuite

class MainTest extends AnyFunSuite {
  test("Main.parseOptions") {
    val noOptions = Main.parseOptions(Nil, Main.defaults)
    assert(noOptions(Symbol("width")) == Main.defaults(Symbol("width")))
    assert(noOptions(Symbol("height")) == Main.defaults(Symbol("height")))

    val options = Main.parseOptions("--width" :: "1024" :: "--height" :: "768" :: Nil, Main.defaults)
    assert(options(Symbol("width")) == 1024)
    assert(options(Symbol("height")) == 768)

    assertThrows[IllegalArgumentException] {
      Main.parseOptions("--nonsense" :: "--options" :: Nil)
    }
  }
}
