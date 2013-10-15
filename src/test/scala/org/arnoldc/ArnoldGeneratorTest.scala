package org.arnoldc

import org.scalatest.{BeforeAndAfter, FlatSpec}
import java.io.{ByteArrayOutputStream, PrintStream}
import org.scalatest.matchers.ShouldMatchers
import org.parboiled.errors.ParsingException

class ArnoldGeneratorTest extends FlatSpec with ShouldMatchers with BeforeAndAfter {

  var arnoldGenerator: ArnoldGenerator = new ArnoldGenerator()
  val template = new Template
  var className = "Hello"

  before {
    SymbolTable.clear()
  }
  "ArnoldGenerator" should "function when a variable is declared" in {
    val code: String =
      "ITS SHOWTIME\n" +
        "HEY CHRISTMAS TREE A\n" +
        "YOU SET US UP 123\n" +
        "YOU HAVE BEEN TERMINATED\n"
    arnoldGenerator.generate(code, className)
  }

  it should "function when an Int is printed" in {
    val code: String =
      "ITS SHOWTIME\n" +
        "TALK TO THE HAND 123\n" +
        "YOU HAVE BEEN TERMINATED\n"
    template.getOutput(code) should equal("123\n")
  }

  it should "function when a Int is declared and printed" in {
    val code: String =
      "ITS SHOWTIME\n" +
        "HEY CHRISTMAS TREE A\n" +
        "YOU SET US UP 999\n" +
        "HEY CHRISTMAS TREE B\n" +
        "YOU SET US UP 555\n" +
        "TALK TO THE HAND A\n" +
        "TALK TO THE HAND B\n" +
        "YOU HAVE BEEN TERMINATED\n"
    template.getOutput(code) should equal("999\n555\n")
  }

  it should "function when assigning a variable" in {
    val input =
      "ITS SHOWTIME\n" +
        "HEY CHRISTMAS TREE VAR\n" +
        "YOU SET US UP 22\n" +
        "GET TO THE CHOPPER VAR\n" +
        "HERE IS MY INVITATION 123\n" +
        "ENOUGH TALK\n" +
        "TALK TO THE HAND VAR\n" +
        "YOU HAVE BEEN TERMINATED\n"

    template.getOutput(input) should equal("123\n")
  }


  it should "function when assigning multiple variables " in {
    val input =
      "ITS SHOWTIME\n" +
        "HEY CHRISTMAS TREE VAR\n" +
        "YOU SET US UP 22\n" +
        "HEY CHRISTMAS TREE VAR2\n" +
        "YOU SET US UP 27\n" +
        "GET TO THE CHOPPER VAR\n" +
        "HERE IS MY INVITATION 123\n" +
        "ENOUGH TALK\n" +
        "GET TO THE CHOPPER VAR2\n" +
        "HERE IS MY INVITATION 707\n" +
        "ENOUGH TALK\n" +
        "GET TO THE CHOPPER VAR\n" +
        "HERE IS MY INVITATION VAR2\n" +
        "GET UP VAR\n" +
        "ENOUGH TALK\n" +
        "TALK TO THE HAND VAR\n" +
        "YOU HAVE BEEN TERMINATED\n"

    template.getOutput(input) should equal("830\n")
  }

  it should "function when a Int is incremented and printed" in {
    val code =
      "ITS SHOWTIME\n" +
        "HEY CHRISTMAS TREE VAR\n" +
        "YOU SET US UP 22\n" +
        "GET TO THE CHOPPER VAR\n" +
        "HERE IS MY INVITATION VAR\n" +
        "GET UP 44\n" +
        "ENOUGH TALK\n" +
        "TALK TO THE HAND VAR\n" +
        "YOU HAVE BEEN TERMINATED\n"
    template.getOutput(code) should equal("66\n")
  }

  it should "function when a Int is decremented and printed" in {
    val code =
      "ITS SHOWTIME\n" +
        "HEY CHRISTMAS TREE VAR\n" +
        "YOU SET US UP 22\n" +
        "GET TO THE CHOPPER VAR\n" +
        "HERE IS MY INVITATION VAR\n" +
        "GET DOWN 44\n" +
        "ENOUGH TALK\n" +
        "TALK TO THE HAND VAR\n" +
        "YOU HAVE BEEN TERMINATED\n"
    template.getOutput(code) should equal("-22\n")
  }

  it should "work using arithmetic operations" in {
    val code =
      "ITS SHOWTIME\n" +
        "HEY CHRISTMAS TREE VAR\n" +
        "YOU SET US UP 22\n" +
        "GET TO THE CHOPPER VAR\n" +
        "HERE IS MY INVITATION 11\n" +
        "GET DOWN 43\n" +
        "GET UP 54\n" +
        "GET UP 44\n" +
        "ENOUGH TALK\n" +
        "TALK TO THE HAND VAR\n" +
        "YOU HAVE BEEN TERMINATED\n"
    template.getOutput(code) should equal("66\n")
  }

