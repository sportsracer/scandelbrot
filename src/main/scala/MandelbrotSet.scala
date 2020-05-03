object MandelbrotSet {

  /** Helper method to be able to write `c inMandelbrotSet` for `c: Complex` */
  implicit class MandelbrotComplex(c: Complex) {
    def inMandelbrotSet(): Boolean = {
      contains(c)
    }
  }

  val maxIterations = 100

  /**
   * How many steps does it take to exceed the sphere with radius 2, for f_c = z ** 2 + c applied in iteration? Returns
   * `None` if the value seems to be bounded (determined by reaching a maximum number of iterations)
   * @param c Complex number we're testing for
   * @param z Value we're iterating on
   * @param depth Counter to determine recursion depth
   * @return Number of steps required to exit sphere of radius 2, or None if value is bounded
   */
  def iterate(c: Complex, z: Complex=Complex(0, 0), depth: Int=0): Option[Int] = {
    if (depth >= maxIterations) {
      return None
    }

    if (z.magnitude() >= 2) {
      return Some(0)
    }

    val zNext = z ** 2 + c
    val nextStep = iterate(c, zNext, depth+1)
    nextStep match {
      case Some(i) => Some(1 + i)
      case None => None
    }
  }

  /**
   * Is this complex number in the Mandelbrot set?
   * @param c Complex number to test for
   * @return True iff c is in M
   */
  def contains(c: Complex): Boolean = {
    val steps = iterate(c)
    steps.isEmpty
  }

}
