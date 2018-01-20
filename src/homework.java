import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Map.Entry;
import java.util.Queue;

public class homework {
	
	static String filename = "inputEx3.txt";
	static HashSet<Clause> KB= new HashSet<Clause>();
	static ArrayList<Predicate> queries= new ArrayList<Predicate>();
	//static ArrayList<Clause> KBcopy = new ArrayList<Clause>();
	
	//static HashMap<Variable, GenericT> theta= new HashMap<Variable, GenericT>();
	static void readInput(Parse p_obj) {
		BufferedReader br = null;
		int nq=0, ns=0;
		try {
			br = new BufferedReader(new FileReader(filename));    //("inputEx5_2.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			nq = Integer.parseInt(br.readLine().trim());
         	String line;
			for(int i =0; i <nq; i++) {
				line = br.readLine();
				Object[] parr= ((p_obj.parser(line)).predList.toArray());
				queries.add((Predicate)parr[0]);
					//System.out.print(queries.get(i).toString());
				}
			ns = Integer.parseInt(br.readLine().trim());
			//System.out.println("\n" + "KB sentences: ");
			for(int i =0; i <ns; i++) {
				line = br.readLine();
				Clause cl = (p_obj.parser(line));
				if(! KB.contains(cl))
					KB.add(cl);
				}
		} catch(Exception e) {System.out.println("In readInput "+e.toString());  }
		for (Clause cl : KB) {
			//System.out.print(cl.toString());
			}
		return;
	}
	
	
	public static void main(String[] args) {		
		//String s= "~first(x,y,z)| second(VX,x) | third(x,SFT)";
		Parse p_obj = new Parse();
		homework hwobj = new homework();
		//p_obj.parser(s);
		readInput(p_obj);
		//Copy of KB to resolve each query
		// To standardize:
		HashMap<Variable,Variable> std = new HashMap<Variable,Variable>();
		boolean[] resultArr = new boolean[queries.size()];
		// Send each query to resolve
		for(int i=0;i< queries.size(); i++) {
			resultArr[i] = hwobj.resolve(queries.get(i));
		//	System.out.println("RESULT:" + resultArr[i]);
		}
		
		FileWriter writer;
		BufferedWriter BW;
		try {
			writer = new FileWriter(new File("output.txt"));
			BW = new BufferedWriter(writer);
			for(int i=0;i< queries.size(); i++) {
				if(resultArr[i])		
					BW.write("TRUE");
				else
					BW.write("FALSE");
				BW.newLine();
				BW.flush();
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public HashMap<Variable,GenericT> unify(GenericT x,GenericT y,HashMap<Variable,GenericT> theta) {
		// if theta = failure then return failure
		if (theta == null) {
			return null;
		} else if (x.equals(y)) {
			// else if x = y then return theta
			return theta;
		} else if (x instanceof Variable) {
			// else if VARIABLE?(x) then return UNIVY-VAR(x, y, theta)
			return unifyVar((Variable) x, y, theta);
		} else if (y instanceof Variable) {
			// else if VARIABLE?(y) then return UNIFY-VAR(y, x, theta)
			return unifyVar((Variable) y, x, theta);
		}
		else {
			return null;
		}
	}
	
	public HashMap<Variable, GenericT> unifyVar(Variable var,GenericT x,HashMap<Variable,GenericT> theta) {
		if (!GenericT.class.isInstance(x)) {
			return null;
		} else if (theta.keySet().contains(var)) {
			// if {var/val} E theta then return UNIFY(val, x, theta)
			return unify(theta.get(var), x, theta);
		} else if (theta.keySet().contains(x)) {
			// else if {x/val} E theta then return UNIFY(var, val, theta)
			return unify(var, theta.get(x), theta);
		} else {
			// else return add {var/x} to theta
			theta.put(var,x);
			return theta;
		}
	}
	
	public HashMap<Variable,Variable> standardize( HashSet<Variable> hs1, HashSet<Variable> hs2) {
		//HashMap<Variable,Integer> v1I = new HashMap<Variable,Integer>();
		//HashMap<Variable,Integer> v2I = new HashMap<Variable,Integer>();
		HashMap<Variable,Variable> mapV = new HashMap<Variable,Variable>();
		//Iterator<Entry<Variable,Integer>> iter = v1I.entrySet().iterator(); //(v1I.size()>v2I.size())? v1I.entrySet().iterator() : v2I.entrySet().iterator();
		
		for( Variable v1: hs1 ) {
			if(hs2.contains(v1)) {
				//hs2.remove(v1);
				Variable newV = new Variable(v1.var+"1");
			if(newV.retVar().length() > 400)
            {
             FileWriter writer;
		BufferedWriter BW;
		try {
			writer = new FileWriter(new File("output.txt"));
			BW = new BufferedWriter(writer);
				BW.write("FALSE");
				BW.newLine();
				BW.flush();
         		System.exit(3);
			}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            }
             mapV.put(v1, newV);
				//hs2.add();
			}
		}
		
		if(mapV.size() ==0) 
			return null;
		return mapV;	
	}

	@SuppressWarnings("unchecked")
	public boolean resolve(Predicate query) {
		
		Queue<Clause> queue = new ArrayDeque<Clause>();
		Predicate negq = new Predicate( !query.neg , query.prName, query.termList );
		HashSet plist = new HashSet<Predicate>();
		HashSet<Clause> visited = new HashSet<Clause>();
		HashSet<Variable> hs1 = new HashSet<Variable>();
		HashSet<Variable> hs2 = new HashSet<Variable>();
		HashMap<Variable,Variable> mapV = new HashMap<Variable,Variable>();
		
		//Take the query and negate and put it in Queue.
		plist.add(negq);
		Clause NegQueryCl = new Clause(plist);
		visited.add(NegQueryCl);
		queue.add(NegQueryCl);

		while(!queue.isEmpty()) {
			Clause popped = queue.poll();
			HashSet<Predicate> poplist = popped.predList;
			for(Predicate poppedPr: poplist) {
				for(Clause clInKB : KB) {
					if(!clInKB.equals(popped)) {
						for(Predicate clausePr : clInKB.predList) {
							// Check 
							Clause tempClause = null;
							HashSet<Predicate> newClList=new HashSet<Predicate>();
							//Found negated equivalent
							if(clausePr.isNeg(poppedPr)) {
								//This clInKB and popped are the two clauses
								if(queue.size()>12000)
                                 return false;
								HashSet<Predicate> duplist = new HashSet<Predicate>(clInKB.predList.size());
								for(Predicate pr : clInKB.predList) {
									duplist.add(pr);
								}
								tempClause = new Clause(duplist);
								boolean canunify;
								HashMap<Variable,GenericT> substi = new HashMap<Variable,GenericT>();

								//Negation so std then resolve then ad  to Q
								//if(poppedPr.isNeg(clausePr)) {
								for(GenericT g : poppedPr.termList) {
									if(g instanceof Variable)
										hs1.add((Variable)g); 
								}
								for(GenericT g : clausePr.termList) {
									if(g instanceof Variable)
										hs2.add((Variable)g); 
								}

								//For clInKB->clausePr, std mapping is:
								if(hs1.size()!=0 && hs2.size()!=0)
									mapV = standardize(hs1,hs2);

								//Apply standard mapping to whole clause tempClause  ???
								if (mapV!=null) { 
									applyStandarize(tempClause.predList, mapV);
								}					

								// tempClause is standardized.
								//System.out.println("Aftr std , temp is "+tempClause.toString()+ "QSize:"+ queue.size());
								//System.out.println("Popped is "+popped.toString());

								// Traverse temp, popped, match predicates then unify
								ArrayList<GenericT> tempList = new ArrayList<GenericT>();

								//	for(Predicate tempPr : tempClause.predList) {
								//	for(Predicate popPruni : popped.predList) {
								//										
								//match predicates. not necessarily the Negations
								//	if(tempPr.prName.equals(poppedPr.prName)) {	

								for(int i=0 ; i <poppedPr.termList.size(); i++) {
									GenericT g1,g2;
									g1 = clausePr.termList.get(i);
									g2 = poppedPr.termList.get(i);
									// Check if both constants are already same. Eg:  ~K(C,T) with K(J,T) | K(C,T)
									if( (g1 instanceof Constant) && (g2 instanceof Constant)) {
										if(! ((Constant)g1).cons.equals(((Constant)g2).cons))
											break;
									}
									unify(g1,g2, substi);
								}
								
								// Apply substitution, form new clause and put in Q. 
								if(substi.size()!=0) {
									//tempClause.predList
									applySubsti(tempClause.predList,popped.predList,substi);
									//System.out.print("Aftr SUBSS , temp is "+tempClause.toString());
									//System.out.println("Popped is "+popped.toString() ); //+ " Constant equals "+ matclPr.isNegandEqualConstants(matclPr) );
									for(int i=0 ; i <poppedPr.termList.size(); i++) {
										tempList.add(poppedPr.termList.get(i));
									}

									//if same as resolving pred, then leave it  //chk temp
									//form new pred
									Predicate newPr = new Predicate(poppedPr.neg,poppedPr.prName,tempList);
									//System.out.println(newPr.equals(matclPr));

									//Destandardize
									for(GenericT g : poppedPr.termList) {		
										if(g instanceof Variable) {
											if(((Variable) g).retVar().length() > 1){
												((Variable)g).var = ((Variable)g).retVar().substring(0,1);
												System.out.print(((Variable)g).var + "   ");
											}
										}
									}
								} // Substi size !=0

								//else {
									//continue; //Unification was not done. next predicate.
								//}
								//}
								canunify=false;
								//Resolve and then destandardize then add to Q if not visited.
								for(Predicate pr1 : tempClause.predList) {
									boolean add = true;
									for(Predicate pr2 : popped.predList) {
										if((pr2.isNegandEqualConstants(pr1))) { 
										 canunify=true;  
                                         add= false;
										}
									}
									if(add) {
										newClList.add(pr1);
									}
								}
								for(Predicate pr1 : popped.predList) {
									boolean add = true;
									for(Predicate pr2 : tempClause.predList) {
										if((pr2.isNegandEqualConstants(pr1))) { 
										 canunify=true;  
                                         add= false;
										}
									}
									if(add) {
										newClList.add(pr1);
									}
								}
								//if(substi.size()!=0 && newClList.size()==0)
								if(newClList.size()==0 && canunify)	
									return true;
							} 
							
							if(newClList.size() !=0) {
								Clause newCl = new Clause (newClList);
								//System.out.print("New Clause is" +newCl.toString()+ "Queue Size: "+queue.size());
								if(!visited.contains(newCl)) {
									visited.add(newCl);
									queue.add(newCl);
									//break;
								}
							}// Neg clause found
						}
					} //break;// already Cl is in KB
					//continue;
				}// Cl in KB
			}
		}	
		if(queue.isEmpty())
			return false;
		return false;
	}

	private void applySubsti(HashSet<Predicate> tempList,HashSet<Predicate> popList, HashMap<Variable,GenericT> substi) {

		for(Predicate pr: tempList) {
			for(int i=0 ; i < pr.termList.size(); i++) {
				GenericT g1;
				g1= pr.termList.get(i);
				if( (g1 instanceof Variable)) {
					if(substi.containsKey((Variable)g1))
						pr.termList.set(i, substi.get((Variable)g1));
				}	
			}
		}
		for(Predicate pr: popList) {	
			for(int i=0 ; i < pr.termList.size(); i++) {
				GenericT g2 = pr.termList.get(i);
				if(g2 instanceof Variable) {
					if(substi.containsKey((Variable)g2))
						pr.termList.set(i, substi.get((Variable)g2));
				}
			} 
		}
	}

	protected void applyStandarize(HashSet<Predicate> plist2, HashMap<Variable, Variable> mapV) {
		for(Predicate pr : plist2) {
			for(int i=0; i< pr.termList.size(); i++) {
				GenericT gen = pr.termList.get(i);
				if(gen instanceof Variable && mapV.containsKey((Variable)gen)) {
					pr.termList.set(i,mapV.get((Variable)gen));
				}
			}
		}
	}


}

