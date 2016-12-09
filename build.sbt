name := "Kalah"

version := "1.0"

scalaVersion := "2.12.0"
    
mainClass in (Compile, run) := Some("info.kwarc.teaching.AI.Kalah.WS1617.evaluation.Profiler")
mainClass in assembly:= Some("info.kwarc.teaching.AI.Kalah.WS1617.evaluation.Profiler")
//mainClass in (Compile, run) := Some("info.kwarc.teaching.AI.Kalah.WS1617.agents.SuperAgentTest")
//mainClass in (Compile, run) := Some("info.kwarc.teaching.AI.Kalah.Test")

libraryDependencies += "org.scala-lang" % "scala-library" % "2.10.0-M1"
exportJars := true
retrieveManaged := true
