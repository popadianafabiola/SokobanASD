package com.asd.sokoban;

/**
 * Created by fabii on 05.05.2018.
 */

public class SinglyLinkedList<E> {

    private Node head;
    private int size;

    public SinglyLinkedList() {
        head = new Node(null);
        size = 0;
    }


    public void add(E element) {
        Node node = new Node(element);
        Node current = head;
        for (int i = 0; (i < size) && (current.getNext() != null); i++) {
            current = current.getNext();
        }

        node.setNext(current.getNext());
        current.setNext(node);
        size++;
    }

    public void pop() {
        Node current = head;
        for (int i = 0; (i < size) && (current.getNext().getNext() != null); i++) {
            current = current.getNext();
        }

        current.setNext(null);
        size--;
    }

    public void insertBefore(E elem1, E elem2){
        Node temp = new Node(elem2);
        Node current = head;
        while(true){
            if((current.getNext().getData() == elem1) || (current.getNext().getData() == null)) break;
            current = current.getNext();
        }
        temp.setNext(current.getNext());
        current.setNext(temp);
        size++;
    }

    public E get(int pos) {
        Node current = head;
        for(int i = 0; i < pos; i++)
            current = current.getNext();
        return current.getData();
    }

    public E back() {
        Node current = head;
        for(int i = 0; i < size; i++)
            current = current.getNext();
        return current.getData();
    }

    public Node head() {
        return head.getNext();
    }


    public Node tail() {
        if (size <= 1) {
            return head.getNext();
        }
        Node current = head.getNext();
        while (true) {
            if (current.getNext() == null) {
                return current;
            }
            current = current.getNext();
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return (size < 0) ? true : false;
    }


    public boolean remove(E element) {
        Node current = head;
        for (int i = 0; i < size; i++) {
            if (current.getNext() == null) {
                return false;
            }
            if (current.getNext().getData() == element) {
                current.setNext(current.getNext().getNext());
                size--;
            }
            current = current.getNext();
        }
        return true;
    }
    public String toString() {
        Node current = head.getNext();
        String output = "{";
        while (current != null) {
            output += "[" + current.getData().toString() + "]";
            current = current.getNext();
        }
        output += "}";
        return output;
    }

    private class Node {
        private Node next;
        private E data;

        public Node(E dataValue) {
            next = null;
            data = dataValue;
        }

        public Node(E dataValue, Node nextValue) {
            next = nextValue;
            data = dataValue;
        }

        public E getData() {
            return data;
        }

        public void setData(E dataValue) {
            data = dataValue;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node nextValue) {
            next = nextValue;
        }
    }
}