package filter.parameter;

public class ParameterTool {
	
	public static String Html2Text(String inputString)
	  {
	    String htmlStr = inputString;
	    String textStr = "";
	    try {
	      htmlStr = htmlStr.replace("having ", "").replace("and ", "").replace("and+", "").replace("and%20", "").replace("exec ", "").replace("select ", "").replace("delete ", "").replace("update ", "").replace("count ", "");
	      htmlStr = htmlStr.replace("chr ", "").replace("mid ", "").replace("master ", "").replace("truncate ", "").replace("char ", "").replace("declare ", "");
	      htmlStr = htmlStr.replace("or ", "").replace("* ", "").replace("%", "").replace("&", "").replace("$", "").replace(";", "");
	      htmlStr = htmlStr.replace("- ", "").replace("+", "").replace(",", "").replace("'", "").replace("\\ ", "").replace("\\' ", "");
	      htmlStr = htmlStr.replace("\\\"", "").replace("=", "").replace("\"", "").replace("/", "").replace("(", "").replace(")", "");
	      htmlStr = htmlStr.replace("<", "&lt;").replace(">", "&gt;");
	      htmlStr = htmlStr.replace("@", "#");
	      textStr = htmlStr;

	      System.out.println("returned string: " + textStr);
	    } catch (Exception e) {
	      System.err.println("Html2Text: " + e.getMessage());
	    }

	    return textStr;
	  }

}
