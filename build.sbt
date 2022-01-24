name := "scandelbrot"

version := "0.1"

scalaVersion := "2.13.8"

scalacOptions += "-deprecation"

libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4"
libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % "test"