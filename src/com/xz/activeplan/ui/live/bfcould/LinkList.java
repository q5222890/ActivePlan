package com.xz.activeplan.ui.live.bfcould;

class Node {
	public long start;
	public long end;
	public Node next;
	
	public Node(long s, long e, Node n) {
		start = s;
		end = e;
		next = n;
	}
}

//save the range to be downloaded
public class LinkList {
	private Node header;
	private int size;
	
	public LinkList() {
		header = new Node(0, 0, null);
		size = 0;
	}
	
	public void add(long start, long end) {
		if (end < start) {
			System.out.println("Invalid: 'end' is little than 'start'!");
			try {
				throw new Exception("Invalid: 'end' is little than 'start'!");
			} catch (Exception e) {
			}
		}
		
		Node node = header;
		
		while (true) {
			if (node.next == null) {
				node.next = new Node(start, end, null);
				size++;
				break;
			}
			
			if (start > node.next.end) {
				node = node.next;
				continue;
			}
			
			Node tmp = node.next;
			if (end < tmp.start) {
				node.next = new Node(start, end, tmp);
				size++;
			} else {
				//merge the area
				if (start < tmp.start) {
					tmp.start = start;
				}
				if (end > tmp.end) {
					tmp.end = end;
				}
			}
			break;
		}
	}
	
	public boolean delete(long start, long end) {
		if (end < start) {
			System.out.println("Invalid: 'end' is little than 'start'!");
			try {
				throw new Exception("Invalid: 'end' is little than 'start'!");
			} catch (Exception e) {
			}
		}

		Node node = header;
		
		while (node.next != null) {
			if (start > node.next.end) {
				node = node.next;
				continue;
			}
			
			Node tmp = node.next;
			
			if (end < tmp.start) {
				break;
			}
			//split the area 
			else if (start <= tmp.start && end >= tmp.end) {
				node.next = tmp.next;
				size--;
			} else if (start <= tmp.start && end < tmp.end) {
				tmp.start = end + 1;
			} else if (start > tmp.start && end >= tmp.end) {
				tmp.end = start - 1;
			} else {
				node.next = new Node(tmp.start, start - 1, tmp);
				tmp.start = end + 1;
				size++;
			}
			
			return true;
		}
		return false;
	}
	
	public void printList() {
		Node node = header;
		while (node.next != null) {
			System.out.println("{" + node.next.start + ", " + node.next.end + "}\t");
			node = node.next;
		}
	}
}

