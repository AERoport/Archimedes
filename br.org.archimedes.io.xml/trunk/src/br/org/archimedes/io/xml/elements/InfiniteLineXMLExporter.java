package br.org.archimedes.io.xml.elements;

import java.io.IOException;
import java.io.OutputStream;

import br.org.archimedes.infiniteline.InfiniteLine;
import br.org.archimedes.interfaces.ElementExporter;
import br.org.archimedes.io.xml.XMLExporterHelper;

/**
 * Exporter for InfiniteLine.
 * 
 * @author werpinheiro
 */
public class InfiniteLineXMLExporter implements ElementExporter<InfiniteLine> {

	/**
	 * (non-Javadoc).
	 * 
	 * @see br.org.archimedes.interfaces.ElementExporter#exportElement(br.org.archimedes.model.Element,
	 *      java.lang.Object)
	 */
	public void exportElement(InfiniteLine element, Object outputObject)
			throws IOException {
	    OutputStream output = (OutputStream) outputObject;
	    
	    StringBuilder sb = new StringBuilder();
		sb.append("<infiniteline>"); //$NON-NLS-1$

		sb.append(XMLExporterHelper.xmlFor("point", element.getInitialPoint())); //$NON-NLS-1$
		sb.append(XMLExporterHelper.xmlFor("point", element.getEndingPoint())); //$NON-NLS-1$
		
		sb.append("</infiniteline>"); //$NON-NLS-1$

		output.write(sb.toString().getBytes());
	}

}