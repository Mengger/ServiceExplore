package com.pjo.build.in.object;

public class JspServletBean {

	public String packageContent=null;
	public StringBuffer importContent=new StringBuffer();
	public String jspName=null;
	public StringBuffer attribute=new StringBuffer();
	public StringBuffer jspServerMethod=new StringBuffer();
	public String responseEconding=null;
	
	public void init(){
		importContent.append("import javax.servlet.*;");
		importContent.append("\t\r");
		importContent.append("import javax.servlet.http.*;");
		importContent.append("\t\r");
		importContent.append("import javax.servlet.jsp.*;");
		importContent.append("\t\r");
		importContent.append("import java.io.PrintWriter;");
		importContent.append("\t\r");
	}
	public StringBuffer generateContent(){
		StringBuffer out=new StringBuffer();
		out.append(this.packageContent);
		out.append("\t\r");
		out.append(this.importContent);
		out.append("\t\r");
		out.append("public class "+jspName+" {");
		out.append("\t\r");
		out.append(attribute);
		out.append("\t\r");
		out.append("public void init(){}");
		out.append("\t\r");
		out.append("public void destroy(){}");	
		out.append("\t\r");	
		out.append("public void jspService(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {");
		out.append("\t\r");
		out.append("PrintWriter out = response.getWriter();");
		out.append("\t\r");
		out.append(jspServerMethod);
		out.append("\t\r");
		out.append("out.flush();");
		out.append("\t\r");
		out.append("out.close();");
		out.append("}");
		out.append("\t\r");
		out.append("}");
		return out;
	}
	
}
