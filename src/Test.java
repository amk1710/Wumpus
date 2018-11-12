
import java.util.Map;

import org.jpl7.*;



public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	    //essa query "abre" o arquivo
		Query q1 =
	            new Query(
	                "consult",
	                new Term[] {new Atom("Prolog/test.pl")}
	            );
	    
	    
	    
	    
	    System.out.println( "consult " + (q1.hasSolution() ? "succeeded" : "failed"));
	    q1.close();
	    
	    
	    
	    Query q3 =
	            new Query(
	                "independent",
	                new Term[] {new Atom("mary")}
	            );
	    
	    System.out.println( "is mary independent? " + (q3.hasSolution() ? "yes" : "no"));
	    q3.close();
	    
	    
	    
	    Query q2 = new Query("descendent_of", new Term[] {new Atom("mary"), new Variable("X")});
	    while(q2.hasNext())
	    {
	    	Map binding = q2.next();
	    	Term t = (Term) binding.get("X");
	    	System.out.println(t);
	    }

	    
	    
	    /*Query q2 =
	            new Query(
	                "descendent_of",
	                new Term[] {new Atom("ralf"), new Variable("X")}
	            );
		
	    System.out.println( "consult " + (q2.hasSolution() ? "succeeded" : "failed"));*/
	    
	    
		/*for (Map m : new Query("descendent_of(ralf,X)")) {
		    System.out.println(m.get("X"));
		}*/

		
		
	}

}
