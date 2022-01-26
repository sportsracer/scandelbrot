# Scandelbrot

Visualize the Mandelbrot set. A toy project I used to teach myself some Scala.

![Scandelbrot example visualization](doc/scandelbrot.gif)

## Usage

Requires `sbt`. Start program with `sbt run`. Click anywhere in the image to zoom in.

## Development

### Lint & run tests

```
# Reformat main source files and tests
sbt scandelbrot/scalafmt scandelbrot/Test/scalafmt

# Run tests
sbt test
```

### Overview of files

* [`Complex`](src/main/scala/Complex.scala) models a complex number including some arithmetic operations
* The [`MandelbrotSet`](src/main/scala/MandelbrotSet.scala) singleton tests whether a complex number is contained within the Mandelbrot set
* [`MandelbrotRenderer`](src/main/scala/MandelbrotRenderer.scala) renders the Mandelbrot set and its surroundings onto an image
* … and in [`Main`](src/main/scala/Main.scala), we create the window where all this is visualized
