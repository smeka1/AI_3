import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Parse {

	public Clause parser(String s) {
		
		String[] tokens;
		HashSet<Predicate> pList= new HashSet<Predicate>();
		tokens = s.split("\\|");
		//System.out.println(tokens[0]+ " "+ tokens[1]);
		int i=0;
		int tl = tokens.length;
		for(i=0;i<tl;i++) {
			pList.add(makePred(tokens[i].trim()));
		}
		Clause cl = new Clause(pList);
		//System.out.println(cl.toString());
		return cl;
	}

	public Predicate makePred(String s) {
		int i=0, l = s.length();
		String pn = null; String list[];
		ArrayList<GenericT> gList = new ArrayList<GenericT>();
		boolean n = false;
		int stBrack =-1, endBrack = -1; 
		if(s.charAt(0) == '~') {
			n = true;
		}
		for(i=1; i<l; i++) {
			if(s.charAt(i) == '(') {
				pn= n?s.substring(1,i):s.substring(0,i);
				stBrack=i;
				continue;
			}
			
			if(s.charAt(i)==')') {
				endBrack = i;
				break;
			}
		}
		list = (s.substring(stBrack+1,endBrack).split(","));

		for(i=0; i< list.length; i++) {
			if( list[i].length()==1) {
				char ch = list[i].charAt(0);
				if( Character.isLowerCase(ch) )
					gList.add(new Variable(String.valueOf(ch)));
				else
					gList.add(new Constant(String.valueOf(ch)));
			}
			else
				gList.add(new Constant(list[i]));
		}
		return (new Predicate(n,pn,gList) );
	}

}