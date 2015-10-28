
import scala.util.parsing.combinator.RegexParsers
import scala.util.parsing.combinator.syntactical.StandardTokenParsers

object CW2 {

  /* =====  Abstract syntax tree for MiniMD  ===== */
  abstract class MiniMDExpr
  // A block of MiniMD expressions. The top-level of a program is a MDDoc.
  case class MDDoc(contents: List[MiniMDExpr]) extends MiniMDExpr

  // A paragraph of free text / bold / italic / section headers
  case class MDPar(contents: List[MiniMDExpr]) extends MiniMDExpr
  case class MDVerbatim(contents: String) extends MiniMDExpr

  // Free text
  case class MDFreeText(text: String) extends MiniMDExpr
  case class MDBold(text: String) extends MiniMDExpr
  case class MDItalic(text: String) extends MiniMDExpr
  case class MDUnderlined(text: String) extends MiniMDExpr


  // List items
  case class MDListItem(exprs: List[MiniMDExpr]) extends MiniMDExpr

  case class MDBulletedList(listItems: List[MDListItem]) extends MiniMDExpr
  case class MDNumberedList(listItems: List[MDListItem]) extends MiniMDExpr

  // Sections; `text` is the section header name.
  case class MDSectionHeader(text: String) extends MiniMDExpr
  case class MDSubsectionHeader(text: String) extends MiniMDExpr

  // Links
  case class MDLink(text: String, url: String) extends MiniMDExpr


  /* ===== Printing Language ===== */
  import scala.language.implicitConversions


  // ======================================================================
  // Part 1: Pretty-printing
  // ======================================================================

  trait Printer {
    // The abstract Doc type
    type Doc

    // An empty document
    val nil : Doc

    // A block of text
    def text(s: String): Doc

    // A line break / newline
    val line: Doc

    // Append two Docs together
    def append(x:Doc, y: Doc): Doc

    // A doc which is nested to a certain level.
    def nest(i: Int, doc: Doc): Doc

    // A doc whose content is rendered without indentation
    // (nesting is suspended)
    def unnest(doc: Doc): Doc

    // A method which prints the Doc to a string.
    def print(doc: Doc): String

    // some Scala incantations to allow infix <> document append operation
    class AppendAssoc(x: Doc) {
      def <> (y: Doc): Doc = append(x,y)
    }
    implicit def doc2AppendAssoc(x: Doc): AppendAssoc = new AppendAssoc(x)



    // ======================================================================
    //  Exercise 1
    // ======================================================================

    def quote(d1: Doc): Doc = text("\"" + print(d1)+ "\"")

    def braces(d1: Doc): Doc = text("{" + print(d1)+ "}")

    def anglebrackets(d1: Doc): Doc = text("<" + print(d1)+ ">")

    /* ======================================================================
     *  Exercise 2
     * ====================================================================== */

    def sep(d: Doc, ds: List[Doc]): Doc = ds match {
      case Nil => nil
      case li :: lis => lis.foldLeft(li)((a: Doc, b: Doc) => append(append(a, d), b))
    }

  }


  // ======================================================================
  // Exercise 3
  // ======================================================================

  //  The following instance is a stub, provided just to make the
  // rest of the code compile.

  object MyPrinter extends Printer {
    type Doc = String
    val nil = ""
    def text(s: String) = sys.error("TODO")
    val line = ""
    def append(x:Doc, y: Doc) = sys.error("TODO")
    def nest(i: Int, doc: Doc) = sys.error("TODO")
    def unnest(doc: Doc) = sys.error("TODO")
    def print(doc: Doc) = sys.error("TODO")
  }

  object PurePrinter extends Printer {

    type FDoc = (Int, Boolean) => String
    type Doc = FDoc

    val nil = (_: Int, _: Boolean) => ""
    val line = (_: Int, _: Boolean) => "\n"
    val tab = "  "

