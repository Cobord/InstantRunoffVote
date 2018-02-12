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
    //hashset each of the items is a voter, and each voter is a deque of preferences
    public VoteTallyer()
    {
        everyonesVotes = new HashSet<Deque<String>>();
    }
    //Add a voter which is given as a deque of their preferences in order
    public void addVoter(Deque<String> aVoter){
        everyonesVotes.add(aVoter);
        totalVoters++;
    }
    //Remove a voter
    private void removeVoter(Deque<String> aVoter){
        everyonesVotes.remove(aVoter);
        totalVoters--;
    }
    //Make a deep copy of everyone's votes
    private void makeCopy(){
    	copyOfeveryonesVotes=new HashSet<Deque<String>>();
    	Iterator<Deque<String>> iter=everyonesVotes.iterator();
    	Deque<String> currentVoter;
        while (iter.hasNext()){
            currentVoter=iter.next();
            copyOfeveryonesVotes.add(new ArrayDeque<String>(currentVoter));
        }
    }
    //Delete the copy
    public void nullifyCopy(){
    	copyOfeveryonesVotes=null;
    }
    //Do Instant Runoff Vote and return a Deque of the election results all the way down to last place
    //This is done by doing IRV to get the actual winner, then eliminating them and trying again
    //Repeat until all the candidates are sorted
    public Deque<String> findAggregatePref(){
    	//where the results of the election will go
    	Deque<String> aggregatePref=new ArrayDeque<String>();
    	//store the candidates who have already been eliminated
    	Set<String> eliminated=new HashSet<String>();
    	try{
    		//Find the winner based on nobody eliminated yet
	    	String currentWinner=findWinner(eliminated);
	    	while(currentWinner!=null)
	    	{
	    		aggregatePref.addLast(currentWinner);
	    		//findWinner modifies eliminated because it is passed by reference
	    		//so we need to remake it for the actual eliminated 
	    		eliminated=new HashSet<String>();
	    		eliminated.addAll(aggregatePref);
	    		currentWinner=findWinner(eliminated);
	    	}
    	}
    	catch(Exception e){}
    	return aggregatePref;
    }
    //If nobody eliminated, then put in an empty hashset for the eliminated
    public String findWinner(){
    	return findWinner(new HashSet<String>());
    }
    //given the candidates already eliminated, find the winner 
    public String findWinner(Set<String> eliminated){
        String winner=null;
        HashMap<String,Integer> tally;
        int mostvotes;
        int leastvotes;
        String plurality=null;
        makeCopy();
        //If everyone is eliminated this will fall into an infinite loop but the calling function
        //		will stop before this for valid everyonesVotes
        while (winner==null){
            tally=tallyVotes(eliminated);
            //finds the candidate with the most votes. They have mostvotes
            //	and their name is plurality
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
            	//find the candidate with the least votes. put them as plurality even though
            	//		the name no longer fits. But variable isn't being used anymore.
                leastvotes=Collections.min(tally.values());
                for (Map.Entry<String,Integer> entry : tally.entrySet()){
                    if (entry.getValue()==leastvotes){
                        plurality=entry.getKey();
                        break;
                    }
                }
                //this copy of eliminated does both people eliminated because they did too well
                //		and are out of consideration for 2nd,3rd etc places
                //		as well as those eliminated for doing too poorly in the current iteration
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
        //Iterate through all the voters
        while (iter.hasNext()){
            currentVoter=iter.next();
            currentVote=currentVoter.peek();
            //if the currentVoter wanted someone eliminated, look at the next choices
            while(eliminated!=null&&eliminated.contains(currentVote))
            {
                currentVoter.remove(currentVote);
                currentVote=currentVoter.peek();
            }
            if(currentVote!=null){
	            oldcount=counter.get(currentVote);
	            //no previous voter wanted currentVote yet so it is not in counter
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