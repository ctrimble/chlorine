package com.github.ctrimble.chlorine.demo.war;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

@SuppressWarnings("serial")
public class DemoServlet extends HttpServlet {
	private static final TransformerFactory TRANSFORMER_FACTORY = new net.sf.saxon.TransformerFactoryImpl();
	private Templates templates = null;
	
	 public void init(ServletConfig config) throws ServletException {
		 try {
			StreamSource source = new StreamSource("context:/skin.xsl");
			templates = TRANSFORMER_FACTORY.newTemplates(source) ;
		} catch (TransformerConfigurationException e) {
			throw new ServletException("Could not load skin xslt.", e);
		}
	 }
	
	@Override
	public void doGet( HttpServletRequest req, HttpServletResponse res )
	  throws ServletException
	{
		String path = req.getServletPath();
		try {
			res.setContentType("application/xhtml+xml;charset=UTF-8");
	        StreamSource source = new StreamSource("context:"+path);
	        StreamResult result;

	      	result = new StreamResult(res.getOutputStream());
	      	
	        Transformer transform = templates.newTransformer();
	        transform.transform(source, result);
		} catch (Exception e) {
			throw new ServletException("Could not render path "+path, e);
		}
	}
}