    def text(s: String) = (tabs: Int, suspended: Boolean) => {
      if (!suspended) tab * tabs + s
      else s
    }
    def append(x: Doc, y: Doc) = {
      text(x(0, false) + y(0, false))
    }
    def nest(i: Int, doc: Doc) = text(doc(i, false))
    def unnest(doc: Doc) = text(doc(0, true))
    def print(doc: Doc) = doc(0, false)

    def wrapHtml(
        tag: String,
        doc: Doc,
        attrs: Map[String, String] = Map[String, String]()
    ) = {
      if (attrs.size > 0) {
        val attributes = attrs map {
          case (key, value) => key + "=\"" + value + "\""
        } mkString " "

        text(s"<${tag} ${attributes}>") <> doc <> text(s"</${tag}>")
      }
      else text(s"<${tag}>") <> doc <> text(s"</${tag}>")
    }

    def wrapBegin(t: String, doc: Doc) =
      text(s"\\begin{${t}}") <> line <> doc <> text(s"\\end{${t}}")

    def wrapEnv(
      env: String,
      doc: Doc,
      args: List[String] = List[String]()
    ) = {
      text(s"\\${env}{") <> doc <> text("}" + args.map((a) => s"{${a}}").mkString(""))
    }

  }

  // Import it for use in the rest of the program
  // import MyPrinter._
  import PurePrinter._

  // A Formatter[T] knows how to turn T into a Doc.
  trait Formatter[T] {
    // The abstract method that needs to be implemented.
    def format(e: T): Doc
    // The method formatList applies format to each element of a list, concatenating the results
    // It can be called from format().
    def formatList(xs: List[T]): Doc = sep(nil,xs.map{x: T => format(x)})
    def formatList(separator: Doc, xs: List[T]): Doc = sep(separator, xs.map{x: T => format(x)})

  }

  // ======================================================================
  // Exercise 4
  // ======================================================================

  object MarkdownFormatter extends Formatter[MiniMDExpr] {

    def format(e: MiniMDExpr) = e match {

      case MDDoc(contents) => formatList(line, contents) <> line
      case MDPar(contents) => formatList(contents) <> line
      case MDFreeText(t) => text(t)
      case MDBold(t) => text("*") <> text(t) <> text("*")
      case MDItalic(t) => text("`") <> text(t) <> text("`")

      case MDUnderlined(t) => text("_") <> text(t) <> text("_")
      case MDBulletedList(items) => sep(
        line,
        items.map( (x) => append(text("* "), format(x)) )
      ) <> line
      case MDListItem(items) => formatList(items)

      case MDNumberedList(items) => sep(
        line,
        items.zipWithIndex.map{ case (item, index) =>
          append(text((index + 1) + ". "), format(item))
        }
      ) <> line

      case MDSectionHeader(header) => text("== ") <> text(header) <> text(" ==")

      case MDSubsectionHeader(header) => text("=== ") <> text(header) <> text(" ===")

      case MDVerbatim(content) => text("{{{") <> line <> text(content) <> line <> text("}}}")

      case MDLink(label, url) => text(s"(${label})[${url}]")

    }

  }

  /* ======================================================================
   *  Exercise 5
   * ====================================================================== */

  object LatexFormatter extends Formatter[MiniMDExpr] {

    def format(e: MiniMDExpr) = e match {
      case MDDoc(contents) => formatList(line, contents) <> line
      case MDPar(contents) => formatList(contents) <> line
      case MDFreeText(t) => text(t)
      case MDBold(t) => wrapEnv("textbf", text(t))
      case MDItalic(t) => wrapEnv("textit", text(t))
      case MDUnderlined(t) => wrapEnv("underline", text(t))

      case MDListItem(items) => text("\\item ") <> formatList(items)

      case MDBulletedList(items) => wrapBegin("itemize", sep(
        line,
        items.map((i) => format(i))
      ) <> line) <> line

      case MDNumberedList(items) => wrapBegin("enumerate", sep(
        line,
        items.map((i) => format(i))
      ) <> line) <> line <> line
      case MDSectionHeader(header) => wrapEnv("section", text(header))
      case MDSubsectionHeader(header) => wrapEnv("subsection", text(header))
      case MDVerbatim(content) => wrapBegin("verbatim", text(content)) <> line
      case MDLink(label, url) => wrapEnv("href", text(url), List(label))
    }

  }