  it should "work using arithmetic operations vol2" in {
    val code =
      "ITS SHOWTIME\n" +
        "HEY CHRISTMAS TREE VAR\n" +
        "YOU SET US UP 22\n" +
        "GET TO THE CHOPPER VAR\n" +
        "HERE IS MY INVITATION 11\n" +
        "GET DOWN 55\n" +
        "GET UP 11\n" +
        "GET UP 22\n" +
        "GET UP 23\n" +
        "GET DOWN 0\n" +
        "GET UP 0\n" +
        "GET UP 1\n" +
        "ENOUGH TALK\n" +
        "TALK TO THE HAND VAR\n" +
        "YOU HAVE BEEN TERMINATED\n"
    template.getOutput(code) should equal("13\n")
  }

  it should "False Or True Evaluate True" in {
    val code = "ITS SHOWTIME\n" +
      "RIGHT? WRONG! VAR\n" +
      "YOU SET US UP I LIED\n" +
      "GET TO THE CHOPPER VAR\n" +
      "I LIED\n" +
      "HE HAD TO SPLIT\n" +
      "NO PROBLEMO\n" +
      "ENOUGH TALK\n" +
      "TALK TO THE HAND VAR\n" +
      "YOU HAVE BEEN TERMINATED\n"
    template.getOutput(code) should equal("1\n")
  }

  it should "True Or False Evaluate True" in {
    val code = "ITS SHOWTIME\n" +
      "RIGHT? WRONG! VAR\n" +
      "YOU SET US UP I LIED\n" +
      "GET TO THE CHOPPER VAR\n" +
      "NO PROBLEMO\n" +
      "HE HAD TO SPLIT\n" +
      "I LIED\n" +
      "ENOUGH TALK\n" +
      "TALK TO THE HAND VAR\n" +
      "YOU HAVE BEEN TERMINATED\n"
    template.getOutput(code) should equal("1\n")
  }

  it should "True Or True Evaluate True" in {
    val code = "ITS SHOWTIME\n" +
      "RIGHT? WRONG! VAR\n" +
      "YOU SET US UP I LIED\n" +
      "GET TO THE CHOPPER VAR\n" +
      "NO PROBLEMO\n" +
      "HE HAD TO SPLIT\n" +
      "NO PROBLEMO\n" +
      "ENOUGH TALK\n" +
      "TALK TO THE HAND VAR\n" +
      "YOU HAVE BEEN TERMINATED\n"
    template.getOutput(code) should equal("1\n")
  }

  it should "False Or False Evaluate False" in {
    val code = "ITS SHOWTIME\n" +
      "RIGHT? WRONG! VAR\n" +
      "YOU SET US UP I LIED\n" +
      "GET TO THE CHOPPER VAR\n" +
      "I LIED\n" +
      "HE HAD TO SPLIT\n" +
      "I LIED\n" +
      "ENOUGH TALK\n" +
      "TALK TO THE HAND VAR\n" +
      "YOU HAVE BEEN TERMINATED\n"
    template.getOutput(code) should equal("0\n")
  }

  it should "False And True Evaluate False" in {
    val code = "ITS SHOWTIME\n" +
      "RIGHT? WRONG! VAR\n" +
      "YOU SET US UP I LIED\n" +
      "GET TO THE CHOPPER VAR\n" +
      "I LIED\n" +
      "KNOCK KNOCK\n" +
      "NO PROBLEMO\n" +
      "ENOUGH TALK\n" +
      "TALK TO THE HAND VAR\n" +
      "YOU HAVE BEEN TERMINATED\n"
    template.getOutput(code) should equal("0\n")
  }

  it should "True And False Evaluate False" in {
    val code = "ITS SHOWTIME\n" +
      "RIGHT? WRONG! VAR\n" +
      "YOU SET US UP I LIED\n" +
      "GET TO THE CHOPPER VAR\n" +
      "NO PROBLEMO\n" +
      "KNOCK KNOCK\n" +
      "I LIED\n" +
      "ENOUGH TALK\n" +
      "TALK TO THE HAND VAR\n" +
      "YOU HAVE BEEN TERMINATED\n"
    template.getOutput(code) should equal("0\n")
  }

  it should "True And True Evaluate True" in {
    val code = "ITS SHOWTIME\n" +
      "RIGHT? WRONG! VAR\n" +
      "YOU SET US UP I LIED\n" +
      "GET TO THE CHOPPER VAR\n" +
      "NO PROBLEMO\n" +
      "KNOCK KNOCK\n" +
      "NO PROBLEMO\n" +
      "ENOUGH TALK\n" +
      "TALK TO THE HAND VAR\n" +
      "YOU HAVE BEEN TERMINATED\n"
    template.getOutput(code) should equal("1\n")
  }

