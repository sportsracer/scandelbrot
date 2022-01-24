name := "scandelbrot"

version := "0.2"

scalaVersion := "3.1.0"

scalacOptions += "-deprecation"

run / fork := true // Without this, "sbt run" exits immediately after creating the Swing window

libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4"
libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % "test"