package bgu.spl.mics.application.passiveObjects;

public class Pair <E,T> {
    E right ;
    T left;


    public Pair(E right,T left) {
        this.left = left;
        this.right = right;
    }


    public E getRight() {
        return right;
    }

    public T getLeft() {
        return left;
    }
}