  it should "False And False Evaluate False" in {
    val code = "ITS SHOWTIME\n" +
      "RIGHT? WRONG! VAR\n" +
      "YOU SET US UP I LIED\n" +
      "GET TO THE CHOPPER VAR\n" +
      "I LIED\n" +
      "KNOCK KNOCK\n" +
      "I LIED\n" +
      "ENOUGH TALK\n" +
      "TALK TO THE HAND VAR\n" +
      "YOU HAVE BEEN TERMINATED\n"
    template.getOutput(code) should equal("0\n")
  }
  it should "False Equals False evaluates True" in {
    val code =
      "ITS SHOWTIME\n" +
        "RIGHT? WRONG! VARFALSE\n" +
        "YOU SET US UP I LIED\n" +
        "RIGHT? WRONG! VARFALSE2\n" +
        "YOU SET US UP I LIED\n" +
        "GET TO THE CHOPPER VARFALSE\n" +
        "I LIED\n" +
        "YOU ARE NOT YOU YOU ARE ME VARFALSE2\n" +
        "ENOUGH TALK\n" +
        "TALK TO THE HAND VARFALSE\n" +
        "YOU HAVE BEEN TERMINATED\n"
    template.getOutput(code) should equal("1\n")
  }
  it should "True Equals False evaluates False" in {
    val code =
      "ITS SHOWTIME\n" +
        "RIGHT? WRONG! VARFALSE\n" +
        "YOU SET US UP I LIED\n" +
        "RIGHT? WRONG! VARFALSE2\n" +
        "YOU SET US UP I LIED\n" +
        "GET TO THE CHOPPER VARFALSE\n" +
        "NO PROBLEMO\n" +
        "YOU ARE NOT YOU YOU ARE ME VARFALSE2\n" +
        "ENOUGH TALK\n" +
        "TALK TO THE HAND VARFALSE\n" +
        "YOU HAVE BEEN TERMINATED\n"
    template.getOutput(code) should equal("0\n")
  }

  it should "1 Equals 2 evaluates False" in {
    val code =
      "ITS SHOWTIME\n" +
        "HEY CHRISTMAS TREE ONE\n" +
        "YOU SET US UP 1\n" +
        "HEY CHRISTMAS TREE TWO\n" +
        "YOU SET US UP 2\n" +
        "RIGHT? WRONG! RESULT\n" +
        "YOU SET US UP NO PROBLEMO\n" +
        "GET TO THE CHOPPER RESULT\n" +
        "HERE IS MY INVITATION ONE\n" +
        "YOU ARE NOT YOU YOU ARE ME TWO\n" +
        "ENOUGH TALK\n" +
        "TALK TO THE HAND RESULT\n" +
        "YOU HAVE BEEN TERMINATED\n"
    template.getOutput(code) should equal("0\n")
  }

  it should "function when printing booleans" in {
    //TODO:
  }

  it should "detect if int is assigned to boolean" in {
    val code =
      "ITS SHOWTIME\n" +
        "RIGHT? WRONG! BOOLEAN\n" +
        "YOU SET US UP 2\n" +
        "YOU HAVE BEEN TERMINATED\n"
    intercept[ParsingException] {
      template.getOutput(code)
    }
  }
  it should "detect if boolean is assigned to int" in {
    val code =
      "ITS SHOWTIME\n" +
        "HEY CHRISTMAS TREE BOOLEAN\n" +
        "YOU SET US UP I LIED\n" +
        "YOU HAVE BEEN TERMINATED\n"
    intercept[ParsingException] {
      template.getOutput(code)
    }
  }
  it should "detect duplicate variable declarations" in {
    val code =
      "ITS SHOWTIME\n" +
        "HEY CHRISTMAS TREE VAR\n" +
        "YOU SET US UP 22\n" +
        "HEY CHRISTMAS TREE VAR\n" +
        "YOU SET US UP 22\n" +
        "YOU HAVE BEEN TERMINATED\n"
    intercept[ParsingException] {
      template.getOutput(code)
    }
  }


  class Template extends ClassLoader {

    def getOutput(code: String): String = {
      val bytecode = arnoldGenerator.generate(code, className)
      val outputRedirectionStream = new ByteArrayOutputStream()
      val originalOutputStream = System.out
      System.setOut(new PrintStream(outputRedirectionStream))

      invokeMainMethod(bytecode)
      System.setOut(originalOutputStream)
      outputRedirectionStream.toString
    }

    def invokeMainMethod(bytecode: Array[Byte]) = {
      val template = new Template()
      val testClass = template.defineClass(className, bytecode, 0, bytecode.length)
      val testInstance = testClass.newInstance().asInstanceOf[ {def main(qwer: Array[String])}]
      testInstance.main(null)
    }
  }

}