  // ======================================================================
  //  Exercise 6
  // ======================================================================


  object HTMLFormatter extends Formatter[MiniMDExpr] {

    def format(e: MiniMDExpr) = e match {
      case MDDoc(contents) => formatList(line, contents) <> line
      case MDPar(contents) => wrapHtml("p", formatList(contents)) <> line

      case MDFreeText(t) => text(t)
      case MDBold(t) => wrapHtml("b", text(t))
      case MDItalic(t) => wrapHtml("i", text(t))
      case MDUnderlined(t) => wrapHtml("u", text(t))

      case MDListItem(items) => wrapHtml("li", formatList(items))

      case MDBulletedList(items) => wrapHtml("ul", line <> sep(
        line,
        items.map((i) => nest(1, format(i)))
      ) <> line)
      case MDNumberedList(items) => wrapHtml("ol", line <> sep(
        line,
        items.map((i) => nest(1, format(i)))
      ) <> line)
      case MDSectionHeader(header) => wrapHtml("h1", text(header))
      case MDSubsectionHeader(header) => wrapHtml("h2", text(header))
      case MDVerbatim(content) => wrapHtml("pre", unnest(text(content)))
      case MDLink(label, url) =>
        wrapHtml("a", text(label), Map("href" -> url))
    }

  }

  // ======================================================================
  // Part 2: Random test case generation
  // ======================================================================

  object Rng {


    // A Gen[T] has a method called get that generates a random T.
    trait Gen[+A] {
      val rng = scala.util.Random

      def get(): A // abstract

      // Gen[T] also supports the map and flatMap methods, so for-comprehensions
      // can be used with them.
      def map[B](f: A => B):Gen[B] =
      {
        val self = this
        new Gen[B] { def get() = f(self.get()) }
      }

      // **********************************************************************
      // Exercise 7
      // **********************************************************************

      def flatMap[B](f: A => Gen[B]) = sys.error("TODO")
    }

    // **********************************************************************
    // Exercise 8
    // **********************************************************************

    def const[T](c: T): Gen[T] =
      sys.error("TODO")

    def flip: Gen[Boolean] =
      sys.error("TODO")

    def range(min: Integer, max: Integer): Gen[Integer] =
      sys.error("TODO")


    def fromList[T](items: List[T]): Gen[T] =
      sys.error("TODO")

    // **********************************************************************
    // Exercise 9
    // **********************************************************************

    def genSectionText: Gen[String] =
      sys.error("TODO")

    def genSubsectionText: Gen[String] =
      sys.error("TODO")

    def genListitemText: Gen[String] =
      sys.error("TODO")

    def genLink: Gen[(String, String)] =
      sys.error("TODO")

    def genFreeText: Gen[String] =
      sys.error("TODO")

    def genVerbatimText: Gen[String] =
      sys.error("TODO")


    // **********************************************************************
    // Exercise 10
    // **********************************************************************

    def genList[T](n: Integer, g: Gen[T]): Gen[List[T]] = sys.error("TODO")

    def genFromList[A](gs: List[Gen[A]]): Gen[A] = sys.error("TODO")


    // **********************************************************************
    // Exercise 11
    // **********************************************************************

    def genMiniMDExpr(n: Integer): Gen[MiniMDExpr] =
      sys.error("TODO")


  }




  /*======================================================================
   The rest of this file is support code, which you should not (and do not
   need to) change.
   ====================================================================== */
  /* ===== MiniMD Parser ===== */

  object MiniMDParser extends RegexParsers {
    val eol = sys.props("line.separator")

    // Here be dragons.

    type P[+A] = Parser[A]
    private val eoi = """\z""".r // end of input
    private val separator = eoi | eol

    override val skipWhitespace = false

    // Paragraph: Either a long line of free text with the whitespace
    // stripped out, or a list with whitespace kept in
    type Line = String
    type Paragraph = String



