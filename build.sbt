ThisBuild / scalaVersion := "2.13.14"
ThisBuild / organization := "opensource.dn"
ThisBuild / version := "0.1.0"

lazy val root = (project in file("."))
  .settings(
    name := "dhira-1",
    libraryDependencies ++= Seq(
      "edu.berkeley.cs" %% "chisel3" % "6.6.0",
      "edu.berkeley.cs" %% "chiseltest" % "6.0.0" % Test,
      "org.scalatest" %% "scalatest" % "3.2.18" % Test
    ),
    testFrameworks += new TestFramework("org.scalatest.tools.Framework"),
    // ChiselTest can use Verilator via annotations when installed.
  )
