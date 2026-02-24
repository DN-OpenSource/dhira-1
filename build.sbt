ThisBuild / scalaVersion := "2.13.14"
ThisBuild / organization := "opensource.dn"
ThisBuild / version := "0.1.0"

lazy val root = (project in file("."))
  .settings(
    name := "dhira-1",
    libraryDependencies ++= Seq(
      "edu.berkeley.cs" %% "chisel3" % "3.6.1",
      "edu.berkeley.cs" %% "chiseltest" % "0.6.2" % Test,
      "org.scalatest" %% "scalatest" % "3.2.18" % Test
    ),
    // Chisel 3.6+ requires a Scala compiler plugin.
    addCompilerPlugin("edu.berkeley.cs" % "chisel3-plugin_2.13.14" % "3.6.1"),
    testFrameworks += new TestFramework("org.scalatest.tools.Framework"),
    // ChiselTest can use Verilator via annotations when installed.
  )
