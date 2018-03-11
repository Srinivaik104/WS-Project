package se.project.id2208.server;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.apache.jena.atlas.web.TypedInputStream;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.web.HttpOp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Define common functionality and constants.
 */
public class Util {
  public static final String FLIGHT_ONTOLOGY_PATH = "FLIGHTS.owl";
  public static final String SERVER_URI = "http://localhost:8080";

  public static final String ONTOLOGY_URI = "/id2208/project/ontology/flights";
  public static final String ABS_ONTOLOGY_URI = SERVER_URI + "/id2208/project/ontology/flights";

  public static final String ONTOLOGY_NS = "/id2208/project/ontology/flights#";
  public static final String ABS_ONTOLOGY_NS = SERVER_URI + ONTOLOGY_NS;

  public static final String RDF_URI = "/id2208/project/rdf/";
  public static final String ABS_RDF_URI = SERVER_URI + RDF_URI;

  private static String readResource(String fileName, Charset charset) throws IOException {
    return Resources.toString(Resources.getResource(fileName), charset);
  }

  /**
   * Retrieves the ontology using the provided path.
   *
   * @param path The path where the ontology resides.
   * @return The ontology model.
   */
  public static OntModel readOntology(String path) {
    OntDocumentManager ontDocumentManager = new OntDocumentManager();
    OntModelSpec ontModelSpec = new OntModelSpec(OntModelSpec.OWL_MEM);
    ontModelSpec.setDocumentManager(ontDocumentManager);
    OntModel ontModel = ModelFactory.createOntologyModel(ontModelSpec, null);

    try {
      ontModel.read(new ByteArrayInputStream(readResource(path, Charsets.UTF_8).getBytes()), "RDF/XML");
    } catch (IOException e) {
      throw new IllegalArgumentException(String.format("Failed to read the file: %s", path));
    }

    return ontModel;
  }

  /**
   * Retrieves the ontology at the provided uri.
   *
   * @param uri Where the ontology resides.
   * @return The ontology model found at the uri.
   */
  public static OntModel readOntologyHttp(String uri) {
    TypedInputStream inputStream = HttpOp.execHttpGet(uri, "application/rdf+xml");
    OntDocumentManager ontDocumentManager = new OntDocumentManager();
    OntModelSpec ontModelSpec = new OntModelSpec(OntModelSpec.OWL_MEM);
    ontModelSpec.setDocumentManager(ontDocumentManager);
    OntModel ontModel = ModelFactory.createOntologyModel(ontModelSpec, null);
    ontModel.read(inputStream, null);

    return ontModel;
  }
}
