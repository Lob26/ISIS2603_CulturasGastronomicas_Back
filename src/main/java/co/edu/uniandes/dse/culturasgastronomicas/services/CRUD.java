package co.edu.uniandes.dse.culturasgastronomicas.services;

public enum CRUD {
    C("CREATE"),
    R_O("RETRIEVE_ONE"),
    R_A("RETRIEVE_ALL"),
    U("UPDATE"),
    D("DELETE"),
    A("ASSOCIATE"),
    A_M("ASSOCIATE_MANY");

    private final String msg;

    CRUD(String s) {this.msg = s;}

    @Override public String toString() {return msg;}
}