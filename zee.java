import java.util.*;
import java.io.*;

public class zee {
    Map <Integer, SortedMap <Integer,String>> locGene;
	Map <Integer, SortedMap <Integer,String>> locRsid;
    
    public zee() {
        locGene = new HashMap<Integer, SortedMap <Integer,String>>();
		locRsid = new HashMap<Integer, SortedMap <Integer,String>>(); 
    }

	public boolean isParsable(String s) {
		if  (Character.isDigit(s.charAt(0))) {
			if(Integer.parseInt(s) <= 22)
				return true;
		}
		return false;
	}

	public void readFile(String location) {				
		String line ="";
		try {
			FileReader fr = new FileReader(location);
			BufferedReader bf =new BufferedReader(fr);
			while ((line = bf.readLine()) != null) {
				String[] row=line.split("\t");
				// Read chromosomes only between 1 and 22
				if (isParsable(row[1])) {
					if (locGene.containsKey(Integer.parseInt(row[1]))) {
						locGene.get(Integer.parseInt(row[1])).put(Integer.parseInt(row[2]),row[3]);
						locRsid.get(Integer.parseInt(row[1])).put(Integer.parseInt(row[2]),row[0]);
					} else {
						SortedMap <Integer,String> sm = new TreeMap<Integer,String>();
						SortedMap <Integer,String> sm1 = new TreeMap<Integer,String>();
						sm.put(Integer.parseInt(row[2]),row[3]);
						sm1.put(Integer.parseInt(row[2]),row[0]);
						locGene.put(Integer.parseInt(row[1]),sm);	
						locRsid.put(Integer.parseInt(row[1]),sm1);
					}			
				}
			}
			bf.close();
		} catch(IOException e) {
		  System.out.println("Exception in reading at "+location);
		  e.printStackTrace();
	    }	
    }

    public static void main(String[] args) {
        for(int k = 0; k < 10; k++) {
          zee z = new zee();
          long bef = System.currentTimeMillis();
          z.readFile(args[0]);
          long aft = System.currentTimeMillis();
          long fz = new File(args[0]).length();
          // we need the leave time for the JIT
          if(k >= 7)
            System.out.format("speed: %.3f GB/s\n",fz/(1024.0*1024.0*1024.0)/((aft-bef)/1000.0));
        }
    }

}