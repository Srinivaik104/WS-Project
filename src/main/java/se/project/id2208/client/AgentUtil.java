package se.project.id2208.client;

import org.apache.jena.atlas.web.TypedInputStream;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.web.HttpOp;

abstract class AgentUtil {
  /**
   * Retrieves the model pointed to by the provided URI.
   *
   * @param uri The URI where the model resides.
   * @return The model pointed to by the URI.
   */
  Model getModel(String uri) {
    TypedInputStream inputStream = HttpOp.execHttpGet(uri, "application/rdf+xml");
    Model model = ModelFactory.createDefaultModel();
    model.read(inputStream, null);

    return model;
  }

  /**
   * Executes the provided query against the provided model.
   *
   * @param queryString The query to execute.
   * @param model The model to execute the query against.
   * @return The <code>ResultSet</code> of executing the query against the model.
   */
  ResultSet runQuery(String queryString, Model model) {
    Query query = QueryFactory.create(queryString);
    QueryExecution qexec = QueryExecutionFactory.create(query, model);

    return qexec.execSelect();
  }
}
