import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class VoteTallyer {
    private Set<Deque<String>> everyonesVotes;
    private Set<Deque<String>> copyOfeveryonesVotes;
    private int totalVoters=0;
    public VoteTallyer()
    {
        everyonesVotes = new HashSet<Deque<String>>();
    }
    public void addVoter(Deque<String> aVoter){
        everyonesVotes.add(aVoter);
        totalVoters++;
    }
    private void removeVoter(Deque<String> aVoter){
        everyonesVotes.remove(aVoter);
        totalVoters--;
    }
    private void makeCopy(){
    	copyOfeveryonesVotes=new HashSet<Deque<String>>();
    	Iterator<Deque<String>> iter=everyonesVotes.iterator();
    	Deque<String> currentVoter;
        while (iter.hasNext()){
            currentVoter=iter.next();
            copyOfeveryonesVotes.add(new ArrayDeque<String>(currentVoter));
        }
    }
    public void nullifyCopy(){
    	copyOfeveryonesVotes=null;
    }
    public Deque<String> findAggregatePref(){
    	Deque<String> aggregatePref=new ArrayDeque<String>();
    	Set<String> eliminated=new HashSet<String>();
    	try{
	    	String currentWinner=findWinner(eliminated);
	    	while(currentWinner!=null)
	    	{
	    		aggregatePref.addLast(currentWinner);
	    		eliminated=new HashSet<String>();
	    		eliminated.addAll(aggregatePref);
	    		currentWinner=findWinner(eliminated);
	    	}
    	}
    	catch(Exception e){}
    	return aggregatePref;
    }
    public String findWinner(){
    	return findWinner(new HashSet<String>());
    }
    public String findWinner(Set<String> eliminated){
        String winner=null;
        HashMap<String,Integer> tally;
        int mostvotes;
        int leastvotes;
        String plurality=null;
        makeCopy();
        while (winner==null){
            tally=tallyVotes(eliminated);
            mostvotes = Collections.max(tally.values());
            for (Map.Entry<String,Integer> entry : tally.entrySet()){
                if (entry.getValue()==mostvotes){
                    plurality=entry.getKey();
                    break;
                }
            }
            System.out.println("The tally so far is "+tally);
            if(mostvotes*2>=totalVoters){
                winner=plurality;
                nullifyCopy();
                System.out.println(winner+" Won!");
                return winner;
            }
            else {
                leastvotes=Collections.min(tally.values());
                for (Map.Entry<String,Integer> entry : tally.entrySet()){
                    if (entry.getValue()==leastvotes){
                        plurality=entry.getKey();
                        break;
                    }
                }
                eliminated.add(plurality);
                System.out.println(plurality+" got eliminated");
            }
        }
        nullifyCopy();
        return winner;
    }
    public HashMap<String,Integer> tallyVotes(Set<String> eliminated){
    	Deque<String> currentVoter;
        HashMap<String,Integer> counter=new HashMap<String,Integer>();
        String currentVote;
        if(copyOfeveryonesVotes==null)
        {
        	makeCopy();
        }
        Iterator<Deque<String>> iter=copyOfeveryonesVotes.iterator();
        Integer oldcount;
        while (iter.hasNext()){
            currentVoter=iter.next();
            currentVote=currentVoter.peek();
            while(eliminated!=null&&eliminated.contains(currentVote))
            {
                currentVoter.remove(currentVote);
                currentVote=currentVoter.peek();
            }
            if(currentVote!=null){
	            oldcount=counter.get(currentVote);
	            if(oldcount==null){
	                oldcount=0;
	            }
	            counter.put(currentVote,oldcount+1);
	        }
        }
        return counter;
    }
    public static void main(String[] args){
    	VoteTallyer lion=new VoteTallyer();
    	Deque<String> squirrel;
    	
    	squirrel=new ArrayDeque<String>();
    	squirrel.add("Turtle");
    	squirrel.add("Owl");
    	squirrel.add("Gorilla");
    	squirrel.add("Leopard");
    	squirrel.add("Tiger");
    	lion.addVoter(squirrel);
    	System.out.println(squirrel);
    	
    	squirrel=new ArrayDeque<String>();
    	squirrel.add("Owl");
    	squirrel.add("Turtle");
    	squirrel.add("Gorilla");
    	squirrel.add("Leopard");
    	squirrel.add("Tiger");
    	lion.addVoter(squirrel);
    	System.out.println(squirrel);
    	
    	squirrel=new ArrayDeque<String>();
    	squirrel.add("Owl");
    	squirrel.add("Turtle");
    	squirrel.add("Gorilla");
    	squirrel.add("Leopard");
    	squirrel.add("Tiger");
    	lion.addVoter(squirrel);
    	System.out.println(squirrel);
    	
    	squirrel=new ArrayDeque<String>();
    	squirrel.add("Tiger");
    	squirrel.add("Owl");
    	squirrel.add("Turtle");
    	squirrel.add("Gorilla");
    	squirrel.add("Leopard");
    	lion.addVoter(squirrel);
    	System.out.println(squirrel);
    	
    	squirrel=new ArrayDeque<String>();
    	squirrel.add("Tiger");
    	squirrel.add("Owl");
    	squirrel.add("Turtle");
    	squirrel.add("Gorilla");
    	squirrel.add("Leopard");
    	lion.addVoter(squirrel);
    	System.out.println(squirrel);
    	
    	squirrel=new ArrayDeque<String>();
    	squirrel.add("Leopard");
    	System.out.println(squirrel);
    	lion.addVoter(squirrel);
    	
    	System.out.println(lion.tallyVotes(null));
    	
    	Set<String> eliminated=new HashSet<String>();
    	eliminated.add("Tiger");
    	lion.nullifyCopy();
    	System.out.println(lion.tallyVotes(eliminated));
    	
    	eliminated.add("Owl");
    	System.out.println(lion.tallyVotes(eliminated));
    	
    	eliminated.add("Turtle");
    	System.out.println(lion.tallyVotes(eliminated));
    	
    	eliminated.add("Gorilla");
    	System.out.println(lion.tallyVotes(eliminated));
    	
    	eliminated=new HashSet<String>();
    	eliminated.add("Owl");
    	lion.nullifyCopy();
    	System.out.println(lion.tallyVotes(eliminated));
    	
    	eliminated=new HashSet<String>();
    	System.out.println(lion.findWinner(eliminated));
    	
    	System.out.println("Aggregate Pref");
    	System.out.println(lion.findAggregatePref());
    }
}