import java.util.ArrayList;
import java.util.HashSet;

public class Clause {

	HashSet<Predicate> predList;

	public Clause(HashSet<Predicate> pList) {
		predList = new HashSet<Predicate>(pList);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((predList == null) ? 0 : predList.hashCode());
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
		Clause other = (Clause) obj;
		if (predList == null) {
			if (other.predList != null)
				return false;
		}
		if(predList.size()!= other.predList.size())
			return false;
		for(int i =0;i< predList.size(); i++) {
			if(!(predList.equals(other.predList)) )
					return false;
		}
		return true;
	}
	

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder("");
		//Predicate p = null;
		res.append("") ;
		char var; String cons;
		int l = predList.size();
		for(Predicate p: predList) {
			//p = predList.get(i);
			if(p.neg)
				res.append("~");
			res.append(p.prName+ "(");
			for(int j=0; j< p.termList.size(); j++) {
				GenericT term = p.termList.get(j);
				if(term instanceof Variable)
					res.append(((Variable)term).retVar());
				else
					res.append(((Constant)term).retCons());
				res.append(",");
			}
			res.append(") | ");
		}
		res.append("\n");
		return res.toString();
	}
}

//  Clause:  American(x) | ~Weapon(y) | ~Sells(x,y,z) |
//  Predicate:   Each thing
//	Predicate(term), Negation maybe.
//  Design:
//	Clause is list of predicates.
//  Predicates ->  {negation:boolean, String prName, List of terms.
//  Term -> Can be a var or constant.

class Variable implements GenericT{
	String var;
	Variable(String c) {
		var =c;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((var == null) ? 0 : var.hashCode());
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
		Variable other = (Variable) obj;
		if (var == null) {
			if (other.var != null)
				return false;
		} else if (!var.equals(other.var))
			return false;
		return true;
	}
	
	String retVar() {
		return var;
	}
}

class Constant implements GenericT{
	String cons;
	Constant(String s) {
		cons = s;
	}
	String retCons() {
		return cons;
	}
}
