import sbt._
import eu.henkelmann.sbt._

class MarkdownTesterProject(info: ProjectInfo) extends DefaultProject(info) 
    with IdeaProject {
    
    val scalatest = "org.scalatest" % "scalatest" % "1.2" % "test->default" withSources()
    val junit = "junit" % "junit" % "4.5" % "test->default"
    val asm = "asm" % "asm" % "3.3.1"
    val asm_tree = "asm" % "asm-tree" % "3.3.1"
    val asm_analysis = "asm" % "asm-analysis" % "3.3.1"
    val asm_util = "asm" % "asm-util" % "3.3.1"

    val knockoff_repo = "t_repo" at "http://tristanhunt.com:8081/content/groups/public/"
    //this repo holds a dependency for knockoff
    val snuggletex_repo = "snuggletex_repo" at "http://www2.ph.ed.ac.uk/maven2"

    
    def junitXmlListener: TestReportListener = new JUnitXmlTestsListener(outputPath.toString)
    override def testListeners: Seq[TestReportListener] = super.testListeners ++ Seq(junitXmlListener)

    val actuarius = "eu.henkelmann" %% "actuarius" % "0.2"
    val knockoff = "com.tristanhunt" %% "knockoff" % "0.7.3-15"
    
    // define additional artifacts
    // create jar paths for javadoc and sources
    override def packageDocsJar = defaultJarPath("-javadoc.jar")
    override def packageSrcJar  = defaultJarPath("-sources.jar")
    val sourceArtifact = Artifact.sources(artifactID)
    val docsArtifact   = Artifact.javadoc(artifactID)

    override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageDocs, packageSrc)
}
