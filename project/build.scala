import sbt._
import Keys._


object BlagCastBuild extends Build {


   val blagCast = (
     play.Project("blagcast", path = file("."))
     settings(
       scalaVersion := "2.10.2",
       version := "1.AWESOME"
     )
   )

}
