import java.util.*;
import java.io.*;
import java.util.stream.*; 

public class dan {

    static class GeneRsid implements Comparable<GeneRsid> {
        int identifier;
        char gene1; // e.g. B
        char gene2; // e.g. T
        String rsid; // e.g. rs32132

        @Override
        public int compareTo(GeneRsid e2) {
            return (identifier < e2.identifier) ? -1 : ((identifier == e2.identifier) ? 0 : 1);
        }
    }

    final static int GENOME_COUNT = 22; // we care only for genomes 1...22

    // each array will contain a sorted array (sorted by identifier)
    ArrayList<GeneRsid>[] locGene = new ArrayList[GENOME_COUNT + 1];
    public dan() {
        for(int k = 1; k <= GENOME_COUNT;k++) {
            locGene[k] = new ArrayList<GeneRsid>();
        }
    }


    public void parseLine(String s) {
        GeneRsid g = new GeneRsid();
        int index = 0;
        int len = s.length();
        for(; (index  < len) && (s.charAt(index) != '\t'); index++) {}
        g.rsid = s.substring(0, index);
        index++;
        // we should now be pointing a number...
        char c;
        c = s.charAt(index);
        int number1 = c & 0xF;
        index++;
        c = s.charAt(index);
        if(c != '\t') {
            number1 = (number1 << 3) + (number1 << 1) + (c & 0xF);
            index++;
        }
        if((number1 > GENOME_COUNT) || (number1 <=0)) return;
        ArrayList <GeneRsid> gen = locGene[number1];
        index++;
        // we should now be pointing at a number...
        int number2 = 0;
        for(;index  < len; index++) {
            c = s.charAt(index);
            if(c != '\t') {
                number2 = (number2 << 3) + (number2 << 1) + (c & 0xF);
            } else {
                break;
            }
        }
        g.identifier = number2;
        index++;
        g.gene1 =s.charAt(index);
        g.gene2 =s.charAt(index+1);
        gen.add(g);
    }

    public static StringBuffer scanFile(String location)  throws IOException {
        FileReader fr = new FileReader(location);
        BufferedReader bf = new BufferedReader(fr);
        StringBuffer sb = new StringBuffer();
        bf.lines().forEach(s -> sb.append(s));
        bf.close();
        return sb;
    }


    public void readFile(String location) throws IOException {				
            FileReader fr = new FileReader(location);
            BufferedReader bf = new BufferedReader(fr);
            bf.lines().forEach(s -> parseLine(s));
            bf.close();
            IntStream.range(1, GENOME_COUNT).parallel().forEach(x -> Collections.sort(locGene[x]));
    }

    public static void main(String[] args)  throws IOException  {
        for(int k = 0; k < 10; k++) {
          dan d = new dan();
          long bef = System.currentTimeMillis();
          d.readFile(args[0]);
          long aft = System.currentTimeMillis();
          long fz = new File(args[0]).length();
           // we need the leave time for the JIT
          if(k >= 7)
            System.out.format("speed: %.3f GB/s\n",fz/(1024.0*1024.0*1024.0)/((aft-bef)/1000.0));
        }
    }

}

