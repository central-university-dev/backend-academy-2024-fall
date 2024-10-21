lazy val `seminar-1` = project.settings(idePackagePrefix := Some("ru.tbank"), scalaVersion := Versions.scala3)
lazy val `seminar-2` = project.settings(idePackagePrefix := Some("ru.tbank"), scalaVersion := Versions.scala3)
lazy val `seminar-3` = project.settings(idePackagePrefix := Some("ru.tbank"), scalaVersion := Versions.scala3)
lazy val `seminar-4` = project.settings(idePackagePrefix := Some("ru.tbank"), scalaVersion := Versions.scala3)
lazy val `seminar-5` = project.settings(idePackagePrefix := Some("ru.tbank"), scalaVersion := Versions.scala3)
lazy val `seminar-6` = project
  .settings(
    idePackagePrefix := Some("ru.tbank"),
    scalaVersion := Versions.scala3,
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % "2.12.0",
      "org.typelevel" %% "cats-laws" % "2.12.0" % Test,
      "org.scalatest" %% "scalatest" % "3.2.9" % Test,
      "org.typelevel" %% "discipline-scalatest" % "2.3.0" % Test,
      "org.scalacheck" %% "scalacheck" % "1.18.1" % Test,
    )
  )
lazy val `seminar-7` = project
  .settings(
    idePackagePrefix := Some("ru.tbank"),
    scalaVersion := Versions.scala3,
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % "2.12.0",
      "org.typelevel" %% "cats-effect" % "3.5.4"
    )
  )