    // Parses an input file
    def parse(input: String): MiniMDExpr = {
      val source = scala.io.Source.fromFile(input)
      val lines = try source.mkString finally source.close()
      val paragraphs = tokeniseParagraphs(lines)

      println("Paragraphs:")
      println(paragraphs + "\n\n")
      val parsedParagraphs = paragraphs.flatMap((par: Paragraph) => parseParagraph(par))

      normalise(MDDoc(parsedParagraphs))
    }

    // Top-level parse function: takes a string, returns a MiniMDExpr.
    // Throws an error upon failure.
    def parseParagraph(input: String): List[MiniMDExpr] = {
      if (input.trim.length == 0) { Nil }
      else {
        parseAll(paragraph, input) match {
          case Success(ast, _) => List(ast)
          case (e: NoSuccess) => {
            sys.error(e.msg + ", " + e.next.pos + ", " + e.next.source)
            Nil
          }
        }
      }
    }


    // Given an input string, generates a list of paragraphs
    def tokeniseParagraphs(input: String): List[Paragraph] = {

      def isEmptyLine(s: String) = s.trim.length == 0

      def isBulletListItem(s: String) = s.startsWith("* ")
      def isNumberListItem(s: String) = """^\d+\. """.r.findFirstIn(s).isDefined
      def isListItem(s: String) = isBulletListItem(s) || isNumberListItem(s)

      def isStartVerbatim(s: String) = s.trim == "{{{"
      def isEndVerbatim(s: String) = s.trim == "}}}"
      def isSection(s: String) = s.trim.startsWith("==")


      def gatherList(isBulleted: Boolean, par: Paragraph,
        remainder: List[Line]): (Paragraph, List[Line]) = remainder match {
        case Nil => (par, remainder)
        case x::xs =>
          if (isBulleted && isBulletListItem(x)) {
            gatherList(isBulleted, par + x, xs)
          } else if (!isBulleted && isNumberListItem(x)) {
            gatherList(isBulleted, par + x, xs)
          } else if (isEmptyLine(x)) {
            (par, xs)
          } else {
            (par, remainder)
          }
      }

      def gatherParagraph(par: Paragraph, remainder: List[Line]):
          (Paragraph, List[Line]) =
        remainder match {
          case Nil => (par, remainder)
          case x::xs =>
            if (isEmptyLine(x)) {
              (par, xs)
            } else if (isListItem(x) || isStartVerbatim(x)) {
              (par, remainder)
            } else {
              gatherParagraph(par + x.stripLineEnd + " ", xs)
            }
        }

      def gatherVerbatim(par: Paragraph, remainder: List[Line]):
          (Paragraph, List[Line]) =
        remainder match {
          case Nil => (par, remainder)
          case x::xs =>
            if (isEndVerbatim(x)) {
              (par + x.trim, xs)
            } else {
              gatherVerbatim(par + x, xs)
            }
        }

      def eatEmptyLines(remainder: List[Line]):
          (List[Line]) =
        remainder match {
          case Nil => Nil
          case x::xs =>
            if (isEmptyLine(x)) {
              eatEmptyLines(xs)
            } else {
              remainder
            }
        }

      def doTokeniseParagraphs(remainder: List[Line]): List[Paragraph] =
        remainder match {
          case Nil => Nil
          case x::xs =>
            if (isEmptyLine(x)) {
              doTokeniseParagraphs(xs)
            } else if (isSection(x)) {
              x.stripLineEnd::(doTokeniseParagraphs(xs))
            } else if (isStartVerbatim(x)) {
              val (par, newRemainder) = gatherVerbatim("", remainder)
              par::(doTokeniseParagraphs(newRemainder))
            } else if (isBulletListItem(x)) {
              val (par, newRemainder) = gatherList(true, "", remainder)
              par::(doTokeniseParagraphs(newRemainder))
            } else if (isNumberListItem(x)) {
              val (par, newRemainder) = gatherList(false, "", remainder)
              par::(doTokeniseParagraphs(newRemainder))
            } else {
              val (par, newRemainder) = gatherParagraph("", remainder)
              par::(doTokeniseParagraphs(newRemainder))
            }
        }

      val linesList = input.linesWithSeparators.toList
      doTokeniseParagraphs(linesList)
    }

