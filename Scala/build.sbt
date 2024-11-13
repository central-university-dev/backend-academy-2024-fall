lazy val `seminar-1` = project.settings(
  idePackagePrefix := Some("ru.tbank"),
  scalaVersion := Versions.scala3
)
lazy val `seminar-2` = project.settings(
  idePackagePrefix := Some("ru.tbank"),
  scalaVersion := Versions.scala3
)
lazy val `seminar-3` = project.settings(
  idePackagePrefix := Some("ru.tbank"),
  scalaVersion := Versions.scala3
)
lazy val `seminar-4` = project.settings(
  idePackagePrefix := Some("ru.tbank"),
  scalaVersion := Versions.scala3
)
lazy val `seminar-5` = project.settings(
  idePackagePrefix := Some("ru.tbank"),
  scalaVersion := Versions.scala3
)
lazy val `seminar-6` = project
  .settings(
    idePackagePrefix := Some("ru.tbank"),
    scalaVersion := Versions.scala3,
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % "2.12.0",
      "org.typelevel" %% "cats-laws" % "2.12.0" % Test,
      "org.scalatest" %% "scalatest" % "3.2.9" % Test,
      "org.typelevel" %% "discipline-scalatest" % "2.3.0" % Test,
      "org.scalacheck" %% "scalacheck" % "1.18.1" % Test
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
lazy val `seminar-8` = project.settings(
  scalaVersion := Versions.scala3,
  libraryDependencies ++= Seq(
    // cats
    "org.typelevel" %% "cats-core" % "2.12.0",
    "org.typelevel" %% "cats-effect" % "3.5.4",

    // tapir
    "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % "1.11.7",
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % "1.11.7",
    "com.softwaremill.sttp.tapir" %% "tapir-json-tethys" % "1.11.7",

    // http4s
    "org.http4s" %% "http4s-ember-server" % "0.23.27",

    // logback
    "ch.qos.logback" % "logback-classic" % "1.5.6",

    // tethys
    "com.tethys-json" %% "tethys-core" % "0.29.1",
    "com.tethys-json" %% "tethys-jackson213" % "0.29.1",
    "com.tethys-json" %% "tethys-derivation" % "0.29.1",
    "com.tethys-json" %% "tethys-enumeratum" % "0.29.1",

    // test
    "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub-server" % "1.10.15" % Test,
    "org.scalatest" %% "scalatest" % "3.2.18" % Test,
    "org.scalamock" %% "scalamock" % "6.0.0" % Test,
    "org.typelevel" %% "munit-cats-effect" % "2.0.0" % Test
  )
)

lazy val `seminar-9` =
  (project in file("seminar-9"))
    .settings(
      libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "3.2.18" % Test,
        "org.scalamock" %% "scalamock" % "6.0.0" % Test
      )
    )

lazy val `seminar-10` = project
  .settings(
      idePackagePrefix := Some("ru.tbank"),
      scalaVersion := Versions.scala3,
      libraryDependencies ++= Seq(
          // cats
          "org.typelevel" %% "cats-core" % "2.12.0",
          "org.typelevel" %% "cats-effect" % "3.5.4",

          // java email
          "javax.mail" % "mail" % "1.4.7",

          // scala email
          "com.github.eikek" %% "emil-common" % "0.15.0",
          "com.github.eikek" %% "emil-javamail" % "0.15.0",

          // test
          "org.scalatest" %% "scalatest" % "3.2.18" % Test,
          "org.scalamock" %% "scalamock" % "6.0.0" % Test,
          "org.typelevel" %% "cats-effect-testing-scalatest" % "1.5.0" % Test,
      )
  )
