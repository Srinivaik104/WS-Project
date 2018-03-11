package se.project.id2208.server;

public class AirportController {
  // DEFINE SINGLETON OF THIS CLASS
  private final static AirportController instance = new AirportController();

  // DEFINE AIRPORTS
  private final Airport jfk = new Airport("jfk");
  private final Airport arn = new Airport("arn", "http://dbpedia.org/resource/Stockholm");
  private final Airport fco = new Airport("fco");
  private final Airport blr = new Airport("blr");

  private AirportController() {
    String sas = "http://dbpedia.org/data/SAS_Group";
    String indianAirlines = "http://dbpedia.org/data/Indian_Airlines";
    String aa = "http://dbpedia.org/data/American_Airlines";

    // DEFINE FLIGHTS
    jfk.addFlight(arn, sas);
    arn.addFlight(fco, indianAirlines);
    arn.addFlight(blr, indianAirlines);
    fco.addFlight(jfk, aa);
    fco.addFlight(arn, sas);
    fco.addFlight(blr, indianAirlines);
    blr.addFlight(jfk, aa);
  }

  public static AirportController getInstance() {
    return instance;
  }

  public Airport getAirport(String airport) {
    switch (airport) {
      case "jfk": return jfk;
      case "arn": return arn;
      case "fco": return fco;
      case "blr": return blr;
      default:
        throw new IllegalArgumentException(String.format("The airport: %s is invalid", airport));
    }
  }
}
