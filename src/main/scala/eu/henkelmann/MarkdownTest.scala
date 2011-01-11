package eu.henkelmann

import actuarius.ActuariusTransformer
import com.tristanhunt.knockoff.DefaultDiscounter
import org.pegdown.PegDownProcessor

class MarkdownTestCase (val title:String, val iterations:Int, val input:String) {
    def measure(f:(String => AnyRef)):(AnyRef, Long) = {
        var result:AnyRef = null
        val start = System.currentTimeMillis
        for (i <- 0 until iterations) result = f(input)
        val end = System.currentTimeMillis
        (result, end - start)
    }

    def run(f:(String => AnyRef), engine:String, printOutput:Boolean) {
        val (result1, millis1) = measure(f)
        print("<td>" + millis1 + "</td>")
        val (result2, millis2) = measure(f)
        print("<td>" + millis2 + "</td>")
        if (printOutput) {
            println("Last output: ")
            println(result2.toString)
        }
    }
}

class MarkdownPerformance(testCases:List[MarkdownTestCase]) {

    val actuariusTransformer = new ActuariusTransformer()
    val pegDownProcessor     = new PegDownProcessor()

    def runActuarius(s:String) = actuariusTransformer(s)

    def runPegDown(s:String) = pegDownProcessor.markdownToHtml(s)

    def runKnockoff(s:String) = DefaultDiscounter.toXHTML(DefaultDiscounter.knockoff(s))

    val engines:List[(String, (String=>AnyRef))] =
        List(("Actuarius", runActuarius), ("PegDown", runPegDown), ("Knockoff", runKnockoff))


    def run(printOutput:Boolean) {
        println("<table>")
        print("  <tr><th>Test</th>")
        for (e <- engines) print("<th colspan=\"2\">" + e._1 + "</th>")
        println("</tr>")
        print("  <tr><td></td>")
        for (e <- engines) print("<td>1st Run (ms)</td><td>2nd Run (ms)</td>")
        println("</tr>")
        for (tc <- testCases) {
            //println("========= Input =========")
            //println(tc.input)
            //println()
            print("  <tr><td>" + tc.title + "</td>")
            for((name, f) <- engines) {
                tc.run(f, name, printOutput)
            }
            println("</tr>")
        }
        println("</table>")
    }
}

object RunSpeedTests {

    //just plain paragraphs
    val plainTextTestCase = new MarkdownTestCase("Plain Paragraphs", 10,
            new TestText(TextGenerator.loremIpsum).document(30, 20, 80)
        )

    //every word emphasized
    val emphasisTestCase = new MarkdownTestCase("Every Word Emphasized", 10,
            new TestText(TextGenerator.loremIpsum) with Emphasis {
                override def emphasisFrequency = 1
            }.document(30, 20, 80)
        )

    //every word strong
    val strongTestCase = new MarkdownTestCase("Every Word Strong", 10,
            new TestText(TextGenerator.loremIpsum) with Strong {
                override def strongFrequency = 1
            }.document(30, 20, 80)
        )

    //every word inline code
    val inlineCodeTestCase = new MarkdownTestCase("Every Word Inline Code", 10,
            new TestText(TextGenerator.loremIpsum) with Code {
                override def codeFrequency = 1
            }.document(30, 20, 80)
        )

    //every word a fast link
    val fastLinksTestCase = new MarkdownTestCase("Every Word a Fast Link", 10,
            new TestText(TextGenerator.loremIpsum) with FastLink {
                override def fastLinkFrequency = 1
            }.document(30, 20, 80)
        )

    //every word xml special chars
    val specialXmlCharsTestCase = new MarkdownTestCase("Every Word Consisting of Special XML Chars", 10,
            new TestText(TextGenerator.loremIpsum) with XmlSpecial {
                override def xmlSpecialFrequency = 1
            }.document(30, 20, 80)
        )

    //every word wrapped in HTML tags
    val inlineHtmlTestCase = new MarkdownTestCase("Every Word wrapped in manual HTML tags", 10,
            new TestText(TextGenerator.loremIpsum) with InlineHtml {
                override def inlineHtmlFrequency = 1
            }.document(30, 20, 80)
        )

    //every line a manual line break
    val manualBreakTestCase = new MarkdownTestCase("Every Line with a manual line break", 10,
            new TestText(TextGenerator.loremIpsum) with ManualBreak {
                override def breakFrequency = 1
            }.document(30, 20, 80)
        )

    //every word a full link
    val fullLinkTestCase = new MarkdownTestCase("Every word with a full link", 10,
            new TestText(TextGenerator.loremIpsum) with FullLink {
                override def fullLinkFrequency = 1
            }.document(30, 20, 80)
        )

    //every word a full image
    val fullImageTestCase = new MarkdownTestCase("Every word with a full image", 10,
            new TestText(TextGenerator.loremIpsum) with FullImage {
                override def fullImageFrequency = 1
            }.document(30, 20, 80)
        )

    //every word a reference link
    val refLinkTestCase = new MarkdownTestCase("Every word with a reference link", 10,
            new TestText(TextGenerator.loremIpsum) with RefLink {
                override def refLinkFrequency = 1
            }.document(30, 20, 80)
        )

    //every block a quote
    val blockQuoteTestCase = new MarkdownTestCase("Every block a quote", 10,
        new TestText(TextGenerator.loremIpsum) with Blockquote {
                override def blockquoteFrequency = 1
            }.document(30, 20, 80)
        )

    //every block code
    val codeblockTestCase = new MarkdownTestCase("Every block a codeblock", 10,
        new TestText(TextGenerator.loremIpsum) with Codeblock {
                override def codeblockFrequency = 1
            }.document(30, 20, 80)
        )

    //every block a list
    val listTestCase = new MarkdownTestCase("Every block a list", 10,
        new TestText(TextGenerator.loremIpsum) with UList {
                override def ulistFrequency = 1
            }.document(30, 20, 80)
        )

    //all scenarios mixed
    val allTestCase = new MarkdownTestCase("All tests together", 10,
        (new TestText(TextGenerator.loremIpsum) with Emphasis with Strong with Code with FastLink with XmlSpecial
            with InlineHtml with ManualBreak with FullLink with FullImage with RefLink with Blockquote with Codeblock
            with UList).document(30,20,80))



    val testCases = List(plainTextTestCase, emphasisTestCase, strongTestCase, inlineCodeTestCase, fastLinksTestCase,
        specialXmlCharsTestCase, inlineHtmlTestCase, manualBreakTestCase, fullLinkTestCase, fullImageTestCase,
        refLinkTestCase, blockQuoteTestCase, codeblockTestCase, listTestCase, allTestCase)

    def main(args:Array[String]) {
        new MarkdownPerformance(testCases).run(false)
    }

}