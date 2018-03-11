package se.project.id2208.server;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

/**
 * Base class for models using RDF.
 */
public abstract class RDFModel {
  final String NS = Util.ABS_RDF_URI;
  private Model model = null;

  /**
   * Creates an RDF model.
   *
   * @return The created RDF model.
   */
  Resource toRDF() {
    return toRDF(FlightOntology.getOntModel(), ModelFactory.createDefaultModel());
  }

  /**
   * Getter for the resource URI for the this model.
   *
   * @return The resource URI for this model.
   */
  abstract String getResourceURI();

  /**
   * Converts the current object to RDF.
   *
   * @param ontModel The ontology model.
   * @param rdfModel The rdf model.
   * @return This object as a resource.
   */
  abstract protected Resource toRDF(OntModel ontModel, Model rdfModel);

  private Model constructRDF() {
    Model rdfModel = ModelFactory.createDefaultModel();
    rdfModel.setNsPrefix("smp", Util.ABS_ONTOLOGY_NS);
    toRDF(FlightOntology.getOntModel(), rdfModel);

    return rdfModel;
  }

  /**
   * Returns the RDF model of this airport.
   *
   * @return The RDF model of this airport.
   */
  public Model getModel() {
    if (model == null) {
      model = constructRDF();
    }

    return model;
  }
}
