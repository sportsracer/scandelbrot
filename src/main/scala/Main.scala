import swing.MainFrame

@main
def main(): Unit =
  val DefaultWidth = 800
  val DefaultHeight = 600

  new MainFrame {
    title = "Scandelbrot"
    contents = MandelbrotViewer(DefaultWidth, DefaultHeight)
  }
    .pack()
    .visible = true
