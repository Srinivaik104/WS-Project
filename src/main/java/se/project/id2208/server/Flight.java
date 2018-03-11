package se.project.id2208.server;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Holds various data for a flight.
 */
public class Flight extends RDFModel {
  private final Airport destination;
  private final String airline;

  public Flight(Airport destination, String airline) {
    this.destination = destination;
    this.airline = airline;
  }

  @Override
  String getResourceURI() {
    throw new NotImplementedException();
  }

  @Override
  protected Resource toRDF(OntModel ontModel, Model rdfModel) {
    Resource flight = rdfModel.createResource();

    flight.addProperty(RDF.type, ontModel.getOntClass(FlightOntology.Flight));
    flight.addProperty(ontModel.getProperty(FlightOntology.hasAirline), ontModel.createResource(airline));
    flight.addProperty(ontModel.getProperty(FlightOntology.hasDestination), ontModel.createResource(destination.getResourceURI()));

    return flight;
  }
}
