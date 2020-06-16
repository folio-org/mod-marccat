package org.folio.marccat.util;

import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.w3c.dom.Document;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;


/**
 * XML Utils.
 *
 * @author wimc
 * author cchiama
 * @since 1.0
 */
public class XmlUtils {

  private static final ThreadLocal <TransformerFactory> FACTORIES = ThreadLocal.withInitial(TransformerFactory::newInstance);

  private static final Log LOGGER = new Log(XmlUtils.class);

  private XmlUtils() {
    throw new IllegalStateException("Utility class");
  }
  /**
   * Returns a string representation of the given XML document.
   *
   * @param document the XML Document.
   * @return a string representation of the given XML document.
   */
  public static String documentToString(final Document document) {
    final StringBuilder builder = new StringBuilder();

    if (document != null) {
      try {
        final Transformer transformer = FACTORIES.get().newTransformer();
        final DOMSource source = new DOMSource(document);
        final StringWriter writer = new StringWriter();

        transformer.transform(source, new StreamResult(writer));

        builder.append(writer.getBuffer());
      } catch (final TransformerException transformerException) {
        LOGGER.error(Message.MOD_MARCCAT_00024_XSLT_FAILURE, transformerException);
      }
    }
    return builder.toString();
  }

}
