name := "tags"

version := "1.0"

scalaVersion := "2.10.5"

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
  "org.apache.thrift" % "libthrift" % "0.9.2",
  "com.twitter" %% "scrooge-core" % "3.19.0",
  "com.twitter" %% "finagle-thrift" % "6.26.0",
  "org.specs2" %% "specs2-core" % "3.6.3" % "test"
)

scalacOptions in Test ++= Seq("-Yrangepos")