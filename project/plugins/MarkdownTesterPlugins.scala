import sbt._

class MarkdownTesterPlugins(info: ProjectInfo) extends PluginDefinition(info) {
    val repo = "GH-pages repo" at "http://mpeltonen.github.com/maven/"
    lazy val idea = "com.github.mpeltonen" % "sbt-idea-plugin" % "0.1-SNAPSHOT"
    val christophsRepo = "Christoph's Repo" at "http://maven.henkelmann.eu"
    val junitXml = "eu.henkelmann" % "junit_xml_listener" % "0.2"
} 
