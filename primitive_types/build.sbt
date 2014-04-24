name := "notastruct-primitives"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.0"

libraryDependencies += "notastruct" %% "primitive-macros" % version.value

addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.0" cross CrossVersion.full)

scalacOptions ++= Seq(
    "-deprecation",
    "-unchecked",
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-language:experimental.macros")
