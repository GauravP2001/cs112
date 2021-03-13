package friends;

import java.util.ArrayList;
import java.util.Arrays;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		 
		if (g == null || p1 == null || p2 == null) {
			return null;
		}
		
		if (g.map.get(p1) == null || g.map.get(p2) == null) {
			return null;
		}
		
		if (g.members[g.map.get(p1)].first == null) {
			return null;
		}
		
		ArrayList<String> name = new ArrayList<String>();
						
		boolean[] visited = new boolean[g.members.length];
		Person[] predecessor = new Person[g.members.length];
		
		Queue<Person> q = new Queue<Person>();
		
		int startIndex = g.map.get(p1);

		q.enqueue(g.members[startIndex]);
		visited[startIndex] = true;
				
		
		while (!q.isEmpty()) {
			
			Person current = q.dequeue();
			
			int currentIndex = g.map.get(current.name);
			visited[currentIndex] = true;
			
			Friend f = current.first;
			
			if (f == null) {
				return null;
			}
			
			while (f != null) {
				
				if (!visited[f.fnum]) {
					
					visited[f.fnum] = true;
					predecessor[f.fnum] = current;
					
					q.enqueue(g.members[f.fnum]);
					
					if (g.members[f.fnum].name.equals(p2)) {
						current = g.members[f.fnum];
						
						while (!current.name.equals(p1)) {
							name.add(0, current.name);
							current = predecessor[g.map.get(current.name)];
							
						}
						
						name.add(0, p1);
						return name;
					}
				}
				
				f = f.next;
			}
		}
			
		return null;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {

		if (g == null || school == null) {
			
			return null;
		}
		
		ArrayList<ArrayList<String>> cliques = new ArrayList<ArrayList<String>>();
		boolean[] visited = new boolean[g.members.length];

		return BFS(g, school, g.members[0], visited, cliques);
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		if (g == null) {
			return null;
		}
		
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> pred = new ArrayList<String>();
		
		boolean[] visited = new boolean[g.members.length];
		int[] DFSNumber = new int[g.members.length];
		int[] before = new int[g.members.length];
		
		for (int i = 0; i < g.members.length; i++) {
			if (!visited[i]) {
				result = DFS(g, result ,visited, DFSNumber, new int[] {0,0}, before, pred, g.members[i], true);
			}
		}
		

		return result;
}
	
	private static ArrayList<ArrayList<String>> BFS(Graph g, String school, Person p1, boolean[] visited, ArrayList<ArrayList<String>> cliques) {
		
		ArrayList<String> result = new ArrayList<String>();
		Queue<Person> q = new Queue<Person>();
		
		q.enqueue(p1);
	
		visited[g.map.get(p1.name)] = true;
		
		Friend n;
		Person current = new Person();
		
		if (p1.school == null || !p1.school.equals(school)) {
			q.dequeue();
			
			for (int i = 0; i < visited.length; i++) {
				
				if (!visited[i]) {
					return BFS(g, school, g.members[i], visited, cliques);
				}
			}
		}
		
		while (!q.isEmpty()) {
			
			current = q.dequeue();
			n = current.first;
			result.add(current.name);
			
			while (n != null) {
				
				if (!visited[n.fnum]) {
					if (g.members[n.fnum].school == null) {
						
					} else {
						if (g.members[n.fnum].school.equals(school)) {
							q.enqueue(g.members[n.fnum]);
						}
					}
					
					visited[n.fnum] = true;
				}
				
				n = n.next;
			}
		}
		
		if (!cliques.isEmpty() && result.isEmpty()) {
			
		} else {
			cliques.add(result);
		}
		
		for (int i = 0; i < visited.length; i++) {
			if (!visited[i]) {
				return BFS(g, school, g.members[i], visited, cliques);
			}
			
		}
		
		return cliques;
	}
	
	private static ArrayList<String> DFS(Graph g, ArrayList<String> result, boolean[] visited, int[] DFSNumber, int[] count, int[] before, ArrayList<String> pred, Person p1, boolean start) {
		
		visited[g.map.get(p1.name)] = true;
		DFSNumber[g.map.get(p1.name)] = count[0];
		before[g.map.get(p1.name)] = count[1];

		Friend n = p1.first;
		
		
		while (n != null) {
			
			if (!(visited[n.fnum])) {
				count[0]++;
				count[1]++;
				
				result = DFS(g, result ,visited, DFSNumber, count, before, pred, g.members[n.fnum], false);
				
				// it is the right one
				if (DFSNumber[g.map.get(p1.name)] <= before[n.fnum]) {
					if ((!(result.contains(p1.name)) && pred.contains(p1.name)) || (!result.contains(p1.name) && !start)) {
						result.add(p1.name);
					}
					
				} else {
					
					int f = before[g.map.get(p1.name)];
					int b = before[n.fnum];
					
					if (f < b) {
						before[g.map.get(p1.name)] = f;
					} else {
						before[g.map.get(p1.name)] = b;
					}	
				}
				pred.add(p1.name);
			} else {
				
				int t = before[g.map.get(p1.name)];
				int x = DFSNumber[n.fnum];
				
				if (t < x) {
					before[g.map.get(p1.name)] = t;
				} else {
					before[g.map.get(p1.name)] = x;
				}	
			}
			
			n = n.next;
		}
		
		return result;
	}	
}

