import java.util.ArrayList;

public class Predicate {
	boolean neg;
	String prName;
	ArrayList<GenericT> termList;
	
	Predicate(boolean n, String pr, ArrayList<GenericT> tList ) {
		neg=n;
		prName = pr;
		termList = tList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (neg ? 1231 : 1237);
		result = prime * result + ((prName == null) ? 0 : prName.hashCode());
		result = prime * result + ((termList == null) ? 0 : termList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Predicate other = (Predicate) obj;
		if (neg != other.neg)
			return false;
		if (prName == null) {
			if (other.prName != null)
				return false;
		} else if (!prName.equals(other.prName))
			return false;
		if (termList == null) {
			if (other.termList != null)
				return false;
		} else if (!termList.equals(other.termList))
			return false;
		return true;
	}

	
	public boolean isSamePredicate(Predicate p2) {
		if (p2.prName == null) {
			if (prName != null)
				return false;
		}
		if(!(prName.equals(p2.prName)))
			return false;
		if(neg!=p2.neg) {
			return false;
		}
		if (termList == null) {
			if (p2.termList != null)
				return false;
		} else {
			if(termList.size() != p2.termList.size())
				return false;
		}
		return true;
	}	
	
	public boolean isNeg(Predicate p2) {
		if (p2.prName == null) {
			if (prName != null)
				return false;
		}
		if(!(prName.equals(p2.prName)))
			return false;
		if(neg==p2.neg) {
			return false;
		}
		return true;
	}

	
	public boolean isNegandEqual(Predicate p2) {
		if (p2.prName == null) {
			if (prName != null)
				return false;
		}
		if(!(prName.equals(p2.prName)))
			return false;
		if(neg==p2.neg) {
			return false;
		}
		else if (!termList.equals(p2.termList))
			return false;
		return true;
	}
	
	public boolean isNegandEqualConstants(Predicate p2) {
		boolean b = true;
		if (p2.prName == null) {
			if (prName != null)
				return false;
		}
		if(!(prName.equals(p2.prName)))
			return false;
		if(neg==p2.neg) {
			return false;
		}
		if(termList.size() != p2.termList.size())
			return false;
		else {
			for(int i =0; i<termList.size(); i++) {
				GenericT g1,g2;
				g1 = termList.get(i);
				g2 = termList.get(i);
				if(g1 instanceof Constant && g2 instanceof Constant) {
						if(! ((Constant)g1).cons.equals(((Constant)g2).cons))
							b=false;
					}
				}
			}
			if(b) return true;
			else
				return false;
	}
	
	@Override
	public String toString() {
		StringBuilder res = new StringBuilder("");
		res.append("Predicate: ");
		if(neg)
			res.append("~"); 
		res.append(" prName=" + prName + "(");
		for(int j=0; j< termList.size(); j++) {
			GenericT term =termList.get(j);
			if(term instanceof Variable)
				res.append(((Variable)term).retVar());
			else
				res.append(((Constant)term).retCons());
			res.append(",");
		}
		res.append(") \n");
		return res.toString();
	}
	
	
}
