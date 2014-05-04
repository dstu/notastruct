organization := "notastruct"

name := "struct-model"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.0"

libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value

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