    def delimitedSequence(delimiter: Char, breakOnNL: Boolean): P[String] = {
      (rep(delimitedChar(delimiter, breakOnNL)) <~ aChar) ^^ {
        case seq => seq.mkString
      }
    }

    def num: P[Int] = "[0-9]+".r ^^ { case n => n.toInt }

    def delimitedChar(delimiter: Char, breakOnNL: Boolean): P[Char] =
      acceptIf {ch => (ch != delimiter) || (breakOnNL && ch == eol)} {_ => "Delimiter char reached"}

    def aChar: P[Char] = Parser { in =>
      if (in.atEnd) {
        Failure("End of input", in)
      } else {
        Success(in.first, in.rest)
      }
    }

    def isDelimiterChar(c: Char): Boolean =
      c match {
        case '*' => true
        case '_' => true
        case '`' => true
        case '=' => true
        case '\r' => true
        case '\n' => true
        case '(' => true
        case '{' => true
        case _ => false
      }

    def bulletedListItem = {
      ("* " ~> listExpr) <~ eol ^^ { case e => MDListItem(e) }
    }

    def bulletedList: P[MiniMDExpr] = {
      rep1(bulletedListItem) ^^ { case seq => MDBulletedList(seq) }
    }

    def numberedListItem: P[MDListItem] =
      ((num <~ ". ") ~ listExpr) <~ eol ^^ {
        case (num ~ e) => MDListItem(e)
      }

    def numberedList: P[MiniMDExpr] = {
      rep1(numberedListItem) ^^ { case seq => MDNumberedList(seq) }
    }

    def parseDelimiterRun(breakOnNL: Boolean): P[MiniMDExpr] = {
      ("*" ~> delimitedSequence('*', breakOnNL) ^^ { case str => MDBold(str) }) |
      ("`" ~> delimitedSequence('`', breakOnNL) ^^ { case str => MDItalic(str) }) |
      ("_" ~> delimitedSequence('_', breakOnNL) ^^ { case str => MDUnderlined(str) })
    }

    def freeChar: P[Char] =
      acceptIf {ch => !isDelimiterChar(ch)} {_ => "Delimiter char reached"}

    // Parse until we hit a delimiter character.
    def freeText: P[MiniMDExpr] =
      aChar ~ rep(freeChar) ^^ { case ch ~ xs => MDFreeText((ch::xs).mkString) }

    def subsectionHeader: P[MiniMDExpr] =
      ("===" ~> "[^=]+".r <~ "===[ ]*".r) ^^ { case headertxt => MDSubsectionHeader(headertxt.trim) }

    def sectionHeader: P[MiniMDExpr] =
      ("==" ~> "[^=]+".r <~ "==[ ]*".r) ^^ { case headertxt => MDSectionHeader(headertxt.trim) }

    def link: P[MiniMDExpr] =
      ("(" ~> ("[^)]*".r) <~ ")") ~ ("[" ~> ("[^\\]]*".r) <~ "]") ^^ {
        case desc~url => MDLink(desc, url)
      }

    def verbatim: P[MiniMDExpr] =
      ("{{{" ~> eol ~> """(?s).*?(?=}}})""".r.unanchored <~ "}}}") ^^ { case vrb => MDVerbatim(vrb) }

    def listExpr: P[List[MiniMDExpr]] = {
      rep1((guard(not(eol))) ~> (parseDelimiterRun(true) | freeText))
    }

    def expr: P[MiniMDExpr] = {
      parseDelimiterRun(false) | link | freeText
    }

    def plainPar: P[MiniMDExpr] = {
      rep1(expr) ^^ { xs => MDPar(xs) }
    }

    def paragraph: P[MiniMDExpr] =
       subsectionHeader | sectionHeader | bulletedList | numberedList | verbatim | plainPar



