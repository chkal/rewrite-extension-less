package de.chkal.rewrite.less;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LessEngineTest
{

   @Test
   public void testCalculation()
   {
      String input = ".class { width: 1 + 1 }";
      String output = new LessEngine().process(input);
      assertSameCSS(".class { width: 2; }", output);
   }

   @Test
   public void testVariables()
   {
      String input = "@nice-blue: #5B83AD;\n.myblue{ color: @nice-blue; }";
      String output = new LessEngine().process(input);
      assertSameCSS(".myblue { color: #5B83AD; }", output);
   }

   @Test
   public void testMixins()
   {
      String input = ".bordered { border: 1px solid red; }\n.navigation { .bordered }\n";
      String output = new LessEngine().process(input);
      assertSameCSS(".bordered{ border: 1px solid red; }\n.navigation{ border: 1px solid red; }\n", output);
   }

   private static void assertSameCSS(String expected, String actual)
   {

      assertEquals(normalize(expected), normalize(actual));
   }

   private static String normalize(String s)
   {
      return s.toLowerCase().replaceAll("\\s+", "");
   }

}