    /* Normalisation pass.
     * We'll get a stream of FreeText / lists / section headers from
     * the parser.
     * We want to ensure that if we have two consecutive FreeTexts,
     * that they're combined into one.
     */
    def normaliseInner(es: List[MiniMDExpr]): List[MiniMDExpr] = es match {
      case Nil => Nil
      case MDFreeText(s1)::MDFreeText(s2)::xs => normaliseInner(MDFreeText(s1 ++ s2)::xs)
      case e::xs => normalise(e)::normaliseInner(xs)
    }

    def normalise(e: MiniMDExpr): MiniMDExpr = e match {
      case MDDoc(xs) => MDDoc(normaliseInner(xs))
      case MDPar(xs) => MDPar(normaliseInner(xs))
      case MDBulletedList(xs) =>
        MDBulletedList(xs.map(x => normalise(x).asInstanceOf[MDListItem]))
      case MDNumberedList(xs) =>
        MDNumberedList(xs.map(x => normalise(x).asInstanceOf[MDListItem]))
      case MDListItem(xs) => MDListItem(normaliseInner(xs))
      case e => e
    }

  }


  object Main {
    def usage() {
      println("Usage: scala CW2Solution.jar <infile> <mode> <outfile>")
      println("<infile> is the input file name, or RANDOM to generate a random test")
      println("<mode> is one of \"html\", \"md\" or \"latex\", and defaults to \"md\"")
      println("<outfile> is optional and defaults to \"output\"")
    }

    def getInput(infile: String): MiniMDExpr = {
      if (infile == "RANDOM") {
        println("Generating random test file...")
        Rng.genMiniMDExpr(100).get()
      }
      else {
        println("Reading " + infile)
        MiniMDParser.parse(infile)
      }
    }

    def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
      val p = new java.io.PrintWriter(f)
      try { op(p) } finally { p.close() }
    }

    def writeHtml(outfile:String, html: String) {
      println("Writing " + outfile)
      printToFile(new java.io.File(outfile)) { out =>
        out.write("<html>\n<body>\n<!-- Beginning of MiniMD code -->\n")
        out.write(html)
        out.write("<!-- End of MiniMD code -->\n</body>\n")
      }
    }

    def writeMd(outfile: String, md: String) {
      println("Writing " + outfile)
      printToFile(new java.io.File(outfile)) { out =>
        out.write(md)
      }
    }

    def writeLatex(outfile: String, latex: String) {
      println("Writing " + outfile)
      printToFile(new java.io.File(outfile)) { out =>
        out.write("\\documentclass{article}\n" +
          "\\usepackage[colorlinks=true]{hyperref}\n" +
          "\\begin{document}\n" +
          "%%% Beginning of MiniMD content\n")
        out.write(latex)
        out.write("%%% End of MiniMD content\n" +
          "\\end{document}\n")
      }
    }

  }
  def main(args: Array[String]): Unit = {
    if (args.length >= 1 ) {
      val infile = args(0)
      val mode =  if (args.length >= 2) { args(1) } else { "md" }
      // Check that outfile is one of html, md or latex
      if (mode == "html" || mode == "md" || mode == "latex") {

        val parsed = Main.getInput(infile)

        println(parsed)

        mode match {
          case "html" => {
            println("Generating HTML...")
            val genHtml = print(HTMLFormatter.format(parsed))
            println(genHtml)
            if (args.length >= 3) {
              Main.writeHtml(args(2), genHtml)
            }
          }
          case "md" => {
            println("Generating MiniMD...")
            val genMd = print(MarkdownFormatter.format(parsed))
            println(genMd)
            if (args.length >= 3) {
              Main.writeMd(args(2), genMd)
            }
          }
          case "latex" => {
            println("Generating LaTeX...")
            val genLatex = print(LatexFormatter.format(parsed))
            println(genLatex)
            if (args.length >= 3) {
              Main.writeLatex(args(2),genLatex)
            }
          }
        }
      } else {
        Main.usage()
      }
    } else {
      Main.usage()
    }
  }
